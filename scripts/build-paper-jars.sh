#!/usr/bin/env bash
#
# Downloads one Paper server per NMS revision (1.8.8 -> 26.3), lets paperclip patch it and installs the
# patched server jar into the local Maven repository (~/.m2) as io.papermc.paper:paper-server:<mc>-local,
# so every Paper/NMS module can compile. Counterpart of build-spigot-jars.sh, same revision table.
#
# Rule: one Paper build per vX_Y_RZ revision, the same Minecraft version build-spigot-jars.sh builds.
# Paper has no build for v1_8_R1 (1.8 - 1.8.2), v1_8_R2 (1.8.3) and v1_9_R1 (1.9 - 1.9.2).
#
# Jar layouts:
#   1.8.8 - 1.17.1    paperclip writes cache/patched_<mc>.jar, which bundles every library
#   1.18+             paperclip writes versions/<mc>/paper-<mc>.jar; its libraries are listed in
#                     META-INF/libraries.list and become dependencies of the installed POM, resolved from
#                     Maven Central, libraries.minecraft.net and repo.papermc.io (see Paper/NMS/pom.xml),
#                     except Paper's own snapshots (see local_version)
#
# Paper 1.17.1 - 1.20.4 runs on Spigot's names. Those jars are also remapped to Mojang's names with Spigot's
# mappings (org.spigotmc:minecraft-server maps-spigot / maps-mojang, installed by build-spigot-jars.sh) and
# installed as the remapped-obf and remapped-mojang classifiers, which Paper/NMS compiles and reobfuscates
# against like Spigot/NMS does.
#
# Usage:
#   scripts/build-paper-jars.sh                  # install everything that is missing
#   scripts/build-paper-jars.sh 1.21.11 26.3     # install only these versions
#
# Environment:
#   PAPER_DIR=...        downloads and paperclip work folders (default: ~/MinecraftWorkspace/Paper)
#   JAVA8_HOME, JAVA11_HOME, JAVA17_HOME, JAVA21_HOME, JAVA25_HOME
#                        JDK to run paperclip with (default: auto-detected)
#   FORCE=1              reinstall even if the jar is already in ~/.m2
#   MAVEN_REPO=...       local Maven repository (default: ~/.m2/repository)
#   CLEAN_AFTER_BUILD=1  delete the download and paperclip's work folder after each install (CI disk space)

set -uo pipefail

# "<minecraft version>  <revision>  <java>"
VERSIONS=(
    "1.8.8    v1_8_R3   8"
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

FILL_API="https://fill.papermc.io/v3/projects/paper/versions"

PAPER_DIR="${PAPER_DIR:-$HOME/MinecraftWorkspace/Paper}"
LOG_DIR="$PAPER_DIR/logs"
M2_PAPER="${MAVEN_REPO:-$HOME/.m2/repository}/io/papermc/paper/paper-server"

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

installed_in_m2() {
    [ -f "$M2_PAPER/$1-local/paper-server-$1-local.jar" ] &&
    { ! needs_remap "$2" || [ -f "$M2_PAPER/$1-local/paper-server-$1-local-remapped-mojang.jar" ]; }
}

# Revisions where Paper runs on Spigot's names: 1.17.1 - 1.20.4.
needs_remap() {
    case $1 in v1_17_R1|v1_18_R*|v1_19_R*|v1_20_R1|v1_20_R2|v1_20_R3) return 0 ;; *) return 1 ;; esac
}

SPECIALSOURCE="net.md-5:SpecialSource:1.11.4:jar:shaded"

# Copies maps-mojang $1 to $3 plus a mapping for every Paper class of obf jar $2 that would land on the Mojang
# name of a vanilla class (Paper's PlayerChunkMap$DistanceManager next to vanilla abe$a = ChunkMap$DistanceManager):
# it's renamed to Paper<name>, SpecialSource would write the same class twice otherwise.
mojang_mappings() {
    python3 -I - "$1" "$2" "$3" <<'EOF'
import sys, zipfile
maps, jar, out = sys.argv[1:]
lines = open(maps, encoding="utf-8").read().splitlines()
obf2moj = {}
for l in lines:
    if l and not l.startswith((" ", "#")) and l.endswith(":"):
        moj, obf = l[:-1].split(" -> ")
        obf2moj[obf.replace(".", "/")] = moj.replace(".", "/")
def target(n):
    if n in obf2moj:
        return obf2moj[n]
    if "$" in n:
        outer, inner = n.rsplit("$", 1)
        return target(outer) + "$" + inner
    return n
names = [n[:-6] for n in zipfile.ZipFile(jar).namelist() if n.endswith(".class")]
vanilla = {target(n) for n in names if n in obf2moj}
for n in names:
    t = target(n)
    if n not in obf2moj and t in vanilla:
        i = max(t.rfind("$"), t.rfind("/")) + 1
        lines.append(f"{t[:i]}Paper{t[i:]} -> {n}:".replace("/", "."))
open(out, "w", encoding="utf-8").write("\n".join(lines) + "\n")
EOF
}

# Installs paper-server $1 remapped to obf (remapped-obf) and Mojang names (remapped-mojang), working in $2.
install_remapped() {
    local mc=$1 work=$2 java ss maps jar="$M2_PAPER/$1-local/paper-server-$1-local.jar"
    maps="${MAVEN_REPO:-$HOME/.m2/repository}/org/spigotmc/minecraft-server/$mc-R0.1-SNAPSHOT/minecraft-server-$mc-R0.1-SNAPSHOT"
    [ -f "$maps-maps-spigot.csrg" ] && [ -f "$maps-maps-mojang.txt" ] || {
        echo "Spigot's $mc mappings missing, run build-spigot-jars.sh $mc first"; return 1; }
    java=$(java_bin "$(find_jdk 21 || find_jdk 25 || find_jdk 17)") || return 1
    mvn -B -q dependency:get -Dartifact="$SPECIALSOURCE" || return 1
    ss="${MAVEN_REPO:-$HOME/.m2/repository}/net/md-5/SpecialSource/1.11.4/SpecialSource-1.11.4-shaded.jar"
    mkdir -p "$work" &&
    "$java" -jar "$(native_path "$ss")" -i "$(native_path "$jar")" --srg-in "$(native_path "$maps-maps-spigot.csrg")" --reverse \
        -o "$(native_path "$work/remapped-obf.jar")" &&
    mojang_mappings "$maps-maps-mojang.txt" "$work/remapped-obf.jar" "$work/maps-mojang.txt" &&
    "$java" -jar "$(native_path "$ss")" -i "$(native_path "$work/remapped-obf.jar")" --srg-in "$(native_path "$work/maps-mojang.txt")" \
        -o "$(native_path "$work/remapped-mojang.jar")" || return 1
    local classifier
    for classifier in remapped-obf remapped-mojang; do
        mvn -B -q install:install-file -Dfile="$(native_path "$work/$classifier.jar")" -Dpackaging=jar -DgeneratePom=false \
            -DgroupId=io.papermc.paper -DartifactId=paper-server -Dversion="$mc-local" -Dclassifier="$classifier" || return 1
    done
    rm -f "$work/remapped-obf.jar" "$work/remapped-mojang.jar" "$work/maps-mojang.txt"
}

# Prints "<name> <sha256> <url>" of the newest build of Paper $1.
latest_build() {
    curl -fsSL "$FILL_API/$1/builds/latest" | python3 -c '
import json, sys
d = json.load(sys.stdin)["downloads"]["server:default"]
print(d["name"], d["checksums"]["sha256"], d["url"])'
}

# Prints the libraries of paperclip jar $1, one "<group> <artifact> <version> <classifier> <path>" per line
# (classifier "-" when there is none). Empty for the self-contained pre-1.18 jars. Leaves out the parameter
# name data of Paper's command system: in no public repository and not needed to compile.
libraries() {
    local coords path g a v c
    # Pre-1.18 paperclip jars have no libraries.list: no libraries, not a failure (pipefail).
    { unzip -p "$1" META-INF/libraries.list 2>/dev/null || true; } | while IFS=$'\t' read -r _ coords path; do
        IFS=: read -r g a v c <<< "$coords"
        [ "$g" = "io.papermc.parchment.data" ] && continue
        printf '%s %s %s %s %s\n' "$g" "$a" "$v" "${c:--}" "$path"
    done
}

# Paper's own libraries (paper-api, paper-mojangapi ...) are snapshots that repo.papermc.io deletes after a
# while. They're installed from the paperclip jar as <version without -SNAPSHOT>-local instead, which no
# repository has and which can't shadow the real snapshots other projects use.
local_version() {
    case $1 in *-SNAPSHOT) printf '%s-local' "${1%-SNAPSHOT}" ;; *) printf '%s' "$1" ;; esac
}

# Installs the snapshot libraries of paperclip jar $1, which paperclip extracted into $2/libraries.
install_snapshot_libraries() {
    local g a v c path
    libraries "$1" | while read -r g a v c path; do
        case $v in *-SNAPSHOT) ;; *) continue ;; esac
        mvn -B -q install:install-file -Dfile="$(native_path "$2/libraries/$path")" -Dpackaging=jar \
            -DgroupId="$g" -DartifactId="$a" -Dversion="$(local_version "$v")" || return 1
    done
}

# Writes the POM of paper-server $1 to $3 with the libraries of paperclip jar $2 as dependencies. Transitive
# dependencies are excluded, the list is complete.
write_pom() {
    local mc=$1 paperclip=$2 pom=$3 g a v c
    {
        cat <<EOF
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <groupId>io.papermc.paper</groupId>
  <artifactId>paper-server</artifactId>
  <version>$mc-local</version>
  <description>Paper $mc server, patched locally by scripts/build-paper-jars.sh</description>
  <dependencies>
EOF
        libraries "$paperclip" | while read -r g a v c _; do
            [ "$c" = "-" ] && c=
            cat <<EOF
    <dependency>
      <groupId>$g</groupId><artifactId>$a</artifactId><version>$(local_version "$v")</version>${c:+<classifier>$c</classifier>}
      <exclusions><exclusion><groupId>*</groupId><artifactId>*</artifactId></exclusion></exclusions>
    </dependency>
EOF
        done
        printf '  </dependencies>\n</project>\n'
    } > "$pom"
}

build_version() {
    local mc=$1 revision=$2 java=$3 jdk name sha url server
    local download="$PAPER_DIR/downloads" work="$PAPER_DIR/work/$mc" logfile="$LOG_DIR/$mc.log"

    if installed_in_m2 "$mc" "$revision" && [ "${FORCE:-0}" != "1" ]; then
        log "$mc ($revision) already installed, skipping"
        SKIPPED+=("$mc"); return 0
    fi

    # Only the remapped jars are missing.
    if [ -f "$M2_PAPER/$mc-local/paper-server-$mc-local.jar" ] && [ "${FORCE:-0}" != "1" ]; then
        log "$mc ($revision) remapping to Mojang's names -> log: $logfile"
        if install_remapped "$mc" "$work" > "$logfile" 2>&1; then BUILT+=("$mc"); return 0; fi
        warn "$mc: remapping failed, see $logfile"
        FAILED+=("$mc (remap)"); return 1
    fi

    if ! jdk=$(find_jdk "$java"); then
        warn "$mc needs Java $java, not found (set JAVA${java}_HOME), skipping"
        FAILED+=("$mc (Java $java missing)"); return 1
    fi

    if ! read -r name sha url < <(latest_build "$mc") || [ -z "${url:-}" ]; then
        warn "$mc: no Paper build found on fill.papermc.io"
        FAILED+=("$mc (no build)"); return 1
    fi

    log "$mc ($revision) $name with Java $java -> log: $logfile"
    {
        mkdir -p "$download" "$work" &&
        if [ ! -f "$download/$name" ]; then curl -fsSL -o "$download/$name.part" "$url" && mv "$download/$name.part" "$download/$name"; fi &&
        echo "$sha  $download/$name" | sha256sum -c - &&
        (cd "$work" && "$(java_bin "$jdk")" -Dpaperclip.patchonly=true -jar "$(native_path "$download/$name")")
    } > "$logfile" 2>&1 || {
        warn "$mc failed, see $logfile"
        FAILED+=("$mc (download / paperclip)"); return 1
    }

    if [ -f "$work/versions/$mc/paper-$mc.jar" ]; then
        server="$work/versions/$mc/paper-$mc.jar"
    elif [ -f "$work/cache/patched_$mc.jar" ]; then
        server="$work/cache/patched_$mc.jar"
    else
        warn "$mc: paperclip produced no server jar, see $logfile"
        FAILED+=("$mc (no patched jar)"); return 1
    fi

    write_pom "$mc" "$download/$name" "$work/paper-server.pom"
    if install_snapshot_libraries "$download/$name" "$work" >> "$logfile" 2>&1 &&
       mvn -B -q install:install-file -Dfile="$(native_path "$server")" -DpomFile="$(native_path "$work/paper-server.pom")" >> "$logfile" 2>&1 &&
       { ! needs_remap "$revision" || install_remapped "$mc" "$work" >> "$logfile" 2>&1; }; then
        BUILT+=("$mc")
        if [ "${CLEAN_AFTER_BUILD:-0}" = "1" ]; then
            rm -rf "$work" "$download/$name"
        fi
    else
        warn "$mc: mvn install:install-file failed, see $logfile"
        FAILED+=("$mc (install)"); return 1
    fi
}

main() {
    command -v curl    >/dev/null 2>&1 || die "curl is required"
    command -v python3 >/dev/null 2>&1 || die "python3 is required"
    command -v unzip   >/dev/null 2>&1 || die "unzip is required"
    command -v mvn     >/dev/null 2>&1 || die "mvn is required"

    mkdir -p "$PAPER_DIR" "$LOG_DIR"

    BUILT=(); SKIPPED=(); FAILED=()
    local entry mc revision java arg wanted
    for entry in "${VERSIONS[@]}"; do
        read -r mc revision java <<< "$entry"
        if [ $# -gt 0 ]; then
            wanted=0
            for arg in "$@"; do [ "$arg" = "$mc" ] && wanted=1; done
            [ $wanted = 1 ] || continue
        fi
        build_version "$mc" "$revision" "$java"
    done

    echo
    log "Installed: ${#BUILT[@]}   ${BUILT[*]:-}"
    log "Skipped:   ${#SKIPPED[@]}   ${SKIPPED[*]:-}"
    if [ ${#FAILED[@]} -gt 0 ]; then
        warn "Failed:    ${#FAILED[@]}"
        printf '    %s\n' "${FAILED[@]}" >&2
        exit 1
    fi
    log "Jars: $M2_PAPER"
}

main "$@"
