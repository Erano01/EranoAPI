# Parçacıklar (`Particle`) ve efektler (`Effect`), 1.8 - 26.3

Hangi parçacık ve dünya efekti hangi revizyonda var, adı ne zaman değişti. EranoAPI'nin `EranoParticle` ve `EranoEffect`'i bu tablolardan üretilir.

Döküm her NMS revizyonunun Spigot API'sinin bytecode'undan (sınıflar yüklenmeden; registry tabanlı değerler de),
isim değişiklikleri CraftBukkit'in kendi tablolarından (`legacy/FieldRename.java`); hepsi
`scripts/registry-report/run.sh` ile yeniden üretilir. Revizyonlar ve yöntem: [Material.md](Material.md),
[Versioned-APIs.md](Versioned-APIs.md).

## Parçacıklar

| Sürümler | Sistem | Veri |
|---|---|---|
| 1.8 - 1.8.8 | Bukkit'te parçacık API'si yok. NMS `EnumParticle` + `PacketPlayOutWorldParticles` (Spigot'un `Effect` parçacık girdileri de aynı paketi yollar) | `int[]`: eşya `{id, veri}`, blok `{id \| veri << 12}`; kızıltaş rengi ofsetlerde |
| 1.9 - 1.12.2 | `Particle` enum'u, adları 1.8'in `EnumParticle`'ıyla aynı | `ItemStack`, `MaterialData` |
| 1.13 - 1.20.4 | Aynı adlar; blok verisi `BlockData`, kızıltaş `DustOptions`; eskileri `LEGACY_BLOCK_CRACK` ... | `BlockData`, `DustOptions`, `ItemStack` ... |
| 1.20.5 - 26.3 | Sabitler Minecraft'ın adlarını aldı (`REDSTONE` → `DUST`, `BLOCK_CRACK` → `BLOCK`) | `ENTITY_EFFECT` artık `Color` ister |

1.8'in parçacıkları buradaki tablolarda `EnumParticle`'dan (Spigot sunucu jar'ı) okundu: 1.9'un `Particle`'ı onun
adlarını aldı, bu yüzden bir parçacığın 1.8'deki adı 1.9 - 1.20.4'teki adıdır. `LEGACY_` sabitleri (1.13 - 1.20.4,
eski `MaterialData` ile) tablolarda yok.

### İsim değişiklikleri

CraftBukkit `legacy/FieldRename.java` `PARTICLE_DATA`; her biri dökümlerde doğrulandı (eski ad, yeninin geldiği
revizyonda kayboluyor).

| Eski ad | Yeni ad | Revizyon |
|---|---|---|
| `EXPLOSION_NORMAL` | `POOF` | V1_20_R4 (1.20.5 - 1.20.6) |
| `EXPLOSION_LARGE` | `EXPLOSION` | V1_20_R4 (1.20.5 - 1.20.6) |
| `EXPLOSION_HUGE` | `EXPLOSION_EMITTER` | V1_20_R4 (1.20.5 - 1.20.6) |
| `FIREWORKS_SPARK` | `FIREWORK` | V1_20_R4 (1.20.5 - 1.20.6) |
| `WATER_BUBBLE` | `BUBBLE` | V1_20_R4 (1.20.5 - 1.20.6) |
| `WATER_SPLASH` | `SPLASH` | V1_20_R4 (1.20.5 - 1.20.6) |
| `WATER_WAKE` | `FISHING` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SUSPENDED` | `UNDERWATER` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SUSPENDED_DEPTH` | `UNDERWATER` | V1_20_R4 (1.20.5 - 1.20.6) |
| `CRIT_MAGIC` | `ENCHANTED_HIT` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SMOKE_NORMAL` | `SMOKE` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SMOKE_LARGE` | `LARGE_SMOKE` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SPELL` | `EFFECT` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SPELL_INSTANT` | `INSTANT_EFFECT` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SPELL_MOB` | `ENTITY_EFFECT` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SPELL_WITCH` | `WITCH` | V1_20_R4 (1.20.5 - 1.20.6) |
| `DRIP_WATER` | `DRIPPING_WATER` | V1_20_R4 (1.20.5 - 1.20.6) |
| `DRIP_LAVA` | `DRIPPING_LAVA` | V1_20_R4 (1.20.5 - 1.20.6) |
| `VILLAGER_ANGRY` | `ANGRY_VILLAGER` | V1_20_R4 (1.20.5 - 1.20.6) |
| `VILLAGER_HAPPY` | `HAPPY_VILLAGER` | V1_20_R4 (1.20.5 - 1.20.6) |
| `TOWN_AURA` | `MYCELIUM` | V1_20_R4 (1.20.5 - 1.20.6) |
| `ENCHANTMENT_TABLE` | `ENCHANT` | V1_20_R4 (1.20.5 - 1.20.6) |
| `REDSTONE` | `DUST` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SNOWBALL` | `ITEM_SNOWBALL` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SNOW_SHOVEL` | `ITEM_SNOWBALL` | V1_20_R4 (1.20.5 - 1.20.6) |
| `SLIME` | `ITEM_SLIME` | V1_20_R4 (1.20.5 - 1.20.6) |
| `ITEM_CRACK` | `ITEM` | V1_20_R4 (1.20.5 - 1.20.6) |
| `BLOCK_CRACK` | `BLOCK` | V1_20_R4 (1.20.5 - 1.20.6) |
| `BLOCK_DUST` | `BLOCK` | V1_20_R4 (1.20.5 - 1.20.6) |
| `WATER_DROP` | `RAIN` | V1_20_R4 (1.20.5 - 1.20.6) |
| `MOB_APPEARANCE` | `ELDER_GUARDIAN` | V1_20_R4 (1.20.5 - 1.20.6) |
| `TOTEM` | `TOTEM_OF_UNDYING` | V1_20_R4 (1.20.5 - 1.20.6) |
| `GUST_EMITTER` | `GUST_EMITTER_LARGE` | V1_20_R4 (1.20.5 - 1.20.6) |

Tabloda olmayan değişiklikler (CraftBukkit eşlemedi, eski ad kaldırıldı):
`BARRIER` ve `LIGHT` 1.18'de `BLOCK_MARKER`'a (bariyer / ışık bloğu verisiyle) döndü; `DRIPPING_CHERRY_LEAVES`,
`FALLING_CHERRY_LEAVES`, `LANDING_CHERRY_LEAVES` 1.20'de tek `CHERRY_LEAVES` oldu; `FOOTSTEP` ve `ITEM_TAKE` 1.13'te,
`GUST_DUST` 1.20.5'te kalktı.

### Revizyon revizyon

| Geçiş | Eklenen | Kaldırılan |
|---|---|---|
| V1_8_R3 → V1_9_R1 (1.9 - 1.9.3) | `DAMAGE_INDICATOR`, `DRAGON_BREATH`, `END_ROD`, `SWEEP_ATTACK` |  |
| V1_9_R2 → V1_10_R1 (1.10.x) | `FALLING_DUST` |  |
| V1_10_R1 → V1_11_R1 (1.11.x) | `SPIT`, `TOTEM` |  |
| V1_12_R1 → V1_13_R1 (1.13) | `BUBBLE_COLUMN_UP`, `BUBBLE_POP`, `CURRENT_DOWN`, `DOLPHIN`, `NAUTILUS`, `SQUID_INK` | `FOOTSTEP`, `ITEM_TAKE` |
| V1_13_R2 → V1_14_R1 (1.14.x) | `CAMPFIRE_COSY_SMOKE`, `CAMPFIRE_SIGNAL_SMOKE`, `COMPOSTER`, `FALLING_LAVA`, `FALLING_WATER`, `FLASH`, `LANDING_LAVA`, `SNEEZE` |  |
| V1_14_R1 → V1_15_R1 (1.15.x) | `DRIPPING_HONEY`, `FALLING_HONEY`, `FALLING_NECTAR`, `LANDING_HONEY` |  |
| V1_15_R1 → V1_16_R1 (1.16 - 1.16.1) | `ASH`, `CRIMSON_SPORE`, `DRIPPING_OBSIDIAN_TEAR`, `FALLING_OBSIDIAN_TEAR`, `LANDING_OBSIDIAN_TEAR`, `REVERSE_PORTAL`, `SOUL`, `SOUL_FIRE_FLAME`, `WARPED_SPORE`, `WHITE_ASH` |  |
| V1_16_R3 → V1_17_R1 (1.17.x) | `DRIPPING_DRIPSTONE_LAVA`, `DRIPPING_DRIPSTONE_WATER`, `DUST_COLOR_TRANSITION`, `ELECTRIC_SPARK`, `FALLING_DRIPSTONE_LAVA`, `FALLING_DRIPSTONE_WATER`, `FALLING_SPORE_BLOSSOM`, `GLOW`, `GLOW_SQUID_INK`, `LIGHT`, `SCRAPE`, `SMALL_FLAME`, `SNOWFLAKE`, `SPORE_BLOSSOM_AIR`, `VIBRATION`, `WAX_OFF`, `WAX_ON` |  |
| V1_17_R1 → V1_18_R1 (1.18 - 1.18.1) | `BLOCK_MARKER` | `BARRIER`, `LIGHT` |
| V1_18_R2 → V1_19_R1 (1.19 - 1.19.2) | `SCULK_CHARGE`, `SCULK_CHARGE_POP`, `SCULK_SOUL`, `SHRIEK`, `SONIC_BOOM` |  |
| V1_19_R2 → V1_19_R3 (1.19.4) | `DRIPPING_CHERRY_LEAVES`, `FALLING_CHERRY_LEAVES`, `LANDING_CHERRY_LEAVES` |  |
| V1_19_R3 → V1_20_R1 (1.20 - 1.20.1) | `CHERRY_LEAVES`, `EGG_CRACK` | `DRIPPING_CHERRY_LEAVES`, `FALLING_CHERRY_LEAVES`, `LANDING_CHERRY_LEAVES` |
| V1_20_R2 → V1_20_R3 (1.20.3 - 1.20.4) | `DUST_PLUME`, `GUST`, `GUST_DUST`, `GUST_EMITTER`, `TRIAL_SPAWNER_DETECTION`, `WHITE_SMOKE` |  |
| V1_20_R3 → V1_20_R4 (1.20.5 - 1.20.6) | 40 değer (aşağıdaki tabloda) | 35 değer (aşağıdaki tabloda) |
| V1_21_R1 → V1_21_R2 (1.21.2 - 1.21.3) | `BLOCK_CRUMBLE`, `TRAIL` |  |
| V1_21_R2 → V1_21_R3 (1.21.4) | `PALE_OAK_LEAVES` |  |
| V1_21_R3 → V1_21_R4 (1.21.5) | `FIREFLY`, `TINTED_LEAVES` |  |
| V1_21_R5 → V1_21_R6 (1.21.9 - 1.21.10) | `COPPER_FIRE_FLAME` |  |
| V1_21_R7 → V26_1 (26.1.x) | `PAUSE_MOB_GROWTH`, `RESET_MOB_GROWTH` |  |
| V26_1 → V26_2 (26.2.x) | `GEYSER`, `GEYSER_BASE`, `GEYSER_PLUME`, `GEYSER_POOF`, `NOXIOUS_GAS`, `NOXIOUS_GAS_CLOUD`, `SULFUR_BUBBLES`, `SULFUR_CUBE_GOO` |  |
| V26_2 → V26_3 (26.3+) | `ORANGE_POPLAR_LEAVES`, `RED_POPLAR_LEAVES`, `YELLOW_POPLAR_LEAVES` |  |

### Bütün değerler

En yeni adıyla; "Eski adlar" en yeniden eskiye, sunucuda bu sırayla aranır.

| Ad | İlk revizyon | Son revizyon | Eski adlar | Anahtar |
|---|---|---|---|---|
| `ANGRY_VILLAGER` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `VILLAGER_ANGRY` | `angry_villager` |
| `BARRIER` | V1_8_R1 (1.8 - 1.8.2) | V1_17_R1 |  | `barrier` |
| `BLOCK` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `BLOCK_CRACK`, `BLOCK_DUST` | `block` |
| `BUBBLE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `WATER_BUBBLE` | `bubble` |
| `CLOUD` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `cloud` |
| `CRIT` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `crit` |
| `DRIPPING_LAVA` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `DRIP_LAVA` | `dripping_lava` |
| `DRIPPING_WATER` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `DRIP_WATER` | `dripping_water` |
| `DUST` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `REDSTONE` | `dust` |
| `EFFECT` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SPELL` | `effect` |
| `ELDER_GUARDIAN` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `MOB_APPEARANCE` | `elder_guardian` |
| `ENCHANT` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `ENCHANTMENT_TABLE` | `enchant` |
| `ENCHANTED_HIT` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `CRIT_MAGIC` | `enchanted_hit` |
| `ENTITY_EFFECT` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SPELL_MOB` | `entity_effect` |
| `EXPLOSION` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `EXPLOSION_LARGE` | `explosion` |
| `EXPLOSION_EMITTER` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `EXPLOSION_HUGE` | `explosion_emitter` |
| `FIREWORK` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `FIREWORKS_SPARK` | `firework` |
| `FISHING` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `WATER_WAKE` | `fishing` |
| `FLAME` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `flame` |
| `FOOTSTEP` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  | `footstep` |
| `HAPPY_VILLAGER` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `VILLAGER_HAPPY` | `happy_villager` |
| `HEART` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `heart` |
| `INSTANT_EFFECT` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SPELL_INSTANT` | `instant_effect` |
| `ITEM` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `ITEM_CRACK` | `item` |
| `ITEM_SLIME` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SLIME` | `item_slime` |
| `ITEM_SNOWBALL` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SNOWBALL`, `SNOW_SHOVEL` | `item_snowball` |
| `ITEM_TAKE` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  | `take` |
| `LARGE_SMOKE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SMOKE_LARGE` | `large_smoke` |
| `LAVA` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `lava` |
| `MYCELIUM` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `TOWN_AURA` | `mycelium` |
| `NOTE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `note` |
| `POOF` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `EXPLOSION_NORMAL` | `poof` |
| `PORTAL` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  | `portal` |
| `RAIN` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `WATER_DROP` | `rain` |
| `SMOKE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SMOKE_NORMAL` | `smoke` |
| `SPELL_MOB_AMBIENT` | V1_8_R1 (1.8 - 1.8.2) | V1_20_R3 |  | `ambient_entity_effect` |
| `SPLASH` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `WATER_SPLASH` | `splash` |
| `UNDERWATER` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SUSPENDED`, `SUSPENDED_DEPTH` | `underwater` |
| `WITCH` | V1_8_R1 (1.8 - 1.8.2) | V26_3 | `SPELL_WITCH` | `witch` |
| `DAMAGE_INDICATOR` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `damage_indicator` |
| `DRAGON_BREATH` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `dragon_breath` |
| `END_ROD` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `end_rod` |
| `SWEEP_ATTACK` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  | `sweep_attack` |
| `FALLING_DUST` | V1_10_R1 (1.10.x) | V26_3 |  | `falling_dust` |
| `SPIT` | V1_11_R1 (1.11.x) | V26_3 |  | `spit` |
| `TOTEM_OF_UNDYING` | V1_11_R1 (1.11.x) | V26_3 | `TOTEM` | `totem_of_undying` |
| `BUBBLE_COLUMN_UP` | V1_13_R1 (1.13) | V26_3 |  | `bubble_column_up` |
| `BUBBLE_POP` | V1_13_R1 (1.13) | V26_3 |  | `bubble_pop` |
| `CURRENT_DOWN` | V1_13_R1 (1.13) | V26_3 |  | `current_down` |
| `DOLPHIN` | V1_13_R1 (1.13) | V26_3 |  | `dolphin` |
| `NAUTILUS` | V1_13_R1 (1.13) | V26_3 |  | `nautilus` |
| `SQUID_INK` | V1_13_R1 (1.13) | V26_3 |  | `squid_ink` |
| `CAMPFIRE_COSY_SMOKE` | V1_14_R1 (1.14.x) | V26_3 |  | `campfire_cosy_smoke` |
| `CAMPFIRE_SIGNAL_SMOKE` | V1_14_R1 (1.14.x) | V26_3 |  | `campfire_signal_smoke` |
| `COMPOSTER` | V1_14_R1 (1.14.x) | V26_3 |  | `composter` |
| `FALLING_LAVA` | V1_14_R1 (1.14.x) | V26_3 |  | `falling_lava` |
| `FALLING_WATER` | V1_14_R1 (1.14.x) | V26_3 |  | `falling_water` |
| `FLASH` | V1_14_R1 (1.14.x) | V26_3 |  | `flash` |
| `LANDING_LAVA` | V1_14_R1 (1.14.x) | V26_3 |  | `landing_lava` |
| `SNEEZE` | V1_14_R1 (1.14.x) | V26_3 |  | `sneeze` |
| `DRIPPING_HONEY` | V1_15_R1 (1.15.x) | V26_3 |  | `dripping_honey` |
| `FALLING_HONEY` | V1_15_R1 (1.15.x) | V26_3 |  | `falling_honey` |
| `FALLING_NECTAR` | V1_15_R1 (1.15.x) | V26_3 |  | `falling_nectar` |
| `LANDING_HONEY` | V1_15_R1 (1.15.x) | V26_3 |  | `landing_honey` |
| `ASH` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `ash` |
| `CRIMSON_SPORE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `crimson_spore` |
| `DRIPPING_OBSIDIAN_TEAR` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `dripping_obsidian_tear` |
| `FALLING_OBSIDIAN_TEAR` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `falling_obsidian_tear` |
| `LANDING_OBSIDIAN_TEAR` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `landing_obsidian_tear` |
| `REVERSE_PORTAL` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `reverse_portal` |
| `SOUL` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `soul` |
| `SOUL_FIRE_FLAME` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `soul_fire_flame` |
| `WARPED_SPORE` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `warped_spore` |
| `WHITE_ASH` | V1_16_R1 (1.16 - 1.16.1) | V26_3 |  | `white_ash` |
| `DRIPPING_DRIPSTONE_LAVA` | V1_17_R1 (1.17.x) | V26_3 |  | `dripping_dripstone_lava` |
| `DRIPPING_DRIPSTONE_WATER` | V1_17_R1 (1.17.x) | V26_3 |  | `dripping_dripstone_water` |
| `DUST_COLOR_TRANSITION` | V1_17_R1 (1.17.x) | V26_3 |  | `dust_color_transition` |
| `ELECTRIC_SPARK` | V1_17_R1 (1.17.x) | V26_3 |  | `electric_spark` |
| `FALLING_DRIPSTONE_LAVA` | V1_17_R1 (1.17.x) | V26_3 |  | `falling_dripstone_lava` |
| `FALLING_DRIPSTONE_WATER` | V1_17_R1 (1.17.x) | V26_3 |  | `falling_dripstone_water` |
| `FALLING_SPORE_BLOSSOM` | V1_17_R1 (1.17.x) | V26_3 |  | `falling_spore_blossom` |
| `GLOW` | V1_17_R1 (1.17.x) | V26_3 |  | `glow` |
| `GLOW_SQUID_INK` | V1_17_R1 (1.17.x) | V26_3 |  | `glow_squid_ink` |
| `LIGHT` | V1_17_R1 (1.17.x) | V1_17_R1 |  |  |
| `SCRAPE` | V1_17_R1 (1.17.x) | V26_3 |  | `scrape` |
| `SMALL_FLAME` | V1_17_R1 (1.17.x) | V26_3 |  | `small_flame` |
| `SNOWFLAKE` | V1_17_R1 (1.17.x) | V26_3 |  | `snowflake` |
| `SPORE_BLOSSOM_AIR` | V1_17_R1 (1.17.x) | V26_3 |  | `spore_blossom_air` |
| `VIBRATION` | V1_17_R1 (1.17.x) | V26_3 |  | `vibration` |
| `WAX_OFF` | V1_17_R1 (1.17.x) | V26_3 |  | `wax_off` |
| `WAX_ON` | V1_17_R1 (1.17.x) | V26_3 |  | `wax_on` |
| `BLOCK_MARKER` | V1_18_R1 (1.18 - 1.18.1) | V26_3 |  | `block_marker` |
| `SCULK_CHARGE` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  | `sculk_charge` |
| `SCULK_CHARGE_POP` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  | `sculk_charge_pop` |
| `SCULK_SOUL` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  | `sculk_soul` |
| `SHRIEK` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  | `shriek` |
| `SONIC_BOOM` | V1_19_R1 (1.19 - 1.19.2) | V26_3 |  | `sonic_boom` |
| `DRIPPING_CHERRY_LEAVES` | V1_19_R3 (1.19.4) | V1_19_R3 |  |  |
| `FALLING_CHERRY_LEAVES` | V1_19_R3 (1.19.4) | V1_19_R3 |  |  |
| `LANDING_CHERRY_LEAVES` | V1_19_R3 (1.19.4) | V1_19_R3 |  |  |
| `CHERRY_LEAVES` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  | `cherry_leaves` |
| `EGG_CRACK` | V1_20_R1 (1.20 - 1.20.1) | V26_3 |  | `egg_crack` |
| `DUST_PLUME` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  | `dust_plume` |
| `GUST` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  | `gust` |
| `GUST_DUST` | V1_20_R3 (1.20.3 - 1.20.4) | V1_20_R3 |  | `gust_dust` |
| `GUST_EMITTER_LARGE` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 | `GUST_EMITTER` | `gust_emitter_large` |
| `TRIAL_SPAWNER_DETECTION` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  | `trial_spawner_detection` |
| `WHITE_SMOKE` | V1_20_R3 (1.20.3 - 1.20.4) | V26_3 |  | `white_smoke` |
| `DUST_PILLAR` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `dust_pillar` |
| `GUST_EMITTER_SMALL` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `gust_emitter_small` |
| `INFESTED` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `infested` |
| `ITEM_COBWEB` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `item_cobweb` |
| `OMINOUS_SPAWNING` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `ominous_spawning` |
| `RAID_OMEN` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `raid_omen` |
| `SMALL_GUST` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `small_gust` |
| `TRIAL_OMEN` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `trial_omen` |
| `TRIAL_SPAWNER_DETECTION_OMINOUS` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `trial_spawner_detection_ominous` |
| `VAULT_CONNECTION` | V1_20_R4 (1.20.5 - 1.20.6) | V26_3 |  | `vault_connection` |
| `BLOCK_CRUMBLE` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  | `block_crumble` |
| `TRAIL` | V1_21_R2 (1.21.2 - 1.21.3) | V26_3 |  | `trail` |
| `PALE_OAK_LEAVES` | V1_21_R3 (1.21.4) | V26_3 |  | `pale_oak_leaves` |
| `FIREFLY` | V1_21_R4 (1.21.5) | V26_3 |  | `firefly` |
| `TINTED_LEAVES` | V1_21_R4 (1.21.5) | V26_3 |  | `tinted_leaves` |
| `COPPER_FIRE_FLAME` | V1_21_R6 (1.21.9 - 1.21.10) | V26_3 |  | `copper_fire_flame` |
| `PAUSE_MOB_GROWTH` | V26_1 (26.1.x) | V26_3 |  | `pause_mob_growth` |
| `RESET_MOB_GROWTH` | V26_1 (26.1.x) | V26_3 |  | `reset_mob_growth` |
| `GEYSER` | V26_2 (26.2.x) | V26_3 |  | `geyser` |
| `GEYSER_BASE` | V26_2 (26.2.x) | V26_3 |  | `geyser_base` |
| `GEYSER_PLUME` | V26_2 (26.2.x) | V26_3 |  | `geyser_plume` |
| `GEYSER_POOF` | V26_2 (26.2.x) | V26_3 |  | `geyser_poof` |
| `NOXIOUS_GAS` | V26_2 (26.2.x) | V26_3 |  | `noxious_gas` |
| `NOXIOUS_GAS_CLOUD` | V26_2 (26.2.x) | V26_3 |  | `noxious_gas_cloud` |
| `SULFUR_BUBBLES` | V26_2 (26.2.x) | V26_3 |  | `sulfur_bubbles` |
| `SULFUR_CUBE_GOO` | V26_2 (26.2.x) | V26_3 |  | `sulfur_cube_goo` |
| `ORANGE_POPLAR_LEAVES` | V26_3 (26.3+) | V26_3 |  | `orange_poplar_leaves` |
| `RED_POPLAR_LEAVES` | V26_3 (26.3+) | V26_3 |  | `red_poplar_leaves` |
| `YELLOW_POPLAR_LEAVES` | V26_3 (26.3+) | V26_3 |  | `yellow_poplar_leaves` |

## Efektler

`World#playEffect`: ses ya da görüntü olan dünya olayları (kapı sesi, `STEP_SOUND` blok kırılma parçacıkları ...).
Adları hiç değişmedi; sadece eklendiler. 1.9 - 1.12'de Spigot'un eklediği parçacık girdileri (`FLAME`,
`HAPPY_VILLAGER` ...) 1.13'te kalktı: onlar parçacık, `EranoParticle` ile.

### Revizyon revizyon

| Geçiş | Eklenen | Kaldırılan |
|---|---|---|
| V1_8_R3 → V1_9_R1 (1.9 - 1.9.3) | `ANVIL_BREAK`, `ANVIL_LAND`, `ANVIL_USE`, `BAT_TAKEOFF`, `BREWING_STAND_BREW`, `CHORUS_FLOWER_DEATH`, `CHORUS_FLOWER_GROW`, `DOOR_CLOSE`, `DRAGON_BREATH`, `ENDERDRAGON_GROWL`, `ENDERDRAGON_SHOOT`, `ENDEREYE_LAUNCH`, `END_GATEWAY_SPAWN`, `FENCE_GATE_CLOSE`, `FENCE_GATE_TOGGLE`, `FIREWORK_SHOOT`, `IRON_DOOR_CLOSE`, `IRON_DOOR_TOGGLE`, `IRON_TRAPDOOR_CLOSE`, `IRON_TRAPDOOR_TOGGLE`, `PORTAL_TRAVEL`, `TRAPDOOR_CLOSE`, `TRAPDOOR_TOGGLE`, `VILLAGER_PLANT_GROW`, `WITHER_BREAK_BLOCK`, `WITHER_SHOOT`, `ZOMBIE_CONVERTED_VILLAGER`, `ZOMBIE_INFECT` |  |
| V1_12_R1 → V1_13_R1 (1.13) |  | `CLOUD`, `COLOURED_DUST`, `CRIT`, `EXPLOSION`, `EXPLOSION_HUGE`, `EXPLOSION_LARGE`, `FIREWORKS_SPARK`, `FLAME`, `FLYING_GLYPH`, `FOOTSTEP`, `HAPPY_VILLAGER`, `HEART`, `INSTANT_SPELL`, `ITEM_BREAK`, `LARGE_SMOKE`, `LAVADRIP`, `LAVA_POP`, `MAGIC_CRIT`, `NOTE`, `PARTICLE_SMOKE`, `PORTAL`, `POTION_SWIRL`, `POTION_SWIRL_TRANSPARENT`, `SLIME`, `SMALL_SMOKE`, `SNOWBALL_BREAK`, `SNOW_SHOVEL`, `SPELL`, `SPLASH`, `TILE_BREAK`, `TILE_DUST`, `VILLAGER_THUNDERCLOUD`, `VOID_FOG`, `WATERDRIP`, `WITCH_MAGIC` |
| V1_14_R1 → V1_15_R1 (1.15.x) | `INSTANT_POTION_BREAK` |  |
| V1_16_R3 → V1_17_R1 (1.17.x) | `BONE_MEAL_USE`, `BOOK_PAGE_TURN`, `COMPOSTER_FILL_ATTEMPT`, `COPPER_WAX_OFF`, `COPPER_WAX_ON`, `DRIPPING_DRIPSTONE`, `ELECTRIC_SPARK`, `ENDER_DRAGON_DESTROY_BLOCK`, `END_PORTAL_FRAME_FILL`, `GRINDSTONE_USE`, `HUSK_CONVERTED_TO_ZOMBIE`, `LAVA_INTERACT`, `OXIDISED_COPPER_SCRAPE`, `PHANTOM_BITE`, `POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON`, `POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON`, `POINTED_DRIPSTONE_LAND`, `REDSTONE_TORCH_BURNOUT`, `SKELETON_CONVERTED_TO_STRAY`, `SMITHING_TABLE_USE`, `SPONGE_DRY`, `ZOMBIE_CONVERTED_TO_DROWNED` |  |

### Bütün değerler

En yeni adıyla; "Eski adlar" en yeniden eskiye, sunucuda bu sırayla aranır.

| Ad | İlk revizyon | Son revizyon | Eski adlar |
|---|---|---|---|
| `BLAZE_SHOOT` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `BOW_FIRE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `CLICK1` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `CLICK2` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `CLOUD` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `COLOURED_DUST` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `CRIT` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `DOOR_TOGGLE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `ENDER_SIGNAL` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `EXPLOSION` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `EXPLOSION_HUGE` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `EXPLOSION_LARGE` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `EXTINGUISH` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `FIREWORKS_SPARK` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `FLAME` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `FLYING_GLYPH` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `FOOTSTEP` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `GHAST_SHOOT` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `GHAST_SHRIEK` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `HAPPY_VILLAGER` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `HEART` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `INSTANT_SPELL` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `ITEM_BREAK` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `LARGE_SMOKE` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `LAVADRIP` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `LAVA_POP` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `MAGIC_CRIT` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `MOBSPAWNER_FLAMES` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `NOTE` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `PARTICLE_SMOKE` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `PORTAL` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `POTION_BREAK` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `POTION_SWIRL` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `POTION_SWIRL_TRANSPARENT` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `RECORD_PLAY` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `SLIME` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `SMALL_SMOKE` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `SMOKE` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `SNOWBALL_BREAK` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `SNOW_SHOVEL` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `SPELL` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `SPLASH` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `STEP_SOUND` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `TILE_BREAK` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `TILE_DUST` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `VILLAGER_THUNDERCLOUD` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `VOID_FOG` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `WATERDRIP` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `WITCH_MAGIC` | V1_8_R1 (1.8 - 1.8.2) | V1_12_R1 |  |
| `ZOMBIE_CHEW_IRON_DOOR` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `ZOMBIE_CHEW_WOODEN_DOOR` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `ZOMBIE_DESTROY_DOOR` | V1_8_R1 (1.8 - 1.8.2) | V26_3 |  |
| `ANVIL_BREAK` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `ANVIL_LAND` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `ANVIL_USE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `BAT_TAKEOFF` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `BREWING_STAND_BREW` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `CHORUS_FLOWER_DEATH` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `CHORUS_FLOWER_GROW` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `DOOR_CLOSE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `DRAGON_BREATH` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `ENDERDRAGON_GROWL` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `ENDERDRAGON_SHOOT` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `ENDEREYE_LAUNCH` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `END_GATEWAY_SPAWN` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `FENCE_GATE_CLOSE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `FENCE_GATE_TOGGLE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `FIREWORK_SHOOT` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `IRON_DOOR_CLOSE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `IRON_DOOR_TOGGLE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `IRON_TRAPDOOR_CLOSE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `IRON_TRAPDOOR_TOGGLE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `PORTAL_TRAVEL` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `TRAPDOOR_CLOSE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `TRAPDOOR_TOGGLE` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `VILLAGER_PLANT_GROW` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `WITHER_BREAK_BLOCK` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `WITHER_SHOOT` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `ZOMBIE_CONVERTED_VILLAGER` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `ZOMBIE_INFECT` | V1_9_R1 (1.9 - 1.9.3) | V26_3 |  |
| `INSTANT_POTION_BREAK` | V1_15_R1 (1.15.x) | V26_3 |  |
| `BONE_MEAL_USE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `BOOK_PAGE_TURN` | V1_17_R1 (1.17.x) | V26_3 |  |
| `COMPOSTER_FILL_ATTEMPT` | V1_17_R1 (1.17.x) | V26_3 |  |
| `COPPER_WAX_OFF` | V1_17_R1 (1.17.x) | V26_3 |  |
| `COPPER_WAX_ON` | V1_17_R1 (1.17.x) | V26_3 |  |
| `DRIPPING_DRIPSTONE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `ELECTRIC_SPARK` | V1_17_R1 (1.17.x) | V26_3 |  |
| `ENDER_DRAGON_DESTROY_BLOCK` | V1_17_R1 (1.17.x) | V26_3 |  |
| `END_PORTAL_FRAME_FILL` | V1_17_R1 (1.17.x) | V26_3 |  |
| `GRINDSTONE_USE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `HUSK_CONVERTED_TO_ZOMBIE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `LAVA_INTERACT` | V1_17_R1 (1.17.x) | V26_3 |  |
| `OXIDISED_COPPER_SCRAPE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `PHANTOM_BITE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON` | V1_17_R1 (1.17.x) | V26_3 |  |
| `POINTED_DRIPSTONE_LAND` | V1_17_R1 (1.17.x) | V26_3 |  |
| `REDSTONE_TORCH_BURNOUT` | V1_17_R1 (1.17.x) | V26_3 |  |
| `SKELETON_CONVERTED_TO_STRAY` | V1_17_R1 (1.17.x) | V26_3 |  |
| `SMITHING_TABLE_USE` | V1_17_R1 (1.17.x) | V26_3 |  |
| `SPONGE_DRY` | V1_17_R1 (1.17.x) | V26_3 |  |
| `ZOMBIE_CONVERTED_TO_DROWNED` | V1_17_R1 (1.17.x) | V26_3 |  |
