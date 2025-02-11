package me.jamino.jukebard.client;

import me.jamino.jukebard.Jukebard;
import me.jamino.jukebard.items.HandheldJukebox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Jukebard.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    public static final RecordOverlayManager OVERLAY_MANAGER = new RecordOverlayManager();

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            HandheldJukebox jukebox = (HandheldJukebox) Jukebard.HANDHELD_JUKEBOX.get();
            jukebox.registerModelProperties();
        });
    }

    @SubscribeEvent
    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(OVERLAY_MANAGER);
    }
}