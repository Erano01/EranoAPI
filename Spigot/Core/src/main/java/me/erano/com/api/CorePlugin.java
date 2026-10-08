package me.erano.com.api;

import me.erano.com.api.display.DisplayDemoCommand;
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
        EranoServices.enable(this);
        // Example: Command to show TPS
        getCommand("showTPS").setExecutor(new ShowTPSCommand(this));
        // Example: new ActionBarTask(this) sends the TPS to every player's action bar. Not scheduled: it would
        // overwrite the action bar of every plugin that depends on EranoAPI.
        getCommand("eranodemo").setExecutor(new DisplayDemoCommand());

        //menu stuff
        MenuDispatcher menuDispatcher = new MenuDispatcher();
        MenuListener menuListener = new MenuListener(menuDispatcher);
        //getServer().getPluginManager()
        Bukkit.getPluginManager().registerEvents(menuListener, this);
        getCommand("piano").setExecutor(new PianoCommand(menuDispatcher));

    }

    @Override
    public void onDisable() {
        EranoServices.disable();
    }

    public TPSHandler getTPSHandler() {
        return tpsHandler;
    }
}
