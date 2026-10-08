package me.erano.com.api.display;

import me.erano.com.common.VersionedService;

/** SPI for {@link MessageService}; see {@link me.erano.com.common.VersionedService} for the rules. */
public interface MessageServiceProvider extends VersionedService {

    MessageService create();
}
