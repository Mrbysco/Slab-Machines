package com.mrbysco.slabmachines.datagen.data;

import com.mrbysco.slabmachines.blocks.TNTSlabBlock;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
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
import java.util.Map;
import java.util.Set;

public class SlabLootProvider extends LootTableProvider {
	public SlabLootProvider(PackOutput packOutput) {
		super(packOutput, Set.of(), List.of(
				new SubProviderEntry(SlabBlockLoot::new, LootContextParamSets.BLOCK)
		));
	}

	private static class SlabBlockLoot extends BlockLootSubProvider {

		protected SlabBlockLoot() {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		protected void generate() {
			this.dropSelf(SlabRegistry.CRAFTING_TABLE_SLAB.get());
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
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
		map.forEach((name, table) -> table.validate(validationtracker));
	}
}
