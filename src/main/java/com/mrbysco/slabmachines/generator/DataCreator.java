package com.mrbysco.slabmachines.generator;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blocks.TNTSlabBlock;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

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
//            generator.addProvider(new ItemModels(generator, helper));
        }
    }

    private static class Loots extends LootTableProvider {
        public Loots(DataGenerator gen) {
            super(gen);
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
            return ImmutableList.of(
                    Pair.of(Blocks::new, LootContextParamSets.BLOCK)
            );
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
            map.forEach((name, table) -> LootTables.validate(validationtracker, name, table));
        }
        private class Blocks extends BlockLoot {
            @Override
            protected void addTables() {
                this.dropSelf(CRAFTING_TABLE_SLAB.get());
                this.add(FURNACE_SLAB.get(), BlockLoot::createNameableBlockEntityTable);
                this.add(CHEST_SLAB.get(), BlockLoot::createNameableBlockEntityTable);
                this.add(TRAPPED_CHEST_SLAB.get(), BlockLoot::createNameableBlockEntityTable);
                this.dropSelf(NOTE_SLAB.get());
                this.add(TNT_SLAB.get(), LootTable.lootTable().withPool(applyExplosionCondition(TNT_SLAB.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TNT_SLAB.get()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TNT_SLAB.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TNTSlabBlock.UNSTABLE, false)))))));
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
