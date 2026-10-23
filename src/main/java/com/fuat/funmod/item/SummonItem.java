package com.fuat.funmod.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class SummonItem extends Item {
    private final EntityType<? extends LivingEntity> entityType;
    private final String displayName;
    private final boolean boss;

    public SummonItem(Settings settings, EntityType<? extends LivingEntity> entityType, String displayName, boolean boss) {
        super(settings);
        this.entityType = entityType;
        this.displayName = displayName;
        this.boss = boss;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            BlockPos spawnPos = context.getBlockPos().offset(context.getSide());
            LivingEntity entity = entityType.spawn(serverWorld, spawnPos, SpawnReason.SPAWN_ITEM_USE);
            if (entity != null) {
                entity.setCustomName(Text.literal(displayName));
                entity.setCustomNameVisible(true);
                if (boss) {
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 12000, 1));
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 12000, 1));
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 12000, 0));
                }
                if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
                    context.getStack().decrement(1);
                }
            }
        }
        return ActionResult.SUCCESS;
    }
}
