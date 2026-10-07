package me.erano.com.api.particle;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Builder sınıfı ile parçacık efekti yapılandırması
 */
public class ParticleBuilder {
    private final World world;
    private final Location location;
    private final ParticleEffect effect;
    private int count = 1;
    private double offsetX = 0;
    private double offsetY = 0;
    private double offsetZ = 0;
    private double speed = 0;
    private Object data = null;
    private Player[] receivers = null;

    public ParticleBuilder(World world, Location location, ParticleEffect effect) {
        this.world = world;
        this.location = location;
        this.effect = effect;
    }

    public ParticleBuilder count(int count) {
        this.count = count;
        return this;
    }

    public ParticleBuilder offset(double x, double y, double z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        return this;
    }

    public ParticleBuilder speed(double speed) {
        this.speed = speed;
        return this;
    }

    public ParticleBuilder data(Object data) {
        this.data = data;
        return this;
    }

    public ParticleBuilder receivers(Player... players) {
        this.receivers = players;
        return this;
    }

    /**
     * Yapılandırılmış parçacık efektini oluşturur
     */
    public void spawn() {
        ParticleManager.getInstance().spawnParticle(
            world, location, effect, count,
            offsetX, offsetY, offsetZ,
            speed, data, receivers
        );
    }
}
