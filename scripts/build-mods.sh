#!/usr/bin/env bash
#
# Builds the Forge / Fabric version projects. Each Forge/V* and Fabric/V* folder is an independent
# Gradle build (its own wrapper, ForgeGradle / Loom version), so they're built one by one here rather
# than from a shared Gradle build.
#
#   1. mvn install in the repository root -> EranoAPI-Common in ~/.m2 (the version projects read it
#      from mavenLocal)
#   2. ./gradlew clean build publishToMavenLocal in every version project (clean: no stale jars of a
#      previous version end up in build/mods). Gradle itself runs on JDK 25
#      (Loom 1.18 / current ForgeGradle require it); each project targets its Minecraft version's Java
#      through its own toolchain / release setting. A project whose toolchain needs an older Gradle JVM
#      (e.g. legacy ForgeGradle) sets build_java=<N> in its gradle.properties.
#   3. jars are collected into build/mods/
#
# Usage:
#   scripts/build-mods.sh                        # every Forge/V* and Fabric/V* project
#   scripts/build-mods.sh Fabric/V26_1_2         # only these projects
#
# Environment:
#   JAVA8_HOME, JAVA17_HOME, JAVA21_HOME, JAVA25_HOME   JDK per Java version (default: auto-detected)
#   SKIP_MAVEN=1                                        don't run mvn install first

set -uo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUT_DIR="$ROOT/build/mods"
LOG_DIR="$ROOT/build/mods/logs"

log()  { printf '\033[1;34m==>\033[0m %s\n' "$*"; }
warn() { printf '\033[1;33m!!\033[0m %s\n' "$*" >&2; }
die()  { printf '\033[1;31mxx\033[0m %s\n' "$*" >&2; exit 1; }

# Prints the major Java version of a JDK home (8, 17, 21, ...), read from its 'release' file.
jdk_major() {
    local v
    v=$(sed -n 's/^JAVA_VERSION="\(.*\)"/\1/p' "$1/release" 2>/dev/null)
    [ -n "$v" ] || return 1
    v=${v#1.}
    printf '%s' "${v%%[._+-]*}"
}

# Prints the home of a JDK with exactly major version $1: JAVA<N>_HOME first, then common install dirs.
find_jdk() {
    local want=$1 var="JAVA${1}_HOME" dir
    if [ -n "${!var:-}" ]; then
        printf '%s' "${!var}"; return 0
    fi
    shopt -s nullglob
    for dir in /usr/lib/jvm/* /usr/lib64/jvm/* /opt/java/* "$HOME"/.sdkman/candidates/java/* "$HOME"/.jdks/*; do
        if [ "$(jdk_major "$dir")" = "$want" ] && [ -x "$dir/bin/java" ]; then
            shopt -u nullglob; printf '%s' "$dir"; return 0
        fi
    done
    shopt -u nullglob
    return 1
}

# JVM that runs Gradle for a project: build_java from its gradle.properties, otherwise 25.
gradle_java_for() {
    local value
    value=$(sed -n 's/^build_java=\([0-9]*\).*/\1/p' "$ROOT/$1/gradle.properties" 2>/dev/null | head -1)
    echo "${value:-25}"
}

build_project() {
    local project=$1 name java jdk logfile
    name=$(echo "$project" | tr '/' '-')
    logfile="$LOG_DIR/$name.log"
    java=$(gradle_java_for "$project")

    if ! jdk=$(find_jdk "$java"); then
        warn "$project needs Java $java, not found (set JAVA${java}_HOME), skipping"
        FAILED+=("$project (Java $java missing)"); return 1
    fi

    log "$project with Java $java -> log: $logfile"
    if (cd "$ROOT/$project" && JAVA_HOME="$jdk" ./gradlew clean build publishToMavenLocal --console=plain) > "$logfile" 2>&1; then
        find "$ROOT/$project/build/libs" -name '*.jar' ! -name '*-sources.jar' ! -name '*-javadoc.jar' -exec cp {} "$OUT_DIR/" \;
        BUILT+=("$project")
    else
        warn "$project failed, see $logfile"
        FAILED+=("$project (build error)")
        {
            printf '== %s (Java %s) - %s\n' "$project" "$java" "$logfile"
            awk '/FAILURE|error:|What went wrong|^> /{ if (n < 8) { print; n++ } } END { exit n == 0 }' "$logfile" \
                || tail -n 8 "$logfile"
            echo
        } >> "$LOG_DIR/failed.txt"
    fi
}

main() {
    mkdir -p "$OUT_DIR" "$LOG_DIR"
    rm -f "$LOG_DIR/failed.txt" "$OUT_DIR"/*.jar

    if [ "${SKIP_MAVEN:-0}" != "1" ]; then
        local jdk25
        jdk25=$(find_jdk 25) || die "The Maven build needs JDK 25 (set JAVA25_HOME)"
        log "mvn install (EranoAPI-Common -> ~/.m2) -> log: $LOG_DIR/maven.log"
        (cd "$ROOT" && JAVA_HOME="$jdk25" mvn -q install -DskipTests) > "$LOG_DIR/maven.log" 2>&1 \
            || die "mvn install failed, see $LOG_DIR/maven.log"
    fi

    local projects=()
    if [ $# -gt 0 ]; then
        projects=("$@")
    else
        local dir
        for dir in "$ROOT"/Forge/V* "$ROOT"/Fabric/V*; do
            [ -x "$dir/gradlew" ] && projects+=("${dir#"$ROOT"/}")
        done
    fi
    [ ${#projects[@]} -gt 0 ] || die "No Forge/V* or Fabric/V* projects found"

    BUILT=(); FAILED=()
    local project
    for project in "${projects[@]}"; do
        build_project "${project%/}"
    done

    echo
    log "Built:  ${#BUILT[@]}   ${BUILT[*]:-}"
    if [ ${#FAILED[@]} -gt 0 ]; then
        warn "Failed: ${#FAILED[@]}"
        printf '    %s\n' "${FAILED[@]}" >&2
        warn "Error summary: $LOG_DIR/failed.txt"
        exit 1
    fi
    log "Jars: $OUT_DIR  (also published to ~/.m2)"
}

main "$@"
