package me.jamino.jukebard.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayMusicPacket {
    private final ResourceLocation soundId;
    private final UUID playerUUID;

    public PlayMusicPacket(ResourceLocation soundId, UUID playerUUID) {
        this.soundId = soundId;
        this.playerUUID = playerUUID;
    }

    public PlayMusicPacket(FriendlyByteBuf buf) {
        this.soundId = buf.readResourceLocation();
        this.playerUUID = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(soundId);
        buf.writeUUID(playerUUID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientMusicHandler.playMusic(soundId, playerUUID);
        });
        return true;
    }
}