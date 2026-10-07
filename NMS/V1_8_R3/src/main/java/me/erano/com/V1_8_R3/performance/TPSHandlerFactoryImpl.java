package me.erano.com.V1_8_R3.performance;


import me.erano.com.api.performance.TPSHandler;
import me.erano.com.api.performance.TPSHandlerFactory;

public class TPSHandlerFactoryImpl implements TPSHandlerFactory {
    @Override
    public TPSHandler createTPSHandler() {
        return new TPSHandlerImpl();
    }

    @Override
    public boolean supportsVersion(String version) {
        return version.equals("1.8.4") || version.equals("1.8.5") || version.equals("1.8.6") || version.equals("1.8.7") || version.equals("1.8.8");
    }
}
