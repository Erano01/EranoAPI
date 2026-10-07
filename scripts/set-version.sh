#!/usr/bin/env bash
#
# Sets the EranoAPI version everywhere it's defined:
#   - every Maven module (root pom + parent references), through versions-maven-plugin
#   - every Forge/V* and Fabric/V* gradle.properties: <version>+<Minecraft version> and the
#     EranoAPI-Common version they compile against
#   - the dependency examples in README.md
# plugin.yml takes the Maven version at build time.
#
# Usage: scripts/set-version.sh 1.0.0-alpha.1

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
new=${1:-}
[[ $new =~ ^[0-9]+\.[0-9]+\.[0-9]+(-[0-9A-Za-z.]+)?$ ]] || { echo "Usage: $0 <major.minor.patch[-pre.release]>" >&2; exit 1; }

old=$(sed -n 's|^\t<version>\(.*\)</version>$|\1|p' "$ROOT/pom.xml" | head -1)
[ -n "$old" ] || { echo "Could not read the version from pom.xml" >&2; exit 1; }
echo "$old -> $new"

(cd "$ROOT" && mvn -q -B versions:set -DnewVersion="$new" -DprocessAllModules=true -DgenerateBackupPoms=false)

for f in "$ROOT"/Forge/V*/gradle.properties "$ROOT"/Fabric/V*/gradle.properties; do
    [ -f "$f" ] || continue
    sed -i -E "s/^((mod_)?version=)[^+]*\+/\1$new+/; s/^eranoapi_common_version=.*/eranoapi_common_version=$new/" "$f"
done

# Coordinates in README: group:artifact:<old>[+mc] and <version><old></version>
sed -i "s|:$old\([+\"]\)|:$new\1|g; s|<version>$old</version>|<version>$new</version>|g" "$ROOT/README.md"

grep -rn --include=pom.xml -l "<version>$old</version>" "$ROOT" | grep -v /target/ && { echo "Some POMs still reference $old" >&2; exit 1; }
echo "Done. Check with: git diff --stat"
