package me.jamino.jukebard.network;

import me.jamino.jukebard.JukebardConfig;
import me.jamino.jukebard.sound.JukebardMovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.registries.BuiltInRegistries;

@OnlyIn(Dist.CLIENT)
public class ClientMusicHandler {
    private static JukebardMovingSound currentSound;

    public static void playMusic(ResourceLocation soundId) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (currentSound != null) {
            stopMusic();
        }

        SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(soundId);
        if (sound != null) {
            currentSound = new JukebardMovingSound(mc.player, sound);
            mc.getSoundManager().play(currentSound);
        }
    }

    public static void stopMusic() {
        if (currentSound != null) {
            Minecraft.getInstance().getSoundManager().stop(currentSound);
            currentSound = null;
        }
    }
}