package me.erano.com.api.menu.dialog;

import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/** Runs button callbacks on the main thread, whichever thread the click arrived on. */
public abstract class AbstractDialogService implements DialogService {

    protected final Plugin plugin;

    protected AbstractDialogService(Plugin plugin) {
        this.plugin = plugin;
    }

    protected void click(Player player, DialogMenu menu, DialogButton button, Map<String, Object> values) {
        Consumer<DialogClick> consumer = button.getEventConsumer();
        if (consumer == null) {
            return;
        }
        Runnable run = () -> consumer.accept(new DialogClick(player, menu, values));
        if (Bukkit.isPrimaryThread()) {
            run.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, run);
        }
    }
}
