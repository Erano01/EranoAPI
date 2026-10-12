package me.erano.com.api.menu.dialog;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.erano.com.common.VersionRange;
import me.erano.com.common.menu.dialog.DialogService;
import me.erano.com.common.menu.dialog.UnsupportedDialogService;

/** Every version: loses to the real providers on 1.21.6+, so older servers get {@link UnsupportedDialogService} without a warning. */
public class UnsupportedDialogServiceProvider implements DialogServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.8");
    }

    @Override
    public DialogService<Player> create(Plugin plugin) {
        return new UnsupportedDialogService<>();
    }
}
