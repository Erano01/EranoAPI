package me.erano.com.api.menu.dialog;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.erano.com.api.Platform;
import me.erano.com.common.VersionRange;
import me.erano.com.common.menu.dialog.DialogService;

/** Spigot 1.21.6+; Paper has its own dialog API, see Paper/Core. */
public class SpigotDialogServiceProvider implements DialogServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.21.6");
    }

    @Override
    public boolean isAvailable() {
        return !Platform.isPaper();
    }

    @Override
    public DialogService<Player> create(Plugin plugin) {
        return new SpigotDialogService(plugin);
    }
}
