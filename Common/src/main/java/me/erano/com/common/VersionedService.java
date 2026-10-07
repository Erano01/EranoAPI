package me.erano.com.common;

/**
 * A {@link java.util.ServiceLoader} provider bound to a range of Minecraft versions; see
 * {@link VersionedServices#select}.
 *
 * <p>Keep implementations cheap to instantiate and free of version specific types in their fields and
 * signatures: every registered provider is instantiated on every server, including those it doesn't
 * support. Touch version specific classes only from the objects the provider creates.
 */
public interface VersionedService {

    VersionRange supportedVersions();

    /** Runtime check on top of the version range, e.g. whether a Paper-only method exists. */
    default boolean isAvailable() {
        return true;
    }

    /** Higher wins over the version range; lets an API based provider beat the NMS ones. */
    default int priority() {
        return 0;
    }
}
