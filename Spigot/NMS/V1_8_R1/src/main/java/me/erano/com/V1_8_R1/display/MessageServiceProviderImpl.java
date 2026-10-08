package me.erano.com.V1_8_R1.display;

import me.erano.com.api.display.MessageService;
import me.erano.com.api.display.MessageServiceProvider;
import me.erano.com.common.VersionRange;

public class MessageServiceProviderImpl implements MessageServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.between("1.8", "1.8.2");
    }

    @Override
    public MessageService create() {
        return new MessageServiceImpl();
    }
}
