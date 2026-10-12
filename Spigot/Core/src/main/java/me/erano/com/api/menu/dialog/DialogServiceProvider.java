package me.erano.com.api.menu.dialog;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.erano.com.common.VersionedService;
import me.erano.com.common.menu.dialog.DialogService;

/** SPI for {@link DialogService}; see {@link VersionedService} for the rules. */
public interface DialogServiceProvider extends VersionedService {

    /** @param plugin owner of the listeners the service registers */
    DialogService<Player> create(Plugin plugin);
}
