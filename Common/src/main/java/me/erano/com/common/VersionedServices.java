package me.erano.com.common;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/** Picks the {@link VersionedService} provider that fits the running Minecraft version. */
public final class VersionedServices {

    private VersionedServices() {
    }

    /**
     * Among the providers whose range contains {@code version} and that are available, returns the one
     * with the highest {@link VersionedService#priority()}, then the one whose range starts latest
     * (the most specific one).
     *
     * <p>Providers that fail to load (e.g. a {@link LinkageError} because they were built for another
     * version) or whose checks throw are skipped.
     *
     * @throws IllegalStateException when no provider fits
     */
    public static <T extends VersionedService> T select(Class<T> type, ClassLoader classLoader, MinecraftVersion version) {
        T best = null;
        Iterator<T> providers = ServiceLoader.load(type, classLoader).iterator();
        while (true) {
            T candidate;
            try {
                if (!providers.hasNext()) {
                    break;
                }
                candidate = providers.next();
                if (!candidate.supportedVersions().contains(version) || !candidate.isAvailable()) {
                    continue;
                }
            } catch (ServiceConfigurationError | LinkageError | RuntimeException e) {
                continue;
            }
            if (best == null || isBetter(candidate, best)) {
                best = candidate;
            }
        }
        if (best == null) {
            throw new IllegalStateException("No " + type.getSimpleName() + " provider for Minecraft " + version);
        }
        return best;
    }

    private static boolean isBetter(VersionedService candidate, VersionedService current) {
        if (candidate.priority() != current.priority()) {
            return candidate.priority() > current.priority();
        }
        return candidate.supportedVersions().min().compareTo(current.supportedVersions().min()) > 0;
    }
}
