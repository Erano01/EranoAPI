package me.erano.com.V1_21_R3.performance;


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
        return VersionRange.only("1.21.4");
    }

    /** Spigot names: Paper 1.20.5+ runs on Mojang's, see Paper/NMS. */
    @Override
    public boolean isAvailable() {
        return !Platform.isPaper();
    }
}
