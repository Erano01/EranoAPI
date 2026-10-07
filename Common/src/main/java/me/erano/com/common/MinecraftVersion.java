package me.erano.com.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A comparable Minecraft version. Parses both the old scheme ({@code 1.21.5}) and the year based
 * one used since 2026 ({@code 26.1}, {@code 26.1.2}); a missing patch number counts as 0.
 */
public final class MinecraftVersion implements Comparable<MinecraftVersion> {

    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(?:\\.(\\d+))?");

    private final int major;
    private final int minor;
    private final int patch;

    public MinecraftVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * @param version e.g. {@code 1.8.8}, {@code 1.21}, {@code 26.1.2}; anything after the version
     *                ({@code 1.21.11-R0.2-SNAPSHOT}) is ignored
     */
    public static MinecraftVersion parse(String version) {
        Matcher matcher = PATTERN.matcher(version);
        if (!matcher.lookingAt()) {
            throw new IllegalArgumentException("Not a Minecraft version: " + version);
        }
        int patch = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
        return new MinecraftVersion(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), patch);
    }

    public int major() {
        return major;
    }

    public int minor() {
        return minor;
    }

    public int patch() {
        return patch;
    }

    public boolean isAtLeast(MinecraftVersion other) {
        return compareTo(other) >= 0;
    }

    @Override
    public int compareTo(MinecraftVersion o) {
        if (major != o.major) {
            return Integer.compare(major, o.major);
        }
        if (minor != o.minor) {
            return Integer.compare(minor, o.minor);
        }
        return Integer.compare(patch, o.patch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MinecraftVersion)) {
            return false;
        }
        MinecraftVersion that = (MinecraftVersion) o;
        return major == that.major && minor == that.minor && patch == that.patch;
    }

    @Override
    public int hashCode() {
        return 31 * (31 * major + minor) + patch;
    }

    /** {@code 1.21} rather than {@code 1.21.0}, matching how Minecraft names its releases. */
    @Override
    public String toString() {
        return patch == 0 ? major + "." + minor : major + "." + minor + "." + patch;
    }
}
