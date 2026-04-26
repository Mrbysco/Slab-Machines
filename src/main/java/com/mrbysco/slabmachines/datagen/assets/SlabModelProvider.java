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
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
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
	                          Identifier bottomTexture, Identifier topTexture,
	                          Identifier sideTexture, Identifier frontTexture) {
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
	                          Identifier bottomTexture, Identifier topTexture,
	                          Identifier northTexture, Identifier eastTexture,
	                          Identifier southTexture, Identifier westTexture) {
		Identifier[] models = generateSlabModels(blockModels, registryObject.get(),
				bottomTexture, topTexture, northTexture, eastTexture, southTexture, westTexture);

		blockModels.blockStateOutput.accept(
				MultiVariantGenerator.dispatch(registryObject.get())
						.with(
								PropertyDispatch.initial(CustomSlabBlock.TYPE)
										.select(CustomSlabType.TOP, BlockModelGenerators.plainVariant(models[0]))
										.select(CustomSlabType.BOTTOM, BlockModelGenerators.plainVariant(models[1]))
						)
		);
	}

	private void generateFacingSlab(BlockModelGenerators blockModels, DeferredHolder<Block, ? extends FacingMultiSlabBlock> registryObject,
	                                Identifier bottomTexture, Identifier topTexture,
	                                Identifier sideTexture, Identifier frontTexture) {
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
	                                Identifier bottomTexture, Identifier topTexture,
	                                Identifier northTexture, Identifier eastTexture,
	                                Identifier southTexture, Identifier westTexture) {
		Identifier[] models = generateSlabModels(blockModels, registryObject.get(),
				bottomTexture, topTexture, northTexture, eastTexture, southTexture, westTexture);

		blockModels.blockStateOutput.accept(
				MultiVariantGenerator.dispatch(registryObject.get())
						.with(
								PropertyDispatch.initial(CustomSlabBlock.TYPE)
										.select(CustomSlabType.TOP, BlockModelGenerators.plainVariant(models[0]))
										.select(CustomSlabType.BOTTOM, BlockModelGenerators.plainVariant(models[1]))
						).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
		);
	}

	private void generateFurnaceSlab(BlockModelGenerators blockModels, DeferredHolder<Block, ? extends AbstractFurnaceSlabBlock> registryObject,
	                                 Identifier bottomTexture, Identifier topTexture,
	                                 Identifier sideTexture, Identifier frontTexture) {
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
	                                 Identifier bottomTexture, Identifier topTexture,
	                                 Identifier northTexture, Identifier eastTexture,
	                                 Identifier southTexture, Identifier westTexture) {
		Identifier[] models = generateSlabModels(blockModels, registryObject.get(),
				bottomTexture, topTexture, northTexture, eastTexture, southTexture, westTexture);
		Identifier[] onModels = generateSlabModelsOn(blockModels, registryObject.get(),
				bottomTexture, topTexture, northTexture.withSuffix("_active"), eastTexture, southTexture, westTexture);

		blockModels.blockStateOutput.accept(
				MultiVariantGenerator.dispatch(registryObject.get())
						.with(
								PropertyDispatch.initial(CustomSlabBlock.TYPE, AbstractFurnaceSlabBlock.LIT)
										.select(CustomSlabType.TOP, false, BlockModelGenerators.plainVariant(models[0]))
										.select(CustomSlabType.BOTTOM, false, BlockModelGenerators.plainVariant(models[1]))
										.select(CustomSlabType.TOP, true, BlockModelGenerators.plainVariant(onModels[0]))
										.select(CustomSlabType.BOTTOM, true, BlockModelGenerators.plainVariant(onModels[1]))
						).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
		);
	}

	private Identifier[] generateSlabModelsOn(BlockModelGenerators blockModels, CustomSlabBlock slabBlock,
	                                          Identifier bottomTexture, Identifier topTexture,
	                                          Identifier northTexture, Identifier eastTexture,
	                                          Identifier southTexture, Identifier westTexture) {
		TextureMapping slabMapping = this.createSlabMapping(bottomTexture, topTexture, northTexture, eastTexture, southTexture, westTexture);

		Identifier[] models = new Identifier[2];
		models[0] = SLAB_TOP.createWithSuffix(slabBlock, "_top_on", slabMapping, blockModels.modelOutput);
		models[1] = SLAB_BOTTOM.createWithSuffix(slabBlock, "_on", slabMapping, blockModels.modelOutput);
		return models;
	}

	private Identifier[] generateSlabModels(BlockModelGenerators blockModels, CustomSlabBlock slabBlock,
	                                        Identifier bottomTexture, Identifier topTexture,
	                                        Identifier northTexture, Identifier eastTexture,
	                                        Identifier southTexture, Identifier westTexture) {
		TextureMapping slabMapping = this.createSlabMapping(bottomTexture, topTexture, northTexture, eastTexture, southTexture, westTexture);

		Identifier[] models = new Identifier[2];
		models[0] = SLAB_TOP.createWithSuffix(slabBlock, "_top", slabMapping, blockModels.modelOutput);
		models[1] = SLAB_BOTTOM.create(slabBlock, slabMapping, blockModels.modelOutput);
		return models;
	}

	private TextureMapping createSlabMapping(Identifier bottomTexture, Identifier topTexture,
	                                         Identifier northTexture, Identifier eastTexture,
	                                         Identifier southTexture, Identifier westTexture) {
		return new TextureMapping()
				.put(TextureSlot.UP, new Material(topTexture))
				.put(TextureSlot.DOWN, new Material(bottomTexture))
				.put(TextureSlot.NORTH, new Material(northTexture))
				.put(TextureSlot.EAST, new Material(eastTexture))
				.put(TextureSlot.SOUTH, new Material(southTexture))
				.put(TextureSlot.WEST, new Material(westTexture));
	}

	private Identifier modTexture(String path) {
		return SlabReference.modLoc(path).withPrefix("block/");
	}

	private Identifier mcLoc(String path) {
		return Identifier.withDefaultNamespace(path);
	}
}
