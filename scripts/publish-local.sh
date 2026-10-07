#!/usr/bin/env bash
#
# Releases the tagged commit from this machine instead of GitHub Actions:
#   1. Maven Central (scripts/publish-central.sh, USER_MANAGED: press "Publish" in the portal)
#   2. a draft GitHub release with EranoAPI.jar and every Forge / Fabric jar
#
# Credentials never leave this terminal:
#   - Central user token: <username> / <password> from a Maven settings.xml snippet (argument 1)
#   - GPG key: exported from the local keyring (argument 2, default: the only secret key); its
#     passphrase is asked once
#
# Usage: scripts/publish-local.sh <settings.xml with the Central token> [gpg key id]

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
settings=${1:-}
key=${2:-}

die() { printf '\033[1;31mxx\033[0m %s\n' "$*" >&2; exit 1; }
log() { printf '\033[1;34m==>\033[0m %s\n' "$*"; }

[ -f "$settings" ] || die "Usage: $0 <settings.xml with the Central token> [gpg key id]"

version=$(sed -n 's|^\t<version>\(.*\)</version>$|\1|p' "$ROOT/pom.xml" | head -1)
tag="v$version"
git -C "$ROOT" rev-parse -q --verify "refs/tags/$tag" >/dev/null || die "Tag $tag doesn't exist"
[ "$(git -C "$ROOT" rev-parse HEAD)" = "$(git -C "$ROOT" rev-parse "$tag^{commit}")" ] \
    || die "HEAD isn't $tag: run 'git checkout $tag' first"

# Central token from the settings snippet; first <username> / <password> in the file.
CENTRAL_USERNAME=$(sed -n 's|.*<username>\(.*\)</username>.*|\1|p' "$settings" | head -1)
CENTRAL_PASSWORD=$(sed -n 's|.*<password>\(.*\)</password>.*|\1|p' "$settings" | head -1)
[ -n "$CENTRAL_USERNAME" ] && [ -n "$CENTRAL_PASSWORD" ] || die "No <username> / <password> in $settings"
export CENTRAL_USERNAME CENTRAL_PASSWORD

if [ -z "$key" ]; then
    key=$(gpg --list-secret-keys --with-colons | awk -F: '/^fpr/ { print $10; exit }')
    [ -n "$key" ] || die "No GPG secret key found"
fi
read -rsp "GPG passphrase for $key: " MAVEN_GPG_PASSPHRASE; echo
export MAVEN_GPG_PASSPHRASE
# Passphrase on stdin rather than the command line, where other processes could see it.
MAVEN_GPG_KEY=$(gpg --batch --pinentry-mode loopback --passphrase-fd 0 --armor --export-secret-keys "$key" <<< "$MAVEN_GPG_PASSPHRASE") \
    || die "Could not export GPG key $key (wrong passphrase?)"
[ -n "$MAVEN_GPG_KEY" ] || die "GPG key $key exported empty"
export MAVEN_GPG_KEY

log "1/2 Maven Central ($tag)"
"$ROOT/scripts/publish-central.sh"

log "2/2 GitHub release ($tag, draft)"
"$ROOT/scripts/build-mods.sh"
(cd "$ROOT" && JAVA_HOME="${JAVA25_HOME:-/usr/lib/jvm/java-25-openjdk}" mvn -B -q package -pl Spigot/Dist -am -DskipTests)
prerelease=()
[[ $version == *-* ]] && prerelease=(--prerelease)
gh release create "$tag" "$ROOT"/build/mods/*.jar "$ROOT/Spigot/Dist/target/EranoAPI.jar" \
    --draft "${prerelease[@]}" --title "EranoAPI $version" --generate-notes

log "Done. Publish both when they look right:"
echo "    Maven Central:  https://central.sonatype.com/publishing/deployments"
echo "    GitHub:         gh release view $tag --web"
