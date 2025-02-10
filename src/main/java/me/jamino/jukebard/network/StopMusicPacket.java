package me.jamino.jukebard.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StopMusicPacket {
    public StopMusicPacket() {}

    public StopMusicPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(ClientMusicHandler::stopMusic);
        return true;
    }
}