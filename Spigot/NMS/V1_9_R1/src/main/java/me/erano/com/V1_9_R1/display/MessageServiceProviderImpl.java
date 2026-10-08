package me.erano.com.V1_9_R1.display;

import me.erano.com.api.display.MessageService;
import me.erano.com.api.display.MessageServiceProvider;
import me.erano.com.common.VersionRange;

public class MessageServiceProviderImpl implements MessageServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.between("1.9", "1.9.3");
    }

    @Override
    public MessageService create() {
        return new MessageServiceImpl();
    }
}
