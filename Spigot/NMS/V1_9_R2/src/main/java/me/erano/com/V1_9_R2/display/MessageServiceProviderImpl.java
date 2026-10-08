package me.erano.com.V1_9_R2.display;

import me.erano.com.api.display.MessageService;
import me.erano.com.api.display.MessageServiceProvider;
import me.erano.com.common.VersionRange;

public class MessageServiceProviderImpl implements MessageServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.only("1.9.4");
    }

    @Override
    public MessageService create() {
        return new MessageServiceImpl();
    }
}
