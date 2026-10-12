package me.erano.com.paper.V1_21_R6.performance;

import me.erano.com.api.performance.TPSHandler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

/** Paper dropped Spigot's {@code MinecraftServer#recentTps}; its {@code getTPS()} returns the same 1m, 5m, 15m averages. */
public class TPSHandlerImpl implements TPSHandler {
    @Override
    public double[] getTPS() {
        return ((CraftServer) Bukkit.getServer()).getServer().getTPS();
    }

}
