package me.erano.com.api.menu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PianoCommand implements CommandExecutor {

    private final MenuDispatcher menuDispatcher;

    public PianoCommand(MenuDispatcher menuDispatcher) {
        this.menuDispatcher = menuDispatcher;
    }

    //String label, String[] args
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player)commandSender;
        this.menuDispatcher.openMenu(new PianoMenu(), player);
        return true;
    }

}
