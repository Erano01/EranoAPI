package me.erano.com.paper.V1_8_R3.display;

import me.erano.com.api.Platform;
import me.erano.com.api.display.MessageService;
import me.erano.com.api.display.MessageServiceProvider;
import me.erano.com.common.VersionRange;

public class MessageServiceProviderImpl implements MessageServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.between("1.8.4", "1.8.9");
    }

    @Override
    public MessageService create() {
        return new MessageServiceImpl();
    }

    @Override
    public boolean isAvailable() {
        return Platform.isPaper();
    }

    @Override
    public int priority() {
        return 1;
    }
}
