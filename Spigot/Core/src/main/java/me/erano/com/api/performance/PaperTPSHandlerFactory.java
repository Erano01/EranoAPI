package me.erano.com.api.performance;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;

import me.erano.com.common.VersionRange;

/**
 * Paper exposes {@code Server#getTPS()}, so no NMS is needed there. Spigot doesn't have it and falls
 * back to the NMS providers.
 */
public class PaperTPSHandlerFactory implements TPSHandlerFactory {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.9");
    }

    @Override
    public boolean isAvailable() {
        return getTpsMethod() != null;
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public TPSHandler createTPSHandler() {
        Method getTps = getTpsMethod();
        return () -> {
            try {
                return (double[]) getTps.invoke(Bukkit.getServer());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Server#getTPS failed", e);
            }
        };
    }

    private static Method getTpsMethod() {
        try {
            Method method = Bukkit.getServer().getClass().getMethod("getTPS");
            return method.getReturnType() == double[].class ? method : null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
