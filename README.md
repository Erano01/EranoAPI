# EranoAPI

Version-agnostic Minecraft API. One plugin jar for every Spigot version from 1.8 to 26.x; Forge and
Fabric support is in progress.

- Modular structure
- Open source under the MIT license

## Modules

```
EranoAPI/
├── Common/        EranoAPI-Common   platform-agnostic contracts (plain Java 8)
└── Spigot/
    ├── Core/      EranoAPI-Spigot   Bukkit-side API, what plugin developers compile against
    ├── NMS/       one module per NMS revision (V1_8_R1 … V1_21_R7, V26_1 … V26_3), internal
    └── Dist/      EranoAPI.jar      the plugin server owners drop into plugins/
```

## Usage

Group: `io.github.erano01`

```xml
<dependency>
  <groupId>io.github.erano01</groupId>
  <artifactId>EranoAPI-Spigot</artifactId>
  <version>1.0.0</version>
  <scope>provided</scope>
</dependency>
```

```kotlin
compileOnly("io.github.erano01:EranoAPI-Spigot:1.0.0")
```

Coordinates are case sensitive. Add `depend: [EranoAPI]` to your `plugin.yml`; the EranoAPI plugin
provides the classes at runtime.

## Building

Requirements: JDK 25 for Maven (the 26.x Spigot jars are Java 25 class files; output still targets
Java 8) and the Spigot server jars in `~/.m2`, which Spigot doesn't publish:

```bash
./scripts/build-spigot-jars.sh          # BuildTools, every NMS revision 1.8 -> 26.x (takes hours the first time)
mvn clean install                       # -> Spigot/Dist/target/EranoAPI.jar
```

On Windows use `scripts\build-spigot-jars.bat`. JDK requirements per Minecraft version are listed in
[dependencies.md](dependencies.md).

## Contributing

Feel free to open a pull request or create an issue to contribute.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
