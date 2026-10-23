package com.fuat.funmod.block;

import com.fuat.funmod.FunMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class CashRegisterBlock extends Block {
    public CashRegisterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.getMainHandStack().isOf(FunMod.SHOP_OPEN_KEY)) {
            if (world instanceof ServerWorld) {
                player.sendMessage(Text.literal("Kasayı açmak için Dükkan Açma Anahtarı kullan."), true);
            }
            return ActionResult.SUCCESS;
        }
        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.SUCCESS;
        }

        int spawned = 0;
        for (int i = 0; i < 3; i++) {
            int dx = serverWorld.getRandom().nextInt(7) - 3;
            int dz = serverWorld.getRandom().nextInt(7) - 3;
            BlockPos spawnPos = pos.add(dx, 1, dz);
            VillagerEntity customer = EntityType.VILLAGER.spawn(serverWorld, spawnPos, SpawnReason.TRIGGERED);
            if (customer == null) continue;

            customer.addCommandTag("funmod_customer");
            int paymentType = serverWorld.getRandom().nextInt(10);
            if (paymentType == 0) {
                customer.addCommandTag("funmod_payment_dollar");
                customer.setCustomName(Text.literal("Müşteri • Dolarla Ödeyecek"));
            } else if (paymentType == 1) {
                customer.addCommandTag("funmod_payment_euro");
                customer.setCustomName(Text.literal("Müşteri • Euro ile Ödeyecek"));
            } else {
                customer.addCommandTag("funmod_payment_emerald");
                customer.setCustomName(Text.literal("Müşteri • Zümrüt Ödemesi"));
            }
            customer.setCustomNameVisible(true);
            spawned++;
        }

        player.sendMessage(Text.literal("Dükkân açıldı! " + spawned + " müşteri geldi. Ödeme için müşteriye kasanın yakınında sağ tıkla."), false);
        return ActionResult.SUCCESS;
    }
}
