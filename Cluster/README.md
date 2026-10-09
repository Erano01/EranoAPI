# EranoAPI-Cluster

Bir minigame ağındaki sunucuların birbirinden haberdar olması: her sunucunun arenaları ve bir arenaya giden
oyuncular. Oyun sunucuları arenalarını yazar; hub'lar, proxy'ler ve öteki oyun sunucuları okur. Herkes bir oyuncuyu
bir arenaya gönderebilir, oyuncunun vardığı sunucu onu o arenaya alır.

Kullananlar: HungerGames (oyun sunucuları) ve HUB-Plugin (hub). İkisi de bu modülü kendi jar'ına gömer (shade +
relocate `me.erano.com.api.cluster`); EranoAPI.jar plugin'inde yoktur, sunucuya ayrıca bir şey kurulmaz. Saf Java:
Spigot'a, Fabric'e, Forge'a ya da bir proxy'ye bağlı değildir.

Oyundan bağımsızdır: her kayıtta `game` (`hungergames`, ileride `skywars` ...) ve herkesin anladığı bir `stage`
(`WAITING` / `PLAYING` / `CLOSED`) vardır; `state` oyunun kendi sözcüğüdür (HG'de aşama adı), menüler onu oyunun dil
dosyasından gösterir.

## Kullanım

```java
ClusterSettings settings = ClusterSettings.redis("localhost", 6379, "hg:").redisAuth("", "secret").build();
// ya da: ClusterSettings.mysql(ClusterSettings.jdbcUrl(host, 3306, "hungergames", options), user, pass, "hg_").build()
Cluster cluster = Cluster.connect(settings, logger);

cluster.publish("hg-1", arenas);                       // oyun sunucusu, birkaç saniyede bir
cluster.arenas().thenAccept(all -> ...);               // herkes
cluster.onChange(server -> ...);                       // Redis: değişiklik anında (cluster.pushesChanges())
cluster.sendTo(uuid, "hg-2", "breeze");                // göndermeden önce, sonra proxy'ye Connect
cluster.arriving(uuid, "hg-2").thenAccept(arena -> ...); // varılan sunucu, oyuncu girince
ArenaStatus best = QuickJoin.any().game("hungergames").best(all);
cluster.close();
```

Çağrılar kütüphanenin kendi thread'inde sırayla çalışır, `CompletableFuture` döner; Bukkit plugin'i oyuna dokunmadan
önce ana thread'e döner.

## Arka uçlar

Ağdaki her sunucu ve hub aynı arka ucu, aynı önekle kullanır.

**MySQL / MariaDB** (`ClusterSettings.mysql`): kurulacak başka bir şey yok.
- `<önek>arenas`: arena başına bir satır (`game, server, arena, state, stage, paused, seconds_left, players,
  spectators, max_players, joinable, updated_at`). 15 sn'den eski satır çökmüş bir sunucunundur, okunmaz; bir gün
  sonra silinir. Eski sürümün tablosu (`game` / `stage` sütunu yok) açılışta silinip yeniden yapılır: satırlar zaten
  saniyelik.
- `<önek>joins`: oyuncu başına bir satır (`uuid, server, arena, created_at`), 60 sn geçerli, bir kez okunur.
- Zamanlar veritabanının saatiyle. Değişiklik bildirimi yok: okuyanlar birkaç saniyede bir okur.

**Redis** (`ClusterSettings.redis`): büyük ağlar için.
- `<önek>servers`: sunucu adları, son yazma zamanıyla (sorted set, Redis'in saati).
- `<önek>server:<sunucu>`: o sunucunun arenaları (hash, arena başına bir değer); 15 sn yazılmazsa kendiliğinden
  silinir. Bir sunucunun arenaları tek transaction'da değişir, okuyan yarısını görmez.
- `<önek>join:<uuid>`: `sunucu<TAB>arena`, 60 sn sonra silinir; okuma GET + DEL tek seferde (Redis 6.2 öncesi de).
- `<önek>arenas` kanalı: bir sunucunun arenaları değişince adı yayınlanır; `onChange` dinleyicileri anında haber
  alır, menüler beklemeden güncellenir. Bağlantı koparsa 5 sn'de bir yeniden abone olunur.

## Sözleşme

`ArenaStatus` alanları, tablo ve anahtar yapısı, `stage` anlamları buradadır; HungerGames ve HUB-Plugin buna göre
yazılır. Değişirse sürüm artar ve iki plugin birlikte güncellenir.

## Derleme

```
mvn -pl Cluster -am install                                   # Redis testleri atlanır; ~/.m2'ye kurar
ERANOAPI_CLUSTER_TEST_REDIS=127.0.0.1:6379 mvn -pl Cluster test   # gerçek bir Redis'e karşı da
```

Java 8 için derlenir (1.8 - 1.12 sunucularında da çalışan plugin'ler gömebilsin diye). EranoAPI'nin öteki modülleriyle
aynı sürümle yayınlanır (`scripts/publish-central.sh`).
