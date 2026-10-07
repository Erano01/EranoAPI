package me.erano.com.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Providers are registered in src/test/resources/META-INF/services. */
class VersionedServicesTest {

    public interface Probe extends VersionedService {
    }

    public static class Legacy implements Probe {
        @Override
        public VersionRange supportedVersions() {
            return VersionRange.between("1.8", "1.12.2");
        }
    }

    public static class Modern implements Probe {
        @Override
        public VersionRange supportedVersions() {
            return VersionRange.atLeast("1.9");
        }
    }

    public static class Latest implements Probe {
        @Override
        public VersionRange supportedVersions() {
            return VersionRange.atLeast("26.1");
        }
    }

    /** Wins on priority whenever it is available. */
    public static class Preferred implements Probe {
        static boolean available;

        @Override
        public VersionRange supportedVersions() {
            return VersionRange.atLeast("1.8");
        }

        @Override
        public boolean isAvailable() {
            return available;
        }

        @Override
        public int priority() {
            return 1;
        }
    }

    /** Stands in for a provider built against classes this server doesn't have. */
    public static class Broken implements Probe {
        public Broken() {
            throw new NoClassDefFoundError("net/minecraft/server/v1_8_R3/Missing");
        }

        @Override
        public VersionRange supportedVersions() {
            return VersionRange.atLeast("1.8");
        }
    }

    /** Its availability check blows up, e.g. Bukkit.getServer() still being null. */
    public static class Throwing implements Probe {
        @Override
        public VersionRange supportedVersions() {
            return VersionRange.atLeast("1.8");
        }

        @Override
        public boolean isAvailable() {
            throw new NullPointerException();
        }

        @Override
        public int priority() {
            return 2;
        }
    }

    private static Probe select(String version) {
        return VersionedServices.select(Probe.class, VersionedServicesTest.class.getClassLoader(), MinecraftVersion.parse(version));
    }

    @Test
    void picksTheMostSpecificRange() {
        Preferred.available = false;
        assertEquals(Legacy.class, select("1.8.8").getClass());
        assertEquals(Modern.class, select("1.12.2").getClass());
        assertEquals(Modern.class, select("1.21.11").getClass());
        assertEquals(Latest.class, select("26.3").getClass());
    }

    @Test
    void priorityBeatsRange() {
        Preferred.available = true;
        try {
            assertEquals(Preferred.class, select("26.3").getClass());
        } finally {
            Preferred.available = false;
        }
    }

    @Test
    void failsWhenNothingFits() {
        Preferred.available = false;
        assertThrows(IllegalStateException.class, () -> select("1.7.10"));
    }
}
