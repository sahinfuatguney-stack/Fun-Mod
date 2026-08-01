package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "funmod";

    // --- SÜRPRIZ KUTU (LUCKY BLOCK) ---
    public static final Block SURPRISE_BOX = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).strength(2.0f)) {
        @Override
        public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
            if (!world.isClient) {
                Random rand = new Random();
                int chance = rand.nextInt(3);

                if (chance == 0) {
                    // Elmas Yağmuru
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.DIAMOND, 5)));
                    player.sendMessage(Text.literal("§b💎 Şanslı Günündesin!"), false);
                } else if (chance == 1) {
                    // Patlama
                    world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3.0F, World.ExplosionSourceType.BLOCK);
                    player.sendMessage(Text.literal("§c💥 Ups! Kaç!"), false);
                } else {
                    // Yıldırım
                    LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                    if (lightning != null) {
                        lightning.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
                        world.spawnEntity(lightning);
                    }
                    player.sendMessage(Text.literal("§e⚡ Yıldırım Düştü!"), false);
                }
            }
            return super.onBreak(world, pos, state, player);
        }
    };

    @Override
    public void onInitialize() {
        // Blok ve Eşya Kaydı
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "surprise_box"), SURPRISE_BOX);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "surprise_box"), new BlockItem(SURPRISE_BOX, new FabricItemSettings()));
    }
}
