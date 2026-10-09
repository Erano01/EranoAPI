# Sürümden Bağımsız API'ler

Bukkit'te adları ya da yapısı 1.8'den 26.x'e değişen alanlar ve EranoAPI'nin her biri için yazacağı API.
[Material.md](Material.md) / `EranoMaterial` bunların ilki; ötekiler aynı yöntemle yapılır.

## Alanlar

CraftBukkit'in `legacy/FieldRename.java`'sı hangi listelerde isim değişikliğini resmi olarak takip ettiğini
gösteriyor: büyü, iksir etkisi, iksir türü, parçacık, varlık türü, biyom, attribute, sancak deseni, müzik aleti, loot
table. Aşağıdakiler bunlar ve Bukkit'in takip etmediği ama çok değişmiş birkaç alan.

| Alan | Neler değişti | Neden önemli | Durum |
|---|---|---|---|
| **Materyal (`Material`)** | 1.13'te flattening (ID + veri değeri → her türün kendi adı); 1.13 sonrası 14 ad değişikliği | Her eşya ve blok | ✅ `EranoMaterial` ([Material.md](Material.md)) |
| **Ses (`Sound`)** | 1.9'da bütün adlar değişti (`NOTE_PLING` → `BLOCK_NOTE_PLING`), 1.13'te bir kez daha (`BLOCK_NOTE_BLOCK_PLING`); 1.21.3'te enum'dan arayüze döndü, `Sound.valueOf` artık çalışmıyor | Her plugin ses çalıyor; en sık kırılan alan | ✅ `EranoSound` ([Sound.md](Sound.md)) |
| **Büyü (`Enchantment`)** | 1.13'te adlardan anahtarlara geçti (`DAMAGE_ALL` → `minecraft:sharpness`); 1.20.5'te alan adları da değişti (`SHARPNESS`) | Kit ve sandık eşyaları | ✅ `EranoEnchantment` ([Enchantment.md](Enchantment.md)) |
| **İksir etkisi / iksir türü** | 1.20.5'te adlar değişti (`INCREASE_DAMAGE` → `STRENGTH`, `JUMP` → `JUMP_BOOST`); iksir eşyası 1.8'de veri değeri, 1.9'dan sonra `PotionMeta` | Kit yetenekleri, iksir eşyaları | ✅ `EranoPotionEffect`, `EranoPotionType` ([Potion.md](Potion.md)) |
| **Parçacık / efekt** | 1.8'de `Particle` API'si yok (`Effect` enum'u ve NMS paketi); 1.9'da geldi, 1.13'te veri tipi değişti (`BlockData`), 1.20.5'te adlar değişti | Görsel efektler; 1.8 için NMS gerekiyor | ✅ `EranoParticle`, `EranoEffect` ([Particle.md](Particle.md)) |
| **Varlık türü (`EntityType`)** | 1.13'te ve 1.20.5'te adlar değişti (`PIG_ZOMBIE` → `ZOMBIFIED_PIGLIN`, `FIREWORK` → `FIREWORK_ROCKET` ...) | Mob çıkarma; spawn yumurtaları (1.8 veri değeri, 1.9 - 1.12 NBT) | yok |
| **Oyun kuralları (`GameRule`)** | 1.13'e kadar sadece metin; 1.13'te tipli; 26.x'te adlar değişti (`DO_IMMEDIATE_RESPAWN` → `IMMEDIATE_RESPAWN`) | Ölüm ekranı, mob ve zaman ayarları | HungerGames'te `compat/ImmediateRespawn` |
| **Blok görünümü (blok verisi)** | 1.8 - 1.12'de veri değeri (yön, renk), 1.13+'ta `BlockData` | Yön, çift sandık, piston, merdiven; bir bloğu aynı görünüşle geri koymak | HungerGames'te `compat/BlockLooks`, `arena/SpawnPistons` |
| **Attribute** | 1.16'da `GENERIC_MAX_HEALTH` gibi adlar, 1.21.3'te önek kalktı (`MAX_HEALTH`); 1.8'de API yok | Can, hız, saldırı gücü | yok |
| **Renk (`DyeColor`)** | 1.13'te `SILVER` → `LIGHT_GRAY` | Koyun, sancak | yok |
| **`ItemFlag`** | 1.20.5'te `HIDE_POTION_EFFECTS` → `HIDE_ADDITIONAL_TOOLTIP` | Menülerde tooltip gizleme | yok |
| Biyom, sancak deseni, istatistik, loot table | Çok sayıda ad değişikliği (1.13; 1.18'de biyom birleşmeleri) | Daha nadir kullanılıyor | yok |

## Sıra

HungerGames'in ihtiyacına ve kazanca göre:

1. ✅ **Ses**, **büyü ve iksir**, **parçacık / efekt** (`scripts/registry-report`).
2. **Varlık türü:** spawn yumurtalarının 1.9 - 1.12 sorununu da çözer (`EranoMaterial` orada yumurtanın türünü
   veremiyor).
3. **Oyun kuralları ve blok görünümü:** HungerGames'in kalan `compat/` sınıfları. Bitince `compat/` paketi tamamen
   kalkar.
4. Attribute, renk, `ItemFlag`, biyom ve ötekiler: ihtiyaç doğdukça.

## Yöntem

Her alan için `Material.md` / `EranoMaterial`'daki gibi (ses, büyü, iksir, parçacık: `scripts/registry-report`;
dökümler sınıfları yüklemeden, bytecode'dan okunur, böylece registry tabanlı sabitler de sunucusuz okunur):

1. **Döküm:** her NMS revizyonunun (EranoAPI'nin `Spigot/NMS/V*` modülleri) Spigot API'sinden o alanın bütün değerleri
   (`scripts/material-report/Dump.java` gibi). 1.21.3 sonrası enum olmaktan çıkanlar (ses ...) registry'den okunur.
2. **İsim değişiklikleri:** CraftBukkit'in kendi tablolarından (`legacy/FieldRename.java`, `util/Commodore.java`) ve
   hangi güncelleme commit'iyle girdikleri (`git log -S`), dökümlerle doğrulanarak; tahminle değil.
3. **Rapor:** `docs/<Alan>.md` (ör. `Sound.md`): hangi değer hangi revizyonda var, ne zaman ve neye değişti.
4. **API:** rapordan üretilen bir enum ve tablolar (elle yazılmaz), en yeni Minecraft'ın adlarıyla; eski sunucuda eski
   adına ya da NMS / veri değeri karşılığına çevrilir. Yeni bir Minecraft gelince betik yeniden çalışır.
5. **Doğrulama:** üretici her revizyonda bir değerin adlarını yeniden eskiye dener ve sunucuda bulunan ilkinin o değerin
   kendisi olduğunu kontrol eder; tutmazsa üretmez.
6. **Test:** sunucusuz birim testleri (eski sunucular API'deki `LEGACY_` / eski adlarla taklit edilerek), sonra 1.8.8
   ve 26.3'te oyun içinde.
