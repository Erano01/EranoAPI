# Architecture

## Modules

```
EranoAPI/
├── Common/        EranoAPI-Common    platform-free contracts, plain Java 8
├── Cluster/       EranoAPI-Cluster   network state over MySQL / Redis, plain Java 8, bundled by its users
├── Spigot/
│   ├── Core/      EranoAPI-Spigot    the Bukkit API plugins compile against
│   ├── NMS/V*/    one module per NMS revision (V1_8_R1 … V1_21_R7, V26_1 … V26_3), internal
│   └── Dist/      EranoAPI.jar       the plugin: Common + Core + every NMS module
├── Forge/V*/      EranoAPI-Forge     one Forge MDK per Minecraft version
├── Fabric/V*/     EranoAPI-Fabric    one Fabric project per Minecraft version
└── scripts/       builds, publishing, the version reports
```

| Artifact | Version | For |
|---|---|---|
| `EranoAPI-Spigot` | `<api>` | plugins (`compileOnly`, `depend: [EranoAPI]`) |
| `EranoAPI-Common` | `<api>` | anything platform-free |
| `EranoAPI-Cluster` | `<api>` | game servers, hubs, proxies (shaded) |
| `EranoAPI-Forge` / `-Fabric` | `<api>+<mc>` | mods |
| `EranoAPI.jar` | GitHub Releases | server owners (`plugins/`) |

Group `io.github.erano01`, artifact ids are case sensitive. NMS modules and Spigot server jars are never published.

## Picking version-specific code (Spigot)

- A feature is a Core interface. What the Bukkit API can do lives in Core; what it can't lives in the NMS modules of
  the revisions that need it, registered in `META-INF/services`.
- Each provider declares its `VersionRange`. `VersionedServices.select` takes the highest priority, then the most
  specific range; providers that fail to load are skipped.
- Classes of newer APIs are only loaded after a version check, so one jar runs on 1.8 and on 26.x.
- Version names (materials, sounds ...) are generated tables, not code: see [Versioned-APIs.md](Versioned-APIs.md).

## Supported versions

| Platform | Versions |
|---|---|
| Spigot / Paper | 1.8 - 26.3: every NMS revision |
| Forge | 1.21.11, 26.1.2, 26.3 |
| Fabric | 1.21.11, 26.1.2, 26.3 |

| Minecraft | Java |
|---|---|
| 26.1+ | 25 |
| 1.20.5 - 1.21.11 | 21 |
| 1.17 - 1.20.4 | 17 |
| 1.8 - 1.16.5 | 8 |

Builds run on JDK 25; every jar targets its Minecraft version's Java (Spigot Core: Java 8). Core compiles against
the newest spigot-api.

## Builds and releases

| Command | Result |
|---|---|
| `scripts/build-spigot-jars.sh` | Spigot server jars of every revision into `~/.m2` (BuildTools) |
| `mvn install` | `Spigot/Dist/target/EranoAPI.jar` |
| `scripts/build-mods.sh` | every Forge / Fabric jar into `build/mods/` |
| `scripts/material-report/run.sh`, `scripts/registry-report/run.sh` | the version reports and tables |
| `scripts/publish-central.sh` | signed bundle to Maven Central |

A `vX.Y.Z` tag publishes to Maven Central and drafts a GitHub release with `EranoAPI.jar` and the mod jars.
