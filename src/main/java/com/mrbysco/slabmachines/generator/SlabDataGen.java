package com.mrbysco.slabmachines.generator;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blocks.TNTSlabBlock;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = SlabReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlabDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new SlabLootProvider(packOutput));
			generator.addProvider(event.includeServer(), new SlabBlockTagProvider(packOutput, lookupProvider, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new SlabItemModelProvider(packOutput, helper));
		}
	}

	private static class SlabLootProvider extends LootTableProvider {
		public SlabLootProvider(PackOutput packOutput) {
			super(packOutput, Set.of(), List.of(
					new SubProviderEntry(SlabBlockLoot::new, LootContextParamSets.BLOCK)
			));
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
			map.forEach((name, table) -> LootTables.validate(validationtracker, name, table));
		}

		private static class SlabBlockLoot extends BlockLootSubProvider {

			protected SlabBlockLoot() {
				super(Set.of(), FeatureFlags.REGISTRY.allFlags());
			}

			@Override
			protected void generate() {
				this.dropSelf(SlabRegistry.CRAFTING_TABLE_SLAB.get());
				this.add(SlabRegistry.FURNACE_SLAB.get(), this::createNameableBlockEntityTable);
				this.add(SlabRegistry.CHEST_SLAB.get(), this::createNameableBlockEntityTable);
				this.add(SlabRegistry.TRAPPED_CHEST_SLAB.get(), this::createNameableBlockEntityTable);
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

	private static class SlabBlockTagProvider extends BlockTagsProvider {
		public SlabBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper fileHelper) {
			super(packOutput, lookupProvider, SlabReference.MOD_ID, fileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			this.tag(BlockTags.MINEABLE_WITH_AXE).add(SlabRegistry.CRAFTING_TABLE_SLAB.get(), SlabRegistry.CHEST_SLAB.get(), SlabRegistry.TRAPPED_CHEST_SLAB.get(), SlabRegistry.NOTE_SLAB.get());
			this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(SlabRegistry.FURNACE_SLAB.get());
		}
	}

	private static class SlabItemModelProvider extends ItemModelProvider {
		public SlabItemModelProvider(PackOutput packOutput, ExistingFileHelper helper) {
			super(packOutput, SlabReference.MOD_ID, helper);
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
