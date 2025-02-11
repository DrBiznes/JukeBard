package me.jamino.jukebard.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import me.jamino.jukebard.Jukebard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RecordOverlayManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordOverlayManager.class);
    private static final Gson GSON = new Gson();
    private static final ResourceLocation DEFAULT_MODDED_OVERLAY = new ResourceLocation(Jukebard.MODID, "item/overlay/modded");
    private Map<ResourceLocation, ResourceLocation> overlayTextures = new HashMap<>();

    public RecordOverlayManager() {
        super(GSON, "record_overlays");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        overlayTextures.clear();

        pObject.forEach((location, jsonElement) -> {
            try {
                Map<String, String> overlays = GSON.fromJson(jsonElement, new TypeToken<Map<String, String>>(){}.getType());
                overlays.forEach((record, texture) -> {
                    overlayTextures.put(
                            new ResourceLocation(record),
                            new ResourceLocation(texture)
                    );
                });
            } catch (Exception e) {
                LOGGER.error("Failed to parse record overlay file: " + location, e);
            }
        });
    }

    public ResourceLocation getOverlayTexture(ResourceLocation recordId) {
        if (recordId == null) return null;

        // First check if we have a specific overlay defined
        ResourceLocation overlay = overlayTextures.get(recordId);
        if (overlay != null) {
            return overlay;
        }

        // If no specific overlay and it's a music disc, return the default modded overlay
        String path = recordId.getPath();
        if (path.contains("music_disc") || path.contains("record")) {
            return DEFAULT_MODDED_OVERLAY;
        }

        return null;
    }
}