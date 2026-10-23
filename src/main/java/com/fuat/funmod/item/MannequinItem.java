package com.fuat.funmod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class MannequinItem extends Item {
    public MannequinItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            BlockPos spawnPos = context.getBlockPos().offset(context.getSide());
            Entity stand = EntityType.ARMOR_STAND.spawn(serverWorld, spawnPos, SpawnReason.SPAWN_ITEM_USE);
            if (stand != null) {
                stand.setCustomName(Text.literal("Mücevher Mankeni"));
                stand.setCustomNameVisible(true);
                if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
                    context.getStack().decrement(1);
                }
            }
        }
        return ActionResult.SUCCESS;
    }
}
