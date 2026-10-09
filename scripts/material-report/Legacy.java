import java.lang.reflect.Method;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

/** For every legacy material and data value 0 - 15: the 1.13 material CraftLegacy turns it into (block, item). */
public class Legacy {
    public static void main(String[] args) throws Exception {
        // Minecraft's registries (blocks, items, the data fixers) must be set up first.
        Class.forName("net.minecraft.server.v1_13_R2.DispenserRegistry").getMethod("c").invoke(null);
        Class<?> legacy = Class.forName("org.bukkit.craftbukkit.v1_13_R2.util.CraftLegacy");
        Method fromData = legacy.getMethod("fromLegacy", MaterialData.class);
        Method fromData2 = null;
        try {
            fromData2 = legacy.getMethod("fromLegacy", MaterialData.class, boolean.class);
        } catch (NoSuchMethodException e) {
            // older
        }
        for (Material m : Material.values()) {
            if (!m.isLegacy()) {
                continue;
            }
            for (int data = 0; data < 16; data++) {
                MaterialData md = new MaterialData(m, (byte) data);
                Object block = fromData2 != null ? fromData2.invoke(null, md, false) : fromData.invoke(null, md);
                Object item = fromData2 != null ? fromData2.invoke(null, md, true) : block;
                System.out.println(m.name().substring("LEGACY_".length()) + "\t" + m.getId() + "\t" + data + "\t"
                        + block + "\t" + item + "\t" + (m.isBlock() ? 1 : 0) + "\t" + (m.isItem() ? 1 : 0));
            }
        }
    }
}
