# EranoAPI docs

What EranoAPI gives a plugin or mod, by feature. Setup and coordinates: the [project README](../README.md).

## Spigot (1.8 - 26.x, one jar)

| Feature | Entry point | How |
|---|---|---|
| Materials | `EranoMaterial` | Today's names everywhere; data values on 1.8 - 1.12. [Material.md](Material.md) |
| Sounds | `EranoSound` | Today's names; 1.9 / 1.13 renames matched by Mojang's sound files. [Sound.md](Sound.md) |
| Particles, effects | `EranoParticle`, `EranoEffect` | Today's names, data converted per version; packets on 1.8. [Particle.md](Particle.md) |
| Potions | `EranoPotionEffect`, `EranoPotionType` | Effects and potion items. [Potion.md](Potion.md) |
| Enchantments | `EranoEnchantment` | [Enchantment.md](Enchantment.md) |
| Action bar, titles | `EranoServices.messages()` | Packets on 1.8 - 1.10, Bukkit after |
| Boss bars | `EranoServices.bossBars()` | Bukkit `BossBar` on 1.9+; a client-side wither on 1.8 (no colors) |
| Holograms | `EranoServices.holograms()` | Per viewer, never saved to the world: packets on 1.8 - 1.12, entities hidden from others on 1.13+ (`TextDisplay` on 1.19.4+) |
| Menus | `Menu`, `MenuButton` | Inventory menus; clicks, shift-clicks and drags can't move items in |
| TPS | `TPSHandlerFactory` | Paper's `getTPS()`, NMS on Spigot |
| Plugin channels | `BukkitServerNetwork` | Raw byte messages to client mods (same `ServerNetwork` contract as Forge / Fabric) |
| Server version | `ServerVersion.current()` | Comparable `MinecraftVersion`, 26.x included |

A service never fails on a version it doesn't cover: `isSupported()` is `false` and calls do nothing.

## Other platforms and modules

| Module | What |
|---|---|
| `EranoAPI-Common` | Platform-free contracts: `MinecraftVersion`, `VersionRange`, versioned service selection, `ClientNetwork` / `ServerNetwork` |
| `EranoAPI-Forge`, `EranoAPI-Fabric` | One jar per Minecraft version (1.21.11, 26.1.2, 26.3), same API: plugin channels for client mods |
| `EranoAPI-Cluster` | A minigame network's shared state over MySQL or Redis: arenas, joins with seat reservation, Quick Join, locks. [Cluster/README.md](../Cluster/README.md) |

## More

- [Architecture.md](Architecture.md): modules, how version-specific code is picked, supported versions, builds.
- [Versioned-APIs.md](Versioned-APIs.md): the Bukkit names that changed from 1.8 to 26.x and which are covered.
- [Roadmap.md](Roadmap.md): what comes next.
