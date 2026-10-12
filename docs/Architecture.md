# Architecture

## Modules

```
EranoAPI/
├── Common/        EranoAPI-Common    platform-free contracts and the menu / dialog model, plain Java 8
├── Cluster/       EranoAPI-Cluster   network state over MySQL / Redis, plain Java 8, bundled by its users
├── Spigot/
│   ├── Core/      EranoAPI-Spigot    the Bukkit API plugins compile against
│   ├── NMS/V*/    one module per NMS revision (V1_8_R1 … V1_21_R7, V26_1 … V26_3), internal
│   └── Dist/      EranoAPI.jar       the plugin: Common + Core + Paper Core + every NMS and Paper NMS module
├── Paper/
│   ├── Core/      EranoAPI-Paper-Core  providers built on Paper's API (no reflection), internal
│   └── NMS/V*/    Paper-V*           the Paper twin of a Spigot NMS revision, internal
├── Bedrock/
│   ├── Crossplay/    EranoAPI-Bedrock-Crossplay     Bedrock players on Spigot / Paper (Geyser / Floodgate), empty
│   ├── Nukkit/       EranoAPI-Bedrock-Nukkit        Nukkit and Nukkit-MOT, empty
│   ├── PowerNukkitX/ EranoAPI-Bedrock-PowerNukkitX  PowerNukkitX, empty
│   ├── Allay/        EranoAPI-Bedrock-Allay         Allay, empty
│   └── Waterdog/     EranoAPI-Bedrock-Waterdog      WaterdogPE proxy, empty
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

## Paper and Purpur (Spigot)

### Core

`Spigot/Core` (`EranoAPI-Spigot`) holds the interfaces plugins compile against (`TPSHandlerFactory`,
`HologramServiceProvider` ...) and the providers built on the Bukkit API alone. Paper is a fork of Spigot:
it carries the whole Bukkit API and runs the same plugins, so Spigot's Core is Paper's Core too. What
breaks on Paper is NMS, not Core.

```
Spigot/Core   Spigot + Paper + Purpur (Bukkit API)
Spigot/NMS    Spigot only
Paper/NMS     Paper / Purpur only
```

`Paper/Core` (`EranoAPI-Paper-Core`) is only for Paper-only features: API the Bukkit API doesn't have
(Adventure `Component`, Brigadier commands, async chunk loading, `Server#getTPS()` ...). Spigot/Core
compiles against spigot-api and reaches those by reflection (`PaperTPSHandlerFactory`); Paper/Core
compiles against paper-api, so it doesn't need to.

- Its providers use `isAvailable() = Platform.isPaper()` and the same `META-INF/services` files.
- Internal: shaded into `EranoAPI.jar`, never published. Plugins still depend on `EranoAPI-Spigot` only.
- A published `EranoAPI-Paper` artifact is only needed once plugins should get Paper-only interfaces,
  e.g. a `MessageService` taking an Adventure `Component`. That's an API design decision; nothing needs
  it yet.

### NMS

Paper (and Purpur, a fork of Paper) patch NMS differently from Spigot, and from 1.20.5 to 1.21.11 run on
Mojang's names with an unrelocated `org.bukkit.craftbukkit`, where Spigot runs on Spigot's names.
So every Spigot NMS revision gets a Paper twin:

```
Spigot/NMS/V1_21_R7   me.erano.com.V1_21_R7.*         Spigot names (reobf), compiled against Spigot
Paper/NMS/V1_21_R7    me.erano.com.paper.V1_21_R7.*   Paper's names, compiled against Paper
```

- Both register in the same `META-INF/services` file with the same `VersionRange`. The Paper provider is
  only available where `Platform.isPaper()` and has `priority() = 1`; the Spigot providers of 1.20.5 -
  1.21.11 are only available where it's false.
- `EranoAPI.jar`'s manifest declares `paperweight-mappings-namespace: mojang`, which turns Paper's plugin
  remapper off. Spigot and Paper before 1.20.5 ignore it.
- A service without a Paper twin yet falls back to the Bukkit API provider on Paper 1.20.5 - 1.21.11, or
  logs that it isn't supported.
- Paper has no build for `v1_8_R1`, `v1_8_R2` and `v1_9_R1`.
- Paper 1.17.1 - 1.20.4 runs on Spigot's names: those twins compile against Paper remapped to Mojang's
  names (`remapped-mojang`, from `build-paper-jars.sh`) and are reobfuscated like Spigot/NMS.

| Server | Selected NMS provider |
|---|---|
| Spigot | `Spigot/NMS` |
| Paper / Purpur | `Paper/NMS` |

## Bedrock

EranoAPI is Java, so it only runs inside Java servers and proxies: one module under `Bedrock/` each.
Bedrock clients update themselves, so a module targets the current Bedrock version only: no version
matrix like Spigot's 1.8 - 26.x.

| Module | Runs on | Players | Features |
|---|---|---|---|
| `Crossplay` | Spigot / Paper + Geyser (protocol translation) + Floodgate (no Java account needed) | Java and Bedrock | all |
| `Nukkit` | Nukkit, Nukkit-MOT (same API) | Bedrock | all |
| `PowerNukkitX` | PowerNukkitX (Nukkit fork with its own API, like Paper next to Spigot) | Bedrock | all |
| `Allay` | Allay | Bedrock | all |
| `Waterdog` | WaterdogPE proxy, in front of any backend: BDS, Endstone, LeviLamina, Dragonfly ... | Bedrock | UI only: forms, titles, boss bars (the proxy can't see the world) |

- **Order: Crossplay, Nukkit / PowerNukkitX, Waterdog, Allay.** Crossplay reaches the most players: every
  Java server EranoAPI supports, Bedrock players included.
- **Crossplay.** Geyser translates chest GUIs, titles, boss bars and armor stand holograms. What's added is
  per-player: `isBedrock(player)` and showing `DialogMenu`s as Bedrock forms. Floodgate's API works on
  every backend, also with Geyser on the proxy; Geyser's own API only where Geyser runs as a plugin.
  EranoAPI uses whichever is installed (Floodgate, then Geyser) and changes nothing without them.
  Geyser 2.11.3 doesn't start on Paper 26.3 yet.
- **Forms work on every Java version.** A Bedrock player sees `DialogMenu`s as forms even on 1.8, since
  forms don't depend on the Java protocol: `TextInput` → input, `BooleanInput` → toggle, `NumberInput` →
  slider, `OptionInput` → dropdown; buttons only → simple form, inputs → custom form.
- **Native servers** implement the platform-independent API in `Common` (`Menu<P>`, `DialogMenu<P>`, `P`
  being the platform's player type), the same layer Fabric and Forge need.
- **Not supported:** servers that can't run Java code: PocketMine-MP (PHP, archived in July 2026), Endstone
  and LeviLamina (C++), Dragonfly (Go), BDS. Their players are reached through `Waterdog`.

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
| `scripts/build-paper-jars.sh` | Paper server jars of every revision into `~/.m2` (paperclip) |
| `mvn install` | `Spigot/Dist/target/EranoAPI.jar` |
| `scripts/build-mods.sh` | every Forge / Fabric jar into `build/mods/` |
| `scripts/material-report/run.sh`, `scripts/registry-report/run.sh` | the version reports and tables |
| `scripts/publish-central.sh` | signed bundle to Maven Central |

A `vX.Y.Z` tag publishes to Maven Central and drafts a GitHub release with `EranoAPI.jar` and the mod jars.
