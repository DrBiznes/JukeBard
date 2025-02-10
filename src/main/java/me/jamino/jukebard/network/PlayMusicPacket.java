package me.jamino.jukebard.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayMusicPacket {
    private final ResourceLocation soundId;

    public PlayMusicPacket(ResourceLocation soundId) {
        this.soundId = soundId;
    }

    public PlayMusicPacket(FriendlyByteBuf buf) {
        this.soundId = buf.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(soundId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Make sure we're on the client
            ClientMusicHandler.playMusic(soundId);
        });
        return true;
    }
}