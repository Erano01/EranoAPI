package me.erano.com.common.network;

import java.util.regex.Pattern;

/** Plugin messaging channel names: {@code namespace:path}, lowercase, as required since Minecraft 1.13. */
public final class Channels {

    private static final Pattern VALID = Pattern.compile("[a-z0-9_.-]+:[a-z0-9_./-]+");

    private Channels() {
    }

    /**
     * @return {@code channel}, so it can be used inline
     * @throws IllegalArgumentException if it isn't {@code namespace:path}
     */
    public static String validate(String channel) {
        if (channel == null || !VALID.matcher(channel).matches()) {
            throw new IllegalArgumentException("Channel must be lowercase namespace:path, got " + channel);
        }
        return channel;
    }
}
