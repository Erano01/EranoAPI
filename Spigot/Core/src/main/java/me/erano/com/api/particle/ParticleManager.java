
package me.erano.com.api.particle;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.erano.com.api.ServerVersion;
import me.erano.com.common.VersionedServices;

/**
 * ParticleManager facade & singleton.
 * SPI ile uygun implementasyonu seçer: 1.9+ Bukkit API, 1.8 NMS.
 * Design Patterns: Facade, Singleton, Service Provider Interface
 *
 * @deprecated {@link EranoParticle}: every particle of today by its newest name, 1.8 included.
 */
@Deprecated
public class ParticleManager {
    private static ParticleManager instance;
    private final IParticleProvider provider;

    private ParticleManager() {
        // The plugin's own class loader: the thread context class loader can't see the NMS modules.
        this.provider = VersionedServices.select(IParticleProvider.class, ParticleManager.class.getClassLoader(), ServerVersion.current());
    }

    public static synchronized ParticleManager getInstance() {
        if (instance == null) {
            instance = new ParticleManager();
        }
        return instance;
    }

    public void spawnParticle(World world, Location location, ParticleEffect effect, int count,
                             double offsetX, double offsetY, double offsetZ, double speed) {
        spawnParticle(world, location, effect, count, offsetX, offsetY, offsetZ, speed, null);
    }

    public void spawnParticle(World world, Location location, ParticleEffect effect, int count,
                             double offsetX, double offsetY, double offsetZ, double speed, Object data, Player... receivers) {
        if (!provider.isSupported(effect)) {
            throw new IllegalArgumentException("Particle effect " + effect + " is not supported in this version");
        }
        provider.spawnParticle(world, location, effect, count, offsetX, offsetY, offsetZ, speed, data, receivers);
    }

    public ParticleEffect[] getSupportedParticles() {
        return ParticleEffect.values();
    }
}
