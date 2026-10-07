#!/usr/bin/env bash
#
# Builds one Spigot server jar per NMS revision (1.8 -> 26.3) with BuildTools and
# installs them into the local Maven repository (~/.m2), so every NMS module can compile.
# Windows counterpart: build-spigot-jars.bat (same version table).
#
# Rule (see dependencies.md): one build per vX_Y_RZ revision; if a revision covers several
# Minecraft versions, the newest of them is built. 26.x has no revisions, so every
# version is built.
#
# Usage:
#   scripts/build-spigot-jars.sh                 # build everything that is missing
#   scripts/build-spigot-jars.sh 1.20.4 26.3     # build only these versions
#
# Environment:
#   BUILDTOOLS_DIR=...   BuildTools directory (default: ~/MinecraftWorkspace/Buildtools)
#   JAVA8_HOME, JAVA11_HOME, JAVA17_HOME, JAVA21_HOME, JAVA25_HOME
#                        JDK to use for that Java version (default: auto-detected)
#   FORCE=1              rebuild even if the jar already exists (in BUILDTOOLS_DIR or ~/.m2)
#   MAVEN_REPO=...       local Maven repository (default: ~/.m2/repository)
#   CLEAN_AFTER_BUILD=1  delete BuildTools' work/ folder and the output jar after each build (CI disk space)

set -uo pipefail

# "<minecraft version>  <revision>  <java>"
# revisions: spigotmc.org wiki "Spigot NMS and Minecraft Versions", java: dependencies.md
VERSIONS=(
    "1.8      v1_8_R1   8"
    "1.8.3    v1_8_R2   8"
    "1.8.8    v1_8_R3   8"
    "1.9.2    v1_9_R1   8"
    "1.9.4    v1_9_R2   8"
    "1.10.2   v1_10_R1  8"
    "1.11.2   v1_11_R1  8"
    "1.12.2   v1_12_R1  8"
    "1.13     v1_13_R1  11"
    "1.13.2   v1_13_R2  11"
    "1.14.4   v1_14_R1  11"
    "1.15.2   v1_15_R1  11"
    "1.16.1   v1_16_R1  11"
    "1.16.3   v1_16_R2  11"
    "1.16.5   v1_16_R3  11"
    "1.17.1   v1_17_R1  17"
    "1.18.1   v1_18_R1  17"
    "1.18.2   v1_18_R2  17"
    "1.19.2   v1_19_R1  17"
    "1.19.3   v1_19_R2  17"
    "1.19.4   v1_19_R3  17"
    "1.20.1   v1_20_R1  17"
    "1.20.2   v1_20_R2  17"
    "1.20.4   v1_20_R3  17"
    "1.20.6   v1_20_R4  21"
    "1.21.1   v1_21_R1  21"
    "1.21.3   v1_21_R2  21"
    "1.21.4   v1_21_R3  21"
    "1.21.5   v1_21_R4  21"
    "1.21.8   v1_21_R5  21"
    "1.21.10  v1_21_R6  21"
    "1.21.11  v1_21_R7  21"
    "26.1.2   -         25"
    "26.2     -         25"
    "26.3     -         25"
)

BUILDTOOLS_URL="https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"

BUILDTOOLS_DIR="${BUILDTOOLS_DIR:-$HOME/MinecraftWorkspace/Buildtools}"
LOG_DIR="$BUILDTOOLS_DIR/logs"
M2_SPIGOT="${MAVEN_REPO:-$HOME/.m2/repository}/org/spigotmc/spigot"

log()  { printf '\033[1;34m==>\033[0m %s\n' "$*"; }
warn() { printf '\033[1;33m!!\033[0m %s\n' "$*" >&2; }
die()  { printf '\033[1;31mxx\033[0m %s\n' "$*" >&2; exit 1; }

# On Git Bash, turn /c/foo into C:/foo before handing paths to java.exe.
native_path() {
    if command -v cygpath >/dev/null 2>&1; then cygpath -m "$1"; else printf '%s' "$1"; fi
}

# Prints the major Java version of a JDK home (8, 17, 21, ...), read from its 'release' file.
jdk_major() {
    local v
    v=$(sed -n 's/^JAVA_VERSION="\(.*\)"/\1/p' "$1/release" 2>/dev/null)
    [ -n "$v" ] || return 1
    v=${v#1.}                 # 1.8.0_392 -> 8.0_392
    printf '%s' "${v%%[._+-]*}"
}

java_bin() {
    if [ -x "$1/bin/java.exe" ]; then printf '%s' "$1/bin/java.exe"; else printf '%s' "$1/bin/java"; fi
}

# Prints the home of a JDK with exactly major version $1: JAVA<N>_HOME first, then common install dirs.
find_jdk() {
    local want=$1 var="JAVA${1}_HOME" dir
    if [ -n "${!var:-}" ]; then
        [ -x "$(java_bin "${!var}")" ] || die "$var=${!var} has no bin/java"
        printf '%s' "${!var}"; return 0
    fi
    shopt -s nullglob
    for dir in /usr/lib/jvm/* /usr/lib64/jvm/* /opt/java/* /opt/jdk* \
               "$HOME"/.sdkman/candidates/java/* "$HOME"/.jdks/* \
               /Library/Java/JavaVirtualMachines/*/Contents/Home \
               "/c/Program Files/Java/"* "/c/Program Files/Eclipse Adoptium/"* "/c/Program Files/Zulu/"* \
               "/c/Program Files/Microsoft/"jdk* "/c/Program Files/Amazon Corretto/"*; do
        if [ "$(jdk_major "$dir")" = "$want" ] && [ -x "$(java_bin "$dir")" ]; then
            shopt -u nullglob; printf '%s' "$dir"; return 0
        fi
    done
    shopt -u nullglob
    return 1
}

# 1.17 - 1.21.x need --remapped so the NMS modules get the remapped-mojang jars + mappings.
# 26.x is unobfuscated, there is nothing to remap.
needs_remapped() {
    local major=${1%%.*} rest=${1#*.}
    local minor=${rest%%.*}
    [ "$major" = "1" ] && [ "$minor" -ge 17 ]
}

# Whether ~/.m2 already has what the NMS module needs (CI caches ~/.m2, not the BuildTools folder).
# 1.21.11 is R0.2, hence the R0.* glob.
installed_in_m2() {
    local rev=$1 dir
    for dir in "$M2_SPIGOT/$rev"-R0.*-SNAPSHOT; do
        [ -f "$dir/spigot-$(basename "$dir").jar" ] || continue
        needs_remapped "$rev" && [ ! -f "$dir/spigot-$(basename "$dir")-remapped-mojang.jar" ] && continue
        return 0
    done
    return 1
}

build_version() {
    local rev=$1 revision=$2 java=$3 jdk
    local jar="$BUILDTOOLS_DIR/spigot-$rev.jar" logfile="$LOG_DIR/$rev.log"

    if { [ -f "$jar" ] || installed_in_m2 "$rev"; } && [ "${FORCE:-0}" != "1" ]; then
        log "$rev ($revision) already built, skipping"
        SKIPPED+=("$rev"); return 0
    fi

    if ! jdk=$(find_jdk "$java"); then
        warn "$rev needs Java $java, not found (set JAVA${java}_HOME), skipping"
        FAILED+=("$rev (Java $java missing)"); return 1
    fi

    local args=(--rev "$rev" --nogui)
    needs_remapped "$rev" && args+=(--remapped)

    log "$rev ($revision) with Java $java -> log: $logfile"
    if (cd "$BUILDTOOLS_DIR" && "$(java_bin "$jdk")" -jar BuildTools.jar "${args[@]}") > "$logfile" 2>&1; then
        BUILT+=("$rev")
        # Everything the NMS modules need is in ~/.m2 now; work/ alone grows to several GB over 35 builds.
        if [ "${CLEAN_AFTER_BUILD:-0}" = "1" ]; then
            rm -rf "$BUILDTOOLS_DIR/work" "$jar"
        fi
    else
        warn "$rev failed, see $logfile"
        FAILED+=("$rev (build error)")
        # Keep the first real error lines so failures can be reviewed without opening every log.
        {
            printf '== %s (%s, Java %s) - %s\n' "$rev" "$revision" "$java" "$logfile"
            awk '/^\[ERROR\]|^error:|Exception in thread|^Caused by:/ && n < 5 { print; n++ } END { exit n == 0 }' "$logfile" \
                || tail -n 5 "$logfile"
            echo
        } >> "$LOG_DIR/failed.txt"
        return 1
    fi
}

main() {
    command -v curl >/dev/null 2>&1 || die "curl is required"
    command -v git  >/dev/null 2>&1 || die "git is required"

    mkdir -p "$BUILDTOOLS_DIR" "$LOG_DIR"

    # oss.sonatype.org is shut down; old versions (1.8 - 1.15) still reference it for
    # bungeecord-chat & co. Redirect it to Spigot's public repo through a *global* settings
    # file so the user's own ~/.m2/settings.xml keeps working.
    cat > "$BUILDTOOLS_DIR/maven-settings.xml" <<'EOF'
<settings>
  <mirrors>
    <mirror>
      <id>spigot-public</id>
      <mirrorOf>sonatype-nexus-snapshots,sonatype-nexus-releases,sonatype,oss-sonatype</mirrorOf>
      <url>https://hub.spigotmc.org/nexus/content/groups/public/</url>
    </mirror>
  </mirrors>
</settings>
EOF
    export MAVEN_ARGS="-gs $(native_path "$BUILDTOOLS_DIR/maven-settings.xml")"

    log "Updating BuildTools in $BUILDTOOLS_DIR"
    (cd "$BUILDTOOLS_DIR" && curl -fsSL -z BuildTools.jar -o BuildTools.jar "$BUILDTOOLS_URL") \
        || die "Could not download BuildTools"

    BUILT=(); SKIPPED=(); FAILED=()
    rm -f "$LOG_DIR/failed.txt"
    local entry rev revision java arg wanted
    for entry in "${VERSIONS[@]}"; do
        read -r rev revision java <<< "$entry"
        if [ $# -gt 0 ]; then
            wanted=0
            for arg in "$@"; do [ "$arg" = "$rev" ] && wanted=1; done
            [ $wanted = 1 ] || continue
        fi
        build_version "$rev" "$revision" "$java"
    done

    echo
    log "Built:   ${#BUILT[@]}   ${BUILT[*]:-}"
    log "Skipped: ${#SKIPPED[@]}   ${SKIPPED[*]:-}"
    if [ ${#FAILED[@]} -gt 0 ]; then
        warn "Failed:  ${#FAILED[@]}"
        printf '    %s\n' "${FAILED[@]}" >&2
        [ -f "$LOG_DIR/failed.txt" ] && warn "Error summary: $LOG_DIR/failed.txt"
        exit 1
    fi
    log "Jars: $BUILDTOOLS_DIR  (also installed into ~/.m2)"
}

main "$@"
