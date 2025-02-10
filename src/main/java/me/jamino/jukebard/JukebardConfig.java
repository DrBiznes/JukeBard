package me.jamino.jukebard;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import me.jamino.jukebard.Jukebard;

@Mod.EventBusSubscriber(modid = Jukebard.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JukebardConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue MUSIC_VOLUME;
    public static final ForgeConfigSpec.DoubleValue MUSIC_RANGE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_PARTICLES;

    static {
        BUILDER.comment("Bony Blockman Settings");

        MUSIC_VOLUME = BUILDER
                .comment("Volume of the music (default: 4.0, vanilla jukebox volume)")
                .defineInRange("musicVolume", 4.0, 0.0, 10.0);

        MUSIC_RANGE = BUILDER
                .comment("How far away the music can be heard in blocks (default: 64)")
                .defineInRange("musicRange", 64.0, 1.0, 256.0);

        ENABLE_PARTICLES = BUILDER
                .comment("Whether to show note particles when playing music")
                .define("enableParticles", true);
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        // This method will be called when the config is loaded or reloaded
    }
}