package com.mrbysco.slabmachines.datagen.data;

import com.mrbysco.slabmachines.blocks.TNTSlabBlock;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.List;
import java.util.Set;

public class SlabLootProvider extends LootTableProvider {

	public SlabLootProvider(Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders) {
		super(requiredTables, subProviders);
	}

	public static class SlabBlockLoot extends BlockLootSubProvider {

		public SlabBlockLoot(LootTableSubProvider.Context context) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), context);
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
			this.add(SlabRegistry.TNT_SLAB.get(), LootTable.lootTable().withPool(applyExplosionCondition(SlabRegistry.TNT_SLAB.get(), LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
					.add(LootItem.lootTableItem(SlabRegistry.TNT_SLAB.get()).when(MatchBlock.blockMatches(this.blocks, SlabRegistry.TNT_SLAB.get(), StatePropertiesPredicate.Builder.properties().hasProperty(TNTSlabBlock.UNSTABLE, false)))))));
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return (Iterable<Block>) SlabRegistry.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
		}
	}
}
