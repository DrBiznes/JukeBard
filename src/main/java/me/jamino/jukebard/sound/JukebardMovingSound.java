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
        this.looping = true;
        this.delay = 0;
        this.volume = JukebardConfig.MUSIC_VOLUME.get().floatValue();
        this.relative = false;
        this.attenuation = Attenuation.LINEAR;
        updatePosition();
    }

    @Override
    public void tick() {
        if (!this.player.isRemoved() && !this.player.isSpectator()) {
            updatePosition();
        } else {
            this.stop();
        }
    }

    private void updatePosition() {
        this.x = this.player.getX();
        this.y = this.player.getY();
        this.z = this.player.getZ();
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