package com.fuat.funmod;

import com.fuat.funmod.block.CashRegisterBlock;
import com.fuat.funmod.block.LuckyBlock;
import com.fuat.funmod.item.DimensionTokenItem;
import com.fuat.funmod.item.MannequinItem;
import com.fuat.funmod.item.SummonItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class FunMod implements ModInitializer {
    public static final String MOD_ID = "funmod";
    public static final RegistryKey<World> NETHERSTORM_WORLD = RegistryKey.of(RegistryKeys.WORLD, id("netherstorm"));
    public static final RegistryKey<World> LUCK_WORLD = RegistryKey.of(RegistryKeys.WORLD, id("luck_realm"));

    public static final Block LUCKY_BLOCK = registerBlock(
            "lucky_block",
            new LuckyBlock(AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK).strength(1.8F).luminance(state -> 5))
    );
    public static final Block RAINBOW_BLOCK = registerBlock(
            "rainbow_block",
            new Block(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).strength(1.5F).luminance(state -> 8))
    );
    public static final Block JEWELRY_CASE = registerBlock(
            "jewelry_case",
            new Block(AbstractBlock.Settings.copy(Blocks.GLASS).strength(1.0F).luminance(state -> 2))
    );
    public static final Block CASH_REGISTER = registerBlock(
            "cash_register",
            new CashRegisterBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).strength(2.0F).luminance(state -> 4))
    );

    public static final Item LUCKY_BLOCK_ITEM = registerBlockItem("lucky_block", LUCKY_BLOCK);
    public static final Item RAINBOW_BLOCK_ITEM = registerBlockItem("rainbow_block", RAINBOW_BLOCK);
    public static final Item JEWELRY_CASE_ITEM = registerBlockItem("jewelry_case", JEWELRY_CASE);
    public static final Item CASH_REGISTER_ITEM = registerBlockItem("cash_register", CASH_REGISTER);

    public static final Item GOLD_BRACELET = registerItem("gold_bracelet", new Item(new Item.Settings().maxCount(64)));
    public static final Item NECKLACE = registerItem("necklace", new Item(new Item.Settings().maxCount(64)));
    public static final Item DIAMOND_EARRING = registerItem("diamond_earring", new Item(new Item.Settings().maxCount(1).equippable(EquipmentSlot.HEAD)));
    public static final Item EMERALD_EARRING = registerItem("emerald_earring", new Item(new Item.Settings().maxCount(1).equippable(EquipmentSlot.HEAD)));
    public static final Item REDSTONE_EARRING = registerItem("redstone_earring", new Item(new Item.Settings().maxCount(1).equippable(EquipmentSlot.HEAD)));
    public static final Item GOLD_EARRING = registerItem("gold_earring", new Item(new Item.Settings().maxCount(1).equippable(EquipmentSlot.HEAD)));

    public static final Item DOLLAR = registerItem("dollar", new Item(new Item.Settings().maxCount(64)));
    public static final Item EURO = registerItem("euro", new Item(new Item.Settings().maxCount(64)));
    public static final Item STORM_SHARD = registerItem("storm_shard", new Item(new Item.Settings().maxCount(64)));
    public static final Item SHOP_OPEN_KEY = registerItem("shop_open_key", new Item(new Item.Settings().maxCount(1)));

    public static final Item NETHERSTORM_TOKEN = registerItem("netherstorm_token",
            new DimensionTokenItem(new Item.Settings().maxCount(1), NETHERSTORM_WORLD, "Netherstorm"));
    public static final Item LUCK_DIMENSION_TOKEN = registerItem("luck_dimension_token",
            new DimensionTokenItem(new Item.Settings().maxCount(1), LUCK_WORLD, "Şans Evreni"));

    public static final Item MANNEQUIN = registerItem("mannequin", new MannequinItem(new Item.Settings().maxCount(16)));
    public static final Item STORM_GOLEM_SUMMON = registerItem(
            "storm_golem_summon", new SummonItem(new Item.Settings().maxCount(16), EntityType.IRON_GOLEM, "Yıldırım Golemi", false));
    public static final Item STORM_LORD_SUMMON = registerItem(
            "storm_lord_summon", new SummonItem(new Item.Settings().maxCount(16), EntityType.WARDEN, "Yıldırım Lordu", true));
    public static final Item LUCKY_VILLAGER_SUMMON = registerItem(
            "lucky_villager_summon", new SummonItem(new Item.Settings().maxCount(16), EntityType.WANDERING_TRADER, "Şans Köylüsü", false));

    private static final ItemGroup FUN_MOD_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(STORM_SHARD))
            .displayName(Text.literal("Fun-Mod 2.0"))
            .entries((context, entries) -> {
                entries.add(LUCKY_BLOCK_ITEM);
                entries.add(RAINBOW_BLOCK_ITEM);
                entries.add(STORM_SHARD);
                entries.add(STORM_GOLEM_SUMMON);
                entries.add(STORM_LORD_SUMMON);
                entries.add(LUCKY_VILLAGER_SUMMON);
                entries.add(NETHERSTORM_TOKEN);
                entries.add(LUCK_DIMENSION_TOKEN);
                entries.add(GOLD_BRACELET);
                entries.add(NECKLACE);
                entries.add(DIAMOND_EARRING);
                entries.add(EMERALD_EARRING);
                entries.add(REDSTONE_EARRING);
                entries.add(GOLD_EARRING);
                entries.add(JEWELRY_CASE_ITEM);
                entries.add(CASH_REGISTER_ITEM);
                entries.add(SHOP_OPEN_KEY);
                entries.add(MANNEQUIN);
                entries.add(DOLLAR);
                entries.add(EURO);
            })
            .build();

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, id(name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return registerItem(name, new BlockItem(block, new Item.Settings()));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, id(name), item);
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, id("main"), FUN_MOD_GROUP);
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                applyEarringEffects(player);
            }
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!(world instanceof ServerWorld serverWorld)
                    || !(entity instanceof VillagerEntity customer)
                    || !customer.getCommandTags().contains("funmod_customer")) {
                return ActionResult.PASS;
            }
            if (!nearRegister(serverWorld, player.getBlockPos())) {
                player.sendMessage(Text.literal("Müşteri ödemesi için kasanın yanında olmalısın."), true);
                return ActionResult.SUCCESS;
            }
            if (customer.getCommandTags().contains("funmod_paid")) {
                return ActionResult.SUCCESS;
            }

            Item payment;
            int amount;
            if (customer.getCommandTags().contains("funmod_payment_dollar")) {
                payment = DOLLAR;
                amount = 1;
            } else if (customer.getCommandTags().contains("funmod_payment_euro")) {
                payment = EURO;
                amount = 1;
            } else {
                payment = Items.EMERALD;
                amount = 3 + serverWorld.getRandom().nextInt(6);
            }

            ItemStack paymentStack = new ItemStack(payment, amount);
            if (!player.giveItemStack(paymentStack)) {
                player.dropItem(paymentStack, false);
            }
            customer.addCommandTag("funmod_paid");
            customer.discard();
            player.sendMessage(Text.literal("Ödeme alındı! Müşteri dükkândan ayrıldı."), true);
            return ActionResult.SUCCESS;
        });
    }

    private static boolean nearRegister(ServerWorld world, BlockPos center) {
        for (int x = -6; x <= 6; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -6; z <= 6; z++) {
                    if (world.getBlockState(center.add(x, y, z)).is(CASH_REGISTER)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void applyEarringEffects(ServerPlayerEntity player) {
        ItemStack head = player.getEquippedStack(EquipmentSlot.HEAD);
        if (head.isOf(GOLD_EARRING)) {
            player.getHungerManager().add(1, 1.0F);
        }
        if (head.isOf(DIAMOND_EARRING)) refresh(player, StatusEffects.STRENGTH, 0);
        if (head.isOf(EMERALD_EARRING)) refresh(player, StatusEffects.HERO_OF_THE_VILLAGE, 0);
        if (head.isOf(REDSTONE_EARRING)) refresh(player, StatusEffects.RESISTANCE, 0);
    }

    private static void refresh(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect, int amplifier) {
        player.addStatusEffect(new StatusEffectInstance(effect, 220, amplifier, true, false, false));
    }
}
