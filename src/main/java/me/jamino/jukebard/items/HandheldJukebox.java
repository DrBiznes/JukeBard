package me.jamino.jukebard.items;

import me.jamino.jukebard.Jukebard;
import me.jamino.jukebard.JukebardConfig;
import me.jamino.jukebard.network.PlayMusicPacket;
import me.jamino.jukebard.network.StopMusicPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.ChatFormatting;
import net.minecraft.util.RandomSource;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class HandheldJukebox extends Item {
    private static final String NBT_CURRENT_DISC = "CurrentDisc";

    public HandheldJukebox() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        tooltip.add(Component.literal("A portable music player")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Right-click with a music disc to play")
                .withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.literal("Heard up to " + Math.round(JukebardConfig.MUSIC_RANGE.get()) + " blocks away")
                .withStyle(ChatFormatting.DARK_GRAY));

        ItemStack currentDisc = getCurrentDisc(stack);
        if (!currentDisc.isEmpty()) {
            tooltip.add(Component.literal("Current Disc: ")
                    .append(currentDisc.getDisplayName())
                    .withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack jukeboxStack = player.getItemInHand(hand);
        ItemStack offhandStack = player.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack currentDisc = getCurrentDisc(jukeboxStack);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (offhandStack.getItem() instanceof RecordItem musicDisc) {
                // Stop current music if playing
                if (!currentDisc.isEmpty()) {
                    // Send stop packet to all nearby players
                    for (ServerPlayer nearbyPlayer : ((ServerLevel)level).getPlayers(p ->
                            p.distanceToSqr(player) <= JukebardConfig.MUSIC_RANGE.get() * JukebardConfig.MUSIC_RANGE.get())) {
                        Jukebard.NETWORK.send(PacketDistributor.PLAYER.with(() -> nearbyPlayer),
                                new StopMusicPacket());
                    }
                }

                // Store and play the new disc
                setCurrentDisc(jukeboxStack, offhandStack.copy());

                // Send play packet to all nearby players with the source player's UUID
                for (ServerPlayer nearbyPlayer : ((ServerLevel)level).getPlayers(p ->
                        p.distanceToSqr(player) <= JukebardConfig.MUSIC_RANGE.get() * JukebardConfig.MUSIC_RANGE.get())) {
                    Jukebard.NETWORK.send(PacketDistributor.PLAYER.with(() -> nearbyPlayer),
                            new PlayMusicPacket(musicDisc.getSound().getLocation(), player.getUUID()));
                }

                // Consume the disc if in survival
                if (!player.getAbilities().instabuild) {
                    offhandStack.shrink(1);
                }
            } else if (!currentDisc.isEmpty()) {
                // Stop music and return disc for all nearby players
                for (ServerPlayer nearbyPlayer : ((ServerLevel)level).getPlayers(p ->
                        p.distanceToSqr(player) <= JukebardConfig.MUSIC_RANGE.get() * JukebardConfig.MUSIC_RANGE.get())) {
                    Jukebard.NETWORK.send(PacketDistributor.PLAYER.with(() -> nearbyPlayer),
                            new StopMusicPacket());
                }

                if (!player.getAbilities().instabuild) {
                    if (!player.getInventory().add(currentDisc)) {
                        player.drop(currentDisc, false);
                    }
                }
                setCurrentDisc(jukeboxStack, ItemStack.EMPTY);
            }
        } else if (level.isClientSide && JukebardConfig.ENABLE_PARTICLES.get()) {
            spawnMusicParticles(level, player);
        }

        return InteractionResultHolder.sidedSuccess(jukeboxStack, level.isClientSide);
    }

    private void spawnMusicParticles(Level level, Player player) {
        RandomSource random = level.getRandom();
        for (int i = 0; i < 6; i++) {
            double x = player.getX() + (random.nextDouble() - 0.5D) * 2.0D;
            double y = player.getY() + random.nextDouble() * 2.0D + 1.0D;
            double z = player.getZ() + (random.nextDouble() - 0.5D) * 2.0D;
            level.addParticle(ParticleTypes.NOTE, x, y, z, 0, 0, 0);
        }
    }

    private ItemStack getCurrentDisc(ItemStack jukeboxStack) {
        CompoundTag tag = jukeboxStack.getOrCreateTag();
        if (tag.contains(NBT_CURRENT_DISC)) {
            return ItemStack.of(tag.getCompound(NBT_CURRENT_DISC));
        }
        return ItemStack.EMPTY;
    }

    private void setCurrentDisc(ItemStack jukeboxStack, ItemStack disc) {
        CompoundTag tag = jukeboxStack.getOrCreateTag();
        if (disc.isEmpty()) {
            tag.remove(NBT_CURRENT_DISC);
        } else {
            CompoundTag discTag = new CompoundTag();
            disc.save(discTag);
            tag.put(NBT_CURRENT_DISC, discTag);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return !getCurrentDisc(stack).isEmpty() || super.isFoil(stack);
    }
}