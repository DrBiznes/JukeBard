package me.jamino.jukebard.sound;

import me.jamino.jukebard.JukebardConfig;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;

public class JukebardMovingSound extends AbstractTickableSoundInstance {
    private final double sourceX, sourceY, sourceZ;

    public JukebardMovingSound(double x, double y, double z, SoundEvent event) {
        super(event, SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.sourceX = x;
        this.sourceY = y;
        this.sourceZ = z;
        this.looping = true;
        this.delay = 0;
        this.volume = JukebardConfig.MUSIC_VOLUME.get().floatValue();
        this.relative = false;
        this.attenuation = Attenuation.LINEAR;
        this.x = sourceX;
        this.y = sourceY;
        this.z = sourceZ;
    }

    @Override
    public void tick() {
        // Keep the sound at the original position
        this.x = sourceX;
        this.y = sourceY;
        this.z = sourceZ;
    }

    @Override
    public float getVolume() {
        return JukebardConfig.MUSIC_VOLUME.get().floatValue();
    }

    @Override
    public boolean canStartSilent() {
        return false;
    }
}