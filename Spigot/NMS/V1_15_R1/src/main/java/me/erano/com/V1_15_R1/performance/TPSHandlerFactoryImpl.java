package me.erano.com.V1_15_R1.performance;


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
        return VersionRange.series("1.15");
    }
}
