#!/usr/bin/env bash
#
# Publishes EranoAPI to Maven Central as ONE deployment:
#   - Maven:  EranoAPI (parent pom), EranoAPI-Common, EranoAPI-Cluster, EranoAPI-Spigot
#   - Gradle: EranoAPI-Forge / EranoAPI-Fabric of every Forge/V* and Fabric/V* project
# Everything is built with sources + javadoc, signed, deployed into build/central/staging, zipped and
# uploaded through the Central Portal publisher API.
#
# Spigot server jars and the NMS modules are never published (Mojang code, internal modules).
#
# Environment:
#   MAVEN_GPG_KEY          armored secret key (gpg --armor --export-secret-keys <id>)   required
#   MAVEN_GPG_PASSPHRASE   its passphrase                                               required if set on the key
#   CENTRAL_USERNAME       Central Portal user token, username part                     required unless DRY_RUN=1
#   CENTRAL_PASSWORD       Central Portal user token, password part                     required unless DRY_RUN=1
#   PUBLISHING_TYPE        USER_MANAGED (default: you press "Publish" in the portal) or AUTOMATIC
#   DRY_RUN=1              build and zip the bundle, don't upload
#   JAVA25_HOME            JDK 25 (default: auto-detected)

set -uo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WORK="$ROOT/build/central"
STAGING="$WORK/staging"
BUNDLE="$WORK/bundle.zip"
PORTAL="https://central.sonatype.com"

log()  { printf '\033[1;34m==>\033[0m %s\n' "$*"; }
die()  { printf '\033[1;31mxx\033[0m %s\n' "$*" >&2; exit 1; }

find_jdk25() {
    if [ -n "${JAVA25_HOME:-}" ]; then printf '%s' "$JAVA25_HOME"; return 0; fi
    local dir
    for dir in /usr/lib/jvm/* /usr/lib64/jvm/* "$HOME"/.sdkman/candidates/java/*; do
        if sed -n 's/^JAVA_VERSION="\(.*\)"/\1/p' "$dir/release" 2>/dev/null | grep -q '^25'; then
            printf '%s' "$dir"; return 0
        fi
    done
    return 1
}

[ -n "${MAVEN_GPG_KEY:-}" ] || die "MAVEN_GPG_KEY isn't set (armored secret key)"
if [ "${DRY_RUN:-0}" != "1" ]; then
    [ -n "${CENTRAL_USERNAME:-}" ] && [ -n "${CENTRAL_PASSWORD:-}" ] || die "CENTRAL_USERNAME / CENTRAL_PASSWORD (Central Portal user token) aren't set"
fi
PUBLISHING_TYPE=${PUBLISHING_TYPE:-USER_MANAGED}
case $PUBLISHING_TYPE in USER_MANAGED|AUTOMATIC) ;; *) die "PUBLISHING_TYPE must be USER_MANAGED or AUTOMATIC" ;; esac
JDK=$(find_jdk25) || die "JDK 25 not found (set JAVA25_HOME)"
export JAVA_HOME="$JDK"

rm -rf "$WORK"
mkdir -p "$STAGING"

# 1. Maven. deploy also installs, so EranoAPI-Common lands in ~/.m2 for the Gradle builds below.
#    Only Common, Cluster and Core (+ the parent pom): the NMS modules would need every Spigot jar and are internal.
log "Maven: EranoAPI, EranoAPI-Common, EranoAPI-Cluster, EranoAPI-Spigot"
(cd "$ROOT" && mvn -B -q -Prelease -pl Common,Cluster,Spigot/Core -am deploy \
    -DaltDeploymentRepository="staging::file://$STAGING") || die "Maven release build failed"

# 2. Gradle: every Forge / Fabric version project.
for dir in "$ROOT"/Forge/V* "$ROOT"/Fabric/V*; do
    [ -x "$dir/gradlew" ] || continue
    project=${dir#"$ROOT"/}
    log "Gradle: $project"
    (cd "$dir" && ./gradlew publishMavenJavaPublicationToStagingRepository \
        -PstagingDir="file://$STAGING" -Psign=true --console=plain -q) || die "$project failed"
done

# Central rejects released POMs that reference -SNAPSHOT versions; fail here instead of after the upload.
snapshots=$(grep -rl --include='*.pom' -- '-SNAPSHOT' "$STAGING" || true)
[ -z "$snapshots" ] || die "POMs referencing -SNAPSHOT versions (Maven Central rejects them): ${snapshots//$'\n'/ }"

# 3. Bundle: Central computes its own maven-metadata.xml, so leave those out.
find "$STAGING" -name 'maven-metadata.xml*' -delete
(cd "$STAGING" && zip -qr "$BUNDLE" .) || die "Could not create $BUNDLE"

log "Bundle: $BUNDLE"
(cd "$STAGING" && find . -name '*.pom' | sed 's|^\./||; s|/[^/]*$||' | sort)

if [ "${DRY_RUN:-0}" = "1" ]; then
    log "DRY_RUN=1, not uploading"
    exit 0
fi

# 4. Upload. USER_MANAGED: the deployment is validated, then waits for "Publish" in the portal.
version=$(sed -n 's|^\t<version>\(.*\)</version>$|\1|p' "$ROOT/pom.xml" | head -1)
token=$(printf '%s:%s' "$CENTRAL_USERNAME" "$CENTRAL_PASSWORD" | base64 | tr -d '\n')
log "Uploading to Maven Central ($PUBLISHING_TYPE)"
deployment=$(curl -fsS -X POST \
    -H "Authorization: Bearer $token" \
    -F "bundle=@$BUNDLE" \
    "$PORTAL/api/v1/publisher/upload?name=EranoAPI-$version&publishingType=$PUBLISHING_TYPE") \
    || die "Upload failed"
log "Deployment $deployment uploaded: $PORTAL/publishing/deployments"
