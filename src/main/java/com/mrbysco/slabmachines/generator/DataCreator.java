package com.mrbysco.slabmachines.generator;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blocks.TNTSlabBlock;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mrbysco.slabmachines.init.SlabRegistry.CHEST_SLAB;
import static com.mrbysco.slabmachines.init.SlabRegistry.CRAFTING_TABLE_SLAB;
import static com.mrbysco.slabmachines.init.SlabRegistry.FURNACE_SLAB;
import static com.mrbysco.slabmachines.init.SlabRegistry.NOTE_SLAB;
import static com.mrbysco.slabmachines.init.SlabRegistry.TNT_SLAB;
import static com.mrbysco.slabmachines.init.SlabRegistry.TRAPPED_CHEST_SLAB;

@Mod.EventBusSubscriber(modid = SlabReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataCreator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeServer()) {
            generator.addProvider(new Loots(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(new ItemModels(generator, helper));
        }
    }

    private static class Loots extends LootTableProvider {
        public Loots(DataGenerator gen) {
            super(gen);
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables() {
            return ImmutableList.of(
                    Pair.of(Blocks::new, LootParameterSets.BLOCK)
            );
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
            map.forEach((name, table) -> LootTableManager.validateLootTable(validationtracker, name, table));
        }
        private class Blocks extends BlockLootTables {
            @Override
            protected void addTables() {
                this.registerDropSelfLootTable(CRAFTING_TABLE_SLAB.get());
                this.registerLootTable(FURNACE_SLAB.get(), BlockLootTables::droppingWithName);
                this.registerLootTable(CHEST_SLAB.get(), BlockLootTables::droppingWithName);
                this.registerLootTable(TRAPPED_CHEST_SLAB.get(), BlockLootTables::droppingWithName);
                this.registerDropSelfLootTable(NOTE_SLAB.get());
                this.registerLootTable(TNT_SLAB.get(), LootTable.builder().addLootPool(withSurvivesExplosion(TNT_SLAB.get(), LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(TNT_SLAB.get()).acceptCondition(BlockStateProperty.builder(TNT_SLAB.get()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withBoolProp(TNTSlabBlock.UNSTABLE, false)))))));
            }

            @Override
            protected Iterable<Block> getKnownBlocks() {
                return (Iterable<Block>) SlabRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
            }
        }
    }

    private static class ItemModels extends ItemModelProvider {
        public ItemModels(DataGenerator gen, ExistingFileHelper helper) {
            super(gen, SlabReference.MOD_ID, helper);
        }

        @Override
        protected void registerModels() {
            makeSlab(CRAFTING_TABLE_SLAB.get());
            makeSlab(FURNACE_SLAB.get());
            makeSlab(CHEST_SLAB.get());
            makeSlab(TRAPPED_CHEST_SLAB.get());
            makeSlab(NOTE_SLAB.get());
            makeSlab(TNT_SLAB.get());
        }

        private void makeSlab(Block block) {
            String path = block.getRegistryName().getPath();
            getBuilder(path)
                    .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
        }

        @Override
        public String getName() {
            return "Item Models";
        }
    }
}
