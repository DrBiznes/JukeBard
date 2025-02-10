package me.jamino.jukebard.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayMusicPacket {
    private final ResourceLocation soundId;
    private final double x, y, z;

    public PlayMusicPacket(ResourceLocation soundId, double x, double y, double z) {
        this.soundId = soundId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PlayMusicPacket(FriendlyByteBuf buf) {
        this.soundId = buf.readResourceLocation();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(soundId);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientMusicHandler.playMusic(soundId, x, y, z);
        });
        return true;
    }
}