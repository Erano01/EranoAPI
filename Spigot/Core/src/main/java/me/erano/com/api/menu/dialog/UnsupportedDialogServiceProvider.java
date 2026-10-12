package me.erano.com.api.menu.dialog;

import org.bukkit.plugin.Plugin;

import me.erano.com.common.VersionRange;

/** Every version: loses to the real providers on 1.21.6+, so older servers get {@link UnsupportedDialogService} without a warning. */
public class UnsupportedDialogServiceProvider implements DialogServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.8");
    }

    @Override
    public DialogService create(Plugin plugin) {
        return new UnsupportedDialogService();
    }
}
