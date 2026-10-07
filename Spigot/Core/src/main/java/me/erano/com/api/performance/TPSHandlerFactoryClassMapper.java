package me.erano.com.api.performance;

import me.erano.com.common.MinecraftVersion;
import me.erano.com.common.VersionedServices;

public class TPSHandlerFactoryClassMapper {

    // SPI - Service Provider Interface
    public static TPSHandlerFactory getTPSHandlerFactory(MinecraftVersion version, ClassLoader classLoader) {
        return VersionedServices.select(TPSHandlerFactory.class, classLoader, version);
    }
}
