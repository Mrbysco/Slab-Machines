package com.mrbysco.slabmachines.datagen.data;

import com.mrbysco.slabmachines.blocks.TNTSlabBlock;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SlabLootProvider extends LootTableProvider {
	public SlabLootProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Set.of(), List.of(
				new SubProviderEntry(SlabBlockLoot::new, LootContextParamSets.BLOCK)
		), lookupProvider);
	}

	private static class SlabBlockLoot extends BlockLootSubProvider {

		protected SlabBlockLoot(HolderLookup.Provider provider) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
		}

		@Override
		protected void generate() {
			this.dropSelf(SlabRegistry.CRAFTING_TABLE_SLAB.get());
			this.dropSelf(SlabRegistry.CARTOGRAPHY_TABLE_SLAB.get());
			this.dropSelf(SlabRegistry.LOOM_SLAB.get());
			this.add(SlabRegistry.FURNACE_SLAB.get(), this::createNameableBlockEntityTable);
			this.add(SlabRegistry.BLAST_FURNACE_SLAB.get(), this::createNameableBlockEntityTable);
			this.add(SlabRegistry.SMOKER_SLAB.get(), this::createNameableBlockEntityTable);
			this.add(SlabRegistry.CHEST_SLAB.get(), this::createNameableBlockEntityTable);
			this.add(SlabRegistry.TRAPPED_CHEST_SLAB.get(), this::createNameableBlockEntityTable);
			this.dropSelf(SlabRegistry.NOTE_SLAB.get());
			this.add(SlabRegistry.TNT_SLAB.get(), LootTable.lootTable().withPool(applyExplosionCondition(SlabRegistry.TNT_SLAB.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(SlabRegistry.TNT_SLAB.get()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(SlabRegistry.TNT_SLAB.get())
							.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TNTSlabBlock.UNSTABLE, false)))))));
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return (Iterable<Block>) SlabRegistry.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
		}
	}

	@Override
	protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
		super.validate(writableregistry, validationcontext, problemreporter$collector);
	}
}
