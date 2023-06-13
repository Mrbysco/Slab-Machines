package com.mrbysco.slabmachines.datagen.data;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SlabBlockTagProvider extends BlockTagsProvider {
	public SlabBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, SlabReference.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(BlockTags.MINEABLE_WITH_AXE).add(SlabRegistry.CRAFTING_TABLE_SLAB.get(), SlabRegistry.CHEST_SLAB.get(), SlabRegistry.TRAPPED_CHEST_SLAB.get(), SlabRegistry.NOTE_SLAB.get());
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(SlabRegistry.FURNACE_SLAB.get(), SlabRegistry.BLAST_FURNACE_SLAB.get(), SlabRegistry.SMOKER_SLAB.get());
		this.tag(BlockTags.GUARDED_BY_PIGLINS).add(SlabRegistry.CHEST_SLAB.get(), SlabRegistry.TRAPPED_CHEST_SLAB.get());
	}
}
