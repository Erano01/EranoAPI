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
('POTTERY_SHARD_ARCHER','ARCHER_POTTERY_SHERD','1.20','none (experimental in 1.19.4)'),('POTTERY_SHARD_ARMS_UP','ARMS_UP_POTTERY_SHERD','1.20','none (experimental in 1.19.4)'),
('POTTERY_SHARD_PRIZE','PRIZE_POTTERY_SHERD','1.20','none (experimental in 1.19.4)'),('POTTERY_SHARD_SKULL','SKULL_POTTERY_SHERD','1.20','none (experimental in 1.19.4)'),
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
w("""# Materials (`org.bukkit.Material`), 1.8 - 26.3

`EranoMaterial` gives every material of the newest Minecraft by its newest name on every server from 1.8:

| Server | What `EranoMaterial.RED_WOOL` is |
|---|---|
| 1.13+ | the server's `Material` of that name, or of its older name if it was renamed later (`SHORT_GRASS` is `GRASS` up to 1.20.2) |
| 1.8 - 1.12.2 | the old material with its data value (`WOOL:14`); items and blocks are made with it |

```java
ItemStack wool = EranoMaterial.RED_WOOL.parseItem(16);
EranoMaterial.match("WOOL:14");                 // also "minecraft:red_wool", "WOOD_SWORD", "A|B"
EranoMaterial.of(player.getItemInHand());       // data value included on 1.8
EranoMaterial.GRANITE.setType(block);           // STONE:1 on 1.8
```

The rest of this page is the data it is generated from (`scripts/material-report/run.sh`):

- **Names:** `Material.values()` of every NMS revision's Spigot API, %d versions (%s).
- **Renames:** CraftBukkit's `util/Commodore.java`, each checked against the dumps.
- **1.13 flattening:** the 1.13.2 server's own `CraftLegacy.fromLegacy`, called for every old name with data 0 - 15
  (no server started, only its classes).
"""%(len(versions),', '.join(versions)))
w("""## Material systems

| Versions | System |
|---|---|
| 1.8 - 1.12.2 | **Numeric id + data value.** A `Material` is a family (`WOOL`); the kind is the data value (`WOOL:14` = red). %d names. |
| 1.13 - 26.3 | **Flattening.** Every kind has its own name (`RED_WOOL`), block state in `BlockData`. The 463 old names stay with a `LEGACY_` prefix. %d names today. |
| 1.20.6 - 26.3 | `ItemType` / `BlockType` registries added next to `Material` (still an enum, not deprecated). |

The only real break is 1.13; after it only single names changed (14, below). Up to 1.12.2 no name changed;
`LOCKED_CHEST` was removed in 1.8.3.
"""%(len([n for n,e in tpre.items() if e['last']=='1.12.2']),len(modern26)))
w("## Changes per revision\n\n| Change | Added | Removed | Removed names |\n|---|---|---|---|")
for c in a['changes']:
    rem=', '.join('`%s`'%r+(' → `%s`'%new_of[r] if r in new_of else '') for r in c['removed'])
    if REV[c['frm']][0]==REV[c['to']][0]:
        w('| %s %s → %s (same revision) | %d | %d | %s |'%(REV[c['to']][0],c['frm'],c['to'],len(c['added']),len(c['removed']),rem or ', '.join('+`%s`'%x for x in c['added'])))
        continue
    w('| %s → %s | %d | %d | %s |'%(REV[c['frm']][0],rev(c['to']),len(c['added']),len(c['removed']),rem))
w("| V1_12_R1 → V1_13_R1 (1.13) | flattening | flattening | 1.12.2's 463 names became `LEGACY_`, %d new names (below) |"%len([n for n,e in tpost.items() if e['first']=='1.13']))
w("\nEvery added name is in Appendix B (\"First revision\").\n")
w("## Renames after 1.13\n\nThe old name went away as the new one came. The server rewrites the old name only for plugins with an older\n`api-version` (Commodore).\n\n| Old name | New name | Minecraft | Revision | Server rewrites it |\n|---|---|---|---|---|")
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
## 1.13 flattening: old name + data value → new name

What each 1.12.2 `NAME:data` became in 1.13 (the 1.13.2 server's own converter). Only data values that pick a kind
(color, wood, stone type) are listed. "Today" applies the later renames. "As a block" is set only when the block
differs from the item (item `CHIPPED_ANVIL`, block `ANVIL`, whose data value is its facing).

| 1.12.2 name | Id | Data | 1.13 name | Today (26.3) | As a block |
|---|---|---|---|---|---|""")
for name,id_,data,r,b in flat:
    t=today(r)
    w('| `%s` | %s | %d | `%s` | %s | %s |'%(name,id_,data,r,('`%s`'%t if t!=r else '='),('`%s`'%b if b else '')))
w("\n%d rows; %d old names have several kinds.\n"%(len(flat),sum(1 for v in seen.values() if len(v)>1)))
w("## Appendix A: 1.8 - 1.12.2 names\n\n| Name | Id | First revision | Last revision |\n|---|---|---|---|")
for n,e in sorted(tpre.items(),key=lambda x:(int(x[1]['id'] or 0),x[0])):
    w('| `%s` | %s | %s | %s |'%(n,e['id'],rev(e['first']),rev(e['last'])))
w("\n## Appendix B: 1.13 - 26.3 names\n\nWithout `LEGACY_` names. A last revision other than `V26_3` means removed (its successor in Note).\n\n| Name | First revision | Last revision | Note |\n|---|---|---|---|")
for n,e in sorted(tpost.items()):
    note=''
    if n in new_of: note='→ `%s` (%s)'%(new_of[n],[r[2] for r in RENAMES if r[0]==n][0])
    elif n in old_of: note='← `%s`'%old_of[n]
    w('| `%s` | %s | %s | %s |'%(n,since(e),REV[e['last']][0],note))
open(sys.argv[1],'w').write('\n'.join(L)+'\n')
print('lines',len(L))
