package me.erano.com.api.bedrock;

import org.bukkit.entity.Player;

import me.erano.com.common.menu.dialog.DialogService;
import me.erano.com.common.menu.dialog.UnsupportedDialogService;

/** Used without Geyser / Floodgate. */
public final class NoBedrockService implements BedrockService {

    private final DialogService<Player> forms = new UnsupportedDialogService<>();

    @Override
    public boolean isBedrock(Player player) {
        return false;
    }

    @Override
    public DialogService<Player> forms() {
        return forms;
    }

    @Override
    public boolean isSupported() {
        return false;
    }
}
