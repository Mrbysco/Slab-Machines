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
		this.tag(BlockTags.MINEABLE_WITH_AXE).add(SlabRegistry.CRAFTING_TABLE_SLAB.getKey(), SlabRegistry.CARTOGRAPHY_TABLE_SLAB.getKey(), SlabRegistry.LOOM_SLAB.getKey(), SlabRegistry.CHEST_SLAB.getKey(), SlabRegistry.TRAPPED_CHEST_SLAB.getKey(), SlabRegistry.NOTE_SLAB.getKey());
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(SlabRegistry.FURNACE_SLAB.getKey(), SlabRegistry.BLAST_FURNACE_SLAB.getKey(), SlabRegistry.SMOKER_SLAB.getKey());
		this.tag(BlockTags.GUARDED_BY_PIGLINS).add(SlabRegistry.CHEST_SLAB.getKey(), SlabRegistry.TRAPPED_CHEST_SLAB.getKey());
	}
}
