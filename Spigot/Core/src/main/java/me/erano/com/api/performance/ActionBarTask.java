package me.erano.com.api.performance;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.erano.com.api.CorePlugin;

public class ActionBarTask implements Runnable{

	private final CorePlugin plugin;

    public ActionBarTask(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            plugin.getTPSHandler().sendActionBar(player, "Current TPS: " + ShowTPSCommand.formatTps(plugin.getTPSHandler().getTPS()[0]));
        }
    }
}
