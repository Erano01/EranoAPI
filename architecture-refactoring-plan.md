# EranoAPI Mimari Refactoring Planı

> Durum: **Taslak**. Açık kararlar (bkz. [Açık Kararlar](#açık-kararlar)) netleşmeden Faz 2'ye geçilmeyecek.

## Amaç

EranoAPI'yi sadece Spigot için version-agnostic bir kütüphane olmaktan çıkarıp **Spigot, Forge ve Fabric** için
ortak bir kütüphane haline getirmek. Geliştiriciler EranoAPI'yi Maven veya Gradle ile
`me.erano.com.eranoapi` group'u altından bağımlılık olarak ekleyebilmeli ve API'nin desteklediği her
platform + Minecraft sürümü kombinasyonunda aynı arayüzleri kullanabilmeli.

İlk tüketici: [HungerGames](https://github.com/Erano01/HungerGames) (her platformda en son MC sürümü).
Sonraki projeler EranoAPI üzerinden eski sürümleri de destekleyebilecek.

## Temel Prensipler

1. **EranoAPI NMS / OBC / Minecraft sınıflarını yeniden dağıtmaz.** Sürümden bağımsız *arayüzler* ve
   bunların sürüme özel *uygulamalarını* sağlar. Ham NMS'e dokunmak isteyen geliştirici Minecraft
   sınıflarını kendi Spigot jar'ından / MDK'sından / Loom'undan alır (lisans gereği de böyle olmalı).
2. **Sürüm modülleri ince, mantık ortak modülde.** Sürüm başına kod paylaşılmadığı için (preprocessor yok)
   sürüm modülleri sadece adaptör içerir.
3. **Stonecutter / Architectury / kaynak ön işleme kullanılmaz.** Her sürüm kendi bağımsız projesidir.
4. **Tek dev Gradle build'i yok.** Her Forge MDK / Fabric template kendi `gradlew`'u ve kendi
   ForgeGradle / Loom sürümüyle bağımsız build edilir; bunları script'ler yönetir
   (Spigot tarafında BuildTools için `scripts/build-spigot-jars.sh` ile yapılanın aynısı).
5. **Forge / Fabric'te sürüm başına ayrı jar.** Modrinth / CurseForge zaten MC sürümü başına dosya
   barındırıyor, launcher doğru dosyayı seçiyor. Tek uber jar ileride isteğe bağlı (bkz. [Riskler](#riskler)).

## Mevcut Durum

```
EranoAPI-Parent/            (Maven, pom packaging)
├── EranoAPI/               artifactId fiilen "eranoapi" (EranoAPI satırı yorumda) - CorePlugin, API arayüzleri
├── NMS/                    V1_8_R1 … V1_21_R7, V26_1 … V26_3 (SPI ile runtime seçimi)
├── Dist/                   shade edilmiş plugin jar'ı (EranoAPI.jar)
└── scripts/                build-spigot-jars.sh / .bat (BuildTools ile tüm Spigot jar'ları)
```

Bilinen sorunlar:

- ~~`EranoAPI/pom.xml` artifactId'si `eranoapi`, NMS modülleri ve `Dist` ise `EranoAPI`'ye bağımlı →
  Maven reactor içinde `mvn package` çalışmıyor.~~ Düzeltildi: bağımlılıklar `eranoapi` oldu.
- ~~NMS modüllerinde Spigot bağımlılığı `compile` scope'undaydı → `Dist` jar'ı tüm sunucu jar'larını
  gömüyordu (97 MB).~~ Düzeltildi: `provided` (jar 151 KB).
- ~~`V1_20_R1` services dosyası `META-INF/resources/` altındaydı → 1.20 / 1.20.1'de sağlayıcı
  bulunmuyordu.~~ Düzeltildi.
- Maven JDK 25+ ile çalışmalı (26.x Spigot jar'ları Java 25 class dosyası); root `pom.xml`'deki
  `maven-enforcer-plugin` bunu kontrol ediyor.
- `Dist/pom.xml` içinde bazı modüller `1.0-SNAPSHOT`, bazıları `1.0` sürümüyle referanslanıyor.
- Particle sağlayıcıları kendi revizyonları dışındaki sürümleri de kabul ediyor
  (`V1_10_R1` → 1.11/1.12, `V1_13_R1` → 1.14–1.18, `V1_19_R1` → 1.20/1.21). Bu sürümlerde ilgili
  `vX_Y_RZ` sınıfları olmadığı için runtime'da çöker.
- Sürüm eşleşmesi `version.equals("1.21.5")` gibi sabit listelerle yapılıyor; yeni bir patch sürümü
  çıktığında hiçbir sağlayıcı eşleşmiyor.
- Parent `pom.xml` kapanan `oss.sonatype.org` reposunu hâlâ listeliyor.

[XTFOverhaul](https://github.com/Erano01/XTFOverhaul) (Forge/Fabric) aynı mimariyi tek bir Gradle
build'i içinde kurmaya çalıştı; `V1_8_9` (ForgeGradle 2.1, eski Gradle, kapanmış jcenter) bu yüzden
tıkandı. Bu plan XTFOverhaul'u EranoAPI'ye taşır.

## Hedef Yapı

```
EranoAPI/                                  ← repo adı (GitHub eski adresi yönlendirir)
├── api/                 eranoapi-api       saf Java 8, Minecraft bağımlılığı yok
├── spigot/
│   ├── core/            eranoapi-spigot    ← şimdiki EranoAPI modülü (CorePlugin, ParticleManager...)
│   ├── nms/V1_8_R1…V26_3                   dahili, ayrı yayınlanmaz (dist'e gömülür)
│   └── dist/                               sunucuya atılan plugin jar'ı
├── forge/
│   ├── V1_21_11/        eranoapi-forge     sürüm: 1.0.0+1.21.11
│   └── V26_1_2/         eranoapi-forge     sürüm: 1.0.0+26.1.2
├── fabric/
│   ├── V1_21_11/        eranoapi-fabric    sürüm: 1.0.0+1.21.11
│   └── V26_1_2/         eranoapi-fabric    sürüm: 1.0.0+26.1.2
└── scripts/
    ├── build-spigot-jars.sh / .bat         (var) BuildTools ile Spigot jar'ları → ~/.m2
    └── build-mods.sh                       api → mavenLocal, sonra her MDK kendi JDK'sı ile → jar'lar
```

### Modüllerin sorumlulukları

| Modül | Bağımlılıklar | İçerik |
|---|---|---|
| `api` | yok (Java 8) | Platformdan bağımsız sözleşmeler: `PluginChannel`, `Scheduler`, `MinecraftVersion`, platform arayüzleri |
| `spigot/core` | `api`, `spigot-api` | Bukkit tarafı API, SPI ile NMS uygulaması seçimi, `CorePlugin` |
| `spigot/nms/*` | `spigot/core`, ilgili Spigot jar'ı | Revizyon başına NMS/OBC uygulamaları |
| `spigot/dist` | `spigot/core`, tüm `nms/*` | Shade edilmiş plugin jar'ı (`ServicesResourceTransformer`) |
| `forge/<sürüm>` | `api` (mavenLocal), ilgili Forge | Dokunulmamış Forge MDK + `api` uygulamaları |
| `fabric/<sürüm>` | `api` (mavenLocal), ilgili Fabric Loader/API | Dokunulmamış Fabric template + `api` uygulamaları |

### Build çıktıları

Root proje uber jar üretmez. Root `pom.xml` sadece Maven tarafını (`api`, `spigot/*`) yönetir; Forge /
Fabric projeleri Maven reactor'ün parçası olmayan, kendi `gradlew`'u olan bağımsız Gradle build'leridir
ve `build-mods.sh` tarafından build edilir.

- **Spigot → uber jar.** `spigot/dist`, `spigot/core` + tüm `nms/*` modüllerini shade eder (bugünkü
  `Dist` modülü gibi). NMS uygulaması runtime'da SPI ile seçilir; sunucu sahibi tek jar indirir.
- **Forge / Fabric → sürüm başına jar.** Her sürüm projesi kendi jar'ını üretir (`api` + o sürümün
  uygulaması). Uber jar ertelendi (bkz. [Riskler](#riskler)).

| Proje | Çıktı | Kimin için |
|---|---|---|
| `api` | `eranoapi-api-1.0.0.jar` | Geliştiricinin derleme bağımlılığı |
| `spigot/core` | `eranoapi-spigot-1.0.0.jar` | Geliştiricinin derleme bağımlılığı (`compileOnly`) |
| `spigot/dist` | `EranoAPI.jar` (**uber jar**: core + tüm NMS) | Sunucu sahibi, `plugins/` klasörüne atar |
| `forge/<sürüm>` | `eranoapi-forge-1.0.0+<mc>.jar` | Geliştirici (jar-in-jar veya ayrı mod) |
| `fabric/<sürüm>` | `eranoapi-fabric-1.0.0+<mc>.jar` | Geliştirici (jar-in-jar veya ayrı mod) |

## Maven Koordinatları

Group: `me.erano.com.eranoapi`

| Artifact | Sürüm şeması | Örnek |
|---|---|---|
| `eranoapi-api` | `<api>` | `1.0.0` |
| `eranoapi-spigot` | `<api>` | `1.0.0` |
| `eranoapi-forge` | `<api>+<mc>` | `1.0.0+26.1.2` |
| `eranoapi-fabric` | `<api>+<mc>` | `1.0.0+26.1.2` |

`+<mc>` şeması Fabric ekosisteminin standardı (Fabric API: `0.162.0+26.3`).

### Geliştirici tarafında kullanım

```kotlin
// Spigot plugin
compileOnly("me.erano.com.eranoapi:eranoapi-spigot:1.0.0")          // runtime'da EranoAPI plugin'i sağlar

// Fabric mod (26.1.2)
modImplementation("me.erano.com.eranoapi:eranoapi-fabric:1.0.0+26.1.2")

// Forge mod (26.1.2)
implementation("me.erano.com.eranoapi:eranoapi-forge:1.0.0+26.1.2")
```

## Desteklenen Sürümler

### Spigot

Kural: her `vX_Y_RZ` revizyonu için bir modül; bir revizyon birden fazla MC sürümünü kapsıyorsa
**en yenisine** karşı derlenir. 26.x'te revizyon yok; her 26.x sürümü ayrı modül, yamalı sürümlerde
(26.1.1, 26.1.2) en yenisi seçilir. Kaynak: `dependencies.md` ve spigotmc.org wiki tabloları.

1.8 → 26.3 arası tüm revizyonlar mevcut (`NMS/` altında).

### Forge / Fabric

| | 1.8.9 | 1.9 – 1.13 | 1.14+ |
|---|---|---|---|
| **Forge** | Var (modern şablonla, örn. Essential'ın `architectury-loom` fork'u) | Var | Var |
| **Fabric** | Sadece Legacy Fabric / Ornithe (topluluk) | Sadece Legacy Fabric / Ornithe | Resmi |

Başlangıç kapsamı: **26.1.2 + 1.21.11** (biri unobfuscated / Mojang isimleri, diğeri obfuscated). Bu ikisi
oturunca aradaki sürümler mekanik olarak eklenir. Forge 1.8.9 sonra; Fabric'te 1.14 altına inilmez.

### JDK gereksinimleri

| Minecraft | Java |
|---|---|
| 26.1+ | 25 |
| 1.20.5 – 1.21.11 | 21 |
| 1.17 – 1.20.4 | 17 |
| 1.13 – 1.16.5 | 11 (BuildTools) / 8 (Forge) |
| 1.8 – 1.12.2 | 8 |

## Fazlar

### Faz 0 - Spigot jar'ları (tamamlandı)

- [x] `scripts/build-spigot-jars.sh` / `.bat`: revizyon başına en yeni sürümü BuildTools ile build eder,
      `dependencies.md`'deki Java tablosunu kullanır, 1.17–1.21.11 için `--remapped`, kapanan
      `oss.sonatype.org` için Maven mirror, hataları `logs/failed.txt`'e özetler.
- [x] Eksik NMS modülleri: `V1_8_R1` – `V1_9_R2`, `V1_20_R4`, `V1_21_R5` – `V1_21_R7`, `V26_1` – `V26_3`.
- [x] Spigot bağımlılıkları revizyonun en yeni sürümüne çekildi (`V1_11_R1`, `V1_12_R1`, `V1_13_R2`,
      `V1_14_R1`, `V1_15_R1`, `V1_16_R2`).
- [x] `TPSHandlerFactoryImpl` sürüm listeleri wiki tablosuyla eşitlendi.
- [x] Tüm Spigot jar'ları build edildi (1.8 → 26.3, 35 sürüm); 1.17–1.21.11 için `remapped-mojang` /
      `remapped-obf` jar'ları ve mapping'ler `~/.m2`'de.
- [x] 35 NMS modülünün hepsi kendi Spigot jar'ına karşı derleniyor (`javac`, Java 8 hedefi; Maven
      reactor'ü Faz 1'e kadar çalışmıyor).
- [x] `26.1.2`: BuildTools `--rev 26.1` verilince serinin en yenisini (26.1.2) build edip
      `spigot-26.1.2.jar` olarak kaydediyor; ayrıca build gerekmedi.

### Faz 1 - Repo yeniden düzenleme (Spigot, Maven)

Bu faz tek başına mevcut Spigot build'ini düzeltir; Forge/Fabric'e dokunmaz.

1. Repo adı `EranoAPI-Parent` → `EranoAPI` (GitHub ayarlarından; eski URL yönlendirilir).
2. Klasörler: `EranoAPI/` → `spigot/core/`, `NMS/` → `spigot/nms/`, `Dist/` → `spigot/dist/`
   (`git mv` ile, geçmiş korunur).
3. `api/` modülü oluşturulur; `spigot/core` içinden platformdan bağımsız arayüzler buraya taşınır.
4. Group `me.erano.com.eranoapi`, artifactId'ler `eranoapi-api`, `eranoapi-spigot`; tüm modüllerde
   tutarlı sürüm (`1.0.0`). Bu, `eranoapi` / `EranoAPI` uyuşmazlığını kendiliğinden düzeltir.
5. Parent `pom.xml`'den `oss.sonatype.org` kaldırılır.
6. Doğrulama: `mvn package` kök dizinden hatasız, `spigot/dist` jar'ı tüm NMS modüllerini ve birleşmiş
   `META-INF/services` dosyalarını içeriyor.

### Faz 2 - Spigot iç düzeltmeleri

1. `MinecraftVersion` (karşılaştırılabilir, `1.21.5` ve `26.1.2` formatlarını parse eder) `api`'ye eklenir.
2. Sağlayıcılar sabit liste yerine sürüm aralığı bildirir (`minVersion()` / `maxVersion()`); seçimde
   aralığa uyanlar arasından `min` değeri en yüksek olan kazanır.
3. Particle sağlayıcılarının kendi revizyonları dışındaki sürümleri kabul etmesi düzeltilir. 1.9+ için
   Bukkit API (`World#spawnParticle`) uygulaması, NMS sadece 1.8 için.
4. TPS: Paper'da `Bukkit.getTPS()`, Spigot'ta NMS.
5. Sağlayıcı oluşturulurken `LinkageError` yakalanıp bir sonraki adaya / API uygulamasına düşülür.

### Faz 3 - Forge / Fabric iskeleti (26.1.2)

1. `forge/V26_1_2`: olduğu gibi Forge MDK (kendi `gradlew`, kendi ForgeGradle sürümü).
2. `fabric/V26_1_2`: olduğu gibi Fabric template (kendi `gradlew`, kendi Loom sürümü).
3. Her ikisi `api`'yi `mavenLocal()` üzerinden `me.erano.com.eranoapi:eranoapi-api` olarak çeker.
4. `scripts/build-mods.sh`: `api`'yi `mavenLocal`'a kurar, sonra her MDK'yı doğru JDK ile kendi
   `gradlew`'u üzerinden build eder, jar'ları toplar. Hata özeti `build-spigot-jars.sh` ile aynı formatta.
5. İlk ortak özellik: **`PluginChannel`** (veri gönder/al, kanal karşı tarafta var mı). HungerGames'in
   Fabric ve Forge client'larındaki tekrarlanan handshake mantığı (`trySendHello`, `onHelloAck`) buna
   taşınır; Spigot uygulaması plugin messaging ile.
6. Doğrulama: HungerGames Spigot + Fabric + Forge handshake'i EranoAPI üzerinden çalışıyor.

### Faz 4 - İkinci sürüm (1.21.11)

1. `forge/V1_21_11`, `fabric/V1_21_11` (obfuscated sürüm: Forge'da official mapping, Fabric'te intermediary).
2. `build-mods.sh` sürüm tablosuna eklenir.
3. Doğrulama: aynı `api` arayüzleri iki sürümde de çalışıyor; sürüm modülleri sadece adaptör içeriyor.

### Faz 5 - Yayınlama

1. Yayın reposunun kurulması (bkz. Açık Kararlar #1).
2. Maven (`spigot/*`) ve Gradle (`forge/*`, `fabric/*`) build'lerinin aynı repoya aynı group altında
   yayınlaması.
3. GitHub Actions: Spigot jar'ları cache'lenerek build, MDK'lar matris ile.
4. HungerGames bağımlılığının yeni koordinatlara geçirilmesi.

### Faz 6 - Kapsam genişletme (ihtiyaç oldukça)

- Forge / Fabric için 1.21.11 ile 26.1.2 arası ve öncesi sürümler.
- Forge 1.8.9 (modern şablonla).
- NeoForge (bkz. Açık Kararlar #3).
- İsteğe bağlı tek uber jar (bkz. Riskler).

## Açık Kararlar

1. **Yayın reposu.**
   - *GitHub Packages* (şu anki): public paketlerde bile tüketicinin token ayarlaması gerekiyor.
   - *Maven Central*: group'un sahip olunan bir domain'e karşılık gelmesi gerekiyor;
     `me.erano.com` uymuyor → `io.github.erano01` zorunlu olur.
   - *JitPack*: token yok, ama group `com.github.Erano01` olur; çok modüllü / çok build'li yapıda
     sınırlara takılabilir.
   - *Kendi Maven reposu* (örn. Reposilite): `me.erano.com.eranoapi` korunur, token yok; sunucu gerekir.
2. **Forge / Fabric'te dağıtım modeli.**
   - *Ayrı mod*: oyuncu EranoAPI'yi ayrıca kurar (Spigot plugin modeli gibi).
   - *Jar-in-jar*: geliştirici kendi mod'una gömer (Fabric `include`, Forge `jarJar`).
   - Öneri: jar-in-jar ile başlamak.
3. **NeoForge** da hedeflenecek mi?
4. **XTFOverhaul** arşivlenecek mi, yoksa yönlendirme README'si ile mi bırakılacak?

## Riskler

- **Tek uber jar (Forge / Fabric).** Ertelendi; denenirse prototiple doğrulanacaklar:
  - Forge jar'daki tüm `@Mod` sınıflarını tarar; uber jar'da tek bir ortak giriş sınıfı gerekir, ama
    giriş noktası / event bus kaydı dönemler arasında değişti (1.8.9 `FMLInitializationEvent`,
    1.13–1.20 `FMLJavaModLoadingContext.get()`, 26.x constructor injection + EventBus 7).
  - Eski Forge sürümleri annotation taraması için ASM kullanıyor; jar'daki Java 25 class dosyalarında
    çökebilir. Tüm modülleri Java 8 hedefiyle derlemek muhtemelen çözer (EranoAPI'de 26.x için çalıştı).
  - Metadata formatları farklı: `mcmod.info`, `mods.toml`, `neoforge.mods.toml`, `fabric.mod.json`.
- **Sürüm modüllerinde kod tekrarı.** Preprocessor olmadığı için bilinçli bir bedel; sürüm modülleri
  ince tutularak sınırlanır.
- **Eski araç zincirleri.** Forge 1.8.9 için orijinal ForgeGradle 2.1 kullanılamaz; topluluk şablonları
  gerekir. Legacy Fabric / Ornithe resmi Fabric API ile aynı API değil.
- **Spigot jar'ları.** Spigot sunucu jar'ını Maven'da yayınlamıyor; CI'da BuildTools ile üretilip
  cache'lenmesi gerekiyor (35 build, saatler sürebilir).
