package me.erano.com.api.menu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.erano.com.api.EranoServices;

public class DialogDemoCommand implements CommandExecutor {

    private final MenuDispatcher menuDispatcher;

    public DialogDemoCommand(MenuDispatcher menuDispatcher) {
        this.menuDispatcher = menuDispatcher;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player) commandSender;
        // Dialog yoksa (1.21.6 öncesi) envanter menüsüne düş.
        Menu menu = EranoServices.dialogs().isSupported() ? new DemoDialogMenu(menuDispatcher) : new PianoMenu();
        this.menuDispatcher.openMenu(menu, player);
        return true;
    }
}
