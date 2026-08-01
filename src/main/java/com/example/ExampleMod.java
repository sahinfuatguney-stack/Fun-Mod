package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "funmod";

    // --- 1. SÜRPRIZ KUTU (LUCKY BLOCK) ---
    public static final Block SURPRISE_BOX = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).strength(2.0f)) {
        @Override
        public BlockState onBreak(World world, BlockPos pos, BlockState state, net.minecraft.entity.player.PlayerEntity player) {
            if (!world.isClient) {
                Random rand = new Random();
                int chance = rand.nextInt(4); // 0, 1, 2, veya 3

                if (chance == 0) {
                    // Elmas Yağmuru
                    world.spawnEntity(new net.minecraft.entity.ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.DIAMOND, 3)));
                    player.sendMessage(Text.literal("§b💎 Şanslı Günündesin!"), false);
                } else if (chance == 1) {
                    // Kötü Şans: TNT
                    world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, World.ExplosionSourceType.BLOCK);
                    player.sendMessage(Text.literal("§c💥 Ups! Kaç!"), false);
                } else if (chance == 2) {
                    // Efsanevi Şans: Yıldırım ve Güç
                    LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                    if (lightning != null) {
                        lightning.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
                        world.spawnEntity(lightning);
                    }
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 2));
                    player.sendMessage(Text.literal("§e⚡ GÜÇ SENDE!"), false);
                } else {
                    // Yaratık Tuzağı: 3 Tane Hızlı Zombi
                    for(int i=0; i<3; i++) {
                        FastZombieEntity zombie = FAST_ZOMBIE.create(world);
                        if(zombie != null) {
                            zombie.refreshPositionAndAngles(pos.getX() + rand.nextInt(3)-1, pos.getY(), pos.getZ() + rand.nextInt(3)-1, 0.0F, 0.0F);
                            world.spawnEntity(zombie);
                        }
                    }
                    player.sendMessage(Text.literal("§6🧟‍♂️ Dikkat! Saldırı!"), false);
                }
            }
            return super.onBreak(world, pos, state, player);
        }
    };

    // --- 2. GÖKKUŞAĞI BLOĞU ---
    public static final Block RAINBOW_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).strength(1.0f).luminance(10)) {
        @Override
        public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
            if (world.isClient && entity instanceof net.minecraft.entity.player.PlayerEntity) {
                world.addParticle(ParticleTypes.HEART, entity.getX(), entity.getY() + 0.5, entity.getZ(), 0.0, 0.0, 0.0);
            }
            super.onSteppedOn(world, pos, state, entity);
        }
    };

    // --- 3. ADALET KILICI (YILDIRIMLI) ---
    public static final ToolItem JUSTICE_SWORD = new SwordItem(ToolMaterials.NETHERITE, 5, -2.4f, new FabricItemSettings()) {
        @Override
        public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
            World world = target.getWorld();
            if (!world.isClient && attacker instanceof net.minecraft.entity.player.PlayerEntity) {
                LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                if (lightning != null) {
                    lightning.refreshPositionAndAngles(target.getX(), target.getY(), target.getZ(), 0.0F, 0.0F);
                    world.spawnEntity(lightning);
                }
            }
            return super.postHit(stack, target, attacker);
        }
    };

    // --- 4. HIZLI ZOMBİ YARATIĞI ---
    public static final EntityType<FastZombieEntity> FAST_ZOMBIE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "fast_zombie"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, FastZombieEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f)).build()
    );

    public static class FastZombieEntity extends ZombieEntity {
        public FastZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
            super(entityType, world);
        }
        public static net.minecraft.entity.attribute.DefaultAttributeContainer.Builder createZombieAttributes() {
            return ZombieEntity.createZombieAttributes()
                    .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                    .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45D)
                    .add(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0D);
        }
    }

    @Override
    public void onInitialize() {
        // Kayıt İşlemleri
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "surprise_box"), SURPRISE_BOX);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "surprise_box"), new BlockItem(SURPRISE_BOX, new FabricItemSettings()));

        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "rainbow_block"), RAINBOW_BLOCK);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "rainbow_block"), new BlockItem(RAINBOW_BLOCK, new FabricItemSettings()));

        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "justice_sword"), JUSTICE_SWORD);

        FabricDefaultAttributeRegistry.register(FAST_ZOMBIE, FastZombieEntity.createZombieAttributes());
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "fast_zombie_spawn_egg"),
                new SpawnEggItem(FAST_ZOMBIE, 0x00FF00, 0xFF0000, new FabricItemSettings()));
    }
}
