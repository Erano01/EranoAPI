package me.erano.com.V1_12_R1.performance;


import me.erano.com.api.performance.TPSHandler;
import me.erano.com.api.performance.TPSHandlerFactory;

public class TPSHandlerFactoryImpl implements TPSHandlerFactory {
    @Override
    public TPSHandler createTPSHandler() {
        return new TPSHandlerImpl();
    }

    @Override
    public boolean supportsVersion(String version) {
        return version.equals("1.12") || version.equals("1.12.1") || version.equals("1.12.2");
    }
}
