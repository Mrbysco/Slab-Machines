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
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
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
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = SlabReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlabDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new Loots(generator));
			generator.addProvider(event.includeServer(), new MineableProvider(generator, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new ItemModels(generator, helper));
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
				this.dropSelf(SlabRegistry.CRAFTING_TABLE_SLAB.get());
				this.add(SlabRegistry.FURNACE_SLAB.get(), BlockLoot::createNameableBlockEntityTable);
				this.add(SlabRegistry.CHEST_SLAB.get(), BlockLoot::createNameableBlockEntityTable);
				this.add(SlabRegistry.TRAPPED_CHEST_SLAB.get(), BlockLoot::createNameableBlockEntityTable);
				this.dropSelf(SlabRegistry.NOTE_SLAB.get());
				this.add(SlabRegistry.TNT_SLAB.get(), LootTable.lootTable().withPool(applyExplosionCondition(SlabRegistry.TNT_SLAB.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(SlabRegistry.TNT_SLAB.get()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(SlabRegistry.TNT_SLAB.get())
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TNTSlabBlock.UNSTABLE, false)))))));
			}

			@Override
			protected Iterable<Block> getKnownBlocks() {
				return (Iterable<Block>) SlabRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
			}
		}
	}

	private static class MineableProvider extends BlockTagsProvider {
		public MineableProvider(DataGenerator gen, ExistingFileHelper fileHelper) {
			super(gen, SlabReference.MOD_ID, fileHelper);
		}

		@Override
		protected void addTags() {
			this.tag(BlockTags.MINEABLE_WITH_AXE).add(SlabRegistry.CRAFTING_TABLE_SLAB.get(), SlabRegistry.CHEST_SLAB.get(), SlabRegistry.TRAPPED_CHEST_SLAB.get(), SlabRegistry.NOTE_SLAB.get());
			this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(SlabRegistry.FURNACE_SLAB.get());
		}
	}

	private static class ItemModels extends ItemModelProvider {
		public ItemModels(DataGenerator gen, ExistingFileHelper helper) {
			super(gen, SlabReference.MOD_ID, helper);
		}

		@Override
		protected void registerModels() {
			makeSlab(SlabRegistry.CRAFTING_TABLE_SLAB.get());
			makeSlab(SlabRegistry.FURNACE_SLAB.get());
			makeSlab(SlabRegistry.CHEST_SLAB.get());
			makeSlab(SlabRegistry.TRAPPED_CHEST_SLAB.get());
			makeSlab(SlabRegistry.NOTE_SLAB.get());
			makeSlab(SlabRegistry.TNT_SLAB.get());
		}

		private void makeSlab(Block block) {
			String path = ForgeRegistries.BLOCKS.getKey(block).getPath();
			getBuilder(path)
					.parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
		}

		@Override
		public String getName() {
			return "Item Models";
		}
	}
}
