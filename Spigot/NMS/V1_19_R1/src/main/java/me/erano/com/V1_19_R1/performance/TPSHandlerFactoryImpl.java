package me.erano.com.V1_19_R1.performance;


import me.erano.com.api.performance.TPSHandler;
import me.erano.com.api.performance.TPSHandlerFactory;

public class TPSHandlerFactoryImpl implements TPSHandlerFactory {
    @Override
    public TPSHandler createTPSHandler() {
        return new TPSHandlerImpl();
    }

    @Override
    public boolean supportsVersion(String version) {
        return version.equals("1.19") || version.equals("1.19.1") || version.equals("1.19.2");
    }
}
