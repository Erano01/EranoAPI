package me.erano.com.api.performance;

public interface TPSHandlerFactory {

    TPSHandler createTPSHandler();

    boolean supportsVersion(String version);
}
