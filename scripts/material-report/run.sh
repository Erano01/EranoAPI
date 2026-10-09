#!/usr/bin/env bash
# Writes docs/Material.md: every org.bukkit.Material of every NMS revision's spigot-api (in ~/.m2), the 1.13
# flattening from the 1.13.2 server's own CraftLegacy (BuildTools' spigot-1.13.2.jar; classes only, no server is
# started), the renames from CraftBukkit's Commodore. Needs JDK 8 (javac / the 1.13.2 classes) and JDK 25.
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"
ROOT="$(cd "$HERE/../.." && pwd)"
JDK8=${JDK8:-/usr/lib/jvm/java-8-openjdk}
JDK25=${JDK25:-/usr/lib/jvm/java-25-openjdk}
M2=${M2:-$HOME/.m2/repository}
BUILDTOOLS=${BUILDTOOLS:-$HOME/MinecraftWorkspace/Buildtools}
WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT
GUAVA=$(find "$M2/com/google/guava/guava" -name 'guava-*.jar' ! -name '*sources*' | sort -V | tail -1)
LEGACY_JAR="$BUILDTOOLS/spigot-1.13.2.jar"

cd "$WORK"
mkdir out
"$JDK8/bin/javac" -d . "$HERE/Dump.java"
for dir in $(ls "$M2/org/spigotmc/spigot-api" | grep -v xml | sort -V); do
    version=${dir%-R0.*}
    jar=$(ls "$M2/org/spigotmc/spigot-api/$dir"/spigot-api-*.jar | grep -v -- -sources | grep -v shaded | head -1)
    "$JDK25/bin/java" -cp ".:$jar:$GUAVA" Dump > "out/$version.tsv"
done
"$JDK8/bin/javac" -cp "$LEGACY_JAR" -d . "$HERE/Legacy.java"
"$JDK8/bin/java" -cp ".:$LEGACY_JAR" Legacy 2>/dev/null | sed 's/^\[STDOUT\]: //' > legacy-1.13.2.tsv
python3 "$HERE/analyze.py" > /dev/null
python3 "$HERE/report.py" "$ROOT/docs/Material.md"
