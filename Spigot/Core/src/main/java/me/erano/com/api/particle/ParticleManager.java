
package me.erano.com.api.particle;

import java.util.ServiceLoader;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * ParticleManager facade & singleton.
 * SPI ile uygun NMS implementasyonunu seçer.
 * Design Patterns: Facade, Singleton, Service Provider Interface
 */
public class ParticleManager {
    private static ParticleManager instance;
    private final IParticleProvider provider;

    private ParticleManager() {
        ServiceLoader<IParticleProvider> loader = ServiceLoader.load(IParticleProvider.class);
        String version = Bukkit.getServer().getBukkitVersion().split("-")[0];
        IParticleProvider found = null;
        for (IParticleProvider prov : loader) {
            if (prov.supportsVersion(version)) {
                found = prov;
                break;
            }
        }
        if (found == null) {
            throw new IllegalStateException("No particle provider found for version " + version);
        }
        this.provider = found;
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
