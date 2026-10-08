package com.mrbysco.slabmachines.datagen.data;

import com.mrbysco.slabmachines.SlabReference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.references.BlockItemIds;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class SlabItemTagProvider extends ItemTagsProvider {
	public SlabItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, SlabReference.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		BlockItemTagsProvider.wrapForItems(this.tag(SlabReference.COBBLESTONE_SLABS)).add(
				BlockItemIds.COBBLESTONE_SLAB,
				BlockItemIds.COBBLED_DEEPSLATE_SLAB,
				BlockItemIds.BLACKSTONE_SLAB
		);
	}
}