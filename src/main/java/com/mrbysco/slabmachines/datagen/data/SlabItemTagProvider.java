package com.mrbysco.slabmachines.datagen.data;

import com.mrbysco.slabmachines.SlabReference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class SlabItemTagProvider extends ItemTagsProvider {
	public SlabItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
							   CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTagProvider, SlabReference.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(SlabReference.COBBLESTONE_SLABS).add(Items.COBBLESTONE_SLAB, Items.COBBLED_DEEPSLATE_SLAB, Items.BLACKSTONE_SLAB);
	}
}
