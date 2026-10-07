package me.erano.com.api.performance;

import me.erano.com.common.VersionedService;

public interface TPSHandlerFactory extends VersionedService {

    TPSHandler createTPSHandler();
}
