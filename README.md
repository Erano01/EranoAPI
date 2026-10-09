# EranoAPI

Version-agnostic Minecraft API for Spigot, Forge and Fabric.

- **Spigot:** one plugin jar for every version from 1.8 to 26.x. Version specific code (NMS) is
  picked at runtime.
- **Forge / Fabric:** one jar per Minecraft version (1.21.11, 26.1.2, 26.3), all with the same API, so
  your mod's code stays the same across versions.
- Open source under the MIT license

| | |
|---|---|
| **Download** (server owners) | [SpigotMC](https://www.spigotmc.org/resources/eranoapi.139499/) · [GitHub Releases](https://github.com/Erano01/EranoAPI/releases) |
| **Maven Central** (developers) | [io.github.erano01:EranoAPI-Spigot](https://central.sonatype.com/artifact/io.github.erano01/EranoAPI-Spigot) |
| **Documentation** | [emberlava.network/eranoapi](https://www.emberlava.network/eranoapi/) |

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

Group `io.github.erano01`, on [Maven Central](https://central.sonatype.com/namespace/io.github.erano01) (no extra
repository needed). Coordinates are case sensitive.

| Artifact | For | Version | Scope |
|---|---|---|---|
| `EranoAPI-Spigot` | Spigot / Paper plugins | `1.0.0-alpha.3` | provided: the EranoAPI plugin supplies it |
| `EranoAPI-Common` | anything (plain Java 8): `MinecraftVersion`, `YamlUpdate`, plugin channel contracts | `1.0.0-alpha.3` | already inside `EranoAPI-Spigot` / `-Forge` / `-Fabric` |
| `EranoAPI-Cluster` | minigame servers, hubs, proxies (plain Java 8) | `1.0.0-alpha.3` | bundled into your jar |
| `EranoAPI-Fabric` | Fabric mods | `1.0.0-alpha.3+<mc>` | |
| `EranoAPI-Forge` | Forge mods | `1.0.0-alpha.3+<mc>` | |

`<mc>` is `1.21.11`, `26.1.2` or `26.3`.

### Spigot / Paper plugin

Maven:

```xml
<dependency>
  <groupId>io.github.erano01</groupId>
  <artifactId>EranoAPI-Spigot</artifactId>
  <version>1.0.0-alpha.3</version>
  <scope>provided</scope>
</dependency>
```

Gradle (Kotlin):

```kotlin
dependencies {
    compileOnly("io.github.erano01:EranoAPI-Spigot:1.0.0-alpha.3")
}
```

Gradle (Groovy):

```groovy
dependencies {
    compileOnly 'io.github.erano01:EranoAPI-Spigot:1.0.0-alpha.3'
}
```

`plugin.yml`:

```yaml
depend: [EranoAPI]
```

Server owners install the plugin jar ([SpigotMC](https://www.spigotmc.org/resources/eranoapi.139499/) or
[GitHub Releases](https://github.com/Erano01/EranoAPI/releases)); it provides the classes at runtime, so don't shade
`EranoAPI-Spigot`.

### Common (plain Java)

Only needed on its own outside Spigot / Forge / Fabric (a proxy plugin, a tool); the platform artifacts already
contain it.

Maven:

```xml
<dependency>
  <groupId>io.github.erano01</groupId>
  <artifactId>EranoAPI-Common</artifactId>
  <version>1.0.0-alpha.3</version>
</dependency>
```

Gradle (Kotlin):

```kotlin
dependencies {
    implementation("io.github.erano01:EranoAPI-Common:1.0.0-alpha.3")
}
```

Gradle (Groovy):

```groovy
dependencies {
    implementation 'io.github.erano01:EranoAPI-Common:1.0.0-alpha.3'
}
```

### Cluster (plain Java)

Not part of the EranoAPI plugin: bundle it into your jar (its dependencies come along transitively) and relocate it
with Jedis and its libraries (commons-pool2, Gson, org.json), HikariCP, the MariaDB driver and SLF4J, so two plugins
bundling different versions don't clash.

Maven (with the shade plugin):

```xml
<dependency>
  <groupId>io.github.erano01</groupId>
  <artifactId>EranoAPI-Cluster</artifactId>
  <version>1.0.0-alpha.3</version>
</dependency>

<!-- build/plugins -->
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-shade-plugin</artifactId>
  <version>3.6.0</version>
  <executions>
    <execution>
      <phase>package</phase>
      <goals><goal>shade</goal></goals>
      <configuration>
        <relocations>
          <relocation>
            <pattern>me.erano.com.api.cluster</pattern>
            <shadedPattern>your.plugin.libs.cluster</shadedPattern>
          </relocation>
          <relocation>
            <pattern>redis.clients</pattern>
            <shadedPattern>your.plugin.libs.jedis</shadedPattern>
          </relocation>
          <relocation>
            <pattern>org.apache.commons.pool2</pattern>
            <shadedPattern>your.plugin.libs.pool2</shadedPattern>
          </relocation>
          <relocation>
            <pattern>com.google.gson</pattern>
            <shadedPattern>your.plugin.libs.gson</shadedPattern>
          </relocation>
          <relocation>
            <pattern>org.json</pattern>
            <shadedPattern>your.plugin.libs.json</shadedPattern>
          </relocation>
          <relocation>
            <pattern>com.zaxxer.hikari</pattern>
            <shadedPattern>your.plugin.libs.hikari</shadedPattern>
          </relocation>
          <relocation>
            <pattern>org.mariadb.jdbc</pattern>
            <shadedPattern>your.plugin.libs.mariadb</shadedPattern>
          </relocation>
          <relocation>
            <pattern>org.slf4j</pattern>
            <shadedPattern>your.plugin.libs.slf4j</shadedPattern>
          </relocation>
        </relocations>
      </configuration>
    </execution>
  </executions>
</plugin>
```

Gradle (Kotlin, with [Shadow](https://gradleup.com/shadow/)):

```kotlin
plugins {
    id("com.gradleup.shadow") version "<latest>"
}

dependencies {
    implementation("io.github.erano01:EranoAPI-Cluster:1.0.0-alpha.3")
}

tasks.shadowJar {
    relocate("me.erano.com.api.cluster", "your.plugin.libs.cluster")
    relocate("redis.clients", "your.plugin.libs.jedis")
    relocate("org.apache.commons.pool2", "your.plugin.libs.pool2")
    relocate("com.google.gson", "your.plugin.libs.gson")
    relocate("org.json", "your.plugin.libs.json")
    relocate("com.zaxxer.hikari", "your.plugin.libs.hikari")
    relocate("org.mariadb.jdbc", "your.plugin.libs.mariadb")
    relocate("org.slf4j", "your.plugin.libs.slf4j")
}
```

Gradle (Groovy):

```groovy
plugins {
    id 'com.gradleup.shadow' version '<latest>'
}

dependencies {
    implementation 'io.github.erano01:EranoAPI-Cluster:1.0.0-alpha.3'
}

shadowJar {
    relocate 'me.erano.com.api.cluster', 'your.plugin.libs.cluster'
    relocate 'redis.clients', 'your.plugin.libs.jedis'
    relocate 'org.apache.commons.pool2', 'your.plugin.libs.pool2'
    relocate 'com.google.gson', 'your.plugin.libs.gson'
    relocate 'org.json', 'your.plugin.libs.json'
    relocate 'com.zaxxer.hikari', 'your.plugin.libs.hikari'
    relocate 'org.mariadb.jdbc', 'your.plugin.libs.mariadb'
    relocate 'org.slf4j', 'your.plugin.libs.slf4j'
}
```

### Fabric mod

The version is `<EranoAPI version>+<Minecraft version>`. Fabric mods are built with Gradle (Loom); `include` puts
EranoAPI inside your mod (jar-in-jar), without it players install EranoAPI themselves.

Gradle (Kotlin):

```kotlin
dependencies {
    modImplementation("io.github.erano01:EranoAPI-Fabric:1.0.0-alpha.3+26.3")
    include("io.github.erano01:EranoAPI-Fabric:1.0.0-alpha.3+26.3")
}
```

Gradle (Groovy):

```groovy
dependencies {
    modImplementation 'io.github.erano01:EranoAPI-Fabric:1.0.0-alpha.3+26.3'
    include 'io.github.erano01:EranoAPI-Fabric:1.0.0-alpha.3+26.3'
}
```

Maven (coordinates only, e.g. for tooling; Loom itself is Gradle):

```xml
<dependency>
  <groupId>io.github.erano01</groupId>
  <artifactId>EranoAPI-Fabric</artifactId>
  <version>1.0.0-alpha.3+26.3</version>
</dependency>
```

### Forge mod

The version is `<EranoAPI version>+<Minecraft version>`. Forge mods are built with Gradle (ForgeGradle).

Gradle (Kotlin):

```kotlin
dependencies {
    implementation("io.github.erano01:EranoAPI-Forge:1.0.0-alpha.3+26.3")
}
```

Gradle (Groovy):

```groovy
dependencies {
    implementation 'io.github.erano01:EranoAPI-Forge:1.0.0-alpha.3+26.3'
}
```

Maven (coordinates only; ForgeGradle itself is Gradle):

```xml
<dependency>
  <groupId>io.github.erano01</groupId>
  <artifactId>EranoAPI-Forge</artifactId>
  <version>1.0.0-alpha.3+26.3</version>
</dependency>
```

EranoAPI-Forge already contains EranoAPI-Common's classes; don't add EranoAPI-Common separately on Forge (Forge
loads mods as JPMS modules and the same package from two jars fails to load).

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
HikariCP, the MariaDB driver) into your plugin: see [Usage](#cluster-plain-java) and [Cluster/README.md](Cluster/README.md).

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
