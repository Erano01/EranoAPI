package me.erano.com.paper.core.dialog;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.erano.com.api.Platform;
import me.erano.com.common.menu.dialog.DialogService;
import me.erano.com.api.menu.dialog.DialogServiceProvider;
import me.erano.com.common.VersionRange;

/** Paper 1.21.6+, where Paper's dialog API with click callbacks exists. */
public class PaperDialogServiceProvider implements DialogServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.21.6");
    }

    @Override
    public boolean isAvailable() {
        if (!Platform.isPaper()) {
            return false;
        }
        try {
            Class.forName("io.papermc.paper.registry.data.dialog.action.DialogActionCallback", false, getClass().getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public DialogService<Player> create(Plugin plugin) {
        return new PaperDialogService(plugin);
    }
}
