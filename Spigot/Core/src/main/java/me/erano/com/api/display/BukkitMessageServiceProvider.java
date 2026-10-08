package me.erano.com.api.display;

import me.erano.com.common.VersionRange;

public class BukkitMessageServiceProvider implements MessageServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.11");
    }

    @Override
    public MessageService create() {
        return new BukkitMessageService();
    }
}
