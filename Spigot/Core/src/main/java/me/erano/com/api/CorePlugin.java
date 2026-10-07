package me.erano.com.api;

import me.erano.com.api.menu.PianoCommand;
import me.erano.com.api.menu.MenuListener;
import me.erano.com.api.menu.MenuDispatcher;
import me.erano.com.api.performance.*;
import me.erano.com.common.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin{

    private TPSHandler tpsHandler;

    @Override
    public void onEnable() {
        MinecraftVersion version = ServerVersion.current();
        TPSHandlerFactory tpsHandlerFactory = TPSHandlerFactoryClassMapper.getTPSHandlerFactory(version, this.getClassLoader());
        getLogger().info("Minecraft " + version + ": using " + tpsHandlerFactory.getClass().getName());

        tpsHandler = tpsHandlerFactory.createTPSHandler();
        // Example: Command to show TPS
        getCommand("showTPS").setExecutor(new ShowTPSCommand(this));
        // Example: Task to send TPS to all players as actionbar (only requires NMS on 1.8)
        getServer().getScheduler().runTaskTimer(this, new ActionBarTask(this), 0, 20);

        //menu stuff
        MenuDispatcher menuDispatcher = new MenuDispatcher();
        MenuListener menuListener = new MenuListener(menuDispatcher);
        //getServer().getPluginManager()
        Bukkit.getPluginManager().registerEvents(menuListener, this);
        getCommand("piano").setExecutor(new PianoCommand(menuDispatcher));

    }

    public TPSHandler getTPSHandler() {
        return tpsHandler;
    }
}
