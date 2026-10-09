"""Sounds, particles and effects, potions, enchantments: the reports (docs/Sound.md, Particle.md, Potion.md,
Enchantment.md) and what EranoAPI's EranoSound, EranoParticle, EranoEffect, EranoPotionEffect, EranoPotionType and
EranoEnchantment are made of (their constants and tables). Run in the work folder after dumpall.py, soundkeys.py and
mojang.py (run.sh does all of it).

Every value is followed through the NMS revisions by its renames (CraftBukkit's FieldRename; sounds by the files
they play, Mojang's sounds.json), and each table is checked: on every revision the first of a value's names the
server has must be the value itself."""
import difflib
import json
import os
import sys

from fieldrename import table
from registry import REV, history, rev, vkey
from soundmap import build

ROOT = sys.argv[1]
CORE = os.path.join(ROOT, 'Spigot/Core')
api = json.load(open('api.json'))
skeys = json.load(open('soundkeys.json'))


# ---------------------------------------------------------------------------------------------------------- rows

def rows_from_history(cls, renames, exclude=lambda n: False):
    """Every value of a Bukkit type by its newest name: the names it had in each revision, key, numeric id."""
    vs, entries, checks, names_in = history(api, cls, renames)
    rows = []
    for name, e in entries.items():
        if exclude(name):
            continue
        rows.append(dict(name=name, hist={v: list(e['all'][v]) for v in e['all']}, key=e['key'] or '',
                         id=e['id'], first=e['first'], last=e['last'], notes={}))
    return vs, rows, checks, names_in


def finish(rows, vs):
    """Older names newest first; alive = in the newest revision."""
    for r in rows:
        old = []
        for v in sorted(r['hist'], key=vkey, reverse=True):
            for n in r['hist'][v]:
                if n != r['name'] and n not in old:
                    old.append(n)
        r['old'] = old
        r['aliases'] = list(old)
        r['first'] = min(r['hist'], key=vkey)
        r['last'] = max(r['hist'], key=vkey)
        r['alive'] = r['last'] == vs[-1]
    return rows


def validate(rows, vs, names_in, what):
    """On every revision the first name the server has must be the value itself (what the API does at runtime)."""
    bad = []
    for r in rows:
        if not r['alive']:
            continue
        candidates = [r['name']] + r['old']
        for v in vs:
            have = set(names_in[v])
            got = next((c for c in candidates if c in have), None)
            want = r['hist'].get(v)
            if (got is None) != (want is None) or (got is not None and got not in want):
                bad.append((r['name'], v, got, want))
    if bad:
        raise SystemExit('%s: names resolve wrongly on some revisions: %s' % (what, bad[:10]))


def sound_rows():
    vs, hist, fuzzy = build(skeys)
    names_in = {v: list(skeys[v]) for v in vs}
    rows = []
    for name, h in hist.items():
        rows.append(dict(name=name, hist={v: [h[v][0]] for v in h}, key=skeys[vs[-1]][name], id=None, notes={}))
    by_name = {r['name']: r for r in rows}
    for name, v, old_key, new_key, j in fuzzy:
        by_name[name]['notes']['fuzzy'] = '%s: `%s` → `%s` (%d%% of the files shared)' % (REV[v][0], new_key, old_key, round(j * 100))
    # Re-recorded in 1.9: no file in common, but 1.8's game played these for the same thing.
    for name, old in SOUND_BY_MEANING.items():
        r = by_name[name]
        assert old in skeys['1.8.8'] and not any(v in r['hist'] for v in ('1.8', '1.8.3', '1.8.8')), name
        for v in ('1.8', '1.8.3', '1.8.8'):
            r['hist'][v] = [old]
        r['notes']['meaning'] = True
    finish(rows, vs)
    # A 1.8 name shared by several of today's sounds is matched as one of them
    owners = {}
    for r in rows:
        for o in r['old']:
            owners.setdefault(o, []).append(r['name'])
    for r in rows:
        r['aliases'] = []
    for o, names in owners.items():
        if o in by_name:
            continue
        if SOUND_PRIMARY.get(o) in names:
            primary = SOUND_PRIMARY[o]
        else:
            if o in SOUND_PRIMARY:
                print('SOUND_PRIMARY: %s is not %s\'s, it is one of %s' % (SOUND_PRIMARY[o], o, names))
            primary = max(sorted(names), key=lambda n: difflib.SequenceMatcher(None, o, strip(n)).ratio())
        by_name[primary]['aliases'].append(o)
    validate(rows, vs, names_in, 'sounds')
    return vs, rows, names_in


def strip(name):
    for prefix in ('ENTITY_', 'BLOCK_', 'ITEM_', 'UI_', 'AMBIENT_'):
        if name.startswith(prefix):
            return name[len(prefix):]
    return name


# 1.8 sounds Mojang re-recorded in 1.9 (the files differ), by what 1.8 played for the same thing
SOUND_BY_MEANING = {
    'BLOCK_CHEST_OPEN': 'CHEST_OPEN', 'BLOCK_CHEST_CLOSE': 'CHEST_CLOSE',
    'BLOCK_WOODEN_DOOR_OPEN': 'DOOR_OPEN', 'BLOCK_WOODEN_DOOR_CLOSE': 'DOOR_CLOSE',
    'BLOCK_WOODEN_TRAPDOOR_OPEN': 'DOOR_OPEN', 'BLOCK_WOODEN_TRAPDOOR_CLOSE': 'DOOR_CLOSE',
    'BLOCK_FENCE_GATE_OPEN': 'DOOR_OPEN', 'BLOCK_FENCE_GATE_CLOSE': 'DOOR_CLOSE',
    'BLOCK_IRON_DOOR_OPEN': 'DOOR_OPEN', 'BLOCK_IRON_DOOR_CLOSE': 'DOOR_CLOSE',
    'BLOCK_IRON_TRAPDOOR_OPEN': 'DOOR_OPEN', 'BLOCK_IRON_TRAPDOOR_CLOSE': 'DOOR_CLOSE',
}
# 1.8 names several of today's sounds play: which one the name means
SOUND_PRIMARY = {
    'CLICK': 'UI_BUTTON_CLICK', 'SHOOT_ARROW': 'ENTITY_ARROW_SHOOT', 'EXPLODE': 'ENTITY_GENERIC_EXPLODE',
    'HURT_FLESH': 'ENTITY_PLAYER_HURT', 'DIG_STONE': 'BLOCK_STONE_BREAK', 'STEP_STONE': 'BLOCK_STONE_STEP',
    'DIG_WOOD': 'BLOCK_WOOD_BREAK', 'STEP_WOOD': 'BLOCK_WOOD_STEP', 'FIZZ': 'BLOCK_FIRE_EXTINGUISH',
    'GHAST_FIREBALL': 'ENTITY_GHAST_SHOOT', 'ANVIL_LAND': 'BLOCK_ANVIL_LAND', 'FALL_BIG': 'ENTITY_PLAYER_BIG_FALL',
    'FALL_SMALL': 'ENTITY_PLAYER_SMALL_FALL', 'SWIM': 'ENTITY_PLAYER_SWIM', 'SPLASH2': 'ENTITY_PLAYER_SPLASH',
    'GLASS': 'BLOCK_GLASS_BREAK', 'FIRE': 'BLOCK_FIRE_AMBIENT', 'FUSE': 'ENTITY_TNT_PRIMED',
    'ITEM_BREAK': 'ENTITY_ITEM_BREAK', 'ENDERMAN_TELEPORT': 'ENTITY_ENDERMAN_TELEPORT',
    'ENDERDRAGON_GROWL': 'ENTITY_ENDER_DRAGON_GROWL', 'SLIME_WALK': 'ENTITY_SLIME_HURT_SMALL',
    'SLIME_WALK2': 'ENTITY_SLIME_SQUISH', 'AMBIENCE_RAIN': 'WEATHER_RAIN', 'ARROW_HIT': 'ENTITY_ARROW_HIT',
    'DOOR_OPEN': 'BLOCK_WOODEN_DOOR_OPEN', 'DOOR_CLOSE': 'BLOCK_WOODEN_DOOR_CLOSE',
}


# --------------------------------------------------------------------------------------------------- the tables

def write_api(package, java, tsv, rows, extra=lambda r: ''):
    alive = sorted((r for r in rows if r['alive']), key=lambda r: r['name'])
    folder = os.path.join(CORE, 'src/main/resources/eranoapi', package)
    os.makedirs(folder, exist_ok=True)
    with open(os.path.join(folder, tsv), 'w') as f:
        f.write('# name\tolder names, newest first\tolder names matched as this\tkey\tfirst revision\textra\n')
        f.write('# Generated by scripts/registry-report/generate.py, do not edit.\n')
        for r in alive:
            f.write('\t'.join([r['name'], ','.join(r['old']), ','.join(r['aliases']), r['key'] or '',
                               REV[r['first']][0], extra(r)]) + '\n')
    path = os.path.join(CORE, 'src/main/java/me/erano/com/api', package, java + '.java')
    body = open(path).read()
    start, end = '    // <generated constants>', '    // </generated constants>'
    i = body.index(start) + len(start)
    j = body.index(end)
    body = body[:i] + '\n' + ',\n'.join('    ' + r['name'] for r in alive) + ';\n' + body[j:]
    open(path, 'w').write(body)
    return len(alive)


# ------------------------------------------------------------------------------------------------------ reports

HEAD = """# %s

%s

The tables below are what it is generated from (`scripts/registry-report/run.sh`): every NMS revision's Spigot API,
read from its bytecode (no class loaded, so registry-backed values too), and CraftBukkit's own rename tables
(`legacy/FieldRename.java`). See also [Versioned-APIs.md](Versioned-APIs.md).
"""


def names(xs, limit=None):
    xs = sorted(xs)
    if limit and len(xs) > limit:
        return '%d values (table below)' % len(xs)
    return ', '.join('`%s`' % x for x in xs)


def changes_table(L, vs, names_in, exclude=lambda n: False, limit=None):
    L.append('| Change | Added | Removed |\n|---|---|---|')
    for a, b in zip(vs, vs[1:]):
        added = [n for n in set(names_in[b]) - set(names_in[a]) if not exclude(n)]
        removed = [n for n in set(names_in[a]) - set(names_in[b]) if not exclude(n)]
        if added or removed:
            L.append('| %s → %s | %s | %s |' % (REV[a][0], rev(b), names(added, limit), names(removed, limit)))
    L.append('')


def renames_table(L, checks, label):
    L.append('From CraftBukkit `legacy/FieldRename.java` `%s`, each checked against the dumps.\n' % label)
    L.append('| Old name | New name | Revision |\n|---|---|---|')
    for old, new, last_old, first_new, ok in checks:
        if ok:
            L.append('| `%s` | `%s` | %s |' % (old, new, rev(first_new)))
        elif last_old is None:
            L.append('| `%s` | `%s` | the field was always `%s`; only the key changed (`%s`) |'
                     % (old, new, new, new.lower()))
        else:
            L.append('| `%s` | `%s` | %s → %s |' % (old, new, REV[last_old][0], rev(first_new) if first_new else '-'))
    L.append('')


def values_table(L, rows, columns=('key', 'id')):
    head = ['Name', 'First revision', 'Last revision', 'Older names']
    if 'key' in columns:
        head.append('Key')
    if 'id' in columns:
        head.append('Numeric id')
    L.append('By newest name; older names newest first, tried on a server in this order.\n')
    L.append('| ' + ' | '.join(head) + ' |\n|' + '---|' * len(head))
    for r in sorted(rows, key=lambda r: (vkey(r['first']), r['name'])):
        cells = ['`%s`' % r['name'], rev(r['first']), REV[r['last']][0], ', '.join('`%s`' % o for o in r['old'])]
        if 'key' in columns:
            cells.append('`%s`' % r['key'] if r['key'] else '')
        if 'id' in columns:
            cells.append('' if r['id'] is None else str(r['id']))
        L.append('| ' + ' | '.join(cells) + ' |')
    L.append('')


def write_doc(name, L):
    open(os.path.join(ROOT, 'docs', name), 'w').write('\n'.join(L).rstrip() + '\n')


# ------------------------------------------------------------------------------------------------ enchantments

def enchantments():
    vs, rows, checks, names_in = rows_from_history('org.bukkit.enchantments.Enchantment', table('ENCHANTMENT_DATA'))
    finish(rows, vs)
    validate(rows, vs, names_in, 'enchantments')
    n = write_api('enchantment', 'EranoEnchantment', 'enchantments.tsv', rows,
                  lambda r: '' if r['id'] is None else str(r['id']))
    L = [HEAD % ('Enchantments (`Enchantment`), 1.8 - 26.3',
                 """`EranoEnchantment`: every enchantment of today by its newest name, on every server from 1.8.

```java
EranoEnchantment.SHARPNESS.enchant(sword, 2);    // DAMAGE_ALL up to 1.20.4
EranoEnchantment.match("DAMAGE_ALL");            // SHARPNESS; keys too: "minecraft:sharpness"
EranoEnchantment.SHARPNESS.legacyId();           // 16, the 1.8 - 1.12 numeric id
```""")]
    L.append("""## Systems

| Versions | System |
|---|---|
| 1.8 - 1.12.2 | Class with numeric ids (`DAMAGE_ALL` = 16); `getByName` / `getById` |
| 1.13 - 1.20.4 | Same names, now with keys (`minecraft:sharpness`, `getByKey`); no numeric ids |
| 1.20.5 - 26.3 | Fields take Minecraft's names (`SHARPNESS`), values come from the registry |

EranoAPI reads the static field (`Enchantment.class.getField(name)`), which every version has.

## Renames
""")
    renames_table(L, checks, 'ENCHANTMENT_DATA')
    L.append('## Changes per revision\n')
    changes_table(L, vs, names_in)
    L.append('## All values\n')
    values_table(L, rows)
    write_doc('Enchantment.md', L)
    return n


# ----------------------------------------------------------------------------------------------------- potions

def potions():
    vs, effects, checks_e, names_e = rows_from_history('org.bukkit.potion.PotionEffectType', table('POTION_EFFECT_TYPE_DATA'))
    finish(effects, vs)
    validate(effects, vs, names_e, 'potion effects')
    vs2, types, checks_t, names_t = rows_from_history('org.bukkit.potion.PotionType', table('POTION_TYPE_DATA'))
    finish(types, vs2)
    validate(types, vs2, names_t, 'potion types')
    n1 = write_api('potion', 'EranoPotionEffect', 'effects.tsv', effects,
                   lambda r: '' if r['id'] is None else str(r['id']))
    n2 = write_api('potion', 'EranoPotionType', 'types.tsv', types)
    L = [HEAD % ('Potions (`PotionEffectType`, `PotionType`), 1.8 - 26.3',
                 """`EranoPotionEffect` (the effect on an entity) and `EranoPotionType` (a potion item's type), by their newest
names on every server from 1.8.

```java
EranoPotionEffect.STRENGTH.apply(player, 200, 1);                    // INCREASE_DAMAGE up to 1.20.4
ItemStack potion = EranoPotionType.LONG_SWIFTNESS.parseItem(EranoPotionType.Form.SPLASH, 1);
```

| Server | Potion item |
|---|---|
| 1.8 | `POTION` with a data value (the `Potion` class; splash is a bit) |
| 1.9 - 1.20.1 | `POTION` / `SPLASH_POTION` / `LINGERING_POTION` + `PotionData(type, extended, upgraded)` |
| 1.20.2 - 26.3 | `PotionMeta#setBasePotionType`; long / strong types are their own constants |""")]
    L.append("""## Potion effects

| Versions | System |
|---|---|
| 1.8 - 1.20.2 | Class with numeric ids (`INCREASE_DAMAGE` = 5); the ids never changed |
| 1.20.3 - 1.20.4 | Keys added (`minecraft:strength`) |
| 1.20.5 - 26.3 | Fields take Minecraft's names (`STRENGTH`), values come from the registry |

### Renames
""")
    renames_table(L, checks_e, 'POTION_EFFECT_TYPE_DATA')
    L.append('### Changes per revision\n')
    changes_table(L, vs, names_e)
    L.append('### All values\n')
    values_table(L, effects)
    L.append("""## Potion types

| Versions | System |
|---|---|
| 1.8 - 1.20.1 | Enum; long / strong are flags of the item |
| 1.20.2 - 1.20.4 | Long / strong types become constants (`LONG_SWIFTNESS`, `STRONG_SWIFTNESS`) |
| 1.20.5 - 26.3 | Constants take Minecraft's names (`SWIFTNESS`, `LEAPING` ...); the `Potion` class is gone |

### Renames
""")
    renames_table(L, checks_t, 'POTION_TYPE_DATA')
    L.append('### Changes per revision\n')
    changes_table(L, vs2, names_t)
    L.append('### All values\n')
    values_table(L, types, columns=('key',))
    write_doc('Potion.md', L)
    return n1, n2


# --------------------------------------------------------------------------------------- particles and effects

def particles():
    legacy = lambda n: n.startswith('LEGACY_')
    vs, rows, checks, names_in = rows_from_history('org.bukkit.Particle', table('PARTICLE_DATA'), exclude=legacy)
    finish(rows, vs)
    names_clean = {v: [n for n in names_in[v] if not legacy(n)] for v in vs}
    validate(rows, vs, names_clean, 'particles')
    n1 = write_api('particle', 'EranoParticle', 'particles.tsv', rows)
    vs2, effects, _, names_e = rows_from_history('org.bukkit.Effect', [])
    finish(effects, vs2)
    validate(effects, vs2, names_e, 'effects')
    n2 = write_api('particle', 'EranoEffect', 'effects.tsv', effects)
    L = [HEAD % ('Particles (`Particle`) and effects (`Effect`), 1.8 - 26.3',
                 """`EranoParticle`: every particle of today by its newest name, on every server from 1.8 (which has no particle
API: EranoAPI sends the packet). Data is given the same way everywhere and converted per version: a `Color` for
`DUST` and `ENTITY_EFFECT`; a `Material`, `EranoMaterial` or `ItemStack` for `BLOCK`, `ITEM`, `FALLING_DUST` ...
`EranoEffect`: the world effects (`World#playEffect`) and which server has which.

```java
EranoParticle.HAPPY_VILLAGER.spawn(location, 10, 0.5, 0.5, 0.5, 0);
EranoParticle.DUST.spawn(location, 1, 0, 0, 0, 0, Color.AQUA);                 // colored REDSTONE packet on 1.8
EranoParticle.BLOCK.spawn(location, 30, 0.3, 0.3, 0.3, 0, EranoMaterial.RED_WOOL);
EranoEffect.STEP_SOUND.play(location, Material.STONE);
```""")]
    L.append("""## Particles

| Versions | System | Data |
|---|---|---|
| 1.8 - 1.8.8 | No Bukkit API: NMS `EnumParticle` + `PacketPlayOutWorldParticles` | `int[]`: item `{id, data}`, block `{id \\| data << 12}`; dust color in the offsets |
| 1.9 - 1.12.2 | `Particle` enum, the names of 1.8's `EnumParticle` | `ItemStack`, `MaterialData` |
| 1.13 - 1.20.4 | Same names; old data types moved to `LEGACY_BLOCK_CRACK` ... | `BlockData`, `DustOptions`, `ItemStack` |
| 1.20.5 - 26.3 | Constants take Minecraft's names (`REDSTONE` → `DUST`, `BLOCK_CRACK` → `BLOCK`) | `ENTITY_EFFECT` needs a `Color` |

1.8's particles here are read from `EnumParticle` (the Spigot server jar). `LEGACY_` constants are left out.

### Renames
""")
    renames_table(L, checks, 'PARTICLE_DATA')
    L.append("""Not in that table (removed without a mapping): `BARRIER` and `LIGHT` became `BLOCK_MARKER` (with the block's
data) in 1.18; the three cherry leaves particles became `CHERRY_LEAVES` in 1.20; `FOOTSTEP` and `ITEM_TAKE` went in
1.13, `GUST_DUST` in 1.20.5.

### Changes per revision
""")
    changes_table(L, vs, names_in, exclude=legacy, limit=30)
    L.append('### All values\n')
    values_table(L, [r for r in rows], columns=('key',))
    L.append("""## Effects

`World#playEffect`: world events that are a sound or a sight (a door, `STEP_SOUND`'s block break ...). Never renamed,
only added. Spigot's particle entries of 1.8 - 1.12 (`FLAME`, `HAPPY_VILLAGER` ...) went in 1.13; they are
`EranoParticle`s.

### Changes per revision
""")
    changes_table(L, vs2, names_e)
    L.append('### All values\n')
    values_table(L, effects, columns=())
    write_doc('Particle.md', L)
    return n1, n2


# ------------------------------------------------------------------------------------------------------- sounds

def sounds():
    vs, rows, names_in = sound_rows()
    n = write_api('sound', 'EranoSound', 'sounds.tsv', rows)
    by_name = {r['name']: r for r in rows}
    L = [HEAD % ('Sounds (`Sound`), 1.8 - 26.3',
                 """`EranoSound`: every sound of today by its newest name, on every server from 1.8.

```java
EranoSound.ENTITY_PLAYER_LEVELUP.play(player, 1f, 1f);       // LEVEL_UP on 1.8
EranoSound.BLOCK_NOTE_BLOCK_PLING.play(location, 1f, 2f);    // BLOCK_NOTE_PLING on 1.9 - 1.12
EranoSound.match("AMBIENCE_THUNDER");                        // ENTITY_LIGHTNING_BOLT_THUNDER; keys too
```""")]
    L.append("""## Systems

| Versions | System |
|---|---|
| 1.8 - 1.8.8 | Enum, 194 sounds with Bukkit's own names (`LEVEL_UP`, `CLICK`) |
| 1.9 - 1.12.2 | Minecraft renamed every sound; the enum took its names (`ENTITY_PLAYER_LEVELUP`) |
| 1.13 - 1.21.1 | Renamed again (`block.note.pling` → `block.note_block.pling`) |
| 1.21.2 - 26.3 | `Sound` is an interface (registry); `valueOf` / `switch` break in old plugins, static fields stay |

## How sounds are matched

CraftBukkit kept no rename table for 1.9 and 1.13, so each sound is followed with Mojang's data:

1. Bukkit constant → Minecraft sound event per revision (CraftBukkit's `CraftSound` before 1.16, the API's keys after).
2. The same event in the next revision is the same sound.
3. Otherwise the older revision's event that **plays the same files** (Mojang's `sounds.json`; a moved file with the
   same content counts). At least half of the files must be shared.
4. A few sounds Mojang re-recorded in 1.9 (chests, doors, trapdoors, fence gates) share no file; they take what 1.8
   played for the same thing ("by meaning" below).
5. Checked on every revision: trying a sound's names newest first, the first one the server has is that sound.

Two of 1.8's 194 sounds have no sound today: `NOTE_BASS` (replaced by another recording in 1.9) and `WOLF_HOWL`
(removed in 1.21.5).

## Common sounds
""")
    L.append('| Today | 1.13 - 1.21 | 1.9 - 1.12 | 1.8 |\n|---|---|---|---|')
    for name in ['ENTITY_PLAYER_LEVELUP', 'ENTITY_EXPERIENCE_ORB_PICKUP', 'UI_BUTTON_CLICK', 'BLOCK_NOTE_BLOCK_PLING',
                 'BLOCK_NOTE_BLOCK_HARP', 'BLOCK_NOTE_BLOCK_BASEDRUM', 'ENTITY_LIGHTNING_BOLT_THUNDER',
                 'ENTITY_GENERIC_EXPLODE', 'ENTITY_ENDERMAN_TELEPORT', 'ENTITY_ENDER_DRAGON_GROWL', 'ENTITY_WITHER_SPAWN',
                 'BLOCK_ANVIL_PLACE', 'BLOCK_ANVIL_LAND', 'BLOCK_CHEST_OPEN', 'ENTITY_ITEM_PICKUP', 'ENTITY_ARROW_HIT_PLAYER',
                 'ENTITY_VILLAGER_NO', 'ENTITY_VILLAGER_YES', 'ENTITY_FIREWORK_ROCKET_BLAST', 'ENTITY_SNOWBALL_THROW']:
        h = by_name[name]['hist']
        cell = lambda v: '`%s`' % h[v][0] if v in h else '-'
        L.append('| `%s` | %s | %s | %s |' % (name, cell('1.13'), cell('1.12.2'), cell('1.8.8')))
    L.append('\n## Changes per revision\n')
    L.append('Renames are not counted as added / removed.\n')
    L.append('| Change | Added | Removed | Renamed |\n|---|---|---|---|')
    for a, b in zip(vs, vs[1:]):
        renamed = sorted({(r['hist'][a][0], r['hist'][b][0]) for r in rows
                          if a in r['hist'] and b in r['hist'] and r['hist'][a][0] != r['hist'][b][0]})
        old_side = {x for x, _ in renamed}
        new_side = {y for _, y in renamed}
        added = [n for n in set(names_in[b]) - set(names_in[a]) if n not in new_side]
        removed = [n for n in set(names_in[a]) - set(names_in[b]) if n not in old_side]
        if added or removed or renamed:
            ren = ('%d sounds (table below)' % len(renamed)) if len(renamed) > 40 else \
                ', '.join('`%s` → `%s`' % x for x in renamed)
            L.append('| %s → %s | %s | %s | %s |' % (REV[a][0], rev(b), names(added, 40), names(removed, 40), ren))
    L.append('\n## All sounds\n')
    L.append('By today\'s name, with its name in each era ("same": unchanged, "-": not there). Note: "by meaning" '
             '(point 4 above), "close" (only part of the files shared: a music split into biomes plays the old general '
             'one).\n')
    L.append('| Name | Key | First revision | 1.13 - 1.21 | 1.9 - 1.12 | 1.8 | Note |\n|---|---|---|---|---|---|---|')
    for r in sorted(rows, key=lambda r: r['name']):
        h = r['hist']
        def era(versions):
            got = []
            for v in versions:
                if v in h and h[v][0] != r['name'] and h[v][0] not in got:
                    got.append(h[v][0])
            if got:
                return ', '.join('`%s`' % x for x in got)
            return 'same' if any(v in h for v in versions) else '-'
        modern = [v for v in vs if vkey(v) >= [1, 13] and vkey(v) < [1, 21, 2]]
        middle = [v for v in vs if [1, 9] <= vkey(v) < [1, 13]]
        old = [v for v in vs if vkey(v) < [1, 9]]
        note = []
        if r['notes'].get('meaning'):
            note.append('by meaning')
        if 'fuzzy' in r['notes']:
            note.append('close: ' + r['notes']['fuzzy'])
        L.append('| `%s` | `%s` | %s | %s | %s | %s | %s |' % (r['name'], r['key'], REV[r['first']][0], era(modern),
                                                             era(middle), era(old), '; '.join(note)))
    write_doc('Sound.md', L)
    return n


if __name__ == '__main__':
    print('enchantments', enchantments())
    print('potion effects, types', potions())
    print('particles, effects', particles())
    print('sounds', sounds())
