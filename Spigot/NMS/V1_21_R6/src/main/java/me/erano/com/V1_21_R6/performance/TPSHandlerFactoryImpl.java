package me.erano.com.V1_21_R6.performance;


import me.erano.com.api.performance.TPSHandler;
import me.erano.com.api.performance.TPSHandlerFactory;

public class TPSHandlerFactoryImpl implements TPSHandlerFactory {
    @Override
    public TPSHandler createTPSHandler() {
        return new TPSHandlerImpl();
    }

    @Override
    public boolean supportsVersion(String version) {
        return version.equals("1.21.9") || version.equals("1.21.10");
    }
}
