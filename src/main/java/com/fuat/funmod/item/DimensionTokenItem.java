package com.fuat.funmod.item;

import java.util.Set;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public final class DimensionTokenItem extends Item {
    private final RegistryKey<World> destinationKey;
    private final String dimensionName;

    public DimensionTokenItem(Settings settings, RegistryKey<World> destinationKey, String dimensionName) {
        super(settings);
        this.destinationKey = destinationKey;
        this.dimensionName = dimensionName;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!(context.getPlayer() instanceof ServerPlayerEntity player) || player.getServer() == null) {
            return ActionResult.SUCCESS;
        }

        ServerWorld destination;
        if (player.getWorld().getRegistryKey().equals(destinationKey)) {
            destination = player.getServer().getOverworld();
        } else {
            destination = player.getServer().getWorld(destinationKey);
        }

        if (destination == null) {
            player.sendMessage(Text.literal(dimensionName + " bu dünyada yüklenemedi. Yeni bir dünya oluşturup tekrar dene."), false);
            return ActionResult.SUCCESS;
        }

        BlockPos top = destination.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(0, 0, 0));
        player.teleport(destination, top.getX() + 0.5, top.getY() + 1.0, top.getZ() + 0.5,
                Set.of(), player.getYaw(), player.getPitch(), true);
        player.sendMessage(Text.literal(destination.getRegistryKey().equals(World.OVERWORLD)
                ? "Ana dünyaya döndün." : dimensionName + " boyutuna geçtin!"), false);
        return ActionResult.SUCCESS;
    }
}
