### Minecraft Versions JDK Requirements
```
minecraft min java (LTS - Long Term Support) requirements:
26.1 and later -> java 25
1.20.5 & 1.20.6 (1_20_R4) – 1.21.11 (R7) -> java 21
1.17 – 1.20.4 (1_20_R3) -> Java 17
1.13 – 1.16.5 -> java 11
1.8 - 1.12.2 -> java 8
```

### Spigot NMS & OBC Interfaces For Each Version
- https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-legacy/ (for 1.8 - 1.9)
- https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-10-1-15/ (1.10 - 1.15)
- https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-16/ (1.16 - 1.20)
- https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-21/ (1.21 - 1.21.11)

// Her bir version için kaç tane interface (R1, R2, R3 ...) olursa olsun hepsini destekleyeceğiz.
// Eğer bir arayüzü birden fazla minecraft versiyonu destekliyorsa atıyorum 1.21.10 ve 1.21.9 bunların ikiside 1_21_R6 bu durumda 1.21.10 versiyonunu seçeceğiz o arayüz için.

### Minecraft Mappings
```
1.14.4 - Latest -> Mojang's official & Searge & Spigot & Intermediary & Yarn mappings
1.13.2 - 1.14.3 -> Searge & Spigot & Intermediary & Yarn mappings
1.8.8 - 1.13.2 -> Searge & Spigot mappings

// Yarn -> Fabric
// Searge -> Forge
```

## Spigot Jars
BuildTools ile `spigot-26.2` build edildikten sonra local `.m2` repository'sine kurulan jar'lar
(JADX projesine eklenenler):

```
~/.m2/repository/org/spigotmc/
spigot-api/26.2-R0.1-SNAPSHOT/spigot-api-26.2-R0.1-SNAPSHOT.jar
spigot/26.2-R0.1-SNAPSHOT/spigot-26.2-R0.1-SNAPSHOT.jar
```

- `spigot-api-...jar`: Bukkit API (`org.bukkit.*`). Event sistemi (`HandlerList`, `SimplePluginManager`,
  `JavaPluginLoader`...) tamamen burada; `spigot-event-dispatcher` bu jar'dan uretildi.
- `spigot-...jar`: Mojang-mapped NMS (`net.minecraft.*`) + OBC (`org.bukkit.craftbukkit.*`). API'yi
  implemente eden ve event'leri fiilen tetikleyen sunucu tarafi (`CraftServer`,
  `ServerGamePacketListenerImpl`...). 3rd-party bagimlilik icermez.

`~/<optional-folder>/Buildtools/spigot-26.2.jar` (bundler) sunucuyu calistirmak icindir: kendi
icinde sadece bootstrap `Main` var, yukaridaki iki jar'i (ve Guava/Netty gibi bagimliliklari)
`META-INF/libraries/` ve `META-INF/versions/` altinda ic ice jar olarak tasir. JADX ic ice jar'lari
da acar, ama ayni siniflari iki kez yukler ("Classes with same name are omitted"); bu yuzden
yukaridaki iki jar yeterli.

## Forge Jars
`forge-26.1.2-64.0.8-mdk` kurulumunda ForgeGradle'in indirdigi jar'lar
(MDK'daki `annotationProcessor 'net.minecraftforge:eventbus-validator:7.0.1'` surumu dogruluyor):

```
~/.gradle/caches/minecraftforge/forgegradle/mavenizer/caches/maven/forge/net/minecraftforge/
eventbus/7.0.1/eventbus-7.0.1.jar
javafmllanguage/26.1.2-64.0.8/javafmllanguage-26.1.2-64.0.8.jar
fmlcore/26.1.2-64.0.8/fmlcore-26.1.2-64.0.8.jar
fmlloader/26.1.2-64.0.8/fmlloader-26.1.2-64.0.8.jar
forge/26.1.2-64.0.8/forge-26.1.2-64.0.8-universal.jar
```
## Fabric Jars
`~/MinecraftWorkspace/fabric/template-mod-26.1.2` sablonunun build'inde Gradle'in indirdigi jar'lar
(`~/.gradle/caches/modules-2/files-2.1/` altinda):

```
net.fabricmc/fabric-loader/0.19.5/fabric-loader-0.19.5.jar
net.fabricmc.fabric-api/fabric-api-base/2.0.3+ece063234c/fabric-api-base-2.0.3+ece063234c.jar
net.fabricmc.fabric-api/fabric-lifecycle-events-v1/4.1.1+df84eb3d4c/fabric-lifecycle-events-v1-4.1.1+df84eb3d4c.jar
net.fabricmc.fabric-api/fabric-message-api-v1/7.0.5+dae8ce3e4c/fabric-message-api-v1-7.0.5+dae8ce3e4c.jar
```