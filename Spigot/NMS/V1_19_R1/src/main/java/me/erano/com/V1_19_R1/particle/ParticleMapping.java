package me.erano.com.V1_19_R1.particle;

import com.mojang.math.Vector3f;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;

/**
 * 1.19_R1 için NMS partikül mapping class'ı.
 * Design Pattern: Abstract Factory, Centralized Mapping
 */
public class ParticleMapping {
    // 1.17+ mojang mapping kullanıyorum.
    // net.minecraft.core.particles.DustParticleOptions (Mojang mapping) == net.minecraft.core.particles.ParticleParamRedstone (Spigot mapping)
    // net.minecraft.core.particles.ParticleTypes (Mojang mapping) == net.minecraft.core.particles.Particles (Spigot mapping)
    public static final ParticleType FLAME = ParticleTypes.FLAME;
    // DustParticleOptions: (Vector3f color, float scale)
    public static final DustParticleOptions REDSTONE = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);
}
