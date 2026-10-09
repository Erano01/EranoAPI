# Enchantments (`Enchantment`), 1.8 - 26.3

`EranoEnchantment`: every enchantment of today by its newest name, on every server from 1.8.

```java
EranoEnchantment.SHARPNESS.enchant(sword, 2);    // DAMAGE_ALL up to 1.20.4
EranoEnchantment.match("DAMAGE_ALL");            // SHARPNESS; keys too: "minecraft:sharpness"
EranoEnchantment.SHARPNESS.legacyId();           // 16, the 1.8 - 1.12 numeric id
```

The tables below are what it is generated from (`scripts/registry-report/run.sh`): every NMS revision's Spigot API,
read from its bytecode (no class loaded, so registry-backed values too), and CraftBukkit's own rename tables
(`legacy/FieldRename.java`). See also [Versioned-APIs.md](Versioned-APIs.md).

## Systems

| Versions | System |
|---|---|
| 1.8 - 1.12.2 | Class with numeric ids (`DAMAGE_ALL` = 16); `getByName` / `getById` |
| 1.13 - 1.20.4 | Same names, now with keys (`minecraft:sharpness`, `getByKey`); no numeric ids |
| 1.20.5 - 26.3 | Fields take Minecraft's names (`SHARPNESS`), values come from the registry |

EranoAPI reads the static field (`Enchantment.class.getField(name)`), which every version has.

## Renames

From CraftBukkit `legacy/FieldRename.java` `ENCHANTMENT_DATA`, each checked against the dumps.

| Old name | New name | Revision |
|---|---|---|
| `PROTECTION_ENVIRONMENTAL` | `PROTECTION` | V1_20_R4 (1.20.5 - 1.20.6) |
| `PROTECTION_FIRE` | `FIRE_PROTECTION` | V1_20_R4 (1.20.5 - 1.20.6) |
| `PROTECTION_FALL` | `FEATHER_FALLING` | V1_20_R4 (1.20.5 - 1.20.6) |
| `PROTECTION_EXPLOSIONS` | `BLAST_PROTECTION` | V1_20_R4 (1.20.5 - 1.20.6) |
| `PROTECTION_PROJECTILE` | `PROJECTILE_PROTECTION` | V1_20_R4 (1.20.5 - 1.20.6) |
| `OXYGEN` | `RESPIRATION` | V1_20_R4 (1.20.5 - 1.20.6) |
| `WATER_WORKER` | `AQUA_AFFINITY` | V1_20_R4 (1.20.5 - 1.20.6) |
| `DAMAGE_ALL` | `SHARPNESS` | V1_20_R4 (1.20.5 - 1.20.6) |
| `DAMAGE_UNDEAD` | `SMITE` | V1_20_R4 (1.20.5 - 1.20.6) |
| `DAMAGE_ARTHROPODS` | `BANE_OF_ARTHROPODS` | V1_20_R4 (1.20.5 - 1.20.6) |
| `LOOT_BONUS_MOBS` | `LOOTING` | V1_20_R4 (1.20.5 - 1.20.6) |
| `DIG_SPEED` | `EFFICIENCY` | V1_20_R4 (1.20.5 - 1.20.6) |
| `DURABILITY` | `UNBREAKING` | V1_20_R4 (1.20.5 - 1.20.6) |
| `LOOT_BONUS_BLOCKS` | `FORTUNE` | V1_20_R4 (1.20.5 - 1.20.6) |
| `ARROW_DAMAGE` | `POWER` | V1_20_R4 (1.20.5 - 1.20.6) |
| `ARROW_KNOCKBACK` | `PUNCH` | V1_20_R4 (1.20.5 - 1.20.6) |
| `ARROW_FIRE` | `FLAME` | V1_20_R4 (1.20.5 - 1.20.6) |
| `ARROW_INFINITE` | `INFINITY` | V1_20_R4 (1.20.5 - 1.20.6) |
| `LUCK` | `LUCK_OF_THE_SEA` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SWEEPING` | `SWEEPING_EDGE` | the field was always `SWEEPING_EDGE`; only the key changed (`sweeping_edge`) |

## Changes per revision

| Change | Added | Removed |
|---|---|---|
| V1_8_R3 → V1_9_R1 (1.9 - 1.9.3) | `FROST_WALKER`, `MENDING` |  |
| V1_10_R1 → V1_11_R1 (1.11.x) | `BINDING_CURSE`, `SWEEPING_EDGE`, `VANISHING_CURSE` |  |
| V1_12_R1 → V1_13_R1 (1.13) | `CHANNELING`, `IMPALING`, `LOYALTY`, `RIPTIDE` |  |
| V1_13_R2 → V1_14_R1 (1.14.x) | `MULTISHOT`, `PIERCING`, `QUICK_CHARGE` |  |
| V1_15_R1 → V1_16_R1 (1.16 - 1.16.1) | `SOUL_SPEED` |  |
| V1_18_R2 → V1_19_R1 (1.19 - 1.19.2) | `SWIFT_SNEAK` |  |
| V1_20_R3 → V1_20_R4 (1.20.5 - 1.20.6) | `AQUA_AFFINITY`, `BANE_OF_ARTHROPODS`, `BLAST_PROTECTION`, `BREACH`, `DENSITY`, `EFFICIENCY`, `FEATHER_FALLING`, `FIRE_PROTECTION`, `FLAME`, `FORTUNE`, `INFINITY`, `LOOTING`, `LUCK_OF_THE_SEA`, `POWER`, `PROJECTILE_PROTECTION`, `PROTECTION`, `PUNCH`, `RESPIRATION`, `SHARPNESS`, `SMITE`, `UNBREAKING`, `WIND_BURST` | `ARROW_DAMAGE`, `ARROW_FIRE`, `ARROW_INFINITE`, `ARROW_KNOCKBACK`, `DAMAGE_ALL`, `DAMAGE_ARTHROPODS`, `DAMAGE_UNDEAD`, `DIG_SPEED`, `DURABILITY`, `LOOT_BONUS_BLOCKS`, `LOOT_BONUS_MOBS`, `LUCK`, `OXYGEN`, `PROTECTION_ENVIRONMENTAL`, `PROTECTION_EXPLOSIONS`, `PROTECTION_FALL`, `PROTECTION_FIRE`, `PROTECTION_PROJECTILE`, `WATER_WORKER` |
| V1_21_R6 → V1_21_R7 (1.21.11) | `LUNGE` |  |

## All values

By newest name; older names newest first, tried on a server in this order.

| Name | First revision | Last revision | Older names | Key | Numeric id |
|---|---|---|---|---|---|
| `AQUA_AFFINITY` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `WATER_WORKER` | `aqua_affinity` | 6 |
| `BANE_OF_ARTHROPODS` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `DAMAGE_ARTHROPODS` | `bane_of_arthropods` | 18 |
| `BLAST_PROTECTION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `PROTECTION_EXPLOSIONS` | `blast_protection` | 3 |
| `DEPTH_STRIDER` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `depth_strider` | 8 |
| `EFFICIENCY` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `DIG_SPEED` | `efficiency` | 32 |
| `FEATHER_FALLING` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `PROTECTION_FALL` | `feather_falling` | 2 |
| `FIRE_ASPECT` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `fire_aspect` | 20 |
| `FIRE_PROTECTION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `PROTECTION_FIRE` | `fire_protection` | 1 |
| `FLAME` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `ARROW_FIRE` | `flame` | 50 |
| `FORTUNE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `LOOT_BONUS_BLOCKS` | `fortune` | 35 |
| `INFINITY` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `ARROW_INFINITE` | `infinity` | 51 |
| `KNOCKBACK` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `knockback` | 19 |
| `LOOTING` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `LOOT_BONUS_MOBS` | `looting` | 21 |
| `LUCK_OF_THE_SEA` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `LUCK` | `luck_of_the_sea` | 61 |
| `LURE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `lure` | 62 |
| `POWER` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `ARROW_DAMAGE` | `power` | 48 |
| `PROJECTILE_PROTECTION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `PROTECTION_PROJECTILE` | `projectile_protection` | 4 |
| `PROTECTION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `PROTECTION_ENVIRONMENTAL` | `protection` | 0 |
| `PUNCH` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `ARROW_KNOCKBACK` | `punch` | 49 |
| `RESPIRATION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `OXYGEN` | `respiration` | 5 |
| `SHARPNESS` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `DAMAGE_ALL` | `sharpness` | 16 |
| `SILK_TOUCH` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `silk_touch` | 33 |
| `SMITE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `DAMAGE_UNDEAD` | `smite` | 17 |
| `THORNS` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `thorns` | 7 |
| `UNBREAKING` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `DURABILITY` | `unbreaking` | 34 |
| `FROST_WALKER` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `frost_walker` | 9 |
| `MENDING` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `mending` | 70 |
| `BINDING_CURSE` | V1_11_R1 (1.11.x) | V26_3 |  | `binding_curse` | 10 |
| `SWEEPING_EDGE` | V1_11_R1 (1.11.x) | V26_3 |  | `sweeping_edge` | 22 |
| `VANISHING_CURSE` | V1_11_R1 (1.11.x) | V26_3 |  | `vanishing_curse` | 71 |
| `CHANNELING` | V1_13_R1 (1.13) | V26_3 |  | `channeling` |  |
| `IMPALING` | V1_13_R1 (1.13) | V26_3 |  | `impaling` |  |
| `LOYALTY` | V1_13_R1 (1.13) | V26_3 |  | `loyalty` |  |
| `RIPTIDE` | V1_13_R1 (1.13) | V26_3 |  | `riptide` |  |
| `MULTISHOT` | V1_14_R1 (1.14.x) | V26_3 |  | `multishot` |  |
| `PIERCING` | V1_14_R1 (1.14.x) | V26_3 |  | `piercing` |  |
| `QUICK_CHARGE` | V1_14_R1 (1.14.x) | V26_3 |  | `quick_charge` |  |
| `SOUL_SPEED` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `soul_speed` |  |
| `SWIFT_SNEAK` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  | `swift_sneak` |  |
| `BREACH` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `breach` |  |
| `DENSITY` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `density` |  |
| `WIND_BURST` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `wind_burst` |  |
| `LUNGE` | V1_21_R7 (1.21.11) | V26_3 |  | `lunge` |  |
