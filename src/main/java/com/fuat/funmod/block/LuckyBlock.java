package com.fuat.funmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class LuckyBlock extends Block {
    public LuckyBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world instanceof ServerWorld serverWorld && !player.isCreative()) {
            switch (world.getRandom().nextInt(5)) {
                case 0 -> {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            new ItemStack(Items.DIAMOND, 3)));
                    player.sendMessage(Text.literal("§bŞanslı gün! 3 elmas kazandın."), true);
                }
                case 1 -> {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            new ItemStack(Items.EMERALD, 8)));
                    player.sendMessage(Text.literal("§aZümrüt yağmuru!"), true);
                }
                case 2 -> {
                    EntityType.CREEPER.spawn(serverWorld, pos.up(), SpawnReason.TRIGGERED);
                    player.sendMessage(Text.literal("§cSürpriz! Bir creeper ortaya çıktı."), true);
                }
                case 3 -> {
                    world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            2.0F, World.ExplosionSourceType.BLOCK);
                    player.sendMessage(Text.literal("§6Şans Bloğu patladı!"), true);
                }
                default -> {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 600, 1));
                    player.sendMessage(Text.literal("§dŞans güçlendirmesi kazandın!"), true);
                }
            }
        }
        return super.onBreak(world, pos, state, player);
    }
}
