package com.mrbysco.slabmachines.datagen.assets;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class SlabBlockModelProvider extends BlockModelProvider {
	public SlabBlockModelProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, SlabReference.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		generateSlab(SlabRegistry.CHEST_SLAB, modTexture("chest_slab_bottom"), modTexture("chest_slab_top"),
				modTexture("chest_slab_side"), modTexture("chest_slab_front"));
		generateSlab(SlabRegistry.CRAFTING_TABLE_SLAB, mcLoc("block/oak_planks"), mcLoc("block/crafting_table_top"),
				modTexture("crafting_table_slab_side"), modTexture("crafting_table_slab_front"));
		generateSlab(SlabRegistry.FURNACE_SLAB, mcLoc("block/furnace_top"), mcLoc("block/furnace_top"),
				modTexture("furnace_slab_side"), modTexture("furnace_slab_front"));
		generateFurnaceSlab(SlabRegistry.FURNACE_SLAB, mcLoc("block/furnace_top"), mcLoc("block/furnace_top"),
				modTexture("furnace_slab_side"), modTexture("furnace_slab_front_active"));
		generateSlab(SlabRegistry.BLAST_FURNACE_SLAB, mcLoc("block/blast_furnace_top"), mcLoc("block/blast_furnace_top"),
				modTexture("blast_furnace_slab_side"), modTexture("blast_furnace_slab_front"));
		generateFurnaceSlab(SlabRegistry.BLAST_FURNACE_SLAB, mcLoc("block/blast_furnace_top"), mcLoc("block/blast_furnace_top"),
				modTexture("blast_furnace_slab_side"), modTexture("blast_furnace_slab_front_active"));
		generateSlab(SlabRegistry.SMOKER_SLAB, mcLoc("block/smoker_top"), mcLoc("block/smoker_top"),
				modTexture("smoker_slab_side"), modTexture("smoker_slab_front"));
		generateFurnaceSlab(SlabRegistry.SMOKER_SLAB, mcLoc("block/smoker_top"), mcLoc("block/smoker_top"),
				modTexture("smoker_slab_side"), modTexture("smoker_slab_front_active"));
		generateSlab(SlabRegistry.NOTE_SLAB, mcLoc("block/note_block"), mcLoc("block/note_block"),
				modTexture("note_slab_side"), null);
		generateSlab(SlabRegistry.TNT_SLAB, mcLoc("block/tnt_bottom"), mcLoc("block/tnt_top"),
				modTexture("tnt_slab_side"), null);
		generateSlab(SlabRegistry.TRAPPED_CHEST_SLAB, modTexture("chest_slab_bottom"), modTexture("chest_slab_top"),
				modTexture("chest_slab_side"), modTexture("trapped_chest_slab_front"));
	}

	private ResourceLocation modTexture(String path) {
		return modLoc("block/" + path);
	}

	private void generateSlab(RegistryObject<Block> registryObject, ResourceLocation bottomTexture, ResourceLocation topTexture, ResourceLocation sideTexture, @Nullable ResourceLocation frontTexture) {
		String path = registryObject.getId().getPath();
		ResourceLocation frontToUse = frontTexture != null ? frontTexture : sideTexture;
		withExistingParent(path, modLoc("block/slab_base_bottom"))
				.texture("particle", frontToUse)
				.texture("down", bottomTexture)
				.texture("up", topTexture)
				.texture("north", frontToUse)
				.texture("east", sideTexture)
				.texture("south", sideTexture)
				.texture("west", sideTexture);

		withExistingParent(path + "_top", modLoc("block/slab_base_top"))
				.texture("particle", frontToUse)
				.texture("down", bottomTexture)
				.texture("up", topTexture)
				.texture("north", frontToUse)
				.texture("east", sideTexture)
				.texture("south", sideTexture)
				.texture("west", sideTexture);
	}

	private void generateFurnaceSlab(RegistryObject<Block> registryObject, ResourceLocation bottomTexture, ResourceLocation topTexture, ResourceLocation sideTexture, @Nullable ResourceLocation frontTexture) {
		String path = registryObject.getId().getPath();
		ResourceLocation frontToUse = frontTexture != null ? frontTexture : sideTexture;
		withExistingParent(path + "_on", modLoc("block/slab_base_bottom"))
				.texture("particle", frontToUse)
				.texture("down", bottomTexture)
				.texture("up", topTexture)
				.texture("north", frontToUse)
				.texture("east", sideTexture)
				.texture("south", sideTexture)
				.texture("west", sideTexture);

		withExistingParent(path + "_top_on", modLoc("block/slab_base_top"))
				.texture("particle", frontToUse)
				.texture("down", bottomTexture)
				.texture("up", topTexture)
				.texture("north", frontToUse)
				.texture("east", sideTexture)
				.texture("south", sideTexture)
				.texture("west", sideTexture);
	}
}
