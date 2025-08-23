package com.mrbysco.slabmachines.datagen.data;

import com.mrbysco.slabmachines.SlabReference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class SlabItemTagProvider extends ItemTagsProvider {
	public SlabItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, SlabReference.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(SlabReference.COBBLESTONE_SLABS).add(Items.COBBLESTONE_SLAB, Items.COBBLED_DEEPSLATE_SLAB, Items.BLACKSTONE_SLAB);
	}
}
