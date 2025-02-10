package me.jamino.jukebard.sound;

import me.jamino.jukebard.JukebardConfig;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvent;

public class JukebardMovingSound extends AbstractTickableSoundInstance {
    private final Player player;

    public JukebardMovingSound(Player player, SoundEvent event) {
        super(event, SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = true;  // Music discs should loop
        this.delay = 0;
        this.volume = JukebardConfig.MUSIC_VOLUME.get().floatValue();
        this.relative = false;
        this.attenuation = Attenuation.LINEAR;  // Sound fades with distance
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    @Override
    public void tick() {
        if (!this.player.isRemoved() && !this.player.isSpectator()) {
            this.x = this.player.getX();
            this.y = this.player.getY();
            this.z = this.player.getZ();
        } else {
            this.stop();
        }
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