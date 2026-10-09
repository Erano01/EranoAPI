package me.erano.com.api.material;

import org.bukkit.Material;

/** The tables and their resolution on this server, each made on first use. */
final class MaterialTables {

    private static volatile MaterialTable table;
    private static volatile ServerMaterials server;

    private MaterialTables() {
    }

    static MaterialTable table() {
        MaterialTable current = table;
        if (current == null) {
            synchronized (MaterialTables.class) {
                if (table == null) {
                    table = new MaterialTable();
                }
                current = table;
            }
        }
        return current;
    }

    static ServerMaterials server() {
        ServerMaterials current = server;
        if (current == null) {
            synchronized (MaterialTables.class) {
                if (server == null) {
                    server = new ServerMaterials(table(), legacy(), Material::getMaterial);
                }
                current = server;
            }
        }
        return current;
    }

    /**
     * A 1.8 - 1.12 {@code Material}: no {@code LEGACY_} constants (1.13 brought them). Read from the enum itself, so it
     * holds without a running server too (tests, tools).
     */
    static boolean legacy() {
        try {
            Material.valueOf("LEGACY_AIR");
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    static boolean supportedOnServer(EranoMaterial material) {
        return server().material(material) != null;
    }

    /** For tests: as if on a server of that kind. */
    static void useServer(ServerMaterials materials) {
        server = materials;
    }
}
