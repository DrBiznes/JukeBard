package me.jamino.jukebard;

import me.jamino.jukebard.items.HandheldJukebox;
import me.jamino.jukebard.network.PlayMusicPacket;
import me.jamino.jukebard.network.StopMusicPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Jukebard.MODID)
public class Jukebard {
    public static final String MODID = "jukebard";

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    // Create DeferredRegister objects
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Register the handheld jukebox item
    public static final RegistryObject<Item> HANDHELD_JUKEBOX = ITEMS.register("handheld_jukebox",
            HandheldJukebox::new);

    // Create creative tab
    public static final RegistryObject<CreativeModeTab> JUKEBARD_TAB = CREATIVE_MODE_TABS.register("jukebard_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.jukebard_tab"))
                    .icon(() -> HANDHELD_JUKEBOX.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(HANDHELD_JUKEBOX.get());
                    })
                    .build()
    );

    public Jukebard() {
        // Even though this method is deprecated, it's still the correct way to get the event bus in 1.20.1
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register DeferredRegisters to the event bus
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register network messages
        int id = 0;
        NETWORK.messageBuilder(PlayMusicPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayMusicPacket::new)
                .encoder(PlayMusicPacket::encode)
                .consumerMainThread(PlayMusicPacket::handle)
                .add();

        NETWORK.messageBuilder(StopMusicPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StopMusicPacket::new)
                .encoder(StopMusicPacket::encode)
                .consumerMainThread(StopMusicPacket::handle)
                .add();
    }
}