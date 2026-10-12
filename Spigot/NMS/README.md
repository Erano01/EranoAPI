# NMS Modülleri

Her NMS revizyonu (`V1_8_R1` … `V1_21_R7`, `V26_1` … `V26_3`) için bir modül. Her biri o revizyonun en yeni
Minecraft sürümünün Spigot jar'ına karşı derlenir (`scripts/build-spigot-jars.sh`), `Spigot/Dist` hepsini
`EranoAPI.jar`'a shade eder. Her revizyonun bir de Paper ikizi var: `Paper/NMS/<revizyon>`
(bkz. [docs/Architecture.md](../../docs/Architecture.md#nms)).

## SPI

Özellik Spigot/Core'da bir arayüzdür (`TPSHandlerFactory`, `HologramServiceProvider`, `MessageServiceProvider`,
`BossBarServiceProvider`, `IParticleProvider` ...). Bir NMS modülü onu uygular ve
`src/main/resources/META-INF/services/<arayüzün tam adı>` dosyasına sınıfını yazar. Hepsi `VersionedService`'tir:
`supportedVersions()` ile sürüm aralığını bildirir, `VersionedServices.select` çalışan sürüme uyanlardan en yüksek
`priority()`'yi, sonra en dar aralığı seçer. Bukkit API'si ile yapılabilen şey NMS'e değil Core'a yazılır.

## Yeni bir revizyon eklemek

1. `scripts/build-spigot-jars.sh` ve `scripts/build-paper-jars.sh` tablolarına sürümü ekleyip jar'ları kurun.
2. En yakın modülü kopyalayıp paket adını (`me.erano.com.V1_XX_RX`), `pom.xml`'deki Spigot sürümünü ve
   `supportedVersions()` aralıklarını güncelleyin. 1.17+ modülleri Mojang isimleriyle derlenip reobf edilir
   (`specialsource-maven-plugin`); 26.x obfuscated değildir.
3. Modülü `Spigot/NMS/pom.xml` ve `Spigot/Dist/pom.xml`'e ekleyin; Paper ikizini `Paper/NMS`'e de.

## Dist

`Spigot/Dist`, Maven Shade Plugin'in `ServicesResourceTransformer`'ı ile bütün modüllerin `META-INF/services`
dosyalarını birleştirir: tüm sürümler için tek bir jar.
