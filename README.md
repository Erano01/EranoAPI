# EranoAPI

Version-agnostic Minecraft API for Spigot, Forge and Fabric.

- **Spigot:** one plugin jar for every version from 1.8 to 26.x. Version specific code (NMS) is
  picked at runtime.
- **Forge / Fabric:** one jar per Minecraft version (1.21.11, 26.1.2, 26.3), all with the same API, so
  your mod's code stays the same across versions.
- Open source under the MIT license

NOTE: Read Dependencies.md before start doing anything in minecraft development.

Features, architecture and roadmap: [docs/](docs/README.md).

## Modules

```
EranoAPI/
├── Common/        EranoAPI-Common   platform-agnostic contracts (plain Java 8)
├── Cluster/       EranoAPI-Cluster  a minigame network's shared state over MySQL or Redis (plain Java 8)
├── Spigot/        (aggregator: mvn -pl Spigot -amd builds Core, NMS and Dist)
│   ├── Core/      EranoAPI-Spigot   Bukkit-side API, what plugin developers compile against
│   ├── NMS/       one module per NMS revision (V1_8_R1 … V1_21_R7, V26_1 … V26_3), internal
│   └── Dist/      EranoAPI.jar      the plugin server owners drop into plugins/
├── Forge/V*/      EranoAPI-Forge    one Forge MDK per Minecraft version
├── Fabric/V*/     EranoAPI-Fabric   one Fabric project per Minecraft version
└── scripts/       build and publishing scripts
```

## Usage

Group: `io.github.erano01`. Coordinates are case sensitive.

### Spigot

```xml
<dependency>
  <groupId>io.github.erano01</groupId>
  <artifactId>EranoAPI-Spigot</artifactId>
  <version>1.0.0-alpha.3</version>
  <scope>provided</scope>
</dependency>
```

```kotlin
compileOnly("io.github.erano01:EranoAPI-Spigot:1.0.0-alpha.3")
```

Add `depend: [EranoAPI]` to your `plugin.yml`; the EranoAPI plugin provides the classes at runtime.

### Fabric / Forge

The version is `<EranoAPI version>+<Minecraft version>`:

```kotlin
// Fabric
modImplementation("io.github.erano01:EranoAPI-Fabric:1.0.0-alpha.3+26.3")
include("io.github.erano01:EranoAPI-Fabric:1.0.0-alpha.3+26.3")      // jar-in-jar, optional

// Forge
implementation("io.github.erano01:EranoAPI-Forge:1.0.0-alpha.3+26.3")
```

EranoAPI-Forge already contains EranoAPI-Common's classes; don't add EranoAPI-Common separately on
Forge (Forge loads mods as JPMS modules and the same package from two jars fails to load).

### Materials

`EranoMaterial`: every material of the newest Minecraft by its newest name, on every server from 1.8 to 26.x. On
1.13+ the server's `Material` of that name (or of its older name: `SHORT_GRASS` is `GRASS` on 1.20.2); on 1.8 -
1.12 the old material with its data value (`RED_WOOL` is `WOOL:14`). Generated from Spigot's own data, see
[docs/Material.md](docs/Material.md). The other APIs of this kind (sounds, enchantments, potion effects, particles,
entity types, game rules ...) and their order: [docs/Versioned-APIs.md](docs/Versioned-APIs.md).

```java
ItemStack wool = EranoMaterial.RED_WOOL.parseItem(16);          // WOOL:14 on 1.8, RED_WOOL on 26.x
EranoMaterial.match("WOOD_SWORD").ifPresent(...);               // WOODEN_SWORD; "minecraft:red_wool", "WOOL:14", "A|B"
EranoMaterial.of(player.getItemInHand());                       // what it is, data value included on 1.8
EranoMaterial.GRANITE.setType(block);                           // STONE:1 on 1.8
```

### Sounds, particles, potions, enchantments

The same idea for the other names that changed between versions, each by its newest name on every server from 1.8:
`EranoSound` (renamed in 1.9 and 1.13, matched by the files Mojang plays), `EranoParticle` (renamed in 1.20.5; on
1.8, which has no particle API, EranoAPI sends the packet) and `EranoEffect`, `EranoPotionEffect`, `EranoPotionType`
(the item too: a data value on 1.8, `PotionData` up to 1.20.1), `EranoEnchantment`. Reports: [docs/Sound.md](docs/Sound.md),
[docs/Particle.md](docs/Particle.md), [docs/Potion.md](docs/Potion.md), [docs/Enchantment.md](docs/Enchantment.md).

```java
EranoSound.ENTITY_PLAYER_LEVELUP.play(player, 1f, 1f);                     // LEVEL_UP on 1.8
EranoParticle.DUST.spawn(location, 1, 0, 0, 0, 0, Color.AQUA);              // REDSTONE packet on 1.8
EranoPotionEffect.STRENGTH.apply(player, 200, 1);                           // INCREASE_DAMAGE up to 1.20.4
EranoEnchantment.SHARPNESS.enchant(sword, 2);                               // DAMAGE_ALL up to 1.20.4
ItemStack potion = EranoPotionType.LONG_SWIFTNESS.parseItem(EranoPotionType.Form.SPLASH, 1);
```

### Cluster

What the servers of a minigame network know about each other: every server's arenas and the players on
their way to one, over MySQL / MariaDB or Redis. Plain Java, for any platform's game server, hub or proxy.
It isn't part of the EranoAPI plugin: bundle it (shade and relocate `me.erano.com.api.cluster`, Jedis,
HikariCP, the MariaDB driver) into your plugin. See [Cluster/README.md](Cluster/README.md).

```kotlin
implementation("io.github.erano01:EranoAPI-Cluster:1.0.0-alpha.3")
```

## Building

Requirements: JDK 25 (Maven and Gradle run on it; every project still targets its own Minecraft
version's Java) and, for the Spigot NMS modules, the Spigot server jars in `~/.m2`. Spigot doesn't
publish those and they may not be redistributed, so they're built locally with BuildTools:

```bash
./scripts/build-spigot-jars.sh          # BuildTools, every NMS revision 1.8 -> 26.x (takes hours the first time)
mvn clean install                       # -> Spigot/Dist/target/EranoAPI.jar
./scripts/build-mods.sh                 # every Forge/V* and Fabric/V* project -> build/mods/
```

On Windows use `scripts\build-spigot-jars.bat`. JDK requirements per Minecraft version are listed in
[dependencies.md](Dependenceis.md).

## Releasing

`scripts/publish-central.sh` builds every published artifact (EranoAPI-Common, EranoAPI-Spigot and the
Forge / Fabric jars) with sources, javadoc and GPG signatures and uploads them to Maven Central as one
deployment. By default the deployment waits for "Publish" at https://central.sonatype.com/publishing;
`DRY_RUN=1` only builds the bundle. Pushing a `vX.Y.Z` tag runs the same script in GitHub Actions and
also creates a draft GitHub release with `EranoAPI.jar` (the Spigot plugin) and every Forge / Fabric jar;
versions with a suffix (`1.0.0-alpha.1`) are marked as pre-releases.

It needs `MAVEN_GPG_KEY` (armored secret key), `MAVEN_GPG_PASSPHRASE`, `CENTRAL_USERNAME` and
`CENTRAL_PASSWORD` (a Central Portal user token), as environment variables locally or as repository
secrets in CI.

## Contributing

Feel free to open a pull request or create an issue to contribute.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
