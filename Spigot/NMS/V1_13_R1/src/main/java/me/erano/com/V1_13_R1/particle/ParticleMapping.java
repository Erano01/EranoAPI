package me.erano.com.V1_13_R1.particle;


import net.minecraft.server.v1_13_R1.ParticleParamRedstone;
import net.minecraft.server.v1_13_R1.ParticleType;
import net.minecraft.server.v1_13_R1.Particles;

/**
 * 1.13_R1 için NMS partikül mapping class'ı.
 * Design Pattern: Abstract Factory, Centralized Mapping
 */
public class ParticleMapping {
    // net.minecraft.client.particle.ParticleFlame (searge mapping)
    // net.minecraft.client.particle.csc -> represents ParticleFlame
    public static final ParticleType FLAME = Particles.y;
    public static final ParticleParamRedstone REDSTONE = new ParticleParamRedstone(1.0F, 0.0F, 0.0F, 1.0F);
}
