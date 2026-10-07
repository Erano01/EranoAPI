package me.erano.com.common;

/**
 * Minecraft versions a provider supports: {@code min} inclusive, {@code max} exclusive, no upper bound
 * when {@code max} is {@code null}.
 */
public final class VersionRange {

    private final MinecraftVersion min;
    private final MinecraftVersion maxExclusive;

    private VersionRange(MinecraftVersion min, MinecraftVersion maxExclusive) {
        if (maxExclusive != null && maxExclusive.compareTo(min) <= 0) {
            throw new IllegalArgumentException("Empty range: " + min + " - " + maxExclusive);
        }
        this.min = min;
        this.maxExclusive = maxExclusive;
    }

    /** {@code first} to {@code last}, both inclusive, e.g. {@code between("1.8.4", "1.8.9")}. */
    public static VersionRange between(String first, String last) {
        MinecraftVersion end = MinecraftVersion.parse(last);
        return new VersionRange(MinecraftVersion.parse(first), new MinecraftVersion(end.major(), end.minor(), end.patch() + 1));
    }

    /** Exactly one release, e.g. {@code only("1.21.4")}. */
    public static VersionRange only(String version) {
        return between(version, version);
    }

    /** Every patch of a release line, e.g. {@code series("26.1")} covers 26.1, 26.1.1, 26.1.2... */
    public static VersionRange series(String version) {
        MinecraftVersion start = MinecraftVersion.parse(version);
        return new VersionRange(start, new MinecraftVersion(start.major(), start.minor() + 1, 0));
    }

    /** {@code version} and everything after it, for the newest provider. */
    public static VersionRange atLeast(String version) {
        return new VersionRange(MinecraftVersion.parse(version), null);
    }

    public boolean contains(MinecraftVersion version) {
        return version.compareTo(min) >= 0 && (maxExclusive == null || version.compareTo(maxExclusive) < 0);
    }

    public MinecraftVersion min() {
        return min;
    }

    @Override
    public String toString() {
        return maxExclusive == null ? min + "+" : "[" + min + ", " + maxExclusive + ")";
    }
}
