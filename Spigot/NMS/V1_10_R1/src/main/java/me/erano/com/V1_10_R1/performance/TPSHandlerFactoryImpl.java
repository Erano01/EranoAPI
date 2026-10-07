package me.erano.com.V1_10_R1.performance;


import me.erano.com.api.performance.TPSHandler;
import me.erano.com.api.performance.TPSHandlerFactory;

public class TPSHandlerFactoryImpl implements TPSHandlerFactory {
    @Override
    public TPSHandler createTPSHandler() {
        return new TPSHandlerImpl();
    }

    @Override
    public boolean supportsVersion(String version) {
        return version.equals("1.10.2") || version.equals("1.10");
    }
}
