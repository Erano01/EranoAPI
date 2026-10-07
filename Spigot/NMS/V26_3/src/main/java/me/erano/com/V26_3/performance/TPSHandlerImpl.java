package me.erano.com.V26_3.performance;

import me.erano.com.api.performance.TPSHandler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

public class TPSHandlerImpl implements TPSHandler {
    @Override
    public double[] getTPS() {
        return ((CraftServer) Bukkit.getServer()).getHandle().getServer().recentTps;
    }

}
