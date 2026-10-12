package me.erano.com.paper.V1_9_R2.performance;


import me.erano.com.api.Platform;
import me.erano.com.api.performance.TPSHandler;
import me.erano.com.api.performance.TPSHandlerFactory;
import me.erano.com.common.VersionRange;

public class TPSHandlerFactoryImpl implements TPSHandlerFactory {
    @Override
    public TPSHandler createTPSHandler() {
        return new TPSHandlerImpl();
    }

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.only("1.9.4");
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
