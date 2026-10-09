# Potions (`PotionEffectType`, `PotionType`), 1.8 - 26.3

`EranoPotionEffect` (the effect on an entity) and `EranoPotionType` (a potion item's type), by their newest
names on every server from 1.8.

```java
EranoPotionEffect.STRENGTH.apply(player, 200, 1);                    // INCREASE_DAMAGE up to 1.20.4
ItemStack potion = EranoPotionType.LONG_SWIFTNESS.parseItem(EranoPotionType.Form.SPLASH, 1);
```

| Server | Potion item |
|---|---|
| 1.8 | `POTION` with a data value (the `Potion` class; splash is a bit) |
| 1.9 - 1.20.1 | `POTION` / `SPLASH_POTION` / `LINGERING_POTION` + `PotionData(type, extended, upgraded)` |
| 1.20.2 - 26.3 | `PotionMeta#setBasePotionType`; long / strong types are their own constants |

The tables below are what it is generated from (`scripts/registry-report/run.sh`): every NMS revision's Spigot API,
read from its bytecode (no class loaded, so registry-backed values too), and CraftBukkit's own rename tables
(`legacy/FieldRename.java`). See also [Versioned-APIs.md](Versioned-APIs.md).

## Potion effects

| Versions | System |
|---|---|
| 1.8 - 1.20.2 | Class with numeric ids (`INCREASE_DAMAGE` = 5); the ids never changed |
| 1.20.3 - 1.20.4 | Keys added (`minecraft:strength`) |
| 1.20.5 - 26.3 | Fields take Minecraft's names (`STRENGTH`), values come from the registry |

### Renames

From CraftBukkit `legacy/FieldRename.java` `POTION_EFFECT_TYPE_DATA`, each checked against the dumps.

| Old name | New name | Revision |
|---|---|---|
| `SLOW` | `SLOWNESS` | V1_20_R4 (1.20.5 - 1.20.6) |
| `FAST_DIGGING` | `HASTE` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SLOW_DIGGING` | `MINING_FATIGUE` | V1_20_R4 (1.20.5 - 1.20.6) |
| `INCREASE_DAMAGE` | `STRENGTH` | V1_20_R4 (1.20.5 - 1.20.6) |
| `HEAL` | `INSTANT_HEALTH` | V1_20_R4 (1.20.5 - 1.20.6) |
| `HARM` | `INSTANT_DAMAGE` | V1_20_R4 (1.20.5 - 1.20.6) |
| `JUMP` | `JUMP_BOOST` | V1_20_R4 (1.20.5 - 1.20.6) |
| `CONFUSION` | `NAUSEA` | V1_20_R4 (1.20.5 - 1.20.6) |
| `DAMAGE_RESISTANCE` | `RESISTANCE` | V1_20_R4 (1.20.5 - 1.20.6) |

### Changes per revision

| Change | Added | Removed |
|---|---|---|
| V1_8_R3 → V1_9_R1 (1.9 - 1.9.3) | `GLOWING`, `LEVITATION`, `LUCK`, `UNLUCK` |  |
| V1_12_R1 → V1_13_R1 (1.13) | `CONDUIT_POWER`, `DOLPHINS_GRACE`, `SLOW_FALLING` |  |
| V1_13_R2 → V1_14_R1 (1.14.x) | `BAD_OMEN`, `HERO_OF_THE_VILLAGE` |  |
| V1_18_R2 → V1_19_R1 (1.19 - 1.19.2) | `DARKNESS` |  |
| V1_20_R3 → V1_20_R4 (1.20.5 - 1.20.6) | `HASTE`, `INFESTED`, `INSTANT_DAMAGE`, `INSTANT_HEALTH`, `JUMP_BOOST`, `MINING_FATIGUE`, `NAUSEA`, `OOZING`, `RAID_OMEN`, `RESISTANCE`, `SLOWNESS`, `STRENGTH`, `TRIAL_OMEN`, `WEAVING`, `WIND_CHARGED` | `CONFUSION`, `DAMAGE_RESISTANCE`, `FAST_DIGGING`, `HARM`, `HEAL`, `INCREASE_DAMAGE`, `JUMP`, `SLOW`, `SLOW_DIGGING` |
| V1_21_R6 → V1_21_R7 (1.21.11) | `BREATH_OF_THE_NAUTILUS` |  |

### All values

By newest name; older names newest first, tried on a server in this order.

| Name | First revision | Last revision | Older names | Key | Numeric id |
|---|---|---|---|---|---|
| `ABSORPTION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `absorption` | 22 |
| `BLINDNESS` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `blindness` | 15 |
| `FIRE_RESISTANCE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `fire_resistance` | 12 |
| `HASTE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `FAST_DIGGING` | `haste` | 3 |
| `HEALTH_BOOST` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `health_boost` | 21 |
| `HUNGER` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `hunger` | 17 |
| `INSTANT_DAMAGE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `HARM` | `instant_damage` | 7 |
| `INSTANT_HEALTH` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `HEAL` | `instant_health` | 6 |
| `INVISIBILITY` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `invisibility` | 14 |
| `JUMP_BOOST` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `JUMP` | `jump_boost` | 8 |
| `MINING_FATIGUE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SLOW_DIGGING` | `mining_fatigue` | 4 |
| `NAUSEA` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `CONFUSION` | `nausea` | 9 |
| `NIGHT_VISION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `night_vision` | 16 |
| `POISON` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `poison` | 19 |
| `REGENERATION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `regeneration` | 10 |
| `RESISTANCE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `DAMAGE_RESISTANCE` | `resistance` | 11 |
| `SATURATION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `saturation` | 23 |
| `SLOWNESS` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SLOW` | `slowness` | 2 |
| `SPEED` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `speed` | 1 |
| `STRENGTH` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `INCREASE_DAMAGE` | `strength` | 5 |
| `WATER_BREATHING` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `water_breathing` | 13 |
| `WEAKNESS` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `weakness` | 18 |
| `WITHER` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `wither` | 20 |
| `GLOWING` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `glowing` | 24 |
| `LEVITATION` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `levitation` | 25 |
| `LUCK` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `luck` | 26 |
| `UNLUCK` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `unluck` | 27 |
| `CONDUIT_POWER` | V1_13_R1 (1.13) | V26_3 |  | `conduit_power` | 29 |
| `DOLPHINS_GRACE` | V1_13_R1 (1.13) | V26_3 |  | `dolphins_grace` | 30 |
| `SLOW_FALLING` | V1_13_R1 (1.13) | V26_3 |  | `slow_falling` | 28 |
| `BAD_OMEN` | V1_14_R1 (1.14.x) | V26_3 |  | `bad_omen` | 31 |
| `HERO_OF_THE_VILLAGE` | V1_14_R1 (1.14.x) | V26_3 |  | `hero_of_the_village` | 32 |
| `DARKNESS` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  | `darkness` | 33 |
| `INFESTED` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `infested` | 39 |
| `OOZING` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `oozing` | 38 |
| `RAID_OMEN` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `raid_omen` | 35 |
| `TRIAL_OMEN` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `trial_omen` | 34 |
| `WEAVING` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `weaving` | 37 |
| `WIND_CHARGED` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `wind_charged` | 36 |
| `BREATH_OF_THE_NAUTILUS` | V1_21_R7 (1.21.11) | V26_3 |  | `breath_of_the_nautilus` | 40 |

## Potion types

| Versions | System |
|---|---|
| 1.8 - 1.20.1 | Enum; long / strong are flags of the item |
| 1.20.2 - 1.20.4 | Long / strong types become constants (`LONG_SWIFTNESS`, `STRONG_SWIFTNESS`) |
| 1.20.5 - 26.3 | Constants take Minecraft's names (`SWIFTNESS`, `LEAPING` ...); the `Potion` class is gone |

### Renames

From CraftBukkit `legacy/FieldRename.java` `POTION_TYPE_DATA`, each checked against the dumps.

| Old name | New name | Revision |
|---|---|---|
| `JUMP` | `LEAPING` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SPEED` | `SWIFTNESS` | V1_20_R4 (1.20.5 - 1.20.6) |
| `INSTANT_HEAL` | `HEALING` | V1_20_R4 (1.20.5 - 1.20.6) |
| `INSTANT_DAMAGE` | `HARMING` | V1_20_R4 (1.20.5 - 1.20.6) |
| `REGEN` | `REGENERATION` | V1_20_R4 (1.20.5 - 1.20.6) |

### Changes per revision

| Change | Added | Removed |
|---|---|---|
| V1_8_R3 → V1_9_R1 (1.9 - 1.9.3) | `AWKWARD`, `LUCK`, `MUNDANE`, `THICK`, `UNCRAFTABLE` |  |
| V1_12_R1 → V1_13_R1 (1.13) | `SLOW_FALLING`, `TURTLE_MASTER` |  |
| V1_20_R1 → V1_20_R2 (1.20.2) | `LONG_FIRE_RESISTANCE`, `LONG_INVISIBILITY`, `LONG_LEAPING`, `LONG_NIGHT_VISION`, `LONG_POISON`, `LONG_REGENERATION`, `LONG_SLOWNESS`, `LONG_SLOW_FALLING`, `LONG_STRENGTH`, `LONG_SWIFTNESS`, `LONG_TURTLE_MASTER`, `LONG_WATER_BREATHING`, `LONG_WEAKNESS`, `STRONG_HARMING`, `STRONG_HEALING`, `STRONG_LEAPING`, `STRONG_POISON`, `STRONG_REGENERATION`, `STRONG_SLOWNESS`, `STRONG_STRENGTH`, `STRONG_SWIFTNESS`, `STRONG_TURTLE_MASTER` |  |
| V1_20_R3 → V1_20_R4 (1.20.5 - 1.20.6) | `HARMING`, `HEALING`, `INFESTED`, `LEAPING`, `OOZING`, `REGENERATION`, `SWIFTNESS`, `WEAVING`, `WIND_CHARGED` | `INSTANT_DAMAGE`, `INSTANT_HEAL`, `JUMP`, `REGEN`, `SPEED`, `UNCRAFTABLE` |

### All values

By newest name; older names newest first, tried on a server in this order.

| Name | First revision | Last revision | Older names | Key |
|---|---|---|---|---|
| `FIRE_RESISTANCE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `fire_resistance` |
| `HARMING` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `INSTANT_DAMAGE` | `harming` |
| `HEALING` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `INSTANT_HEAL` | `healing` |
| `INVISIBILITY` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `invisibility` |
| `LEAPING` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `JUMP` | `leaping` |
| `NIGHT_VISION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `night_vision` |
| `POISON` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `poison` |
| `REGENERATION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `REGEN` | `regeneration` |
| `SLOWNESS` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `slowness` |
| `STRENGTH` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `strength` |
| `SWIFTNESS` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SPEED` | `swiftness` |
| `WATER` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `water` |
| `WATER_BREATHING` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `water_breathing` |
| `WEAKNESS` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `weakness` |
| `AWKWARD` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `awkward` |
| `LUCK` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `luck` |
| `MUNDANE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `mundane` |
| `THICK` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `thick` |
| `UNCRAFTABLE` | V1_9_R1 (1.9 - 1.9.3) | V1_20_R3 |  | `empty` |
| `SLOW_FALLING` | V1_13_R1 (1.13) | V26_3 |  | `slow_falling` |
| `TURTLE_MASTER` | V1_13_R1 (1.13) | V26_3 |  | `turtle_master` |
| `LONG_FIRE_RESISTANCE` | V1_20_R2 (1.20.2) | V26_3 |  | `long_fire_resistance` |
| `LONG_INVISIBILITY` | V1_20_R2 (1.20.2) | V26_3 |  | `long_invisibility` |
| `LONG_LEAPING` | V1_20_R2 (1.20.2) | V26_3 |  | `long_leaping` |
| `LONG_NIGHT_VISION` | V1_20_R2 (1.20.2) | V26_3 |  | `long_night_vision` |
| `LONG_POISON` | V1_20_R2 (1.20.2) | V26_3 |  | `long_poison` |
| `LONG_REGENERATION` | V1_20_R2 (1.20.2) | V26_3 |  | `long_regeneration` |
| `LONG_SLOWNESS` | V1_20_R2 (1.20.2) | V26_3 |  | `long_slowness` |
| `LONG_SLOW_FALLING` | V1_20_R2 (1.20.2) | V26_3 |  | `long_slow_falling` |
| `LONG_STRENGTH` | V1_20_R2 (1.20.2) | V26_3 |  | `long_strength` |
| `LONG_SWIFTNESS` | V1_20_R2 (1.20.2) | V26_3 |  | `long_swiftness` |
| `LONG_TURTLE_MASTER` | V1_20_R2 (1.20.2) | V26_3 |  | `long_turtle_master` |
| `LONG_WATER_BREATHING` | V1_20_R2 (1.20.2) | V26_3 |  | `long_water_breathing` |
| `LONG_WEAKNESS` | V1_20_R2 (1.20.2) | V26_3 |  | `long_weakness` |
| `STRONG_HARMING` | V1_20_R2 (1.20.2) | V26_3 |  | `strong_harming` |
| `STRONG_HEALING` | V1_20_R2 (1.20.2) | V26_3 |  | `strong_healing` |
| `STRONG_LEAPING` | V1_20_R2 (1.20.2) | V26_3 |  | `strong_leaping` |
| `STRONG_POISON` | V1_20_R2 (1.20.2) | V26_3 |  | `strong_poison` |
| `STRONG_REGENERATION` | V1_20_R2 (1.20.2) | V26_3 |  | `strong_regeneration` |
| `STRONG_SLOWNESS` | V1_20_R2 (1.20.2) | V26_3 |  | `strong_slowness` |
| `STRONG_STRENGTH` | V1_20_R2 (1.20.2) | V26_3 |  | `strong_strength` |
| `STRONG_SWIFTNESS` | V1_20_R2 (1.20.2) | V26_3 |  | `strong_swiftness` |
| `STRONG_TURTLE_MASTER` | V1_20_R2 (1.20.2) | V26_3 |  | `strong_turtle_master` |
| `INFESTED` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `infested` |
| `OOZING` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `oozing` |
| `WEAVING` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `weaving` |
| `WIND_CHARGED` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `wind_charged` |
