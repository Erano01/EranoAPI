package me.erano.com.V1_15_R1.performance;

import me.erano.com.api.performance.TPSHandler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;

public class TPSHandlerImpl implements TPSHandler {
	@Override
    public double[] getTPS() {
        return ((CraftServer) Bukkit.getServer()).getHandle().getServer().recentTps;
    }

}
