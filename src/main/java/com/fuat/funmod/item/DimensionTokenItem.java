package com.fuat.funmod.item;

import java.util.Set;

import com.fuat.funmod.FunMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
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
        PlayerEntity user = context.getPlayer();
        if (!(user instanceof ServerPlayerEntity player)) {
            return ActionResult.SUCCESS;
        }

        ServerWorld currentWorld = (ServerWorld) player.getEntityWorld();
        MinecraftServer server = currentWorld.getServer();
        ServerWorld destination = currentWorld.getRegistryKey().equals(destinationKey)
                ? server.getOverworld()
                : server.getWorld(destinationKey);

        if (destination == null) {
            player.sendMessage(Text.literal(dimensionName + " bu dünyada yüklenemedi. Yeni bir dünya oluşturup tekrar dene."), false);
            return ActionResult.SUCCESS;
        }

        BlockPos top = destination.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(0, 0, 0));
        if (destination.getRegistryKey().equals(FunMod.NETHERSTORM_WORLD)) {
            destination.setWeather(0, 1800, true, true);
        }

        player.teleport(destination, top.getX() + 0.5, top.getY() + 1.0, top.getZ() + 0.5,
                Set.of(), player.getYaw(), player.getPitch(), true);
        player.sendMessage(Text.literal(destination.getRegistryKey().equals(World.OVERWORLD)
                ? "Ana dünyaya döndün." : dimensionName + " boyutuna geçtin!"), false);
        return ActionResult.SUCCESS;
    }
}
