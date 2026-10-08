package me.erano.com.api.hologram;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

/** 1.13 - 1.19.3: one invisible marker armor stand per line. */
class ArmorStandHologram extends EntityHologram {

    ArmorStandHologram(EntityHologramService service, Location location, List<String> lines) {
        super(service, location, lines);
    }

    @Override
    protected List<Entity> spawnEntities(boolean hiddenByDefault) {
        List<Entity> spawned = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String text = lines.get(i);
            spawned.add(EntitySpawner.spawn(lineLocation(i), ArmorStand.class, stand -> {
                stand.setPersistent(false);
                stand.setVisible(false);
                stand.setGravity(false);
                stand.setMarker(true);
                stand.setBasePlate(false);
                stand.setInvulnerable(true);
                stand.setSilent(true);
                setText(stand, text);
                if (hiddenByDefault) {
                    stand.setVisibleByDefault(false);
                }
            }));
        }
        return spawned;
    }

    @Override
    protected boolean updateLines(List<Entity> entities) {
        if (entities.size() != lines.size()) {
            return false;
        }
        for (int i = 0; i < lines.size(); i++) {
            setText(entities.get(i), lines.get(i));
        }
        return true;
    }

    private static void setText(Entity stand, String text) {
        stand.setCustomName(text.isEmpty() ? null : text);
        stand.setCustomNameVisible(!text.isEmpty());
    }
}
