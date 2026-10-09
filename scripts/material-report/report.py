"""Writes Material.md from analysis.json (analyze.py) and legacy-1.13.2.tsv (Legacy.java)."""
import json,sys
from collections import defaultdict
a=json.load(open('analysis.json')); tpre=a['tpre']; tpost=a['tpost']; versions=a['versions']
def vkey(v): return [int(x) for x in v.split('.')]
pre=[v for v in versions if vkey(v)<[1,13]]; post=[v for v in versions if vkey(v)>=[1,13]]
# Renames after 1.13: (old, new, exact Minecraft version, source)
RENAMES=[('CACTUS_GREEN','GREEN_DYE','1.14','Commodore'),('DANDELION_YELLOW','YELLOW_DYE','1.14','Commodore'),
('ROSE_RED','RED_DYE','1.14','Commodore'),('SIGN','OAK_SIGN','1.14','Commodore'),('WALL_SIGN','OAK_WALL_SIGN','1.14','Commodore'),
('ZOMBIE_PIGMAN_SPAWN_EGG','ZOMBIFIED_PIGLIN_SPAWN_EGG','1.16','Commodore'),('GRASS_PATH','DIRT_PATH','1.17','Commodore'),
('POTTERY_SHARD_ARCHER','ARCHER_POTTERY_SHERD','1.20','yok (1.19.4\'te deneysel)'),('POTTERY_SHARD_ARMS_UP','ARMS_UP_POTTERY_SHERD','1.20','yok (1.19.4\'te deneysel)'),
('POTTERY_SHARD_PRIZE','PRIZE_POTTERY_SHERD','1.20','yok (1.19.4\'te deneysel)'),('POTTERY_SHARD_SKULL','SKULL_POTTERY_SHERD','1.20','yok (1.19.4\'te deneysel)'),
('GRASS','SHORT_GRASS','1.20.3','Commodore'),('SCUTE','TURTLE_SCUTE','1.20.5','Commodore'),('CHAIN','IRON_CHAIN','1.21.9','Commodore')]
new_of={o:n for o,n,_,_ in RENAMES}; old_of={n:o for o,n,_,_ in RENAMES}
def today(n):
    while n in new_of: n=new_of[n]
    return n
# NMS revision of each version we have (EranoAPI's Spigot/NMS modules and their version ranges).
REV={'1.8':('V1_8_R1','1.8 - 1.8.2'),'1.8.3':('V1_8_R2','1.8.3'),'1.8.8':('V1_8_R3','1.8.4 - 1.8.9'),
'1.9.2':('V1_9_R1','1.9 - 1.9.3'),'1.9.4':('V1_9_R2','1.9.4'),'1.10.2':('V1_10_R1','1.10.x'),'1.11.2':('V1_11_R1','1.11.x'),
'1.12.2':('V1_12_R1','1.12.x'),'1.13':('V1_13_R1','1.13'),'1.13.2':('V1_13_R2','1.13.1 - 1.13.2'),'1.14.4':('V1_14_R1','1.14.x'),
'1.15.2':('V1_15_R1','1.15.x'),'1.16.1':('V1_16_R1','1.16 - 1.16.1'),'1.16.3':('V1_16_R2','1.16.2 - 1.16.3'),
'1.16.5':('V1_16_R3','1.16.4 - 1.16.5'),'1.17.1':('V1_17_R1','1.17.x'),'1.18.1':('V1_18_R1','1.18 - 1.18.1'),
'1.18.2':('V1_18_R2','1.18.2'),'1.19.2':('V1_19_R1','1.19 - 1.19.2'),'1.19.3':('V1_19_R2','1.19.3'),'1.19.4':('V1_19_R3','1.19.4'),
'1.20.1':('V1_20_R1','1.20 - 1.20.1'),'1.20.2':('V1_20_R2','1.20.2'),'1.20.4':('V1_20_R3','1.20.3 - 1.20.4'),
'1.20.6':('V1_20_R4','1.20.5 - 1.20.6'),'1.21.1':('V1_21_R1','1.21 - 1.21.1'),'1.21.3':('V1_21_R2','1.21.2 - 1.21.3'),
'1.21.4':('V1_21_R3','1.21.4'),'1.21.5':('V1_21_R4','1.21.5'),'1.21.6':('V1_21_R5','1.21.6 - 1.21.8'),'1.21.8':('V1_21_R5','1.21.6 - 1.21.8'),
'1.21.10':('V1_21_R6','1.21.9 - 1.21.10'),'1.21.11':('V1_21_R7','1.21.11'),'26.1.2':('V26_1','26.1.x'),'26.2':('V26_2','26.2.x'),
'26.3':('V26_3','26.3+')}
def rev(v):
    return '%s (%s)'%REV[v]
def since(e):
    # Added inside a revision (seen in its later version only): the versions it really has.
    if e['prev'] and REV[e['prev']][0]==REV[e['first']][0]:
        return '%s (1.21.7 - 1.21.8)'%REV[e['first']][0] if e['first']=='1.21.8' else rev(e['first'])
    return rev(e['first'])
L=[]; w=L.append
modern26=[n for n,e in tpost.items() if e['last']==post[-1]]
w("""# Materyaller (`org.bukkit.Material`), 1.8 - 26.3

Hangi materyal hangi sürümde var, adı ne zaman değişti, materyal sistemi kaç kez değişti. EranoAPI'nin sürümden
bağımsız materyal katmanı (HungerGames'in EranoAPI isteği 5) bu tablolara dayanacak.

## Yöntem ve kaynaklar

- **Adlar:** her NMS revizyonunun (EranoAPI'nin `Spigot/NMS/V*` modülleri, R1 / R2 / R3 ...) Spigot API'sinde
  `Material.values()` çalıştırıldı (`Dump.java`): ad, 1.13 öncesi sayısal ID, `isLegacy`, `isBlock`, `isItem`,
  `@Deprecated`. %d sürüm: %s. Tablolarda her ad, ilk görüldüğü revizyon ve o revizyonun Minecraft sürümleriyle
  yazılı (ör. `V1_20_R3 (1.20.3 - 1.20.4)`).
- **Revizyon içinde de materyal eklenebiliyor:** `V1_21_R5`'in iki sürümüne (1.21.6, 1.21.8) bakıldı ve
  `MUSIC_DISC_LAVA_CHICKEN` 1.21.7'de gelmiş. Öteki revizyonlarda tek sürüme bakıldığı için (genelde son alt sürüm)
  revizyonun ilk alt sürümünde olmayan bir ad gözden kaçmış olabilir; o yüzden kod, bir materyalin varlığını
  revizyona değil çalışma anında `Material.matchMaterial`'a sorar.
- **İsim değişiklikleri:** CraftBukkit'in `util/Commodore.java`'sı (eski API'yle yazılmış plugin'lerin bytecode'unda
  eski adı yeniyle değiştirir) ve hangi güncelleme commit'iyle girdikleri (`git log -S`); her biri dökümlerle
  doğrulandı (eski ad kaybolup aynı geçişte yeni ad geliyor).
- **1.13 flattening:** 1.13.2 sunucusunun kendi `CraftLegacy.fromLegacy`'si, her eski ad için 0 - 15 veri değeriyle
  çağrıldı (`Legacy.java`; Minecraft'ın veri dönüştürücüsü). Sunucu başlatılmadı, sadece sınıfları kullanıldı.
- Hepsi `scripts/material-report/` ile yeniden üretilir (yeni bir Minecraft sürümünde de).
"""%(len(versions),', '.join(versions)))
w("""## Materyal sistemi kaç kez değişti

| # | Sürümler | Sistem | Son görüntüsü |
|---|---|---|---|
| 1 | 1.8 - 1.12.2 | **Sayısal ID + veri değeri.** `Material` bir tür ailesi: `WOOL`, türü veri değerinde (`WOOL:14` kırmızı); ayrıntı `MaterialData` ile. Aynı ad hem blok hem eşya. | 1.12.2: %d ad (Ek A) |
| 2 | 1.13 - 26.3 | **Flattening.** Her tür kendi adı (`RED_WOOL`); veri değeri yok, blok durumu `BlockData`'da. Eski 463 ad `LEGACY_` önekiyle duruyor, hepsi `@Deprecated`; `plugin.yml`'de `api-version` olmayan plugin'lerin eski adları sunucu tarafından bunlara çevrilir. | 26.3: %d ad (+ 463 `LEGACY_`) (Ek B) |
| 3 | 1.20.6 - 26.3 | **`ItemType` / `BlockType`** arayüzleri eklendi (Registry tabanlı, deneysel); `Material` kaldırılmadı, deprecated değil, hâlâ enum. Sistem 2'nin yanında. | 26.3: `Material` enum + `ItemType` / `BlockType` |

Yani asıl kırılma bir kez oldu: **1.13**. Sonrasında sistem aynı kaldı; sadece tek tek adlar değişti (aşağıda 14 ad)
ve her sürümde yeni materyaller eklendi. 1.12.2'ye kadar hiçbir ad değişmedi; sadece `LOCKED_CHEST` (1.8, ID 95,
1.7'den kalma; 95 artık `STAINED_GLASS`) 1.8.3'te API'den çıkarıldı.

`isItem()` 1.8 - 1.11'de API'de yok (dökümlerde boş).
"""%(len([n for n,e in tpre.items() if e['last']=='1.12.2']),len(modern26)))
w("## Revizyon revizyon değişiklikler\n\n| Geçiş | Eklenen | Kaldırılan | Kaldırılanlar |\n|---|---|---|---|")
for c in a['changes']:
    rem=', '.join('`%s`'%r+(' → `%s`'%new_of[r] if r in new_of else '') for r in c['removed'])
    if REV[c['frm']][0]==REV[c['to']][0]:
        w('| %s %s → %s (aynı revizyon) | %d | %d | %s |'%(REV[c['to']][0],c['frm'],c['to'],len(c['added']),len(c['removed']),rem or ', '.join('+`%s`'%x for x in c['added'])))
        continue
    w('| %s → %s | %d | %d | %s |'%(REV[c['frm']][0],rev(c['to']),len(c['added']),len(c['removed']),rem))
w("| V1_12_R1 → V1_13_R1 (1.13) | flattening | flattening | 1.12.2'nin 463 adı `LEGACY_` oldu, yerine %d yeni ad (aşağıda) |"%len([n for n,e in tpost.items() if e['first']=='1.13']))
w("\nEklenen adların tam listesi Ek B'de (\"İlk revizyon\" sütunu).\n")
w("## İsim değişiklikleri (1.13 sonrası)\n\nEski ad o sürümde kaldırıldı, yenisi aynı anda geldi. `api-version`'ı eski olan plugin'ler için sunucu eski adı\nyeniye çevirir (Commodore); yeni API'yle yazılan kod eski adı bulamaz.\n\n| Eski ad | Yeni ad | Minecraft sürümü | Revizyon | Sunucunun çevirmesi |\n|---|---|---|---|---|")
for o,n,ver,src in RENAMES:
    w('| `%s` | `%s` | %s | %s → %s | %s |'%(o,n,ver,REV[tpost[o]['last']][0],rev(tpost[n]['first']),src))
# Flattening table
rows=[l.rstrip('\n').split('\t') for l in open('legacy-1.13.2.tsv')]
seen=defaultdict(set); flat=[]
for name,id_,data,block,item,isb,isi in rows:
    data=int(data); r=item if isi=='1' else block
    if r=='AIR' and not (name=='AIR' and data==0): continue
    if r in seen[name]: continue
    seen[name].add(r)
    flat.append((name,id_,data,r,block if (isb=='1' and isi=='1' and block!=item and block!='AIR') else ''))
w("""
## 1.13 flattening: eski ad + veri değeri → yeni ad

1.12.2'de `AD:veri` olarak yazılan her şeyin 1.13'te neye dönüştüğü (1.13.2 sunucusunun kendi dönüştürücüsü). Sadece
anlamı olan veri değerleri var: bir türü (renk, ağaç, taş çeşidi) seçenler; eşyalarda hasar, bloklarda yön / durum gibi
türü değiştirmeyen değerler aynı sonuca gittiği için tekrar yazılmadı. "Bugün" sütunu 1.13 sonrası isim
değişiklikleri uygulanmış hali (26.3). "Blok olarak" sadece blok ve eşya karşılığı farklıysa dolu (ör. eşya
`CHIPPED_ANVIL`, blokta veri değeri yön olduğu için `ANVIL`).

| 1.12.2 adı | ID | Veri | 1.13 adı | Bugün (26.3) | Blok olarak |
|---|---|---|---|---|---|""")
for name,id_,data,r,b in flat:
    t=today(r)
    w('| `%s` | %s | %d | `%s` | %s | %s |'%(name,id_,data,r,('`%s`'%t if t!=r else '='),('`%s`'%b if b else '')))
w("\n%d satır; %d eski adın birden fazla türü var.\n"%(len(flat),sum(1 for v in seen.values() if len(v)>1)))
w("## Ek A: 1.8 - 1.12.2 adları\n\n| Ad | ID | İlk revizyon | Son revizyon |\n|---|---|---|---|")
for n,e in sorted(tpre.items(),key=lambda x:(int(x[1]['id'] or 0),x[0])):
    w('| `%s` | %s | %s | %s |'%(n,e['id'],rev(e['first']),rev(e['last'])))
w("\n## Ek B: 1.13 - 26.3 adları\n\n`LEGACY_` adları hariç. Son revizyonu `V26_3` olmayanlar kaldırıldı (Not sütununda yerine gelen).\n\n| Ad | İlk revizyon | Son revizyon | Not |\n|---|---|---|---|")
for n,e in sorted(tpost.items()):
    note=''
    if n in new_of: note='→ `%s` (%s)'%(new_of[n],[r[2] for r in RENAMES if r[0]==n][0])
    elif n in old_of: note='← `%s`'%old_of[n]
    w('| `%s` | %s | %s | %s |'%(n,since(e),REV[e['last']][0],note))
w("""
## EranoAPI'de kullanımı

`me.erano.com.api.material.EranoMaterial` (Core) bu tablolardan üretildi (`scripts/material-report/generate.py`:
enum'un sabitleri, `eranoapi/material/materials.tsv` ve `legacy.tsv`). Çalışması:
1. **1.13+ sunucuda:** yeni ad doğrudan `Material`; eski bir yeni ad (`GRASS`, `CHAIN` ...) verilirse yukarıdaki
   isim değişikliği tablosuyla bugünkü ada çevrilir, yeni bir ad eski sunucuda yoksa (Ek B, "İlk revizyon") eskisine.
2. **1.8 - 1.12.2 sunucuda:** yeni ad (`RED_WOOL`) flattening tablosuyla eski ad + veri değerine (`WOOL:14`)
   çevrilir, `ItemStack(material, amount, data)` ile verilir; tabloda olmayan ve o sürümde de olmayan (Ek A) ad
   bulunamaz.
3. Tablolar kod içine üretilmiş veri olarak girer (bu betiklerden), elle yazılmaz. Sunucunun 1.8 - 1.12 mi olduğu
   `Material` enum'unda `LEGACY_AIR` olup olmamasından anlaşılır (sunucusuz testlerde de doğru).
""")
open(sys.argv[1],'w').write('\n'.join(L)+'\n')
print('lines',len(L))
