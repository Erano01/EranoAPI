package me.erano.com.api.performance;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TPSHandlerFactoryClassMapper {

    // SPI - Service Provider Interface
    public static TPSHandlerFactory getTPSHandlerFactory(String version, ClassLoader classLoader) {
        ServiceLoader<TPSHandlerFactory> loader = ServiceLoader.load(TPSHandlerFactory.class, classLoader);
        for (TPSHandlerFactory factory : loader) {
            //System.out.println("Loaded factory: " + factory.getClass().getName());
            if (factory.supportsVersion(version)) {
                return factory;
            }
        }
        throw new RuntimeException("No TPSHandlerFactory found for version " + version);
    }
}
