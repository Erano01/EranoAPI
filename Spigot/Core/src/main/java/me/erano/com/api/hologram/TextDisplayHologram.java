package me.erano.com.api.hologram;

import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;

/** 1.19.4+: a single text display, lines joined with newlines. */
class TextDisplayHologram extends EntityHologram {

    TextDisplayHologram(EntityHologramService service, Location location, List<String> lines) {
        super(service, location, lines);
    }

    @Override
    protected List<Entity> spawnEntities(boolean hiddenByDefault) {
        // The text grows upwards from the entity, so it sits at the bottom line.
        Location at = lineLocation(Math.max(0, lines.size() - 1));
        return Collections.singletonList(EntitySpawner.spawn(at, TextDisplay.class, display -> {
            display.setPersistent(false);
            display.setBillboard(Display.Billboard.CENTER);
            display.setText(String.join("\n", lines));
            if (hiddenByDefault) {
                display.setVisibleByDefault(false);
            }
        }));
    }

    @Override
    protected boolean updateLines(List<Entity> entities) {
        TextDisplay display = (TextDisplay) entities.get(0);
        display.setText(String.join("\n", lines));
        display.teleport(lineLocation(Math.max(0, lines.size() - 1)));
        return true;
    }
}
