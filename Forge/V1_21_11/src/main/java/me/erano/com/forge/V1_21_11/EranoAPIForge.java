package me.erano.com.forge.V1_21_11;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Forge needs a {@code @Mod} class for the {@code eranoapi} mod id. The API itself needs no setup:
 * implementations are looked up lazily (META-INF/services), so mod loading order doesn't matter.
 */
@Mod(EranoAPIForge.MOD_ID)
public final class EranoAPIForge {

    public static final String MOD_ID = "eranoapi";

    public EranoAPIForge(FMLJavaModLoadingContext context) {
    }
}
