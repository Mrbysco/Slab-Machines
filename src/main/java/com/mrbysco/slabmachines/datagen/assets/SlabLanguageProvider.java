package com.mrbysco.slabmachines.datagen.assets;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class SlabLanguageProvider extends LanguageProvider {

	public SlabLanguageProvider(PackOutput packOutput) {
		super(packOutput, SlabReference.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("itemGroup.slabmachines.tab", "Slab Machines");

		add("slabmachines.config.title", "Slab Machines Config");

		addBlock(SlabRegistry.CRAFTING_TABLE_SLAB, "Crafting Slab");
		addBlock(SlabRegistry.FURNACE_SLAB, "Furnace Slab");
		addBlock(SlabRegistry.BLAST_FURNACE_SLAB, "Blast Furnace Slab");
		addBlock(SlabRegistry.SMOKER_SLAB, "Smoker Slab");
		addBlock(SlabRegistry.TRAPPED_CHEST_SLAB, "Trapped Chest Slab");
		addBlock(SlabRegistry.CHEST_SLAB, "Chest Slab");
		addBlock(SlabRegistry.NOTE_SLAB, "Note Slab");
		addBlock(SlabRegistry.TNT_SLAB, "Tnt Slab");

		add("block.slabmachines.crafting_station_slab", "Crafting Station Slab");
		add("block.slabmachines.part_builder_slab", "Part Builder Slab");
		add("block.slabmachines.part_chest_slab", "Part Chest Slab");
		add("block.slabmachines.pattern_chest_slab", "Pattern Chest Slab");
		add("block.slabmachines.stencil_table_slab", "Stencil Table Slab");
		add("block.slabmachines.tool_forge_slab", "Tool Forge Slab");
		add("block.slabmachines.tool_station_slab", "Tool Station Slab");

		add("slabmachines:container.furnace", "Furnace Slab");
		add("slabmachines:container.blast_furnace", "Blast Furnace Slab");
		add("slabmachines:container.smoker", "Smoker Slab");
		add("slabmachines:container.chest", "Chest Slab");
		add("slabmachines:container.crafting", "Crafting Slab");

		addEntityType(SlabRegistry.TNT_SLAB_ENTITY, "Slab Primed TNT");
	}
}
