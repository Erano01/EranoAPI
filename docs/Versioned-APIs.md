# Versioned APIs

Bukkit names that changed between 1.8 and 26.x, and EranoAPI's API for each: one enum per area, every constant by
the newest Minecraft's name, resolved on each server to what that server calls it.

| Area | What changed | API |
|---|---|---|
| Materials | 1.13 flattening (id + data value → one name per kind); 14 renames after | ✅ `EranoMaterial` ([Material.md](Material.md)) |
| Sounds | Every name in 1.9 and again in 1.13; `Sound` became an interface in 1.21.2 | ✅ `EranoSound` ([Sound.md](Sound.md)) |
| Enchantments | Keys in 1.13, Minecraft's names in 1.20.5 (`DAMAGE_ALL` → `SHARPNESS`) | ✅ `EranoEnchantment` ([Enchantment.md](Enchantment.md)) |
| Potion effects, types | Renamed in 1.20.5 (`INCREASE_DAMAGE` → `STRENGTH`); potion items differ in 1.8, 1.9, 1.20.2 | ✅ `EranoPotionEffect`, `EranoPotionType` ([Potion.md](Potion.md)) |
| Particles, effects | No API on 1.8; data types in 1.13; renamed in 1.20.5 | ✅ `EranoParticle`, `EranoEffect` ([Particle.md](Particle.md)) |
| Entity types | Renamed in 1.13 and 1.20.5 (`PIG_ZOMBIE` → `ZOMBIFIED_PIGLIN`) | planned |
| Game rules | Strings before 1.13, typed after, renamed in 26.x | planned |
| Block looks | Data values on 1.8 - 1.12, `BlockData` after | planned |
| Attributes | No API on 1.8; `GENERIC_` prefix dropped in 1.21.3 | planned |
| Dye colors, item flags | `SILVER` → `LIGHT_GRAY` (1.13), `HIDE_POTION_EFFECTS` → `HIDE_ADDITIONAL_TOOLTIP` (1.20.5) | planned |

## Common to every one

```java
EranoX.NAME.parseX();      // the server's value, null if it doesn't have it
EranoX.NAME.isSupported();
EranoX.match("old name");  // today's name, older names, minecraft: keys; case-insensitive
EranoX.of(serverValue);    // back to the constant
```

## How the tables are made

Generated, never written by hand (`scripts/material-report`, `scripts/registry-report`):

1. **Dump** every NMS revision's Spigot API from its bytecode, so registry-backed values need no server.
2. **Renames** from CraftBukkit's own tables (`FieldRename`, `Commodore`); sounds by the files Mojang's
   `sounds.json` plays.
3. **Check** every revision: a value's names tried newest first must find the value itself; otherwise nothing is
   generated.
4. **Generate** the enum constants and the tables in Core's resources, and the report in `docs/`.

A new Minecraft version: build its Spigot jar, run both scripts.
