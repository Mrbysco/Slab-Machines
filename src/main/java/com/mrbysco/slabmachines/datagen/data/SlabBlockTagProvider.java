package com.mrbysco.slabmachines.datagen.data;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class SlabBlockTagProvider extends BlockTagsProvider {
	public SlabBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, SlabReference.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(BlockTags.MINEABLE_WITH_AXE).add(SlabRegistry.CRAFTING_TABLE_SLAB.key(), SlabRegistry.CARTOGRAPHY_TABLE_SLAB.key(), SlabRegistry.LOOM_SLAB.key(), SlabRegistry.CHEST_SLAB.key(), SlabRegistry.TRAPPED_CHEST_SLAB.key(), SlabRegistry.NOTE_SLAB.key());
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(SlabRegistry.FURNACE_SLAB.key(), SlabRegistry.BLAST_FURNACE_SLAB.key(), SlabRegistry.SMOKER_SLAB.key());
		this.tag(BlockTags.GUARDED_BY_PIGLINS).add(SlabRegistry.CHEST_SLAB.key(), SlabRegistry.TRAPPED_CHEST_SLAB.key());
	}
}
