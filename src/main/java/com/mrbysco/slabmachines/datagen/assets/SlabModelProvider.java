package com.mrbysco.slabmachines.datagen.assets;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blocks.AbstractFurnaceSlabBlock;
import com.mrbysco.slabmachines.blocks.base.CustomSlabBlock;
import com.mrbysco.slabmachines.blocks.base.FacingMultiSlabBlock;
import com.mrbysco.slabmachines.blocks.base.enums.CustomSlabType;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SlabModelProvider extends ModelProvider {
	public static final ModelTemplate SLAB_TOP = ModelTemplates.create("slabmachines:slab_base_top",
			TextureSlot.NORTH, TextureSlot.EAST, TextureSlot.SOUTH, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN
	);
	public static final ModelTemplate SLAB_BOTTOM = ModelTemplates.create("slabmachines:slab_base_bottom",
			TextureSlot.NORTH, TextureSlot.EAST, TextureSlot.SOUTH, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN
	);

	public SlabModelProvider(PackOutput output) {
		super(output, SlabReference.MOD_ID);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		generateFacingSlab(blockModels, SlabRegistry.CHEST_SLAB, modTexture("chest_slab_bottom"), modTexture("chest_slab_top"),
				modTexture("chest_slab_side"), modTexture("chest_slab_front"));
		generateFacingSlab(blockModels, SlabRegistry.TRAPPED_CHEST_SLAB, modTexture("chest_slab_bottom"), modTexture("chest_slab_top"),
				modTexture("chest_slab_side"), modTexture("trapped_chest_slab_front"));

		generateSlab(blockModels, SlabRegistry.CRAFTING_TABLE_SLAB, mcLoc("block/oak_planks"), mcLoc("block/crafting_table_top"),
				modTexture("crafting_table_slab_side"), modTexture("crafting_table_slab_front"));
		generateSlab(blockModels, SlabRegistry.CARTOGRAPHY_TABLE_SLAB, mcLoc("block/dark_oak_planks"), mcLoc("block/cartography_table_top"),
				modTexture("cartography_table_slab_3"), modTexture("cartography_table_slab_3"),
				modTexture("cartography_table_slab_1"), modTexture("cartography_table_slab_2"));
		generateFacingSlab(blockModels, SlabRegistry.LOOM_SLAB, mcLoc("block/loom_bottom"), mcLoc("block/loom_top"),
				modTexture("loom_slab_side"), modTexture("loom_slab_front"));

		generateFurnaceSlab(blockModels, SlabRegistry.FURNACE_SLAB, mcLoc("block/furnace_top"), mcLoc("block/furnace_top"),
				modTexture("furnace_slab_side"), modTexture("furnace_slab_front"));
		generateFurnaceSlab(blockModels, SlabRegistry.BLAST_FURNACE_SLAB, mcLoc("block/blast_furnace_top"), mcLoc("block/blast_furnace_top"),
				modTexture("blast_furnace_slab_side"), modTexture("blast_furnace_slab_front"));
		generateFurnaceSlab(blockModels, SlabRegistry.SMOKER_SLAB, mcLoc("block/smoker_top"), mcLoc("block/smoker_top"),
				modTexture("smoker_slab_side"), modTexture("smoker_slab_front"));

		generateSlab(blockModels, SlabRegistry.NOTE_SLAB, mcLoc("block/note_block"), mcLoc("block/note_block"),
				modTexture("note_slab_side"), modTexture("note_slab_side"));
		generateSlab(blockModels, SlabRegistry.TNT_SLAB, mcLoc("block/tnt_bottom"), mcLoc("block/tnt_top"),
				modTexture("tnt_slab_side"), modTexture("note_slab_side"));
	}

	private void generateSlab(BlockModelGenerators blockModels, DeferredHolder<Block, ? extends CustomSlabBlock> registryObject,
	                          ResourceLocation bottomTexture, ResourceLocation topTexture,
	                          ResourceLocation sideTexture, ResourceLocation frontTexture) {
		generateSlab(
				blockModels,
				registryObject,
				bottomTexture,
				topTexture,
				frontTexture,
				sideTexture,
				sideTexture,
				sideTexture
		);
	}

	private void generateSlab(BlockModelGenerators blockModels, DeferredHolder<Block, ? extends CustomSlabBlock> registryObject,
	                          ResourceLocation bottomTexture, ResourceLocation topTexture,
	                          ResourceLocation northTexture, ResourceLocation eastTexture,
	                          ResourceLocation southTexture, ResourceLocation westTexture) {
		ResourceLocation[] models = generateSlabModels(blockModels, registryObject.get(),
				bottomTexture, topTexture, northTexture, eastTexture, southTexture, westTexture);

		blockModels.blockStateOutput.accept(
				MultiVariantGenerator.multiVariant(registryObject.get())
						.with(
								PropertyDispatch.property(CustomSlabBlock.TYPE)
										.select(CustomSlabType.TOP, Variant.variant().with(VariantProperties.MODEL, models[0]))
										.select(CustomSlabType.BOTTOM, Variant.variant().with(VariantProperties.MODEL, models[1]))
						)
		);
	}

	private void generateFacingSlab(BlockModelGenerators blockModels, DeferredHolder<Block, ? extends FacingMultiSlabBlock> registryObject,
	                          ResourceLocation bottomTexture, ResourceLocation topTexture,
	                          ResourceLocation sideTexture, ResourceLocation frontTexture) {
		generateFacingSlab(
				blockModels,
				registryObject,
				bottomTexture,
				topTexture,
				frontTexture,
				sideTexture,
				sideTexture,
				sideTexture
		);
	}

	private void generateFacingSlab(BlockModelGenerators blockModels, DeferredHolder<Block, ? extends FacingMultiSlabBlock> registryObject,
	                                ResourceLocation bottomTexture, ResourceLocation topTexture,
	                                ResourceLocation northTexture, ResourceLocation eastTexture,
	                                ResourceLocation southTexture, ResourceLocation westTexture) {
		ResourceLocation[] models = generateSlabModels(blockModels, registryObject.get(),
				bottomTexture, topTexture, northTexture, eastTexture, southTexture, westTexture);

		blockModels.blockStateOutput.accept(
				MultiVariantGenerator.multiVariant(registryObject.get())
						.with(
								PropertyDispatch.property(CustomSlabBlock.TYPE)
										.select(CustomSlabType.TOP, Variant.variant().with(VariantProperties.MODEL, models[0]))
										.select(CustomSlabType.BOTTOM, Variant.variant().with(VariantProperties.MODEL, models[1]))
						).with(BlockModelGenerators.createHorizontalFacingDispatch())
		);
	}

	private void generateFurnaceSlab(BlockModelGenerators blockModels, DeferredHolder<Block, ? extends AbstractFurnaceSlabBlock> registryObject,
	                          ResourceLocation bottomTexture, ResourceLocation topTexture,
	                          ResourceLocation sideTexture, ResourceLocation frontTexture) {
		generateFurnaceSlab(
				blockModels,
				registryObject,
				bottomTexture,
				topTexture,
				frontTexture,
				sideTexture,
				sideTexture,
				sideTexture
		);
	}

	private void generateFurnaceSlab(BlockModelGenerators blockModels, DeferredHolder<Block, ? extends AbstractFurnaceSlabBlock> registryObject,
	                                 ResourceLocation bottomTexture, ResourceLocation topTexture,
	                                 ResourceLocation northTexture, ResourceLocation eastTexture,
	                                 ResourceLocation southTexture, ResourceLocation westTexture) {
		ResourceLocation[] models = generateSlabModels(blockModels, registryObject.get(),
				bottomTexture, topTexture, northTexture, eastTexture, southTexture, westTexture);
		ResourceLocation[] onModels = generateSlabModelsOn(blockModels, registryObject.get(),
				bottomTexture, topTexture, northTexture.withSuffix("_active"), eastTexture, southTexture, westTexture);

		blockModels.blockStateOutput.accept(
				MultiVariantGenerator.multiVariant(registryObject.get())
						.with(
								PropertyDispatch.properties(CustomSlabBlock.TYPE, AbstractFurnaceSlabBlock.LIT)
										.select(CustomSlabType.TOP, false, Variant.variant().with(VariantProperties.MODEL, models[0]))
										.select(CustomSlabType.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, models[1]))
										.select(CustomSlabType.TOP, true, Variant.variant().with(VariantProperties.MODEL, onModels[0]))
										.select(CustomSlabType.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, onModels[1]))
						).with(BlockModelGenerators.createHorizontalFacingDispatch())
		);
	}

	private ResourceLocation[] generateSlabModelsOn(BlockModelGenerators blockModels, CustomSlabBlock slabBlock,
	                                                ResourceLocation bottomTexture, ResourceLocation topTexture,
	                                                ResourceLocation northTexture, ResourceLocation eastTexture,
	                                                ResourceLocation southTexture, ResourceLocation westTexture) {
		TextureMapping slabMapping = this.createSlabMapping(bottomTexture, topTexture, northTexture, eastTexture, southTexture, westTexture);

		ResourceLocation[] models = new ResourceLocation[2];
		models[0] = SLAB_TOP.createWithSuffix(slabBlock, "_top_on", slabMapping, blockModels.modelOutput);
		models[1] = SLAB_BOTTOM.createWithSuffix(slabBlock, "_on", slabMapping, blockModels.modelOutput);
		return models;
	}

	private ResourceLocation[] generateSlabModels(BlockModelGenerators blockModels, CustomSlabBlock slabBlock,
	                                              ResourceLocation bottomTexture, ResourceLocation topTexture,
	                                              ResourceLocation northTexture, ResourceLocation eastTexture,
	                                              ResourceLocation southTexture, ResourceLocation westTexture) {
		TextureMapping slabMapping = this.createSlabMapping(bottomTexture, topTexture, northTexture, eastTexture, southTexture, westTexture);

		ResourceLocation[] models = new ResourceLocation[2];
		models[0] = SLAB_TOP.createWithSuffix(slabBlock, "_top", slabMapping, blockModels.modelOutput);
		models[1] = SLAB_BOTTOM.create(slabBlock, slabMapping, blockModels.modelOutput);
		return models;
	}

	private TextureMapping createSlabMapping(ResourceLocation bottomTexture, ResourceLocation topTexture,
	                                         ResourceLocation northTexture, ResourceLocation eastTexture,
	                                         ResourceLocation southTexture, ResourceLocation westTexture) {
		return new TextureMapping()
				.put(TextureSlot.UP, topTexture)
				.put(TextureSlot.DOWN, bottomTexture)
				.put(TextureSlot.NORTH, northTexture)
				.put(TextureSlot.EAST, eastTexture)
				.put(TextureSlot.SOUTH, southTexture)
				.put(TextureSlot.WEST, westTexture);
	}

	private ResourceLocation modTexture(String path) {
		return SlabReference.modLoc(path).withPrefix("block/");
	}

	private ResourceLocation mcLoc(String path) {
		return ResourceLocation.withDefaultNamespace(path);
	}
}
