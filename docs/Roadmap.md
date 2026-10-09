# Roadmap

What EranoAPI doesn't have yet, roughly in order.

| Feature | What | Platform work |
|---|---|---|
| Item tags | `set` / `get` a value on an `ItemStack` | PDC on 1.14+, NBT via NMS before |
| World templates | Copy a world from a template off the main thread, save it back, atomically | Core |
| Comment-keeping yml writer | Update single values or add missing keys without losing comments and order | Common |
| Scoreboards | Long sidebar lines (16-char limit before 1.13), name tag prefix / suffix / color per player | Core |
| Economy | `PlayerCreditRepository` with amounts, async, atomic transfers; yml, Vault and MySQL backends | Core |
| Proxy | `sendToServer(player, server)` over the `BungeeCord` channel | Core |
| Entity types | `EranoEntityType` (renamed in 1.13 and 1.20.5), spawn eggs on 1.9 - 1.12 | Core, NMS |
| Game rules | `EranoGameRule` (strings before 1.13, typed after, renamed in 26.x) | Core |
| Block looks | Facing, double chests, colors: data values on 1.8 - 1.12, `BlockData` after | Core |
| Attributes, dye colors, item flags | Today's names on every version | Core |
| Forge / Fabric | More Minecraft versions; NeoForge | per version |
