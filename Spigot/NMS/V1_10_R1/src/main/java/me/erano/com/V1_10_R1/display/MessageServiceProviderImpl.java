package me.erano.com.V1_10_R1.display;

import me.erano.com.api.display.MessageService;
import me.erano.com.api.display.MessageServiceProvider;
import me.erano.com.common.VersionRange;

public class MessageServiceProviderImpl implements MessageServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.series("1.10");
    }

    @Override
    public MessageService create() {
        return new MessageServiceImpl();
    }
}
