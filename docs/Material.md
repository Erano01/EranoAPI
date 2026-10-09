# Materials (`org.bukkit.Material`), 1.8 - 26.3

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

- **Names:** `Material.values()` of every NMS revision's Spigot API, 36 versions (1.8, 1.8.3, 1.8.8, 1.9.2, 1.9.4, 1.10.2, 1.11.2, 1.12.2, 1.13, 1.13.2, 1.14.4, 1.15.2, 1.16.1, 1.16.3, 1.16.5, 1.17.1, 1.18.1, 1.18.2, 1.19.2, 1.19.3, 1.19.4, 1.20.1, 1.20.2, 1.20.4, 1.20.6, 1.21.1, 1.21.3, 1.21.4, 1.21.5, 1.21.6, 1.21.8, 1.21.10, 1.21.11, 26.1.2, 26.2, 26.3).
- **Renames:** CraftBukkit's `util/Commodore.java`, each checked against the dumps.
- **1.13 flattening:** the 1.13.2 server's own `CraftLegacy.fromLegacy`, called for every old name with data 0 - 15
  (no server started, only its classes).

## Material systems

| Versions | System |
|---|---|
| 1.8 - 1.12.2 | **Numeric id + data value.** A `Material` is a family (`WOOL`); the kind is the data value (`WOOL:14` = red). 463 names. |
| 1.13 - 26.3 | **Flattening.** Every kind has its own name (`RED_WOOL`), block state in `BlockData`. The 463 old names stay with a `LEGACY_` prefix. 1815 names today. |
| 1.20.6 - 26.3 | `ItemType` / `BlockType` registries added next to `Material` (still an enum, not deprecated). |

The only real break is 1.13; after it only single names changed (14, below). Up to 1.12.2 no name changed;
`LOCKED_CHEST` was removed in 1.8.3.

## Changes per revision

| Change | Added | Removed | Removed names |
|---|---|---|---|
| V1_8_R1 → V1_8_R2 (1.8.3) | 0 | 1 | `LOCKED_CHEST` |
| V1_8_R2 → V1_8_R3 (1.8.4 - 1.8.9) | 0 | 0 |  |
| V1_8_R3 → V1_9_R1 (1.9 - 1.9.3) | 34 | 0 |  |
| V1_9_R1 → V1_9_R2 (1.9.4) | 0 | 0 |  |
| V1_9_R2 → V1_10_R1 (1.10.x) | 5 | 0 |  |
| V1_10_R1 → V1_11_R1 (1.11.x) | 20 | 0 |  |
| V1_11_R1 → V1_12_R1 (1.12.x) | 19 | 0 |  |
| V1_13_R1 → V1_13_R2 (1.13.1 - 1.13.2) | 5 | 0 |  |
| V1_13_R2 → V1_14_R1 (1.14.x) | 103 | 5 | `CACTUS_GREEN` → `GREEN_DYE`, `DANDELION_YELLOW` → `YELLOW_DYE`, `ROSE_RED` → `RED_DYE`, `SIGN` → `OAK_SIGN`, `WALL_SIGN` → `OAK_WALL_SIGN` |
| V1_14_R1 → V1_15_R1 (1.15.x) | 7 | 0 |  |
| V1_15_R1 → V1_16_R1 (1.16 - 1.16.1) | 102 | 1 | `ZOMBIE_PIGMAN_SPAWN_EGG` → `ZOMBIFIED_PIGLIN_SPAWN_EGG` |
| V1_16_R1 → V1_16_R2 (1.16.2 - 1.16.3) | 1 | 0 |  |
| V1_16_R2 → V1_16_R3 (1.16.4 - 1.16.5) | 0 | 0 |  |
| V1_16_R3 → V1_17_R1 (1.17.x) | 151 | 1 | `GRASS_PATH` → `DIRT_PATH` |
| V1_17_R1 → V1_18_R1 (1.18 - 1.18.1) | 1 | 0 |  |
| V1_18_R1 → V1_18_R2 (1.18.2) | 0 | 0 |  |
| V1_18_R2 → V1_19_R1 (1.19 - 1.19.2) | 53 | 0 |  |
| V1_19_R1 → V1_19_R2 (1.19.3) | 46 | 0 |  |
| V1_19_R2 → V1_19_R3 (1.19.4) | 47 | 0 |  |
| V1_19_R3 → V1_20_R1 (1.20 - 1.20.1) | 32 | 4 | `POTTERY_SHARD_ARCHER` → `ARCHER_POTTERY_SHERD`, `POTTERY_SHARD_ARMS_UP` → `ARMS_UP_POTTERY_SHERD`, `POTTERY_SHARD_PRIZE` → `PRIZE_POTTERY_SHERD`, `POTTERY_SHARD_SKULL` → `SKULL_POTTERY_SHERD` |
| V1_20_R1 → V1_20_R2 (1.20.2) | 0 | 0 |  |
| V1_20_R2 → V1_20_R3 (1.20.3 - 1.20.4) | 58 | 1 | `GRASS` → `SHORT_GRASS` |
| V1_20_R3 → V1_20_R4 (1.20.5 - 1.20.6) | 19 | 1 | `SCUTE` → `TURTLE_SCUTE` |
| V1_20_R4 → V1_21_R1 (1.21 - 1.21.1) | 3 | 0 |  |
| V1_21_R1 → V1_21_R2 (1.21.2 - 1.21.3) | 45 | 0 |  |
| V1_21_R2 → V1_21_R3 (1.21.4) | 12 | 0 |  |
| V1_21_R3 → V1_21_R4 (1.21.5) | 11 | 0 |  |
| V1_21_R4 → V1_21_R5 (1.21.6 - 1.21.8) | 19 | 0 |  |
| V1_21_R5 1.21.6 → 1.21.8 (same revision) | 1 | 0 | +`MUSIC_DISC_LAVA_CHICKEN` |
| V1_21_R5 → V1_21_R6 (1.21.9 - 1.21.10) | 74 | 1 | `CHAIN` → `IRON_CHAIN` |
| V1_21_R6 → V1_21_R7 (1.21.11) | 17 | 0 |  |
| V1_21_R7 → V26_1 (26.1.x) | 2 | 0 |  |
| V26_1 → V26_2 (26.2.x) | 31 | 0 |  |
| V26_2 → V26_3 (26.3+) | 124 | 0 |  |
| V1_12_R1 → V1_13_R1 (1.13) | flattening | flattening | 1.12.2's 463 names became `LEGACY_`, 865 new names (below) |

Every added name is in Appendix B ("First revision").

## Renames after 1.13

The old name went away as the new one came. The server rewrites the old name only for plugins with an older
`api-version` (Commodore).

| Old name | New name | Minecraft | Revision | Server rewrites it |
|---|---|---|---|---|
| `CACTUS_GREEN` | `GREEN_DYE` | 1.14 | V1_13_R2 → V1_14_R1 (1.14.x) | Commodore |
| `DANDELION_YELLOW` | `YELLOW_DYE` | 1.14 | V1_13_R2 → V1_14_R1 (1.14.x) | Commodore |
| `ROSE_RED` | `RED_DYE` | 1.14 | V1_13_R2 → V1_14_R1 (1.14.x) | Commodore |
| `SIGN` | `OAK_SIGN` | 1.14 | V1_13_R2 → V1_14_R1 (1.14.x) | Commodore |
| `WALL_SIGN` | `OAK_WALL_SIGN` | 1.14 | V1_13_R2 → V1_14_R1 (1.14.x) | Commodore |
| `ZOMBIE_PIGMAN_SPAWN_EGG` | `ZOMBIFIED_PIGLIN_SPAWN_EGG` | 1.16 | V1_15_R1 → V1_16_R1 (1.16 - 1.16.1) | Commodore |
| `GRASS_PATH` | `DIRT_PATH` | 1.17 | V1_16_R3 → V1_17_R1 (1.17.x) | Commodore |
| `POTTERY_SHARD_ARCHER` | `ARCHER_POTTERY_SHERD` | 1.20 | V1_19_R3 → V1_20_R1 (1.20 - 1.20.1) | none (experimental in 1.19.4) |
| `POTTERY_SHARD_ARMS_UP` | `ARMS_UP_POTTERY_SHERD` | 1.20 | V1_19_R3 → V1_20_R1 (1.20 - 1.20.1) | none (experimental in 1.19.4) |
| `POTTERY_SHARD_PRIZE` | `PRIZE_POTTERY_SHERD` | 1.20 | V1_19_R3 → V1_20_R1 (1.20 - 1.20.1) | none (experimental in 1.19.4) |
| `POTTERY_SHARD_SKULL` | `SKULL_POTTERY_SHERD` | 1.20 | V1_19_R3 → V1_20_R1 (1.20 - 1.20.1) | none (experimental in 1.19.4) |
| `GRASS` | `SHORT_GRASS` | 1.20.3 | V1_20_R2 → V1_20_R3 (1.20.3 - 1.20.4) | Commodore |
| `SCUTE` | `TURTLE_SCUTE` | 1.20.5 | V1_20_R3 → V1_20_R4 (1.20.5 - 1.20.6) | Commodore |
| `CHAIN` | `IRON_CHAIN` | 1.21.9 | V1_21_R5 → V1_21_R6 (1.21.9 - 1.21.10) | Commodore |

## 1.13 flattening: old name + data value → new name

What each 1.12.2 `NAME:data` became in 1.13 (the 1.13.2 server's own converter). Only data values that pick a kind
(color, wood, stone type) are listed. "Today" applies the later renames. "As a block" is set only when the block
differs from the item (item `CHIPPED_ANVIL`, block `ANVIL`, whose data value is its facing).

| 1.12.2 name | Id | Data | 1.13 name | Today (26.3) | As a block |
|---|---|---|---|---|---|
| `AIR` | 0 | 0 | `AIR` | = |  |
| `STONE` | 1 | 0 | `STONE` | = |  |
| `STONE` | 1 | 1 | `GRANITE` | = |  |
| `STONE` | 1 | 2 | `POLISHED_GRANITE` | = |  |
| `STONE` | 1 | 3 | `DIORITE` | = |  |
| `STONE` | 1 | 4 | `POLISHED_DIORITE` | = |  |
| `STONE` | 1 | 5 | `ANDESITE` | = |  |
| `STONE` | 1 | 6 | `POLISHED_ANDESITE` | = |  |
| `GRASS` | 2 | 0 | `GRASS_BLOCK` | = |  |
| `DIRT` | 3 | 0 | `DIRT` | = |  |
| `DIRT` | 3 | 1 | `COARSE_DIRT` | = |  |
| `DIRT` | 3 | 2 | `PODZOL` | = |  |
| `COBBLESTONE` | 4 | 0 | `COBBLESTONE` | = |  |
| `WOOD` | 5 | 0 | `OAK_PLANKS` | = |  |
| `WOOD` | 5 | 1 | `SPRUCE_PLANKS` | = |  |
| `WOOD` | 5 | 2 | `BIRCH_PLANKS` | = |  |
| `WOOD` | 5 | 3 | `JUNGLE_PLANKS` | = |  |
| `WOOD` | 5 | 4 | `ACACIA_PLANKS` | = |  |
| `WOOD` | 5 | 5 | `DARK_OAK_PLANKS` | = |  |
| `SAPLING` | 6 | 0 | `OAK_SAPLING` | = |  |
| `SAPLING` | 6 | 1 | `SPRUCE_SAPLING` | = |  |
| `SAPLING` | 6 | 2 | `BIRCH_SAPLING` | = |  |
| `SAPLING` | 6 | 3 | `JUNGLE_SAPLING` | = |  |
| `SAPLING` | 6 | 4 | `ACACIA_SAPLING` | = |  |
| `SAPLING` | 6 | 5 | `DARK_OAK_SAPLING` | = |  |
| `BEDROCK` | 7 | 0 | `BEDROCK` | = |  |
| `WATER` | 8 | 0 | `WATER` | = |  |
| `STATIONARY_WATER` | 9 | 0 | `WATER` | = |  |
| `LAVA` | 10 | 0 | `LAVA` | = |  |
| `STATIONARY_LAVA` | 11 | 0 | `LAVA` | = |  |
| `SAND` | 12 | 0 | `SAND` | = |  |
| `SAND` | 12 | 1 | `RED_SAND` | = |  |
| `GRAVEL` | 13 | 0 | `GRAVEL` | = |  |
| `GOLD_ORE` | 14 | 0 | `GOLD_ORE` | = |  |
| `IRON_ORE` | 15 | 0 | `IRON_ORE` | = |  |
| `COAL_ORE` | 16 | 0 | `COAL_ORE` | = |  |
| `LOG` | 17 | 0 | `OAK_LOG` | = |  |
| `LOG` | 17 | 1 | `SPRUCE_LOG` | = |  |
| `LOG` | 17 | 2 | `BIRCH_LOG` | = |  |
| `LOG` | 17 | 3 | `JUNGLE_LOG` | = |  |
| `LEAVES` | 18 | 0 | `OAK_LEAVES` | = |  |
| `LEAVES` | 18 | 1 | `SPRUCE_LEAVES` | = |  |
| `LEAVES` | 18 | 2 | `BIRCH_LEAVES` | = |  |
| `LEAVES` | 18 | 3 | `JUNGLE_LEAVES` | = |  |
| `SPONGE` | 19 | 0 | `SPONGE` | = |  |
| `SPONGE` | 19 | 1 | `WET_SPONGE` | = |  |
| `GLASS` | 20 | 0 | `GLASS` | = |  |
| `LAPIS_ORE` | 21 | 0 | `LAPIS_ORE` | = |  |
| `LAPIS_BLOCK` | 22 | 0 | `LAPIS_BLOCK` | = |  |
| `DISPENSER` | 23 | 0 | `DISPENSER` | = |  |
| `SANDSTONE` | 24 | 0 | `SANDSTONE` | = |  |
| `SANDSTONE` | 24 | 1 | `CHISELED_SANDSTONE` | = |  |
| `SANDSTONE` | 24 | 2 | `CUT_SANDSTONE` | = |  |
| `NOTE_BLOCK` | 25 | 0 | `NOTE_BLOCK` | = |  |
| `BED_BLOCK` | 26 | 0 | `RED_BED` | = |  |
| `POWERED_RAIL` | 27 | 0 | `POWERED_RAIL` | = |  |
| `DETECTOR_RAIL` | 28 | 0 | `DETECTOR_RAIL` | = |  |
| `PISTON_STICKY_BASE` | 29 | 0 | `STICKY_PISTON` | = |  |
| `WEB` | 30 | 0 | `COBWEB` | = |  |
| `LONG_GRASS` | 31 | 0 | `DEAD_BUSH` | = |  |
| `LONG_GRASS` | 31 | 1 | `GRASS` | `SHORT_GRASS` |  |
| `LONG_GRASS` | 31 | 2 | `FERN` | = |  |
| `DEAD_BUSH` | 32 | 0 | `DEAD_BUSH` | = |  |
| `PISTON_BASE` | 33 | 0 | `PISTON` | = |  |
| `PISTON_EXTENSION` | 34 | 0 | `PISTON_HEAD` | = |  |
| `WOOL` | 35 | 0 | `WHITE_WOOL` | = |  |
| `WOOL` | 35 | 1 | `ORANGE_WOOL` | = |  |
| `WOOL` | 35 | 2 | `MAGENTA_WOOL` | = |  |
| `WOOL` | 35 | 3 | `LIGHT_BLUE_WOOL` | = |  |
| `WOOL` | 35 | 4 | `YELLOW_WOOL` | = |  |
| `WOOL` | 35 | 5 | `LIME_WOOL` | = |  |
| `WOOL` | 35 | 6 | `PINK_WOOL` | = |  |
| `WOOL` | 35 | 7 | `GRAY_WOOL` | = |  |
| `WOOL` | 35 | 8 | `LIGHT_GRAY_WOOL` | = |  |
| `WOOL` | 35 | 9 | `CYAN_WOOL` | = |  |
| `WOOL` | 35 | 10 | `PURPLE_WOOL` | = |  |
| `WOOL` | 35 | 11 | `BLUE_WOOL` | = |  |
| `WOOL` | 35 | 12 | `BROWN_WOOL` | = |  |
| `WOOL` | 35 | 13 | `GREEN_WOOL` | = |  |
| `WOOL` | 35 | 14 | `RED_WOOL` | = |  |
| `WOOL` | 35 | 15 | `BLACK_WOOL` | = |  |
| `PISTON_MOVING_PIECE` | 36 | 0 | `MOVING_PISTON` | = |  |
| `YELLOW_FLOWER` | 37 | 0 | `DANDELION` | = |  |
| `RED_ROSE` | 38 | 0 | `POPPY` | = |  |
| `RED_ROSE` | 38 | 1 | `BLUE_ORCHID` | = |  |
| `RED_ROSE` | 38 | 2 | `ALLIUM` | = |  |
| `RED_ROSE` | 38 | 3 | `AZURE_BLUET` | = |  |
| `RED_ROSE` | 38 | 4 | `RED_TULIP` | = |  |
| `RED_ROSE` | 38 | 5 | `ORANGE_TULIP` | = |  |
| `RED_ROSE` | 38 | 6 | `WHITE_TULIP` | = |  |
| `RED_ROSE` | 38 | 7 | `PINK_TULIP` | = |  |
| `RED_ROSE` | 38 | 8 | `OXEYE_DAISY` | = |  |
| `BROWN_MUSHROOM` | 39 | 0 | `BROWN_MUSHROOM` | = |  |
| `RED_MUSHROOM` | 40 | 0 | `RED_MUSHROOM` | = |  |
| `GOLD_BLOCK` | 41 | 0 | `GOLD_BLOCK` | = |  |
| `IRON_BLOCK` | 42 | 0 | `IRON_BLOCK` | = |  |
| `DOUBLE_STEP` | 43 | 0 | `STONE_SLAB` | = |  |
| `DOUBLE_STEP` | 43 | 1 | `SANDSTONE_SLAB` | = |  |
| `DOUBLE_STEP` | 43 | 2 | `PETRIFIED_OAK_SLAB` | = |  |
| `DOUBLE_STEP` | 43 | 3 | `COBBLESTONE_SLAB` | = |  |
| `DOUBLE_STEP` | 43 | 4 | `BRICK_SLAB` | = |  |
| `DOUBLE_STEP` | 43 | 5 | `STONE_BRICK_SLAB` | = |  |
| `DOUBLE_STEP` | 43 | 6 | `NETHER_BRICK_SLAB` | = |  |
| `DOUBLE_STEP` | 43 | 7 | `QUARTZ_SLAB` | = |  |
| `DOUBLE_STEP` | 43 | 8 | `SMOOTH_STONE` | = |  |
| `DOUBLE_STEP` | 43 | 9 | `SMOOTH_SANDSTONE` | = |  |
| `DOUBLE_STEP` | 43 | 15 | `SMOOTH_QUARTZ` | = |  |
| `STEP` | 44 | 0 | `STONE_SLAB` | = |  |
| `STEP` | 44 | 1 | `SANDSTONE_SLAB` | = |  |
| `STEP` | 44 | 2 | `PETRIFIED_OAK_SLAB` | = |  |
| `STEP` | 44 | 3 | `COBBLESTONE_SLAB` | = |  |
| `STEP` | 44 | 4 | `BRICK_SLAB` | = |  |
| `STEP` | 44 | 5 | `STONE_BRICK_SLAB` | = |  |
| `STEP` | 44 | 6 | `NETHER_BRICK_SLAB` | = |  |
| `STEP` | 44 | 7 | `QUARTZ_SLAB` | = |  |
| `BRICK` | 45 | 0 | `BRICKS` | = |  |
| `TNT` | 46 | 0 | `TNT` | = |  |
| `BOOKSHELF` | 47 | 0 | `BOOKSHELF` | = |  |
| `MOSSY_COBBLESTONE` | 48 | 0 | `MOSSY_COBBLESTONE` | = |  |
| `OBSIDIAN` | 49 | 0 | `OBSIDIAN` | = |  |
| `TORCH` | 50 | 0 | `TORCH` | = |  |
| `FIRE` | 51 | 0 | `FIRE` | = |  |
| `MOB_SPAWNER` | 52 | 0 | `SPAWNER` | = |  |
| `WOOD_STAIRS` | 53 | 0 | `OAK_STAIRS` | = |  |
| `CHEST` | 54 | 0 | `CHEST` | = |  |
| `REDSTONE_WIRE` | 55 | 0 | `REDSTONE_WIRE` | = |  |
| `DIAMOND_ORE` | 56 | 0 | `DIAMOND_ORE` | = |  |
| `DIAMOND_BLOCK` | 57 | 0 | `DIAMOND_BLOCK` | = |  |
| `WORKBENCH` | 58 | 0 | `CRAFTING_TABLE` | = |  |
| `CROPS` | 59 | 0 | `WHEAT` | = |  |
| `SOIL` | 60 | 0 | `FARMLAND` | = |  |
| `FURNACE` | 61 | 0 | `FURNACE` | = |  |
| `BURNING_FURNACE` | 62 | 2 | `FURNACE` | = |  |
| `SIGN_POST` | 63 | 0 | `SIGN` | `OAK_SIGN` |  |
| `WOODEN_DOOR` | 64 | 0 | `OAK_DOOR` | = |  |
| `LADDER` | 65 | 0 | `LADDER` | = |  |
| `RAILS` | 66 | 0 | `RAIL` | = |  |
| `COBBLESTONE_STAIRS` | 67 | 0 | `COBBLESTONE_STAIRS` | = |  |
| `WALL_SIGN` | 68 | 2 | `WALL_SIGN` | `OAK_WALL_SIGN` |  |
| `LEVER` | 69 | 0 | `LEVER` | = |  |
| `STONE_PLATE` | 70 | 0 | `STONE_PRESSURE_PLATE` | = |  |
| `IRON_DOOR_BLOCK` | 71 | 0 | `IRON_DOOR` | = |  |
| `WOOD_PLATE` | 72 | 0 | `OAK_PRESSURE_PLATE` | = |  |
| `REDSTONE_ORE` | 73 | 0 | `REDSTONE_ORE` | = |  |
| `GLOWING_REDSTONE_ORE` | 74 | 0 | `REDSTONE_ORE` | = |  |
| `REDSTONE_TORCH_OFF` | 75 | 1 | `REDSTONE_WALL_TORCH` | = |  |
| `REDSTONE_TORCH_OFF` | 75 | 5 | `REDSTONE_TORCH` | = |  |
| `REDSTONE_TORCH_ON` | 76 | 0 | `REDSTONE_TORCH` | = |  |
| `STONE_BUTTON` | 77 | 0 | `STONE_BUTTON` | = |  |
| `SNOW` | 78 | 0 | `SNOW` | = |  |
| `ICE` | 79 | 0 | `ICE` | = |  |
| `SNOW_BLOCK` | 80 | 0 | `SNOW_BLOCK` | = |  |
| `CACTUS` | 81 | 0 | `CACTUS` | = |  |
| `CLAY` | 82 | 0 | `CLAY` | = |  |
| `SUGAR_CANE_BLOCK` | 83 | 0 | `SUGAR_CANE` | = |  |
| `JUKEBOX` | 84 | 0 | `JUKEBOX` | = |  |
| `FENCE` | 85 | 0 | `OAK_FENCE` | = |  |
| `PUMPKIN` | 86 | 0 | `CARVED_PUMPKIN` | = |  |
| `NETHERRACK` | 87 | 0 | `NETHERRACK` | = |  |
| `SOUL_SAND` | 88 | 0 | `SOUL_SAND` | = |  |
| `GLOWSTONE` | 89 | 0 | `GLOWSTONE` | = |  |
| `PORTAL` | 90 | 0 | `NETHER_PORTAL` | = |  |
| `JACK_O_LANTERN` | 91 | 0 | `JACK_O_LANTERN` | = |  |
| `CAKE_BLOCK` | 92 | 0 | `CAKE` | = |  |
| `DIODE_BLOCK_OFF` | 93 | 0 | `REPEATER` | = |  |
| `DIODE_BLOCK_ON` | 94 | 0 | `REPEATER` | = |  |
| `STAINED_GLASS` | 95 | 0 | `WHITE_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 1 | `ORANGE_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 2 | `MAGENTA_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 3 | `LIGHT_BLUE_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 4 | `YELLOW_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 5 | `LIME_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 6 | `PINK_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 7 | `GRAY_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 8 | `LIGHT_GRAY_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 9 | `CYAN_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 10 | `PURPLE_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 11 | `BLUE_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 12 | `BROWN_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 13 | `GREEN_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 14 | `RED_STAINED_GLASS` | = |  |
| `STAINED_GLASS` | 95 | 15 | `BLACK_STAINED_GLASS` | = |  |
| `TRAP_DOOR` | 96 | 0 | `OAK_TRAPDOOR` | = |  |
| `MONSTER_EGGS` | 97 | 0 | `INFESTED_STONE` | = |  |
| `MONSTER_EGGS` | 97 | 1 | `INFESTED_COBBLESTONE` | = |  |
| `MONSTER_EGGS` | 97 | 2 | `INFESTED_STONE_BRICKS` | = |  |
| `MONSTER_EGGS` | 97 | 3 | `INFESTED_MOSSY_STONE_BRICKS` | = |  |
| `MONSTER_EGGS` | 97 | 4 | `INFESTED_CRACKED_STONE_BRICKS` | = |  |
| `MONSTER_EGGS` | 97 | 5 | `INFESTED_CHISELED_STONE_BRICKS` | = |  |
| `SMOOTH_BRICK` | 98 | 0 | `STONE_BRICKS` | = |  |
| `SMOOTH_BRICK` | 98 | 1 | `MOSSY_STONE_BRICKS` | = |  |
| `SMOOTH_BRICK` | 98 | 2 | `CRACKED_STONE_BRICKS` | = |  |
| `SMOOTH_BRICK` | 98 | 3 | `CHISELED_STONE_BRICKS` | = |  |
| `HUGE_MUSHROOM_1` | 99 | 0 | `BROWN_MUSHROOM_BLOCK` | = |  |
| `HUGE_MUSHROOM_2` | 100 | 0 | `RED_MUSHROOM_BLOCK` | = |  |
| `IRON_FENCE` | 101 | 0 | `IRON_BARS` | = |  |
| `THIN_GLASS` | 102 | 0 | `GLASS_PANE` | = |  |
| `MELON_BLOCK` | 103 | 0 | `MELON` | = |  |
| `PUMPKIN_STEM` | 104 | 0 | `PUMPKIN_STEM` | = |  |
| `MELON_STEM` | 105 | 0 | `MELON_STEM` | = |  |
| `VINE` | 106 | 0 | `VINE` | = |  |
| `FENCE_GATE` | 107 | 0 | `OAK_FENCE_GATE` | = |  |
| `BRICK_STAIRS` | 108 | 0 | `BRICK_STAIRS` | = |  |
| `SMOOTH_STAIRS` | 109 | 0 | `STONE_BRICK_STAIRS` | = |  |
| `MYCEL` | 110 | 0 | `MYCELIUM` | = |  |
| `WATER_LILY` | 111 | 0 | `LILY_PAD` | = |  |
| `NETHER_BRICK` | 112 | 0 | `NETHER_BRICKS` | = |  |
| `NETHER_FENCE` | 113 | 0 | `NETHER_BRICK_FENCE` | = |  |
| `NETHER_BRICK_STAIRS` | 114 | 0 | `NETHER_BRICK_STAIRS` | = |  |
| `NETHER_WARTS` | 115 | 0 | `NETHER_WART` | = |  |
| `ENCHANTMENT_TABLE` | 116 | 0 | `ENCHANTING_TABLE` | = |  |
| `BREWING_STAND` | 117 | 0 | `BREWING_STAND` | = |  |
| `CAULDRON` | 118 | 0 | `CAULDRON` | = |  |
| `ENDER_PORTAL` | 119 | 0 | `END_PORTAL` | = |  |
| `ENDER_PORTAL_FRAME` | 120 | 0 | `END_PORTAL_FRAME` | = |  |
| `ENDER_STONE` | 121 | 0 | `END_STONE` | = |  |
| `DRAGON_EGG` | 122 | 0 | `DRAGON_EGG` | = |  |
| `REDSTONE_LAMP_OFF` | 123 | 0 | `REDSTONE_LAMP` | = |  |
| `REDSTONE_LAMP_ON` | 124 | 0 | `REDSTONE_LAMP` | = |  |
| `WOOD_DOUBLE_STEP` | 125 | 0 | `OAK_SLAB` | = |  |
| `WOOD_DOUBLE_STEP` | 125 | 1 | `SPRUCE_SLAB` | = |  |
| `WOOD_DOUBLE_STEP` | 125 | 2 | `BIRCH_SLAB` | = |  |
| `WOOD_DOUBLE_STEP` | 125 | 3 | `JUNGLE_SLAB` | = |  |
| `WOOD_DOUBLE_STEP` | 125 | 4 | `ACACIA_SLAB` | = |  |
| `WOOD_DOUBLE_STEP` | 125 | 5 | `DARK_OAK_SLAB` | = |  |
| `WOOD_STEP` | 126 | 0 | `OAK_SLAB` | = |  |
| `WOOD_STEP` | 126 | 1 | `SPRUCE_SLAB` | = |  |
| `WOOD_STEP` | 126 | 2 | `BIRCH_SLAB` | = |  |
| `WOOD_STEP` | 126 | 3 | `JUNGLE_SLAB` | = |  |
| `WOOD_STEP` | 126 | 4 | `ACACIA_SLAB` | = |  |
| `WOOD_STEP` | 126 | 5 | `DARK_OAK_SLAB` | = |  |
| `COCOA` | 127 | 0 | `COCOA` | = |  |
| `SANDSTONE_STAIRS` | 128 | 0 | `SANDSTONE_STAIRS` | = |  |
| `EMERALD_ORE` | 129 | 0 | `EMERALD_ORE` | = |  |
| `ENDER_CHEST` | 130 | 0 | `ENDER_CHEST` | = |  |
| `TRIPWIRE_HOOK` | 131 | 0 | `TRIPWIRE_HOOK` | = |  |
| `TRIPWIRE` | 132 | 0 | `TRIPWIRE` | = |  |
| `EMERALD_BLOCK` | 133 | 0 | `EMERALD_BLOCK` | = |  |
| `SPRUCE_WOOD_STAIRS` | 134 | 0 | `SPRUCE_STAIRS` | = |  |
| `BIRCH_WOOD_STAIRS` | 135 | 0 | `BIRCH_STAIRS` | = |  |
| `JUNGLE_WOOD_STAIRS` | 136 | 0 | `JUNGLE_STAIRS` | = |  |
| `COMMAND` | 137 | 0 | `COMMAND_BLOCK` | = |  |
| `BEACON` | 138 | 0 | `BEACON` | = |  |
| `COBBLE_WALL` | 139 | 0 | `COBBLESTONE_WALL` | = |  |
| `COBBLE_WALL` | 139 | 1 | `MOSSY_COBBLESTONE_WALL` | = |  |
| `FLOWER_POT` | 140 | 0 | `POTTED_CACTUS` | = |  |
| `CARROT` | 141 | 0 | `CARROTS` | = |  |
| `POTATO` | 142 | 0 | `POTATOES` | = |  |
| `WOOD_BUTTON` | 143 | 0 | `OAK_BUTTON` | = |  |
| `ANVIL` | 145 | 0 | `ANVIL` | = |  |
| `ANVIL` | 145 | 1 | `CHIPPED_ANVIL` | = | `ANVIL` |
| `ANVIL` | 145 | 2 | `DAMAGED_ANVIL` | = | `ANVIL` |
| `TRAPPED_CHEST` | 146 | 0 | `TRAPPED_CHEST` | = |  |
| `GOLD_PLATE` | 147 | 0 | `LIGHT_WEIGHTED_PRESSURE_PLATE` | = |  |
| `IRON_PLATE` | 148 | 0 | `HEAVY_WEIGHTED_PRESSURE_PLATE` | = |  |
| `REDSTONE_COMPARATOR_OFF` | 149 | 0 | `COMPARATOR` | = |  |
| `REDSTONE_COMPARATOR_ON` | 150 | 0 | `COMPARATOR` | = |  |
| `DAYLIGHT_DETECTOR` | 151 | 0 | `DAYLIGHT_DETECTOR` | = |  |
| `REDSTONE_BLOCK` | 152 | 0 | `REDSTONE_BLOCK` | = |  |
| `QUARTZ_ORE` | 153 | 0 | `NETHER_QUARTZ_ORE` | = |  |
| `HOPPER` | 154 | 0 | `HOPPER` | = |  |
| `QUARTZ_BLOCK` | 155 | 0 | `QUARTZ_BLOCK` | = |  |
| `QUARTZ_BLOCK` | 155 | 1 | `CHISELED_QUARTZ_BLOCK` | = |  |
| `QUARTZ_BLOCK` | 155 | 2 | `QUARTZ_PILLAR` | = |  |
| `QUARTZ_STAIRS` | 156 | 0 | `QUARTZ_STAIRS` | = |  |
| `ACTIVATOR_RAIL` | 157 | 0 | `ACTIVATOR_RAIL` | = |  |
| `DROPPER` | 158 | 0 | `DROPPER` | = |  |
| `STAINED_CLAY` | 159 | 0 | `WHITE_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 1 | `ORANGE_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 2 | `MAGENTA_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 3 | `LIGHT_BLUE_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 4 | `YELLOW_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 5 | `LIME_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 6 | `PINK_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 7 | `GRAY_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 8 | `LIGHT_GRAY_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 9 | `CYAN_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 10 | `PURPLE_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 11 | `BLUE_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 12 | `BROWN_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 13 | `GREEN_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 14 | `RED_TERRACOTTA` | = |  |
| `STAINED_CLAY` | 159 | 15 | `BLACK_TERRACOTTA` | = |  |
| `STAINED_GLASS_PANE` | 160 | 0 | `WHITE_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 1 | `ORANGE_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 2 | `MAGENTA_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 3 | `LIGHT_BLUE_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 4 | `YELLOW_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 5 | `LIME_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 6 | `PINK_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 7 | `GRAY_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 8 | `LIGHT_GRAY_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 9 | `CYAN_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 10 | `PURPLE_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 11 | `BLUE_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 12 | `BROWN_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 13 | `GREEN_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 14 | `RED_STAINED_GLASS_PANE` | = |  |
| `STAINED_GLASS_PANE` | 160 | 15 | `BLACK_STAINED_GLASS_PANE` | = |  |
| `LEAVES_2` | 161 | 0 | `ACACIA_LEAVES` | = |  |
| `LEAVES_2` | 161 | 1 | `DARK_OAK_LEAVES` | = |  |
| `LOG_2` | 162 | 0 | `ACACIA_LOG` | = |  |
| `LOG_2` | 162 | 1 | `DARK_OAK_LOG` | = |  |
| `ACACIA_STAIRS` | 163 | 0 | `ACACIA_STAIRS` | = |  |
| `DARK_OAK_STAIRS` | 164 | 0 | `DARK_OAK_STAIRS` | = |  |
| `SLIME_BLOCK` | 165 | 0 | `SLIME_BLOCK` | = |  |
| `BARRIER` | 166 | 0 | `BARRIER` | = |  |
| `IRON_TRAPDOOR` | 167 | 0 | `IRON_TRAPDOOR` | = |  |
| `PRISMARINE` | 168 | 0 | `PRISMARINE` | = |  |
| `PRISMARINE` | 168 | 1 | `PRISMARINE_BRICKS` | = |  |
| `PRISMARINE` | 168 | 2 | `DARK_PRISMARINE` | = |  |
| `SEA_LANTERN` | 169 | 0 | `SEA_LANTERN` | = |  |
| `HAY_BLOCK` | 170 | 0 | `HAY_BLOCK` | = |  |
| `CARPET` | 171 | 0 | `WHITE_CARPET` | = |  |
| `CARPET` | 171 | 1 | `ORANGE_CARPET` | = |  |
| `CARPET` | 171 | 2 | `MAGENTA_CARPET` | = |  |
| `CARPET` | 171 | 3 | `LIGHT_BLUE_CARPET` | = |  |
| `CARPET` | 171 | 4 | `YELLOW_CARPET` | = |  |
| `CARPET` | 171 | 5 | `LIME_CARPET` | = |  |
| `CARPET` | 171 | 6 | `PINK_CARPET` | = |  |
| `CARPET` | 171 | 7 | `GRAY_CARPET` | = |  |
| `CARPET` | 171 | 8 | `LIGHT_GRAY_CARPET` | = |  |
| `CARPET` | 171 | 9 | `CYAN_CARPET` | = |  |
| `CARPET` | 171 | 10 | `PURPLE_CARPET` | = |  |
| `CARPET` | 171 | 11 | `BLUE_CARPET` | = |  |
| `CARPET` | 171 | 12 | `BROWN_CARPET` | = |  |
| `CARPET` | 171 | 13 | `GREEN_CARPET` | = |  |
| `CARPET` | 171 | 14 | `RED_CARPET` | = |  |
| `CARPET` | 171 | 15 | `BLACK_CARPET` | = |  |
| `HARD_CLAY` | 172 | 0 | `TERRACOTTA` | = |  |
| `COAL_BLOCK` | 173 | 0 | `COAL_BLOCK` | = |  |
| `PACKED_ICE` | 174 | 0 | `PACKED_ICE` | = |  |
| `DOUBLE_PLANT` | 175 | 0 | `SUNFLOWER` | = |  |
| `DOUBLE_PLANT` | 175 | 1 | `LILAC` | = |  |
| `DOUBLE_PLANT` | 175 | 2 | `TALL_GRASS` | = |  |
| `DOUBLE_PLANT` | 175 | 3 | `LARGE_FERN` | = |  |
| `DOUBLE_PLANT` | 175 | 4 | `ROSE_BUSH` | = |  |
| `DOUBLE_PLANT` | 175 | 5 | `PEONY` | = |  |
| `STANDING_BANNER` | 176 | 0 | `WHITE_BANNER` | = |  |
| `WALL_BANNER` | 177 | 2 | `WHITE_WALL_BANNER` | = |  |
| `DAYLIGHT_DETECTOR_INVERTED` | 178 | 0 | `DAYLIGHT_DETECTOR` | = |  |
| `RED_SANDSTONE` | 179 | 0 | `RED_SANDSTONE` | = |  |
| `RED_SANDSTONE` | 179 | 1 | `CHISELED_RED_SANDSTONE` | = |  |
| `RED_SANDSTONE` | 179 | 2 | `CUT_RED_SANDSTONE` | = |  |
| `RED_SANDSTONE_STAIRS` | 180 | 0 | `RED_SANDSTONE_STAIRS` | = |  |
| `DOUBLE_STONE_SLAB2` | 181 | 0 | `RED_SANDSTONE_SLAB` | = |  |
| `DOUBLE_STONE_SLAB2` | 181 | 8 | `SMOOTH_RED_SANDSTONE` | = |  |
| `STONE_SLAB2` | 182 | 0 | `RED_SANDSTONE_SLAB` | = |  |
| `SPRUCE_FENCE_GATE` | 183 | 0 | `SPRUCE_FENCE_GATE` | = |  |
| `BIRCH_FENCE_GATE` | 184 | 0 | `BIRCH_FENCE_GATE` | = |  |
| `JUNGLE_FENCE_GATE` | 185 | 0 | `JUNGLE_FENCE_GATE` | = |  |
| `DARK_OAK_FENCE_GATE` | 186 | 0 | `DARK_OAK_FENCE_GATE` | = |  |
| `ACACIA_FENCE_GATE` | 187 | 0 | `ACACIA_FENCE_GATE` | = |  |
| `SPRUCE_FENCE` | 188 | 0 | `SPRUCE_FENCE` | = |  |
| `BIRCH_FENCE` | 189 | 0 | `BIRCH_FENCE` | = |  |
| `JUNGLE_FENCE` | 190 | 0 | `JUNGLE_FENCE` | = |  |
| `DARK_OAK_FENCE` | 191 | 0 | `DARK_OAK_FENCE` | = |  |
| `ACACIA_FENCE` | 192 | 0 | `ACACIA_FENCE` | = |  |
| `SPRUCE_DOOR` | 193 | 0 | `SPRUCE_DOOR` | = |  |
| `BIRCH_DOOR` | 194 | 0 | `BIRCH_DOOR` | = |  |
| `JUNGLE_DOOR` | 195 | 0 | `JUNGLE_DOOR` | = |  |
| `ACACIA_DOOR` | 196 | 0 | `ACACIA_DOOR` | = |  |
| `DARK_OAK_DOOR` | 197 | 0 | `DARK_OAK_DOOR` | = |  |
| `END_ROD` | 198 | 0 | `END_ROD` | = |  |
| `CHORUS_PLANT` | 199 | 0 | `CHORUS_PLANT` | = |  |
| `CHORUS_FLOWER` | 200 | 0 | `CHORUS_FLOWER` | = |  |
| `PURPUR_BLOCK` | 201 | 0 | `PURPUR_BLOCK` | = |  |
| `PURPUR_PILLAR` | 202 | 0 | `PURPUR_PILLAR` | = |  |
| `PURPUR_STAIRS` | 203 | 0 | `PURPUR_STAIRS` | = |  |
| `PURPUR_DOUBLE_SLAB` | 204 | 0 | `PURPUR_SLAB` | = |  |
| `PURPUR_SLAB` | 205 | 0 | `PURPUR_SLAB` | = |  |
| `END_BRICKS` | 206 | 0 | `END_STONE_BRICKS` | = |  |
| `BEETROOT_BLOCK` | 207 | 0 | `BEETROOTS` | = |  |
| `GRASS_PATH` | 208 | 0 | `GRASS_PATH` | `DIRT_PATH` |  |
| `END_GATEWAY` | 209 | 0 | `END_GATEWAY` | = |  |
| `COMMAND_REPEATING` | 210 | 0 | `REPEATING_COMMAND_BLOCK` | = |  |
| `COMMAND_CHAIN` | 211 | 0 | `CHAIN_COMMAND_BLOCK` | = |  |
| `FROSTED_ICE` | 212 | 0 | `FROSTED_ICE` | = |  |
| `MAGMA` | 213 | 0 | `MAGMA_BLOCK` | = |  |
| `NETHER_WART_BLOCK` | 214 | 0 | `NETHER_WART_BLOCK` | = |  |
| `RED_NETHER_BRICK` | 215 | 0 | `RED_NETHER_BRICKS` | = |  |
| `BONE_BLOCK` | 216 | 0 | `BONE_BLOCK` | = |  |
| `STRUCTURE_VOID` | 217 | 0 | `STRUCTURE_VOID` | = |  |
| `OBSERVER` | 218 | 0 | `OBSERVER` | = |  |
| `WHITE_SHULKER_BOX` | 219 | 0 | `WHITE_SHULKER_BOX` | = |  |
| `ORANGE_SHULKER_BOX` | 220 | 0 | `ORANGE_SHULKER_BOX` | = |  |
| `MAGENTA_SHULKER_BOX` | 221 | 0 | `MAGENTA_SHULKER_BOX` | = |  |
| `LIGHT_BLUE_SHULKER_BOX` | 222 | 0 | `LIGHT_BLUE_SHULKER_BOX` | = |  |
| `YELLOW_SHULKER_BOX` | 223 | 0 | `YELLOW_SHULKER_BOX` | = |  |
| `LIME_SHULKER_BOX` | 224 | 0 | `LIME_SHULKER_BOX` | = |  |
| `PINK_SHULKER_BOX` | 225 | 0 | `PINK_SHULKER_BOX` | = |  |
| `GRAY_SHULKER_BOX` | 226 | 0 | `GRAY_SHULKER_BOX` | = |  |
| `SILVER_SHULKER_BOX` | 227 | 0 | `LIGHT_GRAY_SHULKER_BOX` | = |  |
| `CYAN_SHULKER_BOX` | 228 | 0 | `CYAN_SHULKER_BOX` | = |  |
| `PURPLE_SHULKER_BOX` | 229 | 0 | `PURPLE_SHULKER_BOX` | = |  |
| `BLUE_SHULKER_BOX` | 230 | 0 | `BLUE_SHULKER_BOX` | = |  |
| `BROWN_SHULKER_BOX` | 231 | 0 | `BROWN_SHULKER_BOX` | = |  |
| `GREEN_SHULKER_BOX` | 232 | 0 | `GREEN_SHULKER_BOX` | = |  |
| `RED_SHULKER_BOX` | 233 | 0 | `RED_SHULKER_BOX` | = |  |
| `BLACK_SHULKER_BOX` | 234 | 0 | `BLACK_SHULKER_BOX` | = |  |
| `WHITE_GLAZED_TERRACOTTA` | 235 | 0 | `WHITE_GLAZED_TERRACOTTA` | = |  |
| `ORANGE_GLAZED_TERRACOTTA` | 236 | 0 | `ORANGE_GLAZED_TERRACOTTA` | = |  |
| `MAGENTA_GLAZED_TERRACOTTA` | 237 | 0 | `MAGENTA_GLAZED_TERRACOTTA` | = |  |
| `LIGHT_BLUE_GLAZED_TERRACOTTA` | 238 | 0 | `LIGHT_BLUE_GLAZED_TERRACOTTA` | = |  |
| `YELLOW_GLAZED_TERRACOTTA` | 239 | 0 | `YELLOW_GLAZED_TERRACOTTA` | = |  |
| `LIME_GLAZED_TERRACOTTA` | 240 | 0 | `LIME_GLAZED_TERRACOTTA` | = |  |
| `PINK_GLAZED_TERRACOTTA` | 241 | 0 | `PINK_GLAZED_TERRACOTTA` | = |  |
| `GRAY_GLAZED_TERRACOTTA` | 242 | 0 | `GRAY_GLAZED_TERRACOTTA` | = |  |
| `SILVER_GLAZED_TERRACOTTA` | 243 | 0 | `LIGHT_GRAY_GLAZED_TERRACOTTA` | = |  |
| `CYAN_GLAZED_TERRACOTTA` | 244 | 0 | `CYAN_GLAZED_TERRACOTTA` | = |  |
| `PURPLE_GLAZED_TERRACOTTA` | 245 | 0 | `PURPLE_GLAZED_TERRACOTTA` | = |  |
| `BLUE_GLAZED_TERRACOTTA` | 246 | 0 | `BLUE_GLAZED_TERRACOTTA` | = |  |
| `BROWN_GLAZED_TERRACOTTA` | 247 | 0 | `BROWN_GLAZED_TERRACOTTA` | = |  |
| `GREEN_GLAZED_TERRACOTTA` | 248 | 0 | `GREEN_GLAZED_TERRACOTTA` | = |  |
| `RED_GLAZED_TERRACOTTA` | 249 | 0 | `RED_GLAZED_TERRACOTTA` | = |  |
| `BLACK_GLAZED_TERRACOTTA` | 250 | 0 | `BLACK_GLAZED_TERRACOTTA` | = |  |
| `CONCRETE` | 251 | 0 | `WHITE_CONCRETE` | = |  |
| `CONCRETE` | 251 | 1 | `ORANGE_CONCRETE` | = |  |
| `CONCRETE` | 251 | 2 | `MAGENTA_CONCRETE` | = |  |
| `CONCRETE` | 251 | 3 | `LIGHT_BLUE_CONCRETE` | = |  |
| `CONCRETE` | 251 | 4 | `YELLOW_CONCRETE` | = |  |
| `CONCRETE` | 251 | 5 | `LIME_CONCRETE` | = |  |
| `CONCRETE` | 251 | 6 | `PINK_CONCRETE` | = |  |
| `CONCRETE` | 251 | 7 | `GRAY_CONCRETE` | = |  |
| `CONCRETE` | 251 | 8 | `LIGHT_GRAY_CONCRETE` | = |  |
| `CONCRETE` | 251 | 9 | `CYAN_CONCRETE` | = |  |
| `CONCRETE` | 251 | 10 | `PURPLE_CONCRETE` | = |  |
| `CONCRETE` | 251 | 11 | `BLUE_CONCRETE` | = |  |
| `CONCRETE` | 251 | 12 | `BROWN_CONCRETE` | = |  |
| `CONCRETE` | 251 | 13 | `GREEN_CONCRETE` | = |  |
| `CONCRETE` | 251 | 14 | `RED_CONCRETE` | = |  |
| `CONCRETE` | 251 | 15 | `BLACK_CONCRETE` | = |  |
| `CONCRETE_POWDER` | 252 | 0 | `WHITE_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 1 | `ORANGE_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 2 | `MAGENTA_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 3 | `LIGHT_BLUE_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 4 | `YELLOW_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 5 | `LIME_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 6 | `PINK_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 7 | `GRAY_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 8 | `LIGHT_GRAY_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 9 | `CYAN_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 10 | `PURPLE_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 11 | `BLUE_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 12 | `BROWN_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 13 | `GREEN_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 14 | `RED_CONCRETE_POWDER` | = |  |
| `CONCRETE_POWDER` | 252 | 15 | `BLACK_CONCRETE_POWDER` | = |  |
| `STRUCTURE_BLOCK` | 255 | 0 | `STRUCTURE_BLOCK` | = |  |
| `IRON_SPADE` | 256 | 0 | `IRON_SHOVEL` | = |  |
| `IRON_PICKAXE` | 257 | 0 | `IRON_PICKAXE` | = |  |
| `IRON_AXE` | 258 | 0 | `IRON_AXE` | = |  |
| `FLINT_AND_STEEL` | 259 | 0 | `FLINT_AND_STEEL` | = |  |
| `APPLE` | 260 | 0 | `APPLE` | = |  |
| `BOW` | 261 | 0 | `BOW` | = |  |
| `ARROW` | 262 | 0 | `ARROW` | = |  |
| `COAL` | 263 | 0 | `COAL` | = |  |
| `COAL` | 263 | 1 | `CHARCOAL` | = |  |
| `DIAMOND` | 264 | 0 | `DIAMOND` | = |  |
| `IRON_INGOT` | 265 | 0 | `IRON_INGOT` | = |  |
| `GOLD_INGOT` | 266 | 0 | `GOLD_INGOT` | = |  |
| `IRON_SWORD` | 267 | 0 | `IRON_SWORD` | = |  |
| `WOOD_SWORD` | 268 | 0 | `WOODEN_SWORD` | = |  |
| `WOOD_SPADE` | 269 | 0 | `WOODEN_SHOVEL` | = |  |
| `WOOD_PICKAXE` | 270 | 0 | `WOODEN_PICKAXE` | = |  |
| `WOOD_AXE` | 271 | 0 | `WOODEN_AXE` | = |  |
| `STONE_SWORD` | 272 | 0 | `STONE_SWORD` | = |  |
| `STONE_SPADE` | 273 | 0 | `STONE_SHOVEL` | = |  |
| `STONE_PICKAXE` | 274 | 0 | `STONE_PICKAXE` | = |  |
| `STONE_AXE` | 275 | 0 | `STONE_AXE` | = |  |
| `DIAMOND_SWORD` | 276 | 0 | `DIAMOND_SWORD` | = |  |
| `DIAMOND_SPADE` | 277 | 0 | `DIAMOND_SHOVEL` | = |  |
| `DIAMOND_PICKAXE` | 278 | 0 | `DIAMOND_PICKAXE` | = |  |
| `DIAMOND_AXE` | 279 | 0 | `DIAMOND_AXE` | = |  |
| `STICK` | 280 | 0 | `STICK` | = |  |
| `BOWL` | 281 | 0 | `BOWL` | = |  |
| `MUSHROOM_SOUP` | 282 | 0 | `MUSHROOM_STEW` | = |  |
| `GOLD_SWORD` | 283 | 0 | `GOLDEN_SWORD` | = |  |
| `GOLD_SPADE` | 284 | 0 | `GOLDEN_SHOVEL` | = |  |
| `GOLD_PICKAXE` | 285 | 0 | `GOLDEN_PICKAXE` | = |  |
| `GOLD_AXE` | 286 | 0 | `GOLDEN_AXE` | = |  |
| `STRING` | 287 | 0 | `STRING` | = |  |
| `FEATHER` | 288 | 0 | `FEATHER` | = |  |
| `SULPHUR` | 289 | 0 | `GUNPOWDER` | = |  |
| `WOOD_HOE` | 290 | 0 | `WOODEN_HOE` | = |  |
| `STONE_HOE` | 291 | 0 | `STONE_HOE` | = |  |
| `IRON_HOE` | 292 | 0 | `IRON_HOE` | = |  |
| `DIAMOND_HOE` | 293 | 0 | `DIAMOND_HOE` | = |  |
| `GOLD_HOE` | 294 | 0 | `GOLDEN_HOE` | = |  |
| `SEEDS` | 295 | 0 | `WHEAT_SEEDS` | = |  |
| `WHEAT` | 296 | 0 | `WHEAT` | = |  |
| `BREAD` | 297 | 0 | `BREAD` | = |  |
| `LEATHER_HELMET` | 298 | 0 | `LEATHER_HELMET` | = |  |
| `LEATHER_CHESTPLATE` | 299 | 0 | `LEATHER_CHESTPLATE` | = |  |
| `LEATHER_LEGGINGS` | 300 | 0 | `LEATHER_LEGGINGS` | = |  |
| `LEATHER_BOOTS` | 301 | 0 | `LEATHER_BOOTS` | = |  |
| `CHAINMAIL_HELMET` | 302 | 0 | `CHAINMAIL_HELMET` | = |  |
| `CHAINMAIL_CHESTPLATE` | 303 | 0 | `CHAINMAIL_CHESTPLATE` | = |  |
| `CHAINMAIL_LEGGINGS` | 304 | 0 | `CHAINMAIL_LEGGINGS` | = |  |
| `CHAINMAIL_BOOTS` | 305 | 0 | `CHAINMAIL_BOOTS` | = |  |
| `IRON_HELMET` | 306 | 0 | `IRON_HELMET` | = |  |
| `IRON_CHESTPLATE` | 307 | 0 | `IRON_CHESTPLATE` | = |  |
| `IRON_LEGGINGS` | 308 | 0 | `IRON_LEGGINGS` | = |  |
| `IRON_BOOTS` | 309 | 0 | `IRON_BOOTS` | = |  |
| `DIAMOND_HELMET` | 310 | 0 | `DIAMOND_HELMET` | = |  |
| `DIAMOND_CHESTPLATE` | 311 | 0 | `DIAMOND_CHESTPLATE` | = |  |
| `DIAMOND_LEGGINGS` | 312 | 0 | `DIAMOND_LEGGINGS` | = |  |
| `DIAMOND_BOOTS` | 313 | 0 | `DIAMOND_BOOTS` | = |  |
| `GOLD_HELMET` | 314 | 0 | `GOLDEN_HELMET` | = |  |
| `GOLD_CHESTPLATE` | 315 | 0 | `GOLDEN_CHESTPLATE` | = |  |
| `GOLD_LEGGINGS` | 316 | 0 | `GOLDEN_LEGGINGS` | = |  |
| `GOLD_BOOTS` | 317 | 0 | `GOLDEN_BOOTS` | = |  |
| `FLINT` | 318 | 0 | `FLINT` | = |  |
| `PORK` | 319 | 0 | `PORKCHOP` | = |  |
| `GRILLED_PORK` | 320 | 0 | `COOKED_PORKCHOP` | = |  |
| `PAINTING` | 321 | 0 | `PAINTING` | = |  |
| `GOLDEN_APPLE` | 322 | 0 | `GOLDEN_APPLE` | = |  |
| `GOLDEN_APPLE` | 322 | 1 | `ENCHANTED_GOLDEN_APPLE` | = |  |
| `SIGN` | 323 | 0 | `SIGN` | `OAK_SIGN` |  |
| `WOOD_DOOR` | 324 | 0 | `OAK_DOOR` | = |  |
| `BUCKET` | 325 | 0 | `BUCKET` | = |  |
| `WATER_BUCKET` | 326 | 0 | `WATER_BUCKET` | = |  |
| `LAVA_BUCKET` | 327 | 0 | `LAVA_BUCKET` | = |  |
| `MINECART` | 328 | 0 | `MINECART` | = |  |
| `SADDLE` | 329 | 0 | `SADDLE` | = |  |
| `IRON_DOOR` | 330 | 0 | `IRON_DOOR` | = |  |
| `REDSTONE` | 331 | 0 | `REDSTONE` | = |  |
| `SNOW_BALL` | 332 | 0 | `SNOWBALL` | = |  |
| `BOAT` | 333 | 0 | `OAK_BOAT` | = |  |
| `LEATHER` | 334 | 0 | `LEATHER` | = |  |
| `MILK_BUCKET` | 335 | 0 | `MILK_BUCKET` | = |  |
| `CLAY_BRICK` | 336 | 0 | `BRICK` | = |  |
| `CLAY_BALL` | 337 | 0 | `CLAY_BALL` | = |  |
| `SUGAR_CANE` | 338 | 0 | `SUGAR_CANE` | = |  |
| `PAPER` | 339 | 0 | `PAPER` | = |  |
| `BOOK` | 340 | 0 | `BOOK` | = |  |
| `SLIME_BALL` | 341 | 0 | `SLIME_BALL` | = |  |
| `STORAGE_MINECART` | 342 | 0 | `CHEST_MINECART` | = |  |
| `POWERED_MINECART` | 343 | 0 | `FURNACE_MINECART` | = |  |
| `EGG` | 344 | 0 | `EGG` | = |  |
| `COMPASS` | 345 | 0 | `COMPASS` | = |  |
| `FISHING_ROD` | 346 | 0 | `FISHING_ROD` | = |  |
| `WATCH` | 347 | 0 | `CLOCK` | = |  |
| `GLOWSTONE_DUST` | 348 | 0 | `GLOWSTONE_DUST` | = |  |
| `RAW_FISH` | 349 | 0 | `COD` | = |  |
| `RAW_FISH` | 349 | 1 | `SALMON` | = |  |
| `RAW_FISH` | 349 | 2 | `TROPICAL_FISH` | = |  |
| `RAW_FISH` | 349 | 3 | `PUFFERFISH` | = |  |
| `COOKED_FISH` | 350 | 0 | `COOKED_COD` | = |  |
| `COOKED_FISH` | 350 | 1 | `COOKED_SALMON` | = |  |
| `INK_SACK` | 351 | 0 | `INK_SAC` | = |  |
| `INK_SACK` | 351 | 1 | `ROSE_RED` | `RED_DYE` |  |
| `INK_SACK` | 351 | 2 | `CACTUS_GREEN` | `GREEN_DYE` |  |
| `INK_SACK` | 351 | 3 | `COCOA_BEANS` | = |  |
| `INK_SACK` | 351 | 4 | `LAPIS_LAZULI` | = |  |
| `INK_SACK` | 351 | 5 | `PURPLE_DYE` | = |  |
| `INK_SACK` | 351 | 6 | `CYAN_DYE` | = |  |
| `INK_SACK` | 351 | 7 | `LIGHT_GRAY_DYE` | = |  |
| `INK_SACK` | 351 | 8 | `GRAY_DYE` | = |  |
| `INK_SACK` | 351 | 9 | `PINK_DYE` | = |  |
| `INK_SACK` | 351 | 10 | `LIME_DYE` | = |  |
| `INK_SACK` | 351 | 11 | `DANDELION_YELLOW` | `YELLOW_DYE` |  |
| `INK_SACK` | 351 | 12 | `LIGHT_BLUE_DYE` | = |  |
| `INK_SACK` | 351 | 13 | `MAGENTA_DYE` | = |  |
| `INK_SACK` | 351 | 14 | `ORANGE_DYE` | = |  |
| `INK_SACK` | 351 | 15 | `BONE_MEAL` | = |  |
| `BONE` | 352 | 0 | `BONE` | = |  |
| `SUGAR` | 353 | 0 | `SUGAR` | = |  |
| `CAKE` | 354 | 0 | `CAKE` | = |  |
| `BED` | 355 | 0 | `RED_BED` | = |  |
| `BED` | 355 | 1 | `ORANGE_BED` | = |  |
| `BED` | 355 | 2 | `MAGENTA_BED` | = |  |
| `BED` | 355 | 3 | `LIGHT_BLUE_BED` | = |  |
| `BED` | 355 | 4 | `YELLOW_BED` | = |  |
| `BED` | 355 | 5 | `LIME_BED` | = |  |
| `BED` | 355 | 6 | `PINK_BED` | = |  |
| `BED` | 355 | 7 | `GRAY_BED` | = |  |
| `BED` | 355 | 8 | `LIGHT_GRAY_BED` | = |  |
| `BED` | 355 | 9 | `CYAN_BED` | = |  |
| `BED` | 355 | 10 | `PURPLE_BED` | = |  |
| `BED` | 355 | 11 | `BLUE_BED` | = |  |
| `BED` | 355 | 12 | `BROWN_BED` | = |  |
| `BED` | 355 | 13 | `GREEN_BED` | = |  |
| `BED` | 355 | 15 | `BLACK_BED` | = |  |
| `DIODE` | 356 | 0 | `REPEATER` | = |  |
| `COOKIE` | 357 | 0 | `COOKIE` | = |  |
| `MAP` | 358 | 0 | `FILLED_MAP` | = |  |
| `SHEARS` | 359 | 0 | `SHEARS` | = |  |
| `MELON` | 360 | 0 | `MELON_SLICE` | = |  |
| `PUMPKIN_SEEDS` | 361 | 0 | `PUMPKIN_SEEDS` | = |  |
| `MELON_SEEDS` | 362 | 0 | `MELON_SEEDS` | = |  |
| `RAW_BEEF` | 363 | 0 | `BEEF` | = |  |
| `COOKED_BEEF` | 364 | 0 | `COOKED_BEEF` | = |  |
| `RAW_CHICKEN` | 365 | 0 | `CHICKEN` | = |  |
| `COOKED_CHICKEN` | 366 | 0 | `COOKED_CHICKEN` | = |  |
| `ROTTEN_FLESH` | 367 | 0 | `ROTTEN_FLESH` | = |  |
| `ENDER_PEARL` | 368 | 0 | `ENDER_PEARL` | = |  |
| `BLAZE_ROD` | 369 | 0 | `BLAZE_ROD` | = |  |
| `GHAST_TEAR` | 370 | 0 | `GHAST_TEAR` | = |  |
| `GOLD_NUGGET` | 371 | 0 | `GOLD_NUGGET` | = |  |
| `NETHER_STALK` | 372 | 0 | `NETHER_WART` | = |  |
| `POTION` | 373 | 0 | `POTION` | = |  |
| `GLASS_BOTTLE` | 374 | 0 | `GLASS_BOTTLE` | = |  |
| `SPIDER_EYE` | 375 | 0 | `SPIDER_EYE` | = |  |
| `FERMENTED_SPIDER_EYE` | 376 | 0 | `FERMENTED_SPIDER_EYE` | = |  |
| `BLAZE_POWDER` | 377 | 0 | `BLAZE_POWDER` | = |  |
| `MAGMA_CREAM` | 378 | 0 | `MAGMA_CREAM` | = |  |
| `BREWING_STAND_ITEM` | 379 | 0 | `BREWING_STAND` | = |  |
| `CAULDRON_ITEM` | 380 | 0 | `CAULDRON` | = |  |
| `EYE_OF_ENDER` | 381 | 0 | `ENDER_EYE` | = |  |
| `SPECKLED_MELON` | 382 | 0 | `GLISTERING_MELON_SLICE` | = |  |
| `MONSTER_EGG` | 383 | 0 | `PIG_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 4 | `ELDER_GUARDIAN_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 5 | `WITHER_SKELETON_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 6 | `STRAY_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 23 | `HUSK_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 27 | `ZOMBIE_VILLAGER_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 28 | `SKELETON_HORSE_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 29 | `ZOMBIE_HORSE_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 31 | `DONKEY_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 32 | `MULE_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 34 | `EVOKER_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 35 | `VEX_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 36 | `VINDICATOR_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 50 | `CREEPER_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 51 | `SKELETON_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 52 | `SPIDER_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 54 | `ZOMBIE_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 55 | `SLIME_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 56 | `GHAST_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 57 | `ZOMBIE_PIGMAN_SPAWN_EGG` | `ZOMBIFIED_PIGLIN_SPAWN_EGG` |  |
| `MONSTER_EGG` | 383 | 58 | `ENDERMAN_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 59 | `CAVE_SPIDER_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 60 | `SILVERFISH_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 61 | `BLAZE_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 62 | `MAGMA_CUBE_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 65 | `BAT_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 66 | `WITCH_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 67 | `ENDERMITE_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 68 | `GUARDIAN_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 69 | `SHULKER_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 91 | `SHEEP_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 92 | `COW_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 93 | `CHICKEN_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 94 | `SQUID_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 95 | `WOLF_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 96 | `MOOSHROOM_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 98 | `OCELOT_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 100 | `HORSE_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 101 | `RABBIT_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 102 | `POLAR_BEAR_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 103 | `LLAMA_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 105 | `PARROT_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 120 | `VILLAGER_SPAWN_EGG` | = |  |
| `MONSTER_EGG` | 383 | 255 | `TURTLE_SPAWN_EGG` | = |  |
| `EXP_BOTTLE` | 384 | 0 | `EXPERIENCE_BOTTLE` | = |  |
| `FIREBALL` | 385 | 0 | `FIRE_CHARGE` | = |  |
| `BOOK_AND_QUILL` | 386 | 0 | `WRITABLE_BOOK` | = |  |
| `WRITTEN_BOOK` | 387 | 0 | `WRITTEN_BOOK` | = |  |
| `EMERALD` | 388 | 0 | `EMERALD` | = |  |
| `ITEM_FRAME` | 389 | 0 | `ITEM_FRAME` | = |  |
| `FLOWER_POT_ITEM` | 390 | 0 | `FLOWER_POT` | = |  |
| `CARROT_ITEM` | 391 | 0 | `CARROT` | = |  |
| `POTATO_ITEM` | 392 | 0 | `POTATO` | = |  |
| `BAKED_POTATO` | 393 | 0 | `BAKED_POTATO` | = |  |
| `POISONOUS_POTATO` | 394 | 0 | `POISONOUS_POTATO` | = |  |
| `EMPTY_MAP` | 395 | 0 | `MAP` | = |  |
| `GOLDEN_CARROT` | 396 | 0 | `GOLDEN_CARROT` | = |  |
| `SKULL_ITEM` | 397 | 0 | `SKELETON_SKULL` | = |  |
| `SKULL_ITEM` | 397 | 1 | `WITHER_SKELETON_SKULL` | = |  |
| `SKULL_ITEM` | 397 | 2 | `ZOMBIE_HEAD` | = |  |
| `SKULL_ITEM` | 397 | 3 | `PLAYER_HEAD` | = |  |
| `SKULL_ITEM` | 397 | 4 | `CREEPER_HEAD` | = |  |
| `SKULL_ITEM` | 397 | 5 | `DRAGON_HEAD` | = |  |
| `CARROT_STICK` | 398 | 0 | `CARROT_ON_A_STICK` | = |  |
| `NETHER_STAR` | 399 | 0 | `NETHER_STAR` | = |  |
| `PUMPKIN_PIE` | 400 | 0 | `PUMPKIN_PIE` | = |  |
| `FIREWORK` | 401 | 0 | `FIREWORK_ROCKET` | = |  |
| `FIREWORK_CHARGE` | 402 | 0 | `FIREWORK_STAR` | = |  |
| `ENCHANTED_BOOK` | 403 | 0 | `ENCHANTED_BOOK` | = |  |
| `REDSTONE_COMPARATOR` | 404 | 0 | `COMPARATOR` | = |  |
| `NETHER_BRICK_ITEM` | 405 | 0 | `NETHER_BRICK` | = |  |
| `QUARTZ` | 406 | 0 | `QUARTZ` | = |  |
| `EXPLOSIVE_MINECART` | 407 | 0 | `TNT_MINECART` | = |  |
| `HOPPER_MINECART` | 408 | 0 | `HOPPER_MINECART` | = |  |
| `PRISMARINE_SHARD` | 409 | 0 | `PRISMARINE_SHARD` | = |  |
| `PRISMARINE_CRYSTALS` | 410 | 0 | `PRISMARINE_CRYSTALS` | = |  |
| `RABBIT` | 411 | 0 | `RABBIT` | = |  |
| `COOKED_RABBIT` | 412 | 0 | `COOKED_RABBIT` | = |  |
| `RABBIT_STEW` | 413 | 0 | `RABBIT_STEW` | = |  |
| `RABBIT_FOOT` | 414 | 0 | `RABBIT_FOOT` | = |  |
| `RABBIT_HIDE` | 415 | 0 | `RABBIT_HIDE` | = |  |
| `ARMOR_STAND` | 416 | 0 | `ARMOR_STAND` | = |  |
| `IRON_BARDING` | 417 | 0 | `IRON_HORSE_ARMOR` | = |  |
| `GOLD_BARDING` | 418 | 0 | `GOLDEN_HORSE_ARMOR` | = |  |
| `DIAMOND_BARDING` | 419 | 0 | `DIAMOND_HORSE_ARMOR` | = |  |
| `LEASH` | 420 | 0 | `LEAD` | = |  |
| `NAME_TAG` | 421 | 0 | `NAME_TAG` | = |  |
| `COMMAND_MINECART` | 422 | 0 | `COMMAND_BLOCK_MINECART` | = |  |
| `MUTTON` | 423 | 0 | `MUTTON` | = |  |
| `COOKED_MUTTON` | 424 | 0 | `COOKED_MUTTON` | = |  |
| `BANNER` | 425 | 0 | `BLACK_BANNER` | = |  |
| `BANNER` | 425 | 1 | `RED_BANNER` | = |  |
| `BANNER` | 425 | 2 | `GREEN_BANNER` | = |  |
| `BANNER` | 425 | 3 | `BROWN_BANNER` | = |  |
| `BANNER` | 425 | 4 | `BLUE_BANNER` | = |  |
| `BANNER` | 425 | 5 | `PURPLE_BANNER` | = |  |
| `BANNER` | 425 | 6 | `CYAN_BANNER` | = |  |
| `BANNER` | 425 | 7 | `LIGHT_GRAY_BANNER` | = |  |
| `BANNER` | 425 | 8 | `GRAY_BANNER` | = |  |
| `BANNER` | 425 | 9 | `PINK_BANNER` | = |  |
| `BANNER` | 425 | 10 | `LIME_BANNER` | = |  |
| `BANNER` | 425 | 11 | `YELLOW_BANNER` | = |  |
| `BANNER` | 425 | 12 | `LIGHT_BLUE_BANNER` | = |  |
| `BANNER` | 425 | 13 | `MAGENTA_BANNER` | = |  |
| `BANNER` | 425 | 14 | `ORANGE_BANNER` | = |  |
| `BANNER` | 425 | 15 | `WHITE_BANNER` | = |  |
| `END_CRYSTAL` | 426 | 0 | `END_CRYSTAL` | = |  |
| `SPRUCE_DOOR_ITEM` | 427 | 0 | `SPRUCE_DOOR` | = |  |
| `BIRCH_DOOR_ITEM` | 428 | 0 | `BIRCH_DOOR` | = |  |
| `JUNGLE_DOOR_ITEM` | 429 | 0 | `JUNGLE_DOOR` | = |  |
| `ACACIA_DOOR_ITEM` | 430 | 0 | `ACACIA_DOOR` | = |  |
| `DARK_OAK_DOOR_ITEM` | 431 | 0 | `DARK_OAK_DOOR` | = |  |
| `CHORUS_FRUIT` | 432 | 0 | `CHORUS_FRUIT` | = |  |
| `CHORUS_FRUIT_POPPED` | 433 | 0 | `POPPED_CHORUS_FRUIT` | = |  |
| `BEETROOT` | 434 | 0 | `BEETROOT` | = |  |
| `BEETROOT_SEEDS` | 435 | 0 | `BEETROOT_SEEDS` | = |  |
| `BEETROOT_SOUP` | 436 | 0 | `BEETROOT_SOUP` | = |  |
| `DRAGONS_BREATH` | 437 | 0 | `DRAGON_BREATH` | = |  |
| `SPLASH_POTION` | 438 | 0 | `SPLASH_POTION` | = |  |
| `SPECTRAL_ARROW` | 439 | 0 | `SPECTRAL_ARROW` | = |  |
| `TIPPED_ARROW` | 440 | 0 | `TIPPED_ARROW` | = |  |
| `LINGERING_POTION` | 441 | 0 | `LINGERING_POTION` | = |  |
| `SHIELD` | 442 | 0 | `SHIELD` | = |  |
| `ELYTRA` | 443 | 0 | `ELYTRA` | = |  |
| `BOAT_SPRUCE` | 444 | 0 | `SPRUCE_BOAT` | = |  |
| `BOAT_BIRCH` | 445 | 0 | `BIRCH_BOAT` | = |  |
| `BOAT_JUNGLE` | 446 | 0 | `JUNGLE_BOAT` | = |  |
| `BOAT_ACACIA` | 447 | 0 | `ACACIA_BOAT` | = |  |
| `BOAT_DARK_OAK` | 448 | 0 | `DARK_OAK_BOAT` | = |  |
| `TOTEM` | 449 | 0 | `TOTEM_OF_UNDYING` | = |  |
| `SHULKER_SHELL` | 450 | 0 | `SHULKER_SHELL` | = |  |
| `IRON_NUGGET` | 452 | 0 | `IRON_NUGGET` | = |  |
| `KNOWLEDGE_BOOK` | 453 | 0 | `KNOWLEDGE_BOOK` | = |  |
| `GOLD_RECORD` | 2256 | 0 | `MUSIC_DISC_13` | = |  |
| `GREEN_RECORD` | 2257 | 0 | `MUSIC_DISC_CAT` | = |  |
| `RECORD_3` | 2258 | 0 | `MUSIC_DISC_BLOCKS` | = |  |
| `RECORD_4` | 2259 | 0 | `MUSIC_DISC_CHIRP` | = |  |
| `RECORD_5` | 2260 | 0 | `MUSIC_DISC_FAR` | = |  |
| `RECORD_6` | 2261 | 0 | `MUSIC_DISC_MALL` | = |  |
| `RECORD_7` | 2262 | 0 | `MUSIC_DISC_MELLOHI` | = |  |
| `RECORD_8` | 2263 | 0 | `MUSIC_DISC_STAL` | = |  |
| `RECORD_9` | 2264 | 0 | `MUSIC_DISC_STRAD` | = |  |
| `RECORD_10` | 2265 | 0 | `MUSIC_DISC_WARD` | = |  |
| `RECORD_11` | 2266 | 0 | `MUSIC_DISC_11` | = |  |
| `RECORD_12` | 2267 | 0 | `MUSIC_DISC_WAIT` | = |  |

756 rows; 43 old names have several kinds.

## Appendix A: 1.8 - 1.12.2 names

| Name | Id | First revision | Last revision |
|---|---|---|---|
| `AIR` | 0 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STONE` | 1 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GRASS` | 2 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIRT` | 3 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COBBLESTONE` | 4 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD` | 5 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SAPLING` | 6 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BEDROCK` | 7 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WATER` | 8 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STATIONARY_WATER` | 9 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LAVA` | 10 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STATIONARY_LAVA` | 11 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SAND` | 12 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GRAVEL` | 13 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_ORE` | 14 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_ORE` | 15 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COAL_ORE` | 16 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LOG` | 17 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LEAVES` | 18 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SPONGE` | 19 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GLASS` | 20 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LAPIS_ORE` | 21 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LAPIS_BLOCK` | 22 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DISPENSER` | 23 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SANDSTONE` | 24 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `NOTE_BLOCK` | 25 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BED_BLOCK` | 26 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `POWERED_RAIL` | 27 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DETECTOR_RAIL` | 28 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PISTON_STICKY_BASE` | 29 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WEB` | 30 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LONG_GRASS` | 31 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DEAD_BUSH` | 32 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PISTON_BASE` | 33 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PISTON_EXTENSION` | 34 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOL` | 35 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PISTON_MOVING_PIECE` | 36 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `YELLOW_FLOWER` | 37 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RED_ROSE` | 38 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BROWN_MUSHROOM` | 39 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RED_MUSHROOM` | 40 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_BLOCK` | 41 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_BLOCK` | 42 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DOUBLE_STEP` | 43 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STEP` | 44 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BRICK` | 45 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `TNT` | 46 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BOOKSHELF` | 47 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MOSSY_COBBLESTONE` | 48 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `OBSIDIAN` | 49 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `TORCH` | 50 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FIRE` | 51 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MOB_SPAWNER` | 52 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_STAIRS` | 53 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CHEST` | 54 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE_WIRE` | 55 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_ORE` | 56 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_BLOCK` | 57 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WORKBENCH` | 58 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CROPS` | 59 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SOIL` | 60 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FURNACE` | 61 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BURNING_FURNACE` | 62 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SIGN_POST` | 63 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOODEN_DOOR` | 64 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LADDER` | 65 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RAILS` | 66 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COBBLESTONE_STAIRS` | 67 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WALL_SIGN` | 68 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LEVER` | 69 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STONE_PLATE` | 70 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_DOOR_BLOCK` | 71 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_PLATE` | 72 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE_ORE` | 73 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GLOWING_REDSTONE_ORE` | 74 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE_TORCH_OFF` | 75 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE_TORCH_ON` | 76 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STONE_BUTTON` | 77 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SNOW` | 78 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ICE` | 79 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SNOW_BLOCK` | 80 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CACTUS` | 81 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CLAY` | 82 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SUGAR_CANE_BLOCK` | 83 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `JUKEBOX` | 84 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FENCE` | 85 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PUMPKIN` | 86 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `NETHERRACK` | 87 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SOUL_SAND` | 88 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GLOWSTONE` | 89 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PORTAL` | 90 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `JACK_O_LANTERN` | 91 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CAKE_BLOCK` | 92 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIODE_BLOCK_OFF` | 93 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIODE_BLOCK_ON` | 94 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LOCKED_CHEST` | 95 | V1_8_R1 (1.8 - 1.8.2) | V1_8_R1 (1.8 - 1.8.2) |
| `STAINED_GLASS` | 95 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `TRAP_DOOR` | 96 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MONSTER_EGGS` | 97 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SMOOTH_BRICK` | 98 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `HUGE_MUSHROOM_1` | 99 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `HUGE_MUSHROOM_2` | 100 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_FENCE` | 101 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `THIN_GLASS` | 102 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MELON_BLOCK` | 103 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PUMPKIN_STEM` | 104 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MELON_STEM` | 105 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `VINE` | 106 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FENCE_GATE` | 107 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BRICK_STAIRS` | 108 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SMOOTH_STAIRS` | 109 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MYCEL` | 110 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WATER_LILY` | 111 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `NETHER_BRICK` | 112 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `NETHER_FENCE` | 113 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `NETHER_BRICK_STAIRS` | 114 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `NETHER_WARTS` | 115 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ENCHANTMENT_TABLE` | 116 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BREWING_STAND` | 117 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CAULDRON` | 118 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ENDER_PORTAL` | 119 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ENDER_PORTAL_FRAME` | 120 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ENDER_STONE` | 121 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DRAGON_EGG` | 122 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE_LAMP_OFF` | 123 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE_LAMP_ON` | 124 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_DOUBLE_STEP` | 125 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_STEP` | 126 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COCOA` | 127 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SANDSTONE_STAIRS` | 128 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `EMERALD_ORE` | 129 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ENDER_CHEST` | 130 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `TRIPWIRE_HOOK` | 131 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `TRIPWIRE` | 132 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `EMERALD_BLOCK` | 133 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SPRUCE_WOOD_STAIRS` | 134 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BIRCH_WOOD_STAIRS` | 135 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `JUNGLE_WOOD_STAIRS` | 136 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COMMAND` | 137 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BEACON` | 138 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COBBLE_WALL` | 139 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FLOWER_POT` | 140 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CARROT` | 141 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `POTATO` | 142 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_BUTTON` | 143 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SKULL` | 144 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ANVIL` | 145 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `TRAPPED_CHEST` | 146 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_PLATE` | 147 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_PLATE` | 148 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE_COMPARATOR_OFF` | 149 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE_COMPARATOR_ON` | 150 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DAYLIGHT_DETECTOR` | 151 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE_BLOCK` | 152 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `QUARTZ_ORE` | 153 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `HOPPER` | 154 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `QUARTZ_BLOCK` | 155 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `QUARTZ_STAIRS` | 156 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ACTIVATOR_RAIL` | 157 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DROPPER` | 158 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STAINED_CLAY` | 159 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STAINED_GLASS_PANE` | 160 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LEAVES_2` | 161 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LOG_2` | 162 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ACACIA_STAIRS` | 163 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DARK_OAK_STAIRS` | 164 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SLIME_BLOCK` | 165 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BARRIER` | 166 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_TRAPDOOR` | 167 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PRISMARINE` | 168 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SEA_LANTERN` | 169 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `HAY_BLOCK` | 170 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CARPET` | 171 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `HARD_CLAY` | 172 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COAL_BLOCK` | 173 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PACKED_ICE` | 174 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DOUBLE_PLANT` | 175 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STANDING_BANNER` | 176 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WALL_BANNER` | 177 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DAYLIGHT_DETECTOR_INVERTED` | 178 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RED_SANDSTONE` | 179 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RED_SANDSTONE_STAIRS` | 180 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DOUBLE_STONE_SLAB2` | 181 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STONE_SLAB2` | 182 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SPRUCE_FENCE_GATE` | 183 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BIRCH_FENCE_GATE` | 184 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `JUNGLE_FENCE_GATE` | 185 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DARK_OAK_FENCE_GATE` | 186 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ACACIA_FENCE_GATE` | 187 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SPRUCE_FENCE` | 188 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BIRCH_FENCE` | 189 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `JUNGLE_FENCE` | 190 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DARK_OAK_FENCE` | 191 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ACACIA_FENCE` | 192 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SPRUCE_DOOR` | 193 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BIRCH_DOOR` | 194 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `JUNGLE_DOOR` | 195 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ACACIA_DOOR` | 196 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DARK_OAK_DOOR` | 197 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `END_ROD` | 198 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `CHORUS_PLANT` | 199 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `CHORUS_FLOWER` | 200 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `PURPUR_BLOCK` | 201 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `PURPUR_PILLAR` | 202 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `PURPUR_STAIRS` | 203 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `PURPUR_DOUBLE_SLAB` | 204 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `PURPUR_SLAB` | 205 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `END_BRICKS` | 206 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `BEETROOT_BLOCK` | 207 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `GRASS_PATH` | 208 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `END_GATEWAY` | 209 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `COMMAND_REPEATING` | 210 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `COMMAND_CHAIN` | 211 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `FROSTED_ICE` | 212 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `MAGMA` | 213 | V1_10_R1 (1.10.x) | V1_12_R1 (1.12.x) |
| `NETHER_WART_BLOCK` | 214 | V1_10_R1 (1.10.x) | V1_12_R1 (1.12.x) |
| `RED_NETHER_BRICK` | 215 | V1_10_R1 (1.10.x) | V1_12_R1 (1.12.x) |
| `BONE_BLOCK` | 216 | V1_10_R1 (1.10.x) | V1_12_R1 (1.12.x) |
| `STRUCTURE_VOID` | 217 | V1_10_R1 (1.10.x) | V1_12_R1 (1.12.x) |
| `OBSERVER` | 218 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `WHITE_SHULKER_BOX` | 219 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `ORANGE_SHULKER_BOX` | 220 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `MAGENTA_SHULKER_BOX` | 221 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `LIGHT_BLUE_SHULKER_BOX` | 222 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `YELLOW_SHULKER_BOX` | 223 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `LIME_SHULKER_BOX` | 224 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `PINK_SHULKER_BOX` | 225 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `GRAY_SHULKER_BOX` | 226 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `SILVER_SHULKER_BOX` | 227 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `CYAN_SHULKER_BOX` | 228 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `PURPLE_SHULKER_BOX` | 229 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `BLUE_SHULKER_BOX` | 230 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `BROWN_SHULKER_BOX` | 231 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `GREEN_SHULKER_BOX` | 232 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `RED_SHULKER_BOX` | 233 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `BLACK_SHULKER_BOX` | 234 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `WHITE_GLAZED_TERRACOTTA` | 235 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `ORANGE_GLAZED_TERRACOTTA` | 236 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `MAGENTA_GLAZED_TERRACOTTA` | 237 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `LIGHT_BLUE_GLAZED_TERRACOTTA` | 238 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `YELLOW_GLAZED_TERRACOTTA` | 239 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `LIME_GLAZED_TERRACOTTA` | 240 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `PINK_GLAZED_TERRACOTTA` | 241 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `GRAY_GLAZED_TERRACOTTA` | 242 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `SILVER_GLAZED_TERRACOTTA` | 243 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `CYAN_GLAZED_TERRACOTTA` | 244 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `PURPLE_GLAZED_TERRACOTTA` | 245 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `BLUE_GLAZED_TERRACOTTA` | 246 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `BROWN_GLAZED_TERRACOTTA` | 247 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `GREEN_GLAZED_TERRACOTTA` | 248 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `RED_GLAZED_TERRACOTTA` | 249 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `BLACK_GLAZED_TERRACOTTA` | 250 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `CONCRETE` | 251 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `CONCRETE_POWDER` | 252 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `STRUCTURE_BLOCK` | 255 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `IRON_SPADE` | 256 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_PICKAXE` | 257 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_AXE` | 258 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FLINT_AND_STEEL` | 259 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `APPLE` | 260 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BOW` | 261 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ARROW` | 262 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COAL` | 263 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND` | 264 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_INGOT` | 265 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_INGOT` | 266 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_SWORD` | 267 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_SWORD` | 268 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_SPADE` | 269 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_PICKAXE` | 270 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_AXE` | 271 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STONE_SWORD` | 272 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STONE_SPADE` | 273 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STONE_PICKAXE` | 274 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STONE_AXE` | 275 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_SWORD` | 276 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_SPADE` | 277 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_PICKAXE` | 278 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_AXE` | 279 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STICK` | 280 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BOWL` | 281 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MUSHROOM_SOUP` | 282 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_SWORD` | 283 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_SPADE` | 284 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_PICKAXE` | 285 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_AXE` | 286 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STRING` | 287 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FEATHER` | 288 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SULPHUR` | 289 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_HOE` | 290 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STONE_HOE` | 291 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_HOE` | 292 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_HOE` | 293 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_HOE` | 294 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SEEDS` | 295 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WHEAT` | 296 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BREAD` | 297 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LEATHER_HELMET` | 298 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LEATHER_CHESTPLATE` | 299 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LEATHER_LEGGINGS` | 300 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LEATHER_BOOTS` | 301 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CHAINMAIL_HELMET` | 302 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CHAINMAIL_CHESTPLATE` | 303 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CHAINMAIL_LEGGINGS` | 304 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CHAINMAIL_BOOTS` | 305 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_HELMET` | 306 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_CHESTPLATE` | 307 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_LEGGINGS` | 308 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_BOOTS` | 309 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_HELMET` | 310 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_CHESTPLATE` | 311 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_LEGGINGS` | 312 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_BOOTS` | 313 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_HELMET` | 314 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_CHESTPLATE` | 315 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_LEGGINGS` | 316 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_BOOTS` | 317 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FLINT` | 318 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PORK` | 319 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GRILLED_PORK` | 320 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PAINTING` | 321 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLDEN_APPLE` | 322 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SIGN` | 323 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WOOD_DOOR` | 324 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BUCKET` | 325 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WATER_BUCKET` | 326 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LAVA_BUCKET` | 327 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MINECART` | 328 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SADDLE` | 329 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_DOOR` | 330 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE` | 331 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SNOW_BALL` | 332 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BOAT` | 333 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LEATHER` | 334 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MILK_BUCKET` | 335 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CLAY_BRICK` | 336 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CLAY_BALL` | 337 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SUGAR_CANE` | 338 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PAPER` | 339 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BOOK` | 340 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SLIME_BALL` | 341 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `STORAGE_MINECART` | 342 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `POWERED_MINECART` | 343 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `EGG` | 344 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COMPASS` | 345 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FISHING_ROD` | 346 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WATCH` | 347 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GLOWSTONE_DUST` | 348 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RAW_FISH` | 349 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COOKED_FISH` | 350 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `INK_SACK` | 351 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BONE` | 352 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SUGAR` | 353 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CAKE` | 354 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BED` | 355 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIODE` | 356 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COOKIE` | 357 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MAP` | 358 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SHEARS` | 359 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MELON` | 360 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PUMPKIN_SEEDS` | 361 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MELON_SEEDS` | 362 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RAW_BEEF` | 363 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COOKED_BEEF` | 364 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RAW_CHICKEN` | 365 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COOKED_CHICKEN` | 366 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ROTTEN_FLESH` | 367 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ENDER_PEARL` | 368 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BLAZE_ROD` | 369 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GHAST_TEAR` | 370 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_NUGGET` | 371 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `NETHER_STALK` | 372 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `POTION` | 373 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GLASS_BOTTLE` | 374 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SPIDER_EYE` | 375 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FERMENTED_SPIDER_EYE` | 376 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BLAZE_POWDER` | 377 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MAGMA_CREAM` | 378 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BREWING_STAND_ITEM` | 379 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CAULDRON_ITEM` | 380 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `EYE_OF_ENDER` | 381 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SPECKLED_MELON` | 382 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MONSTER_EGG` | 383 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `EXP_BOTTLE` | 384 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FIREBALL` | 385 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BOOK_AND_QUILL` | 386 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `WRITTEN_BOOK` | 387 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `EMERALD` | 388 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ITEM_FRAME` | 389 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FLOWER_POT_ITEM` | 390 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CARROT_ITEM` | 391 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `POTATO_ITEM` | 392 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BAKED_POTATO` | 393 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `POISONOUS_POTATO` | 394 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `EMPTY_MAP` | 395 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLDEN_CARROT` | 396 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `SKULL_ITEM` | 397 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CARROT_STICK` | 398 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `NETHER_STAR` | 399 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PUMPKIN_PIE` | 400 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FIREWORK` | 401 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `FIREWORK_CHARGE` | 402 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ENCHANTED_BOOK` | 403 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `REDSTONE_COMPARATOR` | 404 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `NETHER_BRICK_ITEM` | 405 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `QUARTZ` | 406 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `EXPLOSIVE_MINECART` | 407 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `HOPPER_MINECART` | 408 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PRISMARINE_SHARD` | 409 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `PRISMARINE_CRYSTALS` | 410 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RABBIT` | 411 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COOKED_RABBIT` | 412 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RABBIT_STEW` | 413 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RABBIT_FOOT` | 414 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RABBIT_HIDE` | 415 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ARMOR_STAND` | 416 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `IRON_BARDING` | 417 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GOLD_BARDING` | 418 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DIAMOND_BARDING` | 419 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `LEASH` | 420 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `NAME_TAG` | 421 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COMMAND_MINECART` | 422 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `MUTTON` | 423 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `COOKED_MUTTON` | 424 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BANNER` | 425 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `END_CRYSTAL` | 426 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `SPRUCE_DOOR_ITEM` | 427 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `BIRCH_DOOR_ITEM` | 428 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `JUNGLE_DOOR_ITEM` | 429 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `ACACIA_DOOR_ITEM` | 430 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `DARK_OAK_DOOR_ITEM` | 431 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `CHORUS_FRUIT` | 432 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `CHORUS_FRUIT_POPPED` | 433 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `BEETROOT` | 434 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `BEETROOT_SEEDS` | 435 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `BEETROOT_SOUP` | 436 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `DRAGONS_BREATH` | 437 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `SPLASH_POTION` | 438 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `SPECTRAL_ARROW` | 439 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `TIPPED_ARROW` | 440 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `LINGERING_POTION` | 441 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `SHIELD` | 442 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `ELYTRA` | 443 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `BOAT_SPRUCE` | 444 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `BOAT_BIRCH` | 445 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `BOAT_JUNGLE` | 446 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `BOAT_ACACIA` | 447 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `BOAT_DARK_OAK` | 448 | V1_9_R1 (1.9 - 1.9.3) | V1_12_R1 (1.12.x) |
| `TOTEM` | 449 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `SHULKER_SHELL` | 450 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `IRON_NUGGET` | 452 | V1_11_R1 (1.11.x) | V1_12_R1 (1.12.x) |
| `KNOWLEDGE_BOOK` | 453 | V1_12_R1 (1.12.x) | V1_12_R1 (1.12.x) |
| `GOLD_RECORD` | 2256 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `GREEN_RECORD` | 2257 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RECORD_3` | 2258 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RECORD_4` | 2259 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RECORD_5` | 2260 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RECORD_6` | 2261 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RECORD_7` | 2262 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RECORD_8` | 2263 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RECORD_9` | 2264 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RECORD_10` | 2265 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RECORD_11` | 2266 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |
| `RECORD_12` | 2267 | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 (1.12.x) |

## Appendix B: 1.13 - 26.3 names

Without `LEGACY_` names. A last revision other than `V26_3` means removed (its successor in Note).

| Name | First revision | Last revision | Note |
|---|---|---|---|
| `ABANDONED_CAMP_MAP` | V26_3 (26.3+) | V26_3 |  |
| `ACACIA_BOAT` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_BUTTON` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_CHEST_BOAT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `ACACIA_DOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_FENCE` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_FENCE_GATE` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `ACACIA_LEAVES` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_PLANKS` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_PRESSURE_PLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `ACACIA_SIGN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `ACACIA_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_TRAPDOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `ACACIA_WALL_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `ACACIA_WALL_SIGN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `ACACIA_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `ACTIVATOR_RAIL` | V1_13_R1 (1.13) | V26_3 |  |
| `AIR` | V1_13_R1 (1.13) | V26_3 |  |
| `ALLAY_SPAWN_EGG` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `ALLIUM` | V1_13_R1 (1.13) | V26_3 |  |
| `AMETHYST_BLOCK` | V1_17_R1 (1.17.x) | V26_3 |  |
| `AMETHYST_CLUSTER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `AMETHYST_SHARD` | V1_17_R1 (1.17.x) | V26_3 |  |
| `ANCIENT_DEBRIS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `ANDESITE` | V1_13_R1 (1.13) | V26_3 |  |
| `ANDESITE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `ANDESITE_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `ANDESITE_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `ANGLER_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `ANVIL` | V1_13_R1 (1.13) | V26_3 |  |
| `APPLE` | V1_13_R1 (1.13) | V26_3 |  |
| `ARCHER_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 | ← `POTTERY_SHARD_ARCHER` |
| `ARMADILLO_SCUTE` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `ARMADILLO_SPAWN_EGG` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `ARMOR_STAND` | V1_13_R1 (1.13) | V26_3 |  |
| `ARMS_UP_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 | ← `POTTERY_SHARD_ARMS_UP` |
| `ARROW` | V1_13_R1 (1.13) | V26_3 |  |
| `ATTACHED_MELON_STEM` | V1_13_R1 (1.13) | V26_3 |  |
| `ATTACHED_PUMPKIN_STEM` | V1_13_R1 (1.13) | V26_3 |  |
| `AXOLOTL_BUCKET` | V1_17_R1 (1.17.x) | V26_3 |  |
| `AXOLOTL_SPAWN_EGG` | V1_17_R1 (1.17.x) | V26_3 |  |
| `AZALEA` | V1_17_R1 (1.17.x) | V26_3 |  |
| `AZALEA_LEAVES` | V1_17_R1 (1.17.x) | V26_3 |  |
| `AZURE_BLUET` | V1_13_R1 (1.13) | V26_3 |  |
| `BAKED_POTATO` | V1_13_R1 (1.13) | V26_3 |  |
| `BAMBOO` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BAMBOO_BLOCK` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_BUTTON` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_CHEST_RAFT` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_DOOR` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_FENCE` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_FENCE_GATE` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_MOSAIC` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_MOSAIC_SLAB` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_MOSAIC_STAIRS` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_PLANKS` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_PRESSURE_PLATE` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_RAFT` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_SAPLING` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BAMBOO_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `BAMBOO_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_SLAB` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_STAIRS` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_TRAPDOOR` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_WALL_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BAMBOO_WALL_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BARREL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BARRIER` | V1_13_R1 (1.13) | V26_3 |  |
| `BASALT` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `BAT_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `BEACON` | V1_13_R1 (1.13) | V26_3 |  |
| `BEDROCK` | V1_13_R1 (1.13) | V26_3 |  |
| `BEEF` | V1_13_R1 (1.13) | V26_3 |  |
| `BEEHIVE` | V1_15_R1 (1.15.x) | V26_3 |  |
| `BEETROOT` | V1_13_R1 (1.13) | V26_3 |  |
| `BEETROOTS` | V1_13_R1 (1.13) | V26_3 |  |
| `BEETROOT_SEEDS` | V1_13_R1 (1.13) | V26_3 |  |
| `BEETROOT_SOUP` | V1_13_R1 (1.13) | V26_3 |  |
| `BEE_NEST` | V1_15_R1 (1.15.x) | V26_3 |  |
| `BEE_SPAWN_EGG` | V1_15_R1 (1.15.x) | V26_3 |  |
| `BELL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BIG_DRIPLEAF` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BIG_DRIPLEAF_STEM` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BIRCH_BOAT` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_BUTTON` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_CHEST_BOAT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `BIRCH_DOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_FENCE` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_FENCE_GATE` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BIRCH_LEAVES` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_PLANKS` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_PRESSURE_PLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `BIRCH_SIGN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BIRCH_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_TRAPDOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `BIRCH_WALL_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `BIRCH_WALL_SIGN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BIRCH_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACKSTONE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `BLACKSTONE_SLAB` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `BLACKSTONE_STAIRS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `BLACKSTONE_WALL` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `BLACK_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `BLACK_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BLACK_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BLACK_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `BLACK_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `BLACK_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `BLACK_DYE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BLACK_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `BLACK_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `BLACK_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `BLACK_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `BLADE_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `BLAST_FURNACE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BLAZE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `BLAZE_ROD` | V1_13_R1 (1.13) | V26_3 |  |
| `BLAZE_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `BLUE_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BLUE_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BLUE_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `BLUE_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `BLUE_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `BLUE_DYE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BLUE_EGG` | V1_21_R4 (1.21.5) | V26_3 |  |
| `BLUE_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `BLUE_ICE` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_ORCHID` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `BLUE_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `BLUE_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `BOGGED_SPAWN_EGG` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `BOLT_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `BONE` | V1_13_R1 (1.13) | V26_3 |  |
| `BONE_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `BONE_MEAL` | V1_13_R1 (1.13) | V26_3 |  |
| `BOOK` | V1_13_R1 (1.13) | V26_3 |  |
| `BOOKSHELF` | V1_13_R1 (1.13) | V26_3 |  |
| `BORDURE_INDENTED_BANNER_PATTERN` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `BOW` | V1_13_R1 (1.13) | V26_3 |  |
| `BOWL` | V1_13_R1 (1.13) | V26_3 |  |
| `BRAIN_CORAL` | V1_13_R1 (1.13) | V26_3 |  |
| `BRAIN_CORAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `BRAIN_CORAL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `BRAIN_CORAL_WALL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `BREAD` | V1_13_R1 (1.13) | V26_3 |  |
| `BREEZE_ROD` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `BREEZE_SPAWN_EGG` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `BREWER_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `BREWING_STAND` | V1_13_R1 (1.13) | V26_3 |  |
| `BRICK` | V1_13_R1 (1.13) | V26_3 |  |
| `BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `BRICK_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `BRICK_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `BRICK_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BROWN_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `BROWN_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BROWN_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BROWN_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `BROWN_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `BROWN_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `BROWN_DYE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `BROWN_EGG` | V1_21_R4 (1.21.5) | V26_3 |  |
| `BROWN_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `BROWN_MUSHROOM` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_MUSHROOM_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `BROWN_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `BROWN_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `BRUSH` | V1_19_R3 (1.19.4) | V26_3 |  |
| `BUBBLE_COLUMN` | V1_13_R1 (1.13) | V26_3 |  |
| `BUBBLE_CORAL` | V1_13_R1 (1.13) | V26_3 |  |
| `BUBBLE_CORAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `BUBBLE_CORAL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `BUBBLE_CORAL_WALL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `BUCKET` | V1_13_R1 (1.13) | V26_3 |  |
| `BUDDING_AMETHYST` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BUNDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BURIED_ANCIENT_CITY_MAP` | V26_3 (26.3+) | V26_3 |  |
| `BURIED_MINESHAFT_MAP` | V26_3 (26.3+) | V26_3 |  |
| `BURIED_TREASURE_MAP` | V26_3 (26.3+) | V26_3 |  |
| `BURIED_TRIAL_CHAMBERS_MAP` | V26_3 (26.3+) | V26_3 |  |
| `BURN_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `BUSH` | V1_21_R4 (1.21.5) | V26_3 |  |
| `CACTUS` | V1_13_R1 (1.13) | V26_3 |  |
| `CACTUS_FLOWER` | V1_21_R4 (1.21.5) | V26_3 |  |
| `CACTUS_GREEN` | V1_13_R1 (1.13) | V1_13_R2 | → `GREEN_DYE` (1.14) |
| `CAKE` | V1_13_R1 (1.13) | V26_3 |  |
| `CALCITE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CALIBRATED_SCULK_SENSOR` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `CAMEL_HUSK_SPAWN_EGG` | V1_21_R7 (1.21.11) | V26_3 |  |
| `CAMEL_SPAWN_EGG` | V1_19_R2 (1.19.3) | V26_3 |  |
| `CAMPFIRE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CARROT` | V1_13_R1 (1.13) | V26_3 |  |
| `CARROTS` | V1_13_R1 (1.13) | V26_3 |  |
| `CARROT_ON_A_STICK` | V1_13_R1 (1.13) | V26_3 |  |
| `CARTOGRAPHY_TABLE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `CARVED_PUMPKIN` | V1_13_R1 (1.13) | V26_3 |  |
| `CAT_SPAWN_EGG` | V1_14_R1 (1.14.x) | V26_3 |  |
| `CAULDRON` | V1_13_R1 (1.13) | V26_3 |  |
| `CAVE_AIR` | V1_13_R1 (1.13) | V26_3 |  |
| `CAVE_SPIDER_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `CAVE_VINES` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CAVE_VINES_PLANT` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CHAIN` | V1_16_R1 (1.16 - 1.16.1) | V1_21_R5 | → `IRON_CHAIN` (1.21.9) |
| `CHAINMAIL_BOOTS` | V1_13_R1 (1.13) | V26_3 |  |
| `CHAINMAIL_CHESTPLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `CHAINMAIL_HELMET` | V1_13_R1 (1.13) | V26_3 |  |
| `CHAINMAIL_LEGGINGS` | V1_13_R1 (1.13) | V26_3 |  |
| `CHAIN_COMMAND_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `CHARCOAL` | V1_13_R1 (1.13) | V26_3 |  |
| `CHERRY_BOAT` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_BUTTON` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_CHEST_BOAT` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_DOOR` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_FENCE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_FENCE_GATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_HANGING_SIGN` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_LEAVES` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_LOG` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_PLANKS` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_PRESSURE_PLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_SAPLING` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `CHERRY_SIGN` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_SLAB` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_STAIRS` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_TRAPDOOR` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_WALL_HANGING_SIGN` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_WALL_SIGN` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHERRY_WOOD` | V1_19_R3 (1.19.4) | V26_3 |  |
| `CHEST` | V1_13_R1 (1.13) | V26_3 |  |
| `CHEST_MINECART` | V1_13_R1 (1.13) | V26_3 |  |
| `CHICKEN` | V1_13_R1 (1.13) | V26_3 |  |
| `CHICKEN_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `CHIPPED_ANVIL` | V1_13_R1 (1.13) | V26_3 |  |
| `CHISELED_BOOKSHELF` | V1_19_R2 (1.19.3) | V26_3 |  |
| `CHISELED_CINNABAR` | V26_2 (26.2.x) | V26_3 |  |
| `CHISELED_COPPER` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `CHISELED_DEEPSLATE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CHISELED_NETHER_BRICKS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CHISELED_POLISHED_BLACKSTONE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CHISELED_QUARTZ_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `CHISELED_RED_SANDSTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `CHISELED_RESIN_BRICKS` | V1_21_R3 (1.21.4) | V26_3 |  |
| `CHISELED_SANDSTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `CHISELED_STONE_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `CHISELED_SULFUR` | V26_2 (26.2.x) | V26_3 |  |
| `CHISELED_TUFF` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `CHISELED_TUFF_BRICKS` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `CHORUS_FLOWER` | V1_13_R1 (1.13) | V26_3 |  |
| `CHORUS_FRUIT` | V1_13_R1 (1.13) | V26_3 |  |
| `CHORUS_PLANT` | V1_13_R1 (1.13) | V26_3 |  |
| `CINNABAR` | V26_2 (26.2.x) | V26_3 |  |
| `CINNABAR_BRICKS` | V26_2 (26.2.x) | V26_3 |  |
| `CINNABAR_BRICK_SLAB` | V26_2 (26.2.x) | V26_3 |  |
| `CINNABAR_BRICK_STAIRS` | V26_2 (26.2.x) | V26_3 |  |
| `CINNABAR_BRICK_WALL` | V26_2 (26.2.x) | V26_3 |  |
| `CINNABAR_SLAB` | V26_2 (26.2.x) | V26_3 |  |
| `CINNABAR_STAIRS` | V26_2 (26.2.x) | V26_3 |  |
| `CINNABAR_WALL` | V26_2 (26.2.x) | V26_3 |  |
| `CLAY` | V1_13_R1 (1.13) | V26_3 |  |
| `CLAY_BALL` | V1_13_R1 (1.13) | V26_3 |  |
| `CLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `CLOSED_EYEBLOSSOM` | V1_21_R3 (1.21.4) | V26_3 |  |
| `COAL` | V1_13_R1 (1.13) | V26_3 |  |
| `COAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `COAL_ORE` | V1_13_R1 (1.13) | V26_3 |  |
| `COARSE_DIRT` | V1_13_R1 (1.13) | V26_3 |  |
| `COAST_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `COBBLED_DEEPSLATE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `COBBLED_DEEPSLATE_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `COBBLED_DEEPSLATE_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `COBBLED_DEEPSLATE_WALL` | V1_17_R1 (1.17.x) | V26_3 |  |
| `COBBLESTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `COBBLESTONE_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `COBBLESTONE_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `COBBLESTONE_WALL` | V1_13_R1 (1.13) | V26_3 |  |
| `COBWEB` | V1_13_R1 (1.13) | V26_3 |  |
| `COCOA` | V1_13_R1 (1.13) | V26_3 |  |
| `COCOA_BEANS` | V1_13_R1 (1.13) | V26_3 |  |
| `COD` | V1_13_R1 (1.13) | V26_3 |  |
| `COD_BUCKET` | V1_13_R1 (1.13) | V26_3 |  |
| `COD_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `COMMAND_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `COMMAND_BLOCK_MINECART` | V1_13_R1 (1.13) | V26_3 |  |
| `COMPARATOR` | V1_13_R1 (1.13) | V26_3 |  |
| `COMPASS` | V1_13_R1 (1.13) | V26_3 |  |
| `COMPOSTER` | V1_14_R1 (1.14.x) | V26_3 |  |
| `CONDUIT` | V1_13_R1 (1.13) | V26_3 |  |
| `COOKED_BEEF` | V1_13_R1 (1.13) | V26_3 |  |
| `COOKED_CHICKEN` | V1_13_R1 (1.13) | V26_3 |  |
| `COOKED_COD` | V1_13_R1 (1.13) | V26_3 |  |
| `COOKED_MUTTON` | V1_13_R1 (1.13) | V26_3 |  |
| `COOKED_PORKCHOP` | V1_13_R1 (1.13) | V26_3 |  |
| `COOKED_RABBIT` | V1_13_R1 (1.13) | V26_3 |  |
| `COOKED_SALMON` | V1_13_R1 (1.13) | V26_3 |  |
| `COOKIE` | V1_13_R1 (1.13) | V26_3 |  |
| `COPPER_AXE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_BARS` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_BLOCK` | V1_17_R1 (1.17.x) | V26_3 |  |
| `COPPER_BOOTS` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_BULB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `COPPER_CHAIN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_CHEST` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_CHESTPLATE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_DOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `COPPER_GOLEM_SPAWN_EGG` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_GOLEM_STATUE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_GRATE` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `COPPER_HELMET` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_HOE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_HORSE_ARMOR` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_INGOT` | V1_17_R1 (1.17.x) | V26_3 |  |
| `COPPER_LANTERN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_LEGGINGS` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_NAUTILUS_ARMOR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `COPPER_NUGGET` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_ORE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `COPPER_PICKAXE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_SHOVEL` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_SPEAR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `COPPER_SWORD` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_TORCH` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `COPPER_TRAPDOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `COPPER_WALL_TORCH` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `CORNFLOWER` | V1_14_R1 (1.14.x) | V26_3 |  |
| `COW_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `CRACKED_DEEPSLATE_BRICKS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CRACKED_DEEPSLATE_TILES` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CRACKED_NETHER_BRICKS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRACKED_POLISHED_BLACKSTONE_BRICKS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRACKED_STONE_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `CRAFTER` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `CRAFTING_TABLE` | V1_13_R1 (1.13) | V26_3 |  |
| `CREAKING_HEART` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `CREAKING_SPAWN_EGG` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `CREEPER_BANNER_PATTERN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `CREEPER_HEAD` | V1_13_R1 (1.13) | V26_3 |  |
| `CREEPER_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `CREEPER_WALL_HEAD` | V1_13_R1 (1.13) | V26_3 |  |
| `CRIMSON_BUTTON` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_DOOR` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_FENCE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_FENCE_GATE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_FUNGUS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `CRIMSON_HYPHAE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_NYLIUM` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_PLANKS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_PRESSURE_PLATE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_ROOTS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `CRIMSON_SIGN` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_SLAB` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_STAIRS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_STEM` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_TRAPDOOR` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CRIMSON_WALL_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `CRIMSON_WALL_SIGN` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CROSSBOW` | V1_14_R1 (1.14.x) | V26_3 |  |
| `CRYING_OBSIDIAN` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `CUT_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CUT_COPPER_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CUT_COPPER_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CUT_RED_SANDSTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `CUT_RED_SANDSTONE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `CUT_SANDSTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `CUT_SANDSTONE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `CYAN_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `CYAN_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CYAN_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `CYAN_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `CYAN_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `CYAN_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `CYAN_DYE` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `CYAN_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `CYAN_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `CYAN_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `DAMAGED_ANVIL` | V1_13_R1 (1.13) | V26_3 |  |
| `DANDELION` | V1_13_R1 (1.13) | V26_3 |  |
| `DANDELION_YELLOW` | V1_13_R1 (1.13) | V1_13_R2 | → `YELLOW_DYE` (1.14) |
| `DANGER_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `DARK_OAK_BOAT` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_BUTTON` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_CHEST_BOAT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `DARK_OAK_DOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_FENCE` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_FENCE_GATE` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `DARK_OAK_LEAVES` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_PLANKS` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_PRESSURE_PLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `DARK_OAK_SIGN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `DARK_OAK_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_TRAPDOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_OAK_WALL_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `DARK_OAK_WALL_SIGN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `DARK_OAK_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_PRISMARINE` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_PRISMARINE_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `DARK_PRISMARINE_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `DAYLIGHT_DETECTOR` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_BRAIN_CORAL` | V1_13_R2 (1.13.1 - 1.13.2) | V26_3 |  |
| `DEAD_BRAIN_CORAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_BRAIN_CORAL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_BRAIN_CORAL_WALL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_BUBBLE_CORAL` | V1_13_R2 (1.13.1 - 1.13.2) | V26_3 |  |
| `DEAD_BUBBLE_CORAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_BUBBLE_CORAL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_BUBBLE_CORAL_WALL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_BUSH` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_FIRE_CORAL` | V1_13_R2 (1.13.1 - 1.13.2) | V26_3 |  |
| `DEAD_FIRE_CORAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_FIRE_CORAL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_FIRE_CORAL_WALL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_HORN_CORAL` | V1_13_R2 (1.13.1 - 1.13.2) | V26_3 |  |
| `DEAD_HORN_CORAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_HORN_CORAL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_HORN_CORAL_WALL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_TUBE_CORAL` | V1_13_R2 (1.13.1 - 1.13.2) | V26_3 |  |
| `DEAD_TUBE_CORAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_TUBE_CORAL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `DEAD_TUBE_CORAL_WALL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `DEBUG_STICK` | V1_13_R1 (1.13) | V26_3 |  |
| `DECORATED_POT` | V1_19_R3 (1.19.4) | V26_3 |  |
| `DEEPSLATE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_BRICKS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_BRICK_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_BRICK_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_BRICK_WALL` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_COAL_ORE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_COPPER_ORE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_DIAMOND_ORE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_EMERALD_ORE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_GOLD_ORE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_IRON_ORE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_LAPIS_ORE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_REDSTONE_ORE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_TILES` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_TILE_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_TILE_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DEEPSLATE_TILE_WALL` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DESERT_PYRAMID_MAP` | V26_3 (26.3+) | V26_3 |  |
| `DESERT_VILLAGE_MAP` | V26_3 (26.3+) | V26_3 |  |
| `DETECTOR_RAIL` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_AXE` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_BOOTS` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_CHESTPLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_HELMET` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_HOE` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_HORSE_ARMOR` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_LEGGINGS` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_NAUTILUS_ARMOR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `DIAMOND_ORE` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_PICKAXE` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_SHOVEL` | V1_13_R1 (1.13) | V26_3 |  |
| `DIAMOND_SPEAR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `DIAMOND_SWORD` | V1_13_R1 (1.13) | V26_3 |  |
| `DIORITE` | V1_13_R1 (1.13) | V26_3 |  |
| `DIORITE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `DIORITE_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `DIORITE_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `DIRT` | V1_13_R1 (1.13) | V26_3 |  |
| `DIRT_PATH` | V1_17_R1 (1.17.x) | V26_3 | ← `GRASS_PATH` |
| `DISC_FRAGMENT_5` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `DISPENSER` | V1_13_R1 (1.13) | V26_3 |  |
| `DOLPHIN_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `DONKEY_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `DRAGON_BREATH` | V1_13_R1 (1.13) | V26_3 |  |
| `DRAGON_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `DRAGON_HEAD` | V1_13_R1 (1.13) | V26_3 |  |
| `DRAGON_WALL_HEAD` | V1_13_R1 (1.13) | V26_3 |  |
| `DRIED_GHAST` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `DRIED_KELP` | V1_13_R1 (1.13) | V26_3 |  |
| `DRIED_KELP_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `DRIPSTONE_BLOCK` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DROPPER` | V1_13_R1 (1.13) | V26_3 |  |
| `DROWNED_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `DUNE_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `ECHO_SHARD` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `ELDER_GUARDIAN_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `ELYTRA` | V1_13_R1 (1.13) | V26_3 |  |
| `EMERALD` | V1_13_R1 (1.13) | V26_3 |  |
| `EMERALD_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `EMERALD_ORE` | V1_13_R1 (1.13) | V26_3 |  |
| `ENCHANTED_BOOK` | V1_13_R1 (1.13) | V26_3 |  |
| `ENCHANTED_GOLDEN_APPLE` | V1_13_R1 (1.13) | V26_3 |  |
| `ENCHANTING_TABLE` | V1_13_R1 (1.13) | V26_3 |  |
| `ENDERMAN_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `ENDERMITE_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `ENDER_CHEST` | V1_13_R1 (1.13) | V26_3 |  |
| `ENDER_DRAGON_SPAWN_EGG` | V1_19_R2 (1.19.3) | V26_3 |  |
| `ENDER_EYE` | V1_13_R1 (1.13) | V26_3 |  |
| `ENDER_PEARL` | V1_13_R1 (1.13) | V26_3 |  |
| `END_CRYSTAL` | V1_13_R1 (1.13) | V26_3 |  |
| `END_GATEWAY` | V1_13_R1 (1.13) | V26_3 |  |
| `END_PORTAL` | V1_13_R1 (1.13) | V26_3 |  |
| `END_PORTAL_FRAME` | V1_13_R1 (1.13) | V26_3 |  |
| `END_ROD` | V1_13_R1 (1.13) | V26_3 |  |
| `END_STONE` | V1_13_R1 (1.13) | V26_3 |  |
| `END_STONE_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `END_STONE_BRICK_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `END_STONE_BRICK_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `END_STONE_BRICK_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `EVOKER_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `EXPERIENCE_BOTTLE` | V1_13_R1 (1.13) | V26_3 |  |
| `EXPLORER_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `EXPOSED_CHISELED_COPPER` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `EXPOSED_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `EXPOSED_COPPER_BARS` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `EXPOSED_COPPER_BULB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `EXPOSED_COPPER_CHAIN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `EXPOSED_COPPER_CHEST` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `EXPOSED_COPPER_DOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `EXPOSED_COPPER_GOLEM_STATUE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `EXPOSED_COPPER_GRATE` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `EXPOSED_COPPER_LANTERN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `EXPOSED_COPPER_TRAPDOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `EXPOSED_CUT_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `EXPOSED_CUT_COPPER_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `EXPOSED_CUT_COPPER_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `EXPOSED_LIGHTNING_ROD` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `EYE_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `FARMLAND` | V1_13_R1 (1.13) | V26_3 |  |
| `FEATHER` | V1_13_R1 (1.13) | V26_3 |  |
| `FERMENTED_SPIDER_EYE` | V1_13_R1 (1.13) | V26_3 |  |
| `FERN` | V1_13_R1 (1.13) | V26_3 |  |
| `FIELD_MASONED_BANNER_PATTERN` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `FILLED_MAP` | V1_13_R1 (1.13) | V26_3 |  |
| `FIRE` | V1_13_R1 (1.13) | V26_3 |  |
| `FIREFLY_BUSH` | V1_21_R4 (1.21.5) | V26_3 |  |
| `FIREWORK_ROCKET` | V1_13_R1 (1.13) | V26_3 |  |
| `FIREWORK_STAR` | V1_13_R1 (1.13) | V26_3 |  |
| `FIRE_CHARGE` | V1_13_R1 (1.13) | V26_3 |  |
| `FIRE_CORAL` | V1_13_R1 (1.13) | V26_3 |  |
| `FIRE_CORAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `FIRE_CORAL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `FIRE_CORAL_WALL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `FISHING_ROD` | V1_13_R1 (1.13) | V26_3 |  |
| `FLETCHING_TABLE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `FLINT` | V1_13_R1 (1.13) | V26_3 |  |
| `FLINT_AND_STEEL` | V1_13_R1 (1.13) | V26_3 |  |
| `FLOWERING_AZALEA` | V1_17_R1 (1.17.x) | V26_3 |  |
| `FLOWERING_AZALEA_LEAVES` | V1_17_R1 (1.17.x) | V26_3 |  |
| `FLOWER_BANNER_PATTERN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `FLOWER_POT` | V1_13_R1 (1.13) | V26_3 |  |
| `FLOW_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `FLOW_BANNER_PATTERN` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `FLOW_POTTERY_SHERD` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `FOX_SPAWN_EGG` | V1_14_R1 (1.14.x) | V26_3 |  |
| `FRIEND_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `FROGSPAWN` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `FROG_SPAWN_EGG` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `FROSTED_ICE` | V1_13_R1 (1.13) | V26_3 |  |
| `FURNACE` | V1_13_R1 (1.13) | V26_3 |  |
| `FURNACE_MINECART` | V1_13_R1 (1.13) | V26_3 |  |
| `GHAST_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `GHAST_TEAR` | V1_13_R1 (1.13) | V26_3 |  |
| `GILDED_BLACKSTONE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `GLASS_BOTTLE` | V1_13_R1 (1.13) | V26_3 |  |
| `GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `GLISTERING_MELON_SLICE` | V1_13_R1 (1.13) | V26_3 |  |
| `GLOBE_BANNER_PATTERN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `GLOWSTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `GLOWSTONE_DUST` | V1_13_R1 (1.13) | V26_3 |  |
| `GLOW_BERRIES` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GLOW_INK_SAC` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GLOW_ITEM_FRAME` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GLOW_LICHEN` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GLOW_SQUID_SPAWN_EGG` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GOAT_HORN` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `GOAT_SPAWN_EGG` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GOLDEN_APPLE` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_AXE` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_BOOTS` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_CARROT` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_CHESTPLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_DANDELION` | V26_1 (26.1.x) | V26_3 |  |
| `GOLDEN_HELMET` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_HOE` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_HORSE_ARMOR` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_LEGGINGS` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_NAUTILUS_ARMOR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `GOLDEN_PICKAXE` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_SHOVEL` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLDEN_SPEAR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `GOLDEN_SWORD` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLD_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLD_INGOT` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLD_NUGGET` | V1_13_R1 (1.13) | V26_3 |  |
| `GOLD_ORE` | V1_13_R1 (1.13) | V26_3 |  |
| `GRANITE` | V1_13_R1 (1.13) | V26_3 |  |
| `GRANITE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `GRANITE_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `GRANITE_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `GRASS` | V1_13_R1 (1.13) | V1_20_R2 | → `SHORT_GRASS` (1.20.3) |
| `GRASS_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `GRASS_PATH` | V1_13_R1 (1.13) | V1_16_R3 | → `DIRT_PATH` (1.17) |
| `GRAVEL` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `GRAY_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GRAY_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GRAY_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `GRAY_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `GRAY_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `GRAY_DYE` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `GRAY_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `GRAY_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `GRAY_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `GREEN_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `GREEN_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GREEN_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GREEN_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `GREEN_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `GREEN_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `GREEN_DYE` | V1_14_R1 (1.14.x) | V26_3 | ← `CACTUS_GREEN` |
| `GREEN_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `GREEN_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `GREEN_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `GREEN_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `GRINDSTONE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `GUARDIAN_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `GUNPOWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `GUSTER_BANNER_PATTERN` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `GUSTER_POTTERY_SHERD` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `HANGING_ROOTS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `HAPPY_GHAST_SPAWN_EGG` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `HAY_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `HEARTBREAK_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `HEART_OF_THE_SEA` | V1_13_R1 (1.13) | V26_3 |  |
| `HEART_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `HEAVY_CORE` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `HEAVY_WEIGHTED_PRESSURE_PLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `HOGLIN_SPAWN_EGG` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `HONEYCOMB` | V1_15_R1 (1.15.x) | V26_3 |  |
| `HONEYCOMB_BLOCK` | V1_15_R1 (1.15.x) | V26_3 |  |
| `HONEY_BLOCK` | V1_15_R1 (1.15.x) | V26_3 |  |
| `HONEY_BOTTLE` | V1_15_R1 (1.15.x) | V26_3 |  |
| `HOPPER` | V1_13_R1 (1.13) | V26_3 |  |
| `HOPPER_MINECART` | V1_13_R1 (1.13) | V26_3 |  |
| `HORN_CORAL` | V1_13_R1 (1.13) | V26_3 |  |
| `HORN_CORAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `HORN_CORAL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `HORN_CORAL_WALL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `HORSE_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `HOST_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `HOWL_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `HUSK_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `ICE` | V1_13_R1 (1.13) | V26_3 |  |
| `INFESTED_CHISELED_STONE_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `INFESTED_COBBLESTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `INFESTED_CRACKED_STONE_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `INFESTED_DEEPSLATE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `INFESTED_MOSSY_STONE_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `INFESTED_STONE` | V1_13_R1 (1.13) | V26_3 |  |
| `INFESTED_STONE_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `INK_SAC` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_AXE` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_BARS` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_BOOTS` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_CHAIN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 | ← `CHAIN` |
| `IRON_CHESTPLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_DOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_GOLEM_SPAWN_EGG` | V1_19_R2 (1.19.3) | V26_3 |  |
| `IRON_HELMET` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_HOE` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_HORSE_ARMOR` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_INGOT` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_LEGGINGS` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_NAUTILUS_ARMOR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `IRON_NUGGET` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_ORE` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_PICKAXE` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_SHOVEL` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_SPEAR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `IRON_SWORD` | V1_13_R1 (1.13) | V26_3 |  |
| `IRON_TRAPDOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `ITEM_FRAME` | V1_13_R1 (1.13) | V26_3 |  |
| `JACK_O_LANTERN` | V1_13_R1 (1.13) | V26_3 |  |
| `JIGSAW` | V1_14_R1 (1.14.x) | V26_3 |  |
| `JUKEBOX` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_BOAT` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_BUTTON` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_CHEST_BOAT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `JUNGLE_DOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_FENCE` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_FENCE_GATE` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `JUNGLE_LEAVES` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_PLANKS` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_PRESSURE_PLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_PYRAMID_MAP` | V26_3 (26.3+) | V26_3 |  |
| `JUNGLE_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `JUNGLE_SIGN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `JUNGLE_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_TRAPDOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `JUNGLE_WALL_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `JUNGLE_WALL_SIGN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `JUNGLE_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `KELP` | V1_13_R1 (1.13) | V26_3 |  |
| `KELP_PLANT` | V1_13_R1 (1.13) | V26_3 |  |
| `KNOWLEDGE_BOOK` | V1_13_R1 (1.13) | V26_3 |  |
| `LADDER` | V1_13_R1 (1.13) | V26_3 |  |
| `LANTERN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `LAPIS_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `LAPIS_LAZULI` | V1_13_R1 (1.13) | V26_3 |  |
| `LAPIS_ORE` | V1_13_R1 (1.13) | V26_3 |  |
| `LARGE_AMETHYST_BUD` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LARGE_FERN` | V1_13_R1 (1.13) | V26_3 |  |
| `LAVA` | V1_13_R1 (1.13) | V26_3 |  |
| `LAVA_BUCKET` | V1_13_R1 (1.13) | V26_3 |  |
| `LAVA_CAULDRON` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LEAD` | V1_13_R1 (1.13) | V26_3 |  |
| `LEAF_LITTER` | V1_21_R4 (1.21.5) | V26_3 |  |
| `LEATHER` | V1_13_R1 (1.13) | V26_3 |  |
| `LEATHER_BOOTS` | V1_13_R1 (1.13) | V26_3 |  |
| `LEATHER_CHESTPLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `LEATHER_HELMET` | V1_13_R1 (1.13) | V26_3 |  |
| `LEATHER_HORSE_ARMOR` | V1_14_R1 (1.14.x) | V26_3 |  |
| `LEATHER_LEGGINGS` | V1_13_R1 (1.13) | V26_3 |  |
| `LECTERN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `LEVER` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LIGHTNING_ROD` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LIGHT_BLUE_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `LIGHT_BLUE_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LIGHT_BLUE_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LIGHT_BLUE_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `LIGHT_BLUE_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `LIGHT_BLUE_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `LIGHT_BLUE_DYE` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `LIGHT_BLUE_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_BLUE_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `LIGHT_BLUE_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `LIGHT_GRAY_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `LIGHT_GRAY_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LIGHT_GRAY_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LIGHT_GRAY_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `LIGHT_GRAY_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `LIGHT_GRAY_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `LIGHT_GRAY_DYE` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `LIGHT_GRAY_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `LIGHT_GRAY_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `LIGHT_GRAY_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `LIGHT_WEIGHTED_PRESSURE_PLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `LILAC` | V1_13_R1 (1.13) | V26_3 |  |
| `LILY_OF_THE_VALLEY` | V1_14_R1 (1.14.x) | V26_3 |  |
| `LILY_PAD` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `LIME_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LIME_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LIME_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `LIME_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `LIME_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `LIME_DYE` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `LIME_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `LIME_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `LIME_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `LINGERING_POTION` | V1_13_R1 (1.13) | V26_3 |  |
| `LLAMA_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `LODESTONE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `LOOM` | V1_14_R1 (1.14.x) | V26_3 |  |
| `MACE` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `MAGENTA_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `MAGENTA_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `MAGENTA_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `MAGENTA_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `MAGENTA_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `MAGENTA_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `MAGENTA_DYE` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `MAGENTA_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGENTA_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `MAGENTA_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `MAGMA_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGMA_CREAM` | V1_13_R1 (1.13) | V26_3 |  |
| `MAGMA_CUBE_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `MANGROVE_BOAT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_BUTTON` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_CHEST_BOAT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_DOOR` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_FENCE` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_FENCE_GATE` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `MANGROVE_LEAVES` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_LOG` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_PLANKS` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_PRESSURE_PLATE` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_PROPAGULE` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_ROOTS` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `MANGROVE_SIGN` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_SLAB` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_STAIRS` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_TRAPDOOR` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_WALL_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `MANGROVE_WALL_SIGN` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MANGROVE_WOOD` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MAP` | V1_13_R1 (1.13) | V26_3 |  |
| `MEDIUM_AMETHYST_BUD` | V1_17_R1 (1.17.x) | V26_3 |  |
| `MELON` | V1_13_R1 (1.13) | V26_3 |  |
| `MELON_SEEDS` | V1_13_R1 (1.13) | V26_3 |  |
| `MELON_SLICE` | V1_13_R1 (1.13) | V26_3 |  |
| `MELON_STEM` | V1_13_R1 (1.13) | V26_3 |  |
| `MILK_BUCKET` | V1_13_R1 (1.13) | V26_3 |  |
| `MINECART` | V1_13_R1 (1.13) | V26_3 |  |
| `MINER_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `MOJANG_BANNER_PATTERN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `MOOSHROOM_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `MOSSY_COBBLESTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `MOSSY_COBBLESTONE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `MOSSY_COBBLESTONE_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `MOSSY_COBBLESTONE_WALL` | V1_13_R1 (1.13) | V26_3 |  |
| `MOSSY_STONE_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `MOSSY_STONE_BRICK_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `MOSSY_STONE_BRICK_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `MOSSY_STONE_BRICK_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `MOSS_BLOCK` | V1_17_R1 (1.17.x) | V26_3 |  |
| `MOSS_CARPET` | V1_17_R1 (1.17.x) | V26_3 |  |
| `MOURNER_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `MOVING_PISTON` | V1_13_R1 (1.13) | V26_3 |  |
| `MUD` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MUDDY_MANGROVE_ROOTS` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MUD_BRICKS` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MUD_BRICK_SLAB` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MUD_BRICK_STAIRS` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MUD_BRICK_WALL` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MULE_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSHROOM_STEM` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSHROOM_STEW` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_11` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_13` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_5` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `MUSIC_DISC_BLOCKS` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_BOUNCE` | V26_2 (26.2.x) | V26_3 |  |
| `MUSIC_DISC_CAT` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_CHIRP` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_CREATOR` | V1_21_R1 (1.21 - 1.21.1) | V26_3 |  |
| `MUSIC_DISC_CREATOR_MUSIC_BOX` | V1_21_R1 (1.21 - 1.21.1) | V26_3 |  |
| `MUSIC_DISC_FAR` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_LAVA_CHICKEN` | V1_21_R5 (1.21.7 - 1.21.8) | V26_3 |  |
| `MUSIC_DISC_MALL` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_MELLOHI` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_OTHERSIDE` | V1_18_R1 (1.18 - 1.18.1) | V26_3 |  |
| `MUSIC_DISC_PIGSTEP` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `MUSIC_DISC_PRECIPICE` | V1_21_R1 (1.21 - 1.21.1) | V26_3 |  |
| `MUSIC_DISC_RELIC` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `MUSIC_DISC_STAL` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_STRAD` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_TEARS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `MUSIC_DISC_WAIT` | V1_13_R1 (1.13) | V26_3 |  |
| `MUSIC_DISC_WARD` | V1_13_R1 (1.13) | V26_3 |  |
| `MUTTON` | V1_13_R1 (1.13) | V26_3 |  |
| `MYCELIUM` | V1_13_R1 (1.13) | V26_3 |  |
| `NAME_TAG` | V1_13_R1 (1.13) | V26_3 |  |
| `NAUTILUS_SHELL` | V1_13_R1 (1.13) | V26_3 |  |
| `NAUTILUS_SPAWN_EGG` | V1_21_R7 (1.21.11) | V26_3 |  |
| `NETHERITE_AXE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_BLOCK` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_BOOTS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_CHESTPLATE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_HELMET` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_HOE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_HORSE_ARMOR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `NETHERITE_INGOT` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_LEGGINGS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_NAUTILUS_ARMOR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `NETHERITE_PICKAXE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_SCRAP` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_SHOVEL` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_SPEAR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `NETHERITE_SWORD` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHERITE_UPGRADE_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `NETHERRACK` | V1_13_R1 (1.13) | V26_3 |  |
| `NETHER_BRICK` | V1_13_R1 (1.13) | V26_3 |  |
| `NETHER_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `NETHER_BRICK_FENCE` | V1_13_R1 (1.13) | V26_3 |  |
| `NETHER_BRICK_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `NETHER_BRICK_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `NETHER_BRICK_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `NETHER_GOLD_ORE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHER_PORTAL` | V1_13_R1 (1.13) | V26_3 |  |
| `NETHER_QUARTZ_ORE` | V1_13_R1 (1.13) | V26_3 |  |
| `NETHER_SPROUTS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `NETHER_STAR` | V1_13_R1 (1.13) | V26_3 |  |
| `NETHER_WART` | V1_13_R1 (1.13) | V26_3 |  |
| `NETHER_WART_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `NOTE_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_BOAT` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_BUTTON` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_CHEST_BOAT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `OAK_DOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_FENCE` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_FENCE_GATE` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `OAK_LEAVES` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_PLANKS` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_PRESSURE_PLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `OAK_SIGN` | V1_14_R1 (1.14.x) | V26_3 | ← `SIGN` |
| `OAK_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_TRAPDOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `OAK_WALL_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `OAK_WALL_SIGN` | V1_14_R1 (1.14.x) | V26_3 | ← `WALL_SIGN` |
| `OAK_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `OBSERVER` | V1_13_R1 (1.13) | V26_3 |  |
| `OBSIDIAN` | V1_13_R1 (1.13) | V26_3 |  |
| `OCEAN_MONUMENT_MAP` | V26_3 (26.3+) | V26_3 |  |
| `OCELOT_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `OCHRE_FROGLIGHT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `OMINOUS_BOTTLE` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `OMINOUS_TRIAL_KEY` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `OPEN_EYEBLOSSOM` | V1_21_R3 (1.21.4) | V26_3 |  |
| `ORANGE_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `ORANGE_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `ORANGE_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `ORANGE_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `ORANGE_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `ORANGE_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `ORANGE_DYE` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `ORANGE_POPLAR_LEAVES` | V26_3 (26.3+) | V26_3 |  |
| `ORANGE_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_TULIP` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `ORANGE_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `ORANGE_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `OXEYE_DAISY` | V1_13_R1 (1.13) | V26_3 |  |
| `OXIDIZED_CHISELED_COPPER` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `OXIDIZED_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `OXIDIZED_COPPER_BARS` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `OXIDIZED_COPPER_BULB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `OXIDIZED_COPPER_CHAIN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `OXIDIZED_COPPER_CHEST` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `OXIDIZED_COPPER_DOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `OXIDIZED_COPPER_GOLEM_STATUE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `OXIDIZED_COPPER_GRATE` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `OXIDIZED_COPPER_LANTERN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `OXIDIZED_COPPER_TRAPDOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `OXIDIZED_CUT_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `OXIDIZED_CUT_COPPER_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `OXIDIZED_CUT_COPPER_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `OXIDIZED_LIGHTNING_ROD` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `PACKED_ICE` | V1_13_R1 (1.13) | V26_3 |  |
| `PACKED_MUD` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `PAINTING` | V1_13_R1 (1.13) | V26_3 |  |
| `PALE_HANGING_MOSS` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_MOSS_BLOCK` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_MOSS_CARPET` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_BOAT` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_BUTTON` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_CHEST_BOAT` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_DOOR` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_FENCE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_FENCE_GATE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_HANGING_SIGN` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_LEAVES` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_LOG` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_PLANKS` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_PRESSURE_PLATE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_SAPLING` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `PALE_OAK_SIGN` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_SLAB` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_STAIRS` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_TRAPDOOR` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_WALL_HANGING_SIGN` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_WALL_SIGN` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PALE_OAK_WOOD` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PANDA_SPAWN_EGG` | V1_14_R1 (1.14.x) | V26_3 |  |
| `PAPER` | V1_13_R1 (1.13) | V26_3 |  |
| `PARCHED_SPAWN_EGG` | V1_21_R7 (1.21.11) | V26_3 |  |
| `PARROT_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `PEARLESCENT_FROGLIGHT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `PEONY` | V1_13_R1 (1.13) | V26_3 |  |
| `PETRIFIED_OAK_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `PHANTOM_MEMBRANE` | V1_13_R1 (1.13) | V26_3 |  |
| `PHANTOM_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `PIGLIN_BANNER_PATTERN` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `PIGLIN_BRUTE_SPAWN_EGG` | V1_16_R2 (1.16.2 - 1.16.3) | V26_3 |  |
| `PIGLIN_HEAD` | V1_19_R2 (1.19.3) | V26_3 |  |
| `PIGLIN_SPAWN_EGG` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `PIGLIN_WALL_HEAD` | V1_19_R2 (1.19.3) | V26_3 |  |
| `PIG_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `PILLAGER_SPAWN_EGG` | V1_14_R1 (1.14.x) | V26_3 |  |
| `PINK_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PINK_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `PINK_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `PINK_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `PINK_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `PINK_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `PINK_DYE` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `PINK_PETALS` | V1_19_R3 (1.19.4) | V26_3 |  |
| `PINK_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_TULIP` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `PINK_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `PINK_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `PISTON` | V1_13_R1 (1.13) | V26_3 |  |
| `PISTON_HEAD` | V1_13_R1 (1.13) | V26_3 |  |
| `PITCHER_CROP` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `PITCHER_PLANT` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `PITCHER_POD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `PLAINS_VILLAGE_MAP` | V26_3 (26.3+) | V26_3 |  |
| `PLAYER_HEAD` | V1_13_R1 (1.13) | V26_3 |  |
| `PLAYER_WALL_HEAD` | V1_13_R1 (1.13) | V26_3 |  |
| `PLENTY_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `PODZOL` | V1_13_R1 (1.13) | V26_3 |  |
| `POINTED_DRIPSTONE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POISONOUS_POTATO` | V1_13_R1 (1.13) | V26_3 |  |
| `POLAR_BEAR_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `POLISHED_ANDESITE` | V1_13_R1 (1.13) | V26_3 |  |
| `POLISHED_ANDESITE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `POLISHED_ANDESITE_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `POLISHED_BASALT` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_BLACKSTONE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_BLACKSTONE_BRICKS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_BLACKSTONE_BRICK_SLAB` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_BLACKSTONE_BRICK_STAIRS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_BLACKSTONE_BRICK_WALL` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_BLACKSTONE_BUTTON` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_BLACKSTONE_PRESSURE_PLATE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_BLACKSTONE_SLAB` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_BLACKSTONE_STAIRS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_BLACKSTONE_WALL` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POLISHED_CINNABAR` | V26_2 (26.2.x) | V26_3 |  |
| `POLISHED_CINNABAR_SLAB` | V26_2 (26.2.x) | V26_3 |  |
| `POLISHED_CINNABAR_STAIRS` | V26_2 (26.2.x) | V26_3 |  |
| `POLISHED_CINNABAR_WALL` | V26_2 (26.2.x) | V26_3 |  |
| `POLISHED_DEEPSLATE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POLISHED_DEEPSLATE_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POLISHED_DEEPSLATE_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POLISHED_DEEPSLATE_WALL` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POLISHED_DIORITE` | V1_13_R1 (1.13) | V26_3 |  |
| `POLISHED_DIORITE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `POLISHED_DIORITE_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `POLISHED_GRANITE` | V1_13_R1 (1.13) | V26_3 |  |
| `POLISHED_GRANITE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `POLISHED_GRANITE_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `POLISHED_SULFUR` | V26_2 (26.2.x) | V26_3 |  |
| `POLISHED_SULFUR_SLAB` | V26_2 (26.2.x) | V26_3 |  |
| `POLISHED_SULFUR_STAIRS` | V26_2 (26.2.x) | V26_3 |  |
| `POLISHED_SULFUR_WALL` | V26_2 (26.2.x) | V26_3 |  |
| `POLISHED_TUFF` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `POLISHED_TUFF_SLAB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `POLISHED_TUFF_STAIRS` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `POLISHED_TUFF_WALL` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `POPLAR_BOAT` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_BUTTON` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_CHEST_BOAT` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_DOOR` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_FENCE` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_FENCE_GATE` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_HANGING_SIGN` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_LOG` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_PLANKS` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_PRESSURE_PLATE` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_SAPLING` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_SHELF` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_SIGN` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_TRAPDOOR` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_WALL_HANGING_SIGN` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_WALL_SIGN` | V26_3 (26.3+) | V26_3 |  |
| `POPLAR_WOOD` | V26_3 (26.3+) | V26_3 |  |
| `POPPED_CHORUS_FRUIT` | V1_13_R1 (1.13) | V26_3 |  |
| `POPPY` | V1_13_R1 (1.13) | V26_3 |  |
| `PORKCHOP` | V1_13_R1 (1.13) | V26_3 |  |
| `POTATO` | V1_13_R1 (1.13) | V26_3 |  |
| `POTATOES` | V1_13_R1 (1.13) | V26_3 |  |
| `POTENT_SULFUR` | V26_2 (26.2.x) | V26_3 |  |
| `POTION` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_ACACIA_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_ALLIUM` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_AZALEA_BUSH` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POTTED_AZURE_BLUET` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_BAMBOO` | V1_14_R1 (1.14.x) | V26_3 |  |
| `POTTED_BIRCH_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_BLUE_ORCHID` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_BROWN_MUSHROOM` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_CACTUS` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_CHERRY_SAPLING` | V1_19_R3 (1.19.4) | V26_3 |  |
| `POTTED_CLOSED_EYEBLOSSOM` | V1_21_R3 (1.21.4) | V26_3 |  |
| `POTTED_CORNFLOWER` | V1_14_R1 (1.14.x) | V26_3 |  |
| `POTTED_CRIMSON_FUNGUS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POTTED_CRIMSON_ROOTS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POTTED_DANDELION` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_DARK_OAK_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_DEAD_BUSH` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_FERN` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_FLOWERING_AZALEA_BUSH` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POTTED_GOLDEN_DANDELION` | V26_1 (26.1.x) | V26_3 |  |
| `POTTED_JUNGLE_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_LILY_OF_THE_VALLEY` | V1_14_R1 (1.14.x) | V26_3 |  |
| `POTTED_MANGROVE_PROPAGULE` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `POTTED_OAK_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_OPEN_EYEBLOSSOM` | V1_21_R3 (1.21.4) | V26_3 |  |
| `POTTED_ORANGE_TULIP` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_OXEYE_DAISY` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_PALE_OAK_SAPLING` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `POTTED_PINK_TULIP` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_POPLAR_SAPLING` | V26_3 (26.3+) | V26_3 |  |
| `POTTED_POPPY` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_RED_MUSHROOM` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_RED_TULIP` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_SPRUCE_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_TORCHFLOWER` | V1_19_R3 (1.19.4) | V26_3 |  |
| `POTTED_WARPED_FUNGUS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POTTED_WARPED_ROOTS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `POTTED_WHITE_TULIP` | V1_13_R1 (1.13) | V26_3 |  |
| `POTTED_WITHER_ROSE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `POTTERY_SHARD_ARCHER` | V1_19_R3 (1.19.4) | V1_19_R3 | → `ARCHER_POTTERY_SHERD` (1.20) |
| `POTTERY_SHARD_ARMS_UP` | V1_19_R3 (1.19.4) | V1_19_R3 | → `ARMS_UP_POTTERY_SHERD` (1.20) |
| `POTTERY_SHARD_PRIZE` | V1_19_R3 (1.19.4) | V1_19_R3 | → `PRIZE_POTTERY_SHERD` (1.20) |
| `POTTERY_SHARD_SKULL` | V1_19_R3 (1.19.4) | V1_19_R3 | → `SKULL_POTTERY_SHERD` (1.20) |
| `POWDER_SNOW` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POWDER_SNOW_BUCKET` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POWDER_SNOW_CAULDRON` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POWERED_RAIL` | V1_13_R1 (1.13) | V26_3 |  |
| `PRISMARINE` | V1_13_R1 (1.13) | V26_3 |  |
| `PRISMARINE_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `PRISMARINE_BRICK_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `PRISMARINE_BRICK_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `PRISMARINE_CRYSTALS` | V1_13_R1 (1.13) | V26_3 |  |
| `PRISMARINE_SHARD` | V1_13_R1 (1.13) | V26_3 |  |
| `PRISMARINE_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `PRISMARINE_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `PRISMARINE_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `PRIZE_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 | ← `POTTERY_SHARD_PRIZE` |
| `PUFFERFISH` | V1_13_R1 (1.13) | V26_3 |  |
| `PUFFERFISH_BUCKET` | V1_13_R1 (1.13) | V26_3 |  |
| `PUFFERFISH_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `PUMPKIN` | V1_13_R1 (1.13) | V26_3 |  |
| `PUMPKIN_PIE` | V1_13_R1 (1.13) | V26_3 |  |
| `PUMPKIN_SEEDS` | V1_13_R1 (1.13) | V26_3 |  |
| `PUMPKIN_STEM` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `PURPLE_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `PURPLE_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `PURPLE_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `PURPLE_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `PURPLE_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `PURPLE_DYE` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `PURPLE_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPLE_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `PURPLE_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `PURPUR_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPUR_PILLAR` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPUR_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `PURPUR_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `QUARTZ` | V1_13_R1 (1.13) | V26_3 |  |
| `QUARTZ_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `QUARTZ_BRICKS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `QUARTZ_PILLAR` | V1_13_R1 (1.13) | V26_3 |  |
| `QUARTZ_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `QUARTZ_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `RABBIT` | V1_13_R1 (1.13) | V26_3 |  |
| `RABBIT_FOOT` | V1_13_R1 (1.13) | V26_3 |  |
| `RABBIT_HIDE` | V1_13_R1 (1.13) | V26_3 |  |
| `RABBIT_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `RABBIT_STEW` | V1_13_R1 (1.13) | V26_3 |  |
| `RAIL` | V1_13_R1 (1.13) | V26_3 |  |
| `RAISER_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `RAVAGER_SPAWN_EGG` | V1_14_R1 (1.14.x) | V26_3 |  |
| `RAW_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `RAW_COPPER_BLOCK` | V1_17_R1 (1.17.x) | V26_3 |  |
| `RAW_GOLD` | V1_17_R1 (1.17.x) | V26_3 |  |
| `RAW_GOLD_BLOCK` | V1_17_R1 (1.17.x) | V26_3 |  |
| `RAW_IRON` | V1_17_R1 (1.17.x) | V26_3 |  |
| `RAW_IRON_BLOCK` | V1_17_R1 (1.17.x) | V26_3 |  |
| `RECOVERY_COMPASS` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `REDSTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `REDSTONE_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `REDSTONE_LAMP` | V1_13_R1 (1.13) | V26_3 |  |
| `REDSTONE_ORE` | V1_13_R1 (1.13) | V26_3 |  |
| `REDSTONE_TORCH` | V1_13_R1 (1.13) | V26_3 |  |
| `REDSTONE_WALL_TORCH` | V1_13_R1 (1.13) | V26_3 |  |
| `REDSTONE_WIRE` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `RED_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `RED_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `RED_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `RED_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `RED_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `RED_DYE` | V1_14_R1 (1.14.x) | V26_3 | ← `ROSE_RED` |
| `RED_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `RED_MUSHROOM` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_MUSHROOM_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_NETHER_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_NETHER_BRICK_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `RED_NETHER_BRICK_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `RED_NETHER_BRICK_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `RED_POPLAR_LEAVES` | V26_3 (26.3+) | V26_3 |  |
| `RED_SAND` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_SANDSTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_SANDSTONE_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_SANDSTONE_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_SANDSTONE_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `RED_SHRUB` | V26_3 (26.3+) | V26_3 |  |
| `RED_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_TULIP` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `RED_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `RED_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `REINFORCED_DEEPSLATE` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `REPEATER` | V1_13_R1 (1.13) | V26_3 |  |
| `REPEATING_COMMAND_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `RESIN_BLOCK` | V1_21_R3 (1.21.4) | V26_3 |  |
| `RESIN_BRICK` | V1_21_R3 (1.21.4) | V26_3 |  |
| `RESIN_BRICKS` | V1_21_R3 (1.21.4) | V26_3 |  |
| `RESIN_BRICK_SLAB` | V1_21_R3 (1.21.4) | V26_3 |  |
| `RESIN_BRICK_STAIRS` | V1_21_R3 (1.21.4) | V26_3 |  |
| `RESIN_BRICK_WALL` | V1_21_R3 (1.21.4) | V26_3 |  |
| `RESIN_CLUMP` | V1_21_R3 (1.21.4) | V26_3 |  |
| `RESPAWN_ANCHOR` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `RIB_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `ROOTED_DIRT` | V1_17_R1 (1.17.x) | V26_3 |  |
| `ROSE_BUSH` | V1_13_R1 (1.13) | V26_3 |  |
| `ROSE_RED` | V1_13_R1 (1.13) | V1_13_R2 | → `RED_DYE` (1.14) |
| `ROTTEN_FLESH` | V1_13_R1 (1.13) | V26_3 |  |
| `SADDLE` | V1_13_R1 (1.13) | V26_3 |  |
| `SALMON` | V1_13_R1 (1.13) | V26_3 |  |
| `SALMON_BUCKET` | V1_13_R1 (1.13) | V26_3 |  |
| `SALMON_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `SAND` | V1_13_R1 (1.13) | V26_3 |  |
| `SANDSTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `SANDSTONE_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `SANDSTONE_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `SANDSTONE_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SAVANNA_VILLAGE_MAP` | V26_3 (26.3+) | V26_3 |  |
| `SCAFFOLDING` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SCRAPE_POTTERY_SHERD` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `SCULK` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `SCULK_CATALYST` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `SCULK_SENSOR` | V1_17_R1 (1.17.x) | V26_3 |  |
| `SCULK_SHRIEKER` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `SCULK_VEIN` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `SCUTE` | V1_13_R1 (1.13) | V1_20_R3 | → `TURTLE_SCUTE` (1.20.5) |
| `SEAGRASS` | V1_13_R1 (1.13) | V26_3 |  |
| `SEA_LANTERN` | V1_13_R1 (1.13) | V26_3 |  |
| `SEA_PICKLE` | V1_13_R1 (1.13) | V26_3 |  |
| `SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `SHEAF_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `SHEARS` | V1_13_R1 (1.13) | V26_3 |  |
| `SHEEP_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `SHELF_MUSHROOM` | V26_3 (26.3+) | V26_3 |  |
| `SHELTER_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `SHIELD` | V1_13_R1 (1.13) | V26_3 |  |
| `SHORT_DRY_GRASS` | V1_21_R4 (1.21.5) | V26_3 |  |
| `SHORT_GRASS` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 | ← `GRASS` |
| `SHROOMLIGHT` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `SHULKER_SHELL` | V1_13_R1 (1.13) | V26_3 |  |
| `SHULKER_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `SIGN` | V1_13_R1 (1.13) | V1_13_R2 | → `OAK_SIGN` (1.14) |
| `SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `SILVERFISH_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `SKELETON_HORSE_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `SKELETON_SKULL` | V1_13_R1 (1.13) | V26_3 |  |
| `SKELETON_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `SKELETON_WALL_SKULL` | V1_13_R1 (1.13) | V26_3 |  |
| `SKULL_BANNER_PATTERN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SKULL_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 | ← `POTTERY_SHARD_SKULL` |
| `SLIME_BALL` | V1_13_R1 (1.13) | V26_3 |  |
| `SLIME_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `SLIME_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `SMALL_AMETHYST_BUD` | V1_17_R1 (1.17.x) | V26_3 |  |
| `SMALL_DRIPLEAF` | V1_17_R1 (1.17.x) | V26_3 |  |
| `SMITHING_TABLE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SMOKER` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SMOOTH_BASALT` | V1_17_R1 (1.17.x) | V26_3 |  |
| `SMOOTH_QUARTZ` | V1_13_R1 (1.13) | V26_3 |  |
| `SMOOTH_QUARTZ_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SMOOTH_QUARTZ_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SMOOTH_RED_SANDSTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `SMOOTH_RED_SANDSTONE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SMOOTH_RED_SANDSTONE_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SMOOTH_SANDSTONE` | V1_13_R1 (1.13) | V26_3 |  |
| `SMOOTH_SANDSTONE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SMOOTH_SANDSTONE_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SMOOTH_STONE` | V1_13_R1 (1.13) | V26_3 |  |
| `SMOOTH_STONE_SLAB` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SNIFFER_EGG` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `SNIFFER_SPAWN_EGG` | V1_19_R3 (1.19.4) | V26_3 |  |
| `SNORT_POTTERY_SHERD` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `SNOW` | V1_13_R1 (1.13) | V26_3 |  |
| `SNOWBALL` | V1_13_R1 (1.13) | V26_3 |  |
| `SNOWY_VILLAGE_MAP` | V26_3 (26.3+) | V26_3 |  |
| `SNOW_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `SNOW_GOLEM_SPAWN_EGG` | V1_19_R2 (1.19.3) | V26_3 |  |
| `SOUL_CAMPFIRE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `SOUL_FIRE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `SOUL_LANTERN` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `SOUL_SAND` | V1_13_R1 (1.13) | V26_3 |  |
| `SOUL_SOIL` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `SOUL_TORCH` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `SOUL_WALL_TORCH` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `SPAWNER` | V1_13_R1 (1.13) | V26_3 |  |
| `SPECTRAL_ARROW` | V1_13_R1 (1.13) | V26_3 |  |
| `SPIDER_EYE` | V1_13_R1 (1.13) | V26_3 |  |
| `SPIDER_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `SPLASH_POTION` | V1_13_R1 (1.13) | V26_3 |  |
| `SPONGE` | V1_13_R1 (1.13) | V26_3 |  |
| `SPORE_BLOSSOM` | V1_17_R1 (1.17.x) | V26_3 |  |
| `SPRUCE_BOAT` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_BUTTON` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_CHEST_BOAT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `SPRUCE_DOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_FENCE` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_FENCE_GATE` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `SPRUCE_LEAVES` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_PLANKS` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_PRESSURE_PLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_SAPLING` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `SPRUCE_SIGN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SPRUCE_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_TRAPDOOR` | V1_13_R1 (1.13) | V26_3 |  |
| `SPRUCE_WALL_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `SPRUCE_WALL_SIGN` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SPRUCE_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `SPYGLASS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `SQUID_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `STICK` | V1_13_R1 (1.13) | V26_3 |  |
| `STICKY_PISTON` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE` | V1_13_R1 (1.13) | V26_3 |  |
| `STONECUTTER` | V1_14_R1 (1.14.x) | V26_3 |  |
| `STONE_AXE` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE_BRICKS` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE_BRICK_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE_BRICK_STAIRS` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE_BRICK_WALL` | V1_14_R1 (1.14.x) | V26_3 |  |
| `STONE_BUTTON` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE_HOE` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE_PICKAXE` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE_PRESSURE_PLATE` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE_SHOVEL` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE_SLAB` | V1_13_R1 (1.13) | V26_3 |  |
| `STONE_SPEAR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `STONE_STAIRS` | V1_14_R1 (1.14.x) | V26_3 |  |
| `STONE_SWORD` | V1_13_R1 (1.13) | V26_3 |  |
| `STRAW_BED` | V26_3 (26.3+) | V26_3 |  |
| `STRAY_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIDER_SPAWN_EGG` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `STRING` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_ACACIA_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_ACACIA_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_BAMBOO_BLOCK` | V1_19_R2 (1.19.3) | V26_3 |  |
| `STRIPPED_BIRCH_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_BIRCH_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_CHERRY_LOG` | V1_19_R3 (1.19.4) | V26_3 |  |
| `STRIPPED_CHERRY_WOOD` | V1_19_R3 (1.19.4) | V26_3 |  |
| `STRIPPED_CRIMSON_HYPHAE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `STRIPPED_CRIMSON_STEM` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `STRIPPED_DARK_OAK_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_DARK_OAK_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_JUNGLE_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_JUNGLE_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_MANGROVE_LOG` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `STRIPPED_MANGROVE_WOOD` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `STRIPPED_OAK_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_OAK_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_PALE_OAK_LOG` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `STRIPPED_PALE_OAK_WOOD` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `STRIPPED_POPLAR_LOG` | V26_3 (26.3+) | V26_3 |  |
| `STRIPPED_POPLAR_WOOD` | V26_3 (26.3+) | V26_3 |  |
| `STRIPPED_SPRUCE_LOG` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_SPRUCE_WOOD` | V1_13_R1 (1.13) | V26_3 |  |
| `STRIPPED_WARPED_HYPHAE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `STRIPPED_WARPED_STEM` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `STRUCTURE_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `STRUCTURE_VOID` | V1_13_R1 (1.13) | V26_3 |  |
| `SUGAR` | V1_13_R1 (1.13) | V26_3 |  |
| `SUGAR_CANE` | V1_13_R1 (1.13) | V26_3 |  |
| `SULFUR` | V26_2 (26.2.x) | V26_3 |  |
| `SULFUR_BRICKS` | V26_2 (26.2.x) | V26_3 |  |
| `SULFUR_BRICK_SLAB` | V26_2 (26.2.x) | V26_3 |  |
| `SULFUR_BRICK_STAIRS` | V26_2 (26.2.x) | V26_3 |  |
| `SULFUR_BRICK_WALL` | V26_2 (26.2.x) | V26_3 |  |
| `SULFUR_CUBE_BUCKET` | V26_2 (26.2.x) | V26_3 |  |
| `SULFUR_CUBE_SPAWN_EGG` | V26_2 (26.2.x) | V26_3 |  |
| `SULFUR_SLAB` | V26_2 (26.2.x) | V26_3 |  |
| `SULFUR_SPIKE` | V26_2 (26.2.x) | V26_3 |  |
| `SULFUR_STAIRS` | V26_2 (26.2.x) | V26_3 |  |
| `SULFUR_WALL` | V26_2 (26.2.x) | V26_3 |  |
| `SUNFLOWER` | V1_13_R1 (1.13) | V26_3 |  |
| `SUSPICIOUS_GRAVEL` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `SUSPICIOUS_SAND` | V1_19_R3 (1.19.4) | V26_3 |  |
| `SUSPICIOUS_STEW` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SWAMP_HUT_MAP` | V26_3 (26.3+) | V26_3 |  |
| `SWEET_BERRIES` | V1_14_R1 (1.14.x) | V26_3 |  |
| `SWEET_BERRY_BUSH` | V1_14_R1 (1.14.x) | V26_3 |  |
| `TADPOLE_BUCKET` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `TADPOLE_SPAWN_EGG` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `TAIGA_VILLAGE_MAP` | V26_3 (26.3+) | V26_3 |  |
| `TALL_DRY_GRASS` | V1_21_R4 (1.21.5) | V26_3 |  |
| `TALL_GRASS` | V1_13_R1 (1.13) | V26_3 |  |
| `TALL_SEAGRASS` | V1_13_R1 (1.13) | V26_3 |  |
| `TARGET` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `TEST_BLOCK` | V1_21_R4 (1.21.5) | V26_3 |  |
| `TEST_INSTANCE_BLOCK` | V1_21_R4 (1.21.5) | V26_3 |  |
| `TIDE_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `TINTED_GLASS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `TIPPED_ARROW` | V1_13_R1 (1.13) | V26_3 |  |
| `TNT` | V1_13_R1 (1.13) | V26_3 |  |
| `TNT_MINECART` | V1_13_R1 (1.13) | V26_3 |  |
| `TORCH` | V1_13_R1 (1.13) | V26_3 |  |
| `TORCHFLOWER` | V1_19_R3 (1.19.4) | V26_3 |  |
| `TORCHFLOWER_CROP` | V1_19_R3 (1.19.4) | V26_3 |  |
| `TORCHFLOWER_SEEDS` | V1_19_R3 (1.19.4) | V26_3 |  |
| `TOTEM_OF_UNDYING` | V1_13_R1 (1.13) | V26_3 |  |
| `TRADER_LLAMA_SPAWN_EGG` | V1_14_R1 (1.14.x) | V26_3 |  |
| `TRAPPED_CHEST` | V1_13_R1 (1.13) | V26_3 |  |
| `TRIAL_KEY` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `TRIAL_SPAWNER` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `TRIDENT` | V1_13_R1 (1.13) | V26_3 |  |
| `TRIPWIRE` | V1_13_R1 (1.13) | V26_3 |  |
| `TRIPWIRE_HOOK` | V1_13_R1 (1.13) | V26_3 |  |
| `TROPICAL_FISH` | V1_13_R1 (1.13) | V26_3 |  |
| `TROPICAL_FISH_BUCKET` | V1_13_R1 (1.13) | V26_3 |  |
| `TROPICAL_FISH_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `TUBE_CORAL` | V1_13_R1 (1.13) | V26_3 |  |
| `TUBE_CORAL_BLOCK` | V1_13_R1 (1.13) | V26_3 |  |
| `TUBE_CORAL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `TUBE_CORAL_WALL_FAN` | V1_13_R1 (1.13) | V26_3 |  |
| `TUFF` | V1_17_R1 (1.17.x) | V26_3 |  |
| `TUFF_BRICKS` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `TUFF_BRICK_SLAB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `TUFF_BRICK_STAIRS` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `TUFF_BRICK_WALL` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `TUFF_SLAB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `TUFF_STAIRS` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `TUFF_WALL` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `TURTLE_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `TURTLE_HELMET` | V1_13_R1 (1.13) | V26_3 |  |
| `TURTLE_SCUTE` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 | ← `SCUTE` |
| `TURTLE_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `TWISTING_VINES` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `TWISTING_VINES_PLANT` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `VAULT` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `VERDANT_FROGLIGHT` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `VEX_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `VEX_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `VILLAGER_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `VINDICATOR_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `VINE` | V1_13_R1 (1.13) | V26_3 |  |
| `VOID_AIR` | V1_13_R1 (1.13) | V26_3 |  |
| `WALL_SIGN` | V1_13_R1 (1.13) | V1_13_R2 | → `OAK_WALL_SIGN` (1.14) |
| `WALL_TORCH` | V1_13_R1 (1.13) | V26_3 |  |
| `WANDERING_TRADER_SPAWN_EGG` | V1_14_R1 (1.14.x) | V26_3 |  |
| `WARDEN_SPAWN_EGG` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  |
| `WARD_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `WARM_OCEAN_RUINS_MAP` | V26_3 (26.3+) | V26_3 |  |
| `WARPED_BUTTON` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_DOOR` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_FENCE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_FENCE_GATE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_FUNGUS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_FUNGUS_ON_A_STICK` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `WARPED_HYPHAE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_NYLIUM` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_PLANKS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_PRESSURE_PLATE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_ROOTS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_SHELF` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WARPED_SIGN` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_SLAB` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_STAIRS` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_STEM` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_TRAPDOOR` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_WALL_HANGING_SIGN` | V1_19_R2 (1.19.3) | V26_3 |  |
| `WARPED_WALL_SIGN` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WARPED_WART_BLOCK` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WATER` | V1_13_R1 (1.13) | V26_3 |  |
| `WATER_BUCKET` | V1_13_R1 (1.13) | V26_3 |  |
| `WATER_CAULDRON` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_CHISELED_COPPER` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_COPPER_BARS` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_COPPER_BLOCK` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_COPPER_BULB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_COPPER_CHAIN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_COPPER_CHEST` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_COPPER_DOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_COPPER_GOLEM_STATUE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_COPPER_GRATE` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_COPPER_LANTERN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_COPPER_TRAPDOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_CUT_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_CUT_COPPER_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_CUT_COPPER_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_EXPOSED_CHISELED_COPPER` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_EXPOSED_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_EXPOSED_COPPER_BARS` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_EXPOSED_COPPER_BULB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_EXPOSED_COPPER_CHAIN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_EXPOSED_COPPER_CHEST` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_EXPOSED_COPPER_DOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_EXPOSED_COPPER_GOLEM_STATUE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_EXPOSED_COPPER_GRATE` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_EXPOSED_COPPER_LANTERN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_EXPOSED_COPPER_TRAPDOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_EXPOSED_CUT_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_EXPOSED_CUT_COPPER_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_EXPOSED_CUT_COPPER_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_EXPOSED_LIGHTNING_ROD` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_LIGHTNING_ROD` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_OXIDIZED_CHISELED_COPPER` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_OXIDIZED_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_OXIDIZED_COPPER_BARS` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_OXIDIZED_COPPER_BULB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_OXIDIZED_COPPER_CHAIN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_OXIDIZED_COPPER_CHEST` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_OXIDIZED_COPPER_DOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_OXIDIZED_COPPER_GOLEM_STATUE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_OXIDIZED_COPPER_GRATE` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_OXIDIZED_COPPER_LANTERN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_OXIDIZED_COPPER_TRAPDOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_OXIDIZED_CUT_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_OXIDIZED_CUT_COPPER_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_OXIDIZED_CUT_COPPER_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_OXIDIZED_LIGHTNING_ROD` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_WEATHERED_CHISELED_COPPER` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_WEATHERED_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_WEATHERED_COPPER_BARS` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_WEATHERED_COPPER_BULB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_WEATHERED_COPPER_CHAIN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_WEATHERED_COPPER_CHEST` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_WEATHERED_COPPER_DOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_WEATHERED_COPPER_GOLEM_STATUE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_WEATHERED_COPPER_GRATE` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_WEATHERED_COPPER_LANTERN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAXED_WEATHERED_COPPER_TRAPDOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WAXED_WEATHERED_CUT_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_WEATHERED_CUT_COPPER_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_WEATHERED_CUT_COPPER_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WAXED_WEATHERED_LIGHTNING_ROD` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  |
| `WEATHERED_CHISELED_COPPER` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WEATHERED_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WEATHERED_COPPER_BARS` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WEATHERED_COPPER_BULB` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WEATHERED_COPPER_CHAIN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WEATHERED_COPPER_CHEST` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WEATHERED_COPPER_DOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WEATHERED_COPPER_GOLEM_STATUE` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WEATHERED_COPPER_GRATE` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WEATHERED_COPPER_LANTERN` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WEATHERED_COPPER_TRAPDOOR` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  |
| `WEATHERED_CUT_COPPER` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WEATHERED_CUT_COPPER_SLAB` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WEATHERED_CUT_COPPER_STAIRS` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WEATHERED_LIGHTNING_ROD` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  |
| `WEEPING_VINES` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WEEPING_VINES_PLANT` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `WET_SPONGE` | V1_13_R1 (1.13) | V26_3 |  |
| `WHEAT` | V1_13_R1 (1.13) | V26_3 |  |
| `WHEAT_SEEDS` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `WHITE_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WHITE_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `WHITE_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `WHITE_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `WHITE_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `WHITE_DYE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `WHITE_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `WHITE_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_TULIP` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `WHITE_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `WHITE_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `WILDFLOWERS` | V1_21_R4 (1.21.5) | V26_3 |  |
| `WILD_ARMOR_TRIM_SMITHING_TEMPLATE` | V1_19_R3 (1.19.4) | V26_3 |  |
| `WIND_CHARGE` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `WITCH_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `WITHER_ROSE` | V1_14_R1 (1.14.x) | V26_3 |  |
| `WITHER_SKELETON_SKULL` | V1_13_R1 (1.13) | V26_3 |  |
| `WITHER_SKELETON_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `WITHER_SKELETON_WALL_SKULL` | V1_13_R1 (1.13) | V26_3 |  |
| `WITHER_SPAWN_EGG` | V1_19_R2 (1.19.3) | V26_3 |  |
| `WOLF_ARMOR` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  |
| `WOLF_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `WOODEN_AXE` | V1_13_R1 (1.13) | V26_3 |  |
| `WOODEN_HOE` | V1_13_R1 (1.13) | V26_3 |  |
| `WOODEN_PICKAXE` | V1_13_R1 (1.13) | V26_3 |  |
| `WOODEN_SHOVEL` | V1_13_R1 (1.13) | V26_3 |  |
| `WOODEN_SPEAR` | V1_21_R7 (1.21.11) | V26_3 |  |
| `WOODEN_SWORD` | V1_13_R1 (1.13) | V26_3 |  |
| `WOODLAND_MANSION_MAP` | V26_3 (26.3+) | V26_3 |  |
| `WRITABLE_BOOK` | V1_13_R1 (1.13) | V26_3 |  |
| `WRITTEN_BOOK` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_BED` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_BUNDLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  |
| `YELLOW_CANDLE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `YELLOW_CANDLE_CAKE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `YELLOW_CARPET` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_CONCRETE` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_CONCRETE_POWDER` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_CONCRETE_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `YELLOW_CONCRETE_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `YELLOW_CUSHION` | V26_3 (26.3+) | V26_3 |  |
| `YELLOW_DYE` | V1_14_R1 (1.14.x) | V26_3 | ← `DANDELION_YELLOW` |
| `YELLOW_GLAZED_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_HARNESS` | V1_21_R5 (1.21.6 - 1.21.8) | V26_3 |  |
| `YELLOW_POPLAR_LEAVES` | V26_3 (26.3+) | V26_3 |  |
| `YELLOW_SHULKER_BOX` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_STAINED_GLASS` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_STAINED_GLASS_PANE` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_TERRACOTTA` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_WALL_BANNER` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_WOOL` | V1_13_R1 (1.13) | V26_3 |  |
| `YELLOW_WOOL_SLAB` | V26_3 (26.3+) | V26_3 |  |
| `YELLOW_WOOL_STAIRS` | V26_3 (26.3+) | V26_3 |  |
| `ZOGLIN_SPAWN_EGG` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  |
| `ZOMBIE_HEAD` | V1_13_R1 (1.13) | V26_3 |  |
| `ZOMBIE_HORSE_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `ZOMBIE_NAUTILUS_SPAWN_EGG` | V1_21_R7 (1.21.11) | V26_3 |  |
| `ZOMBIE_PIGMAN_SPAWN_EGG` | V1_13_R1 (1.13) | V1_15_R1 | → `ZOMBIFIED_PIGLIN_SPAWN_EGG` (1.16) |
| `ZOMBIE_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `ZOMBIE_VILLAGER_SPAWN_EGG` | V1_13_R1 (1.13) | V26_3 |  |
| `ZOMBIE_WALL_HEAD` | V1_13_R1 (1.13) | V26_3 |  |
| `ZOMBIFIED_PIGLIN_SPAWN_EGG` | V1_16_R1 (1.16 - 1.16.1) | V26_3 | ← `ZOMBIE_PIGMAN_SPAWN_EGG` |
