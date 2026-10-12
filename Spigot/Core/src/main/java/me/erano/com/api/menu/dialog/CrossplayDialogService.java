package me.erano.com.api.menu.dialog;

import org.bukkit.entity.Player;

import me.erano.com.api.bedrock.BedrockService;
import me.erano.com.common.menu.dialog.DialogMenu;
import me.erano.com.common.menu.dialog.DialogService;

/** Decorator: Bedrock players get the menu as a form, Java players as a dialog. */
public class CrossplayDialogService implements DialogService<Player> {

    private final DialogService<Player> java;
    private final BedrockService bedrock;

    public CrossplayDialogService(DialogService<Player> java, BedrockService bedrock) {
        this.java = java;
        this.bedrock = bedrock;
    }

    @Override
    public void show(Player player, DialogMenu<Player> menu) {
        target(player).show(player, menu);
    }

    @Override
    public void close(Player player) {
        target(player).close(player);
    }

    /** Whether the Java players can be shown dialogs; see {@link #isSupported(Player)}. */
    @Override
    public boolean isSupported() {
        return java.isSupported();
    }

    @Override
    public boolean isSupported(Player player) {
        return bedrock.isBedrock(player) || java.isSupported();
    }

    private DialogService<Player> target(Player player) {
        return bedrock.isBedrock(player) ? bedrock.forms() : java;
    }
}
