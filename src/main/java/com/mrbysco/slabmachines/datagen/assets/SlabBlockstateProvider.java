package com.mrbysco.slabmachines.datagen.assets;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blocks.FurnaceSlabBlock;
import com.mrbysco.slabmachines.blocks.base.CustomSlabBlock;
import com.mrbysco.slabmachines.blocks.base.FacingMultiSlabBlock;
import com.mrbysco.slabmachines.blocks.base.enums.CustomSlabType;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class SlabBlockstateProvider extends BlockStateProvider {
	public SlabBlockstateProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, SlabReference.MOD_ID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		for (RegistryObject<Block> registryObject : SlabRegistry.BLOCKS.getEntries()) {
			if (registryObject.get() instanceof CustomSlabBlock) {
				if (registryObject.get() instanceof FacingMultiSlabBlock) {
					if (registryObject.get() instanceof FurnaceSlabBlock) {
						generateFurnaceSlab(registryObject);
					} else {
						generateFacingSlab(registryObject);
					}
				} else {
					generateSlab(registryObject);
				}
			}
		}
	}

	private void generateSlab(RegistryObject<Block> registryObject) {
		ModelFile topModel = models().getExistingFile(modLoc("block/" + registryObject.getId().getPath() + "_top"));
		ModelFile bottomModel = models().getExistingFile(modLoc("block/" + registryObject.getId().getPath()));
		getVariantBuilder(registryObject.get())
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP)
				.modelForState().modelFile(topModel).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM)
				.modelForState().modelFile(bottomModel).addModel();
	}

	private void generateFacingSlab(RegistryObject<Block> registryObject) {
		ModelFile topModel = models().getExistingFile(modLoc("block/" + registryObject.getId().getPath() + "_top"));
		ModelFile bottomModel = models().getExistingFile(modLoc("block/" + registryObject.getId().getPath()));
		getVariantBuilder(registryObject.get())
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.EAST)
				.modelForState().modelFile(topModel).rotationY(90).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.NORTH)
				.modelForState().modelFile(topModel).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.SOUTH)
				.modelForState().modelFile(topModel).rotationY(180).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.WEST)
				.modelForState().modelFile(topModel).rotationY(270).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.EAST)
				.modelForState().modelFile(bottomModel).rotationY(90).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.NORTH)
				.modelForState().modelFile(bottomModel).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.SOUTH)
				.modelForState().modelFile(bottomModel).rotationY(180).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.WEST)
				.modelForState().modelFile(bottomModel).rotationY(270).addModel();
	}

	private void generateFurnaceSlab(RegistryObject<Block> registryObject) {
		ModelFile topModel = models().getExistingFile(modLoc("block/" + registryObject.getId().getPath() + "_top"));
		ModelFile bottomModel = models().getExistingFile(modLoc("block/" + registryObject.getId().getPath()));
		ModelFile topModelOn = models().getExistingFile(modLoc("block/" + registryObject.getId().getPath() + "_top_on"));
		ModelFile bottomModelOn = models().getExistingFile(modLoc("block/" + registryObject.getId().getPath() + "_on"));
		getVariantBuilder(registryObject.get())
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.EAST).with(FurnaceSlabBlock.LIT, true)
				.modelForState().modelFile(topModelOn).rotationY(90).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.NORTH).with(FurnaceSlabBlock.LIT, true)
				.modelForState().modelFile(topModelOn).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.SOUTH).with(FurnaceSlabBlock.LIT, true)
				.modelForState().modelFile(topModelOn).rotationY(180).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.WEST).with(FurnaceSlabBlock.LIT, true)
				.modelForState().modelFile(topModelOn).rotationY(270).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.EAST).with(FurnaceSlabBlock.LIT, true)
				.modelForState().modelFile(bottomModelOn).rotationY(90).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.NORTH).with(FurnaceSlabBlock.LIT, true)
				.modelForState().modelFile(bottomModelOn).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.SOUTH).with(FurnaceSlabBlock.LIT, true)
				.modelForState().modelFile(bottomModelOn).rotationY(180).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.WEST).with(FurnaceSlabBlock.LIT, true)
				.modelForState().modelFile(bottomModelOn).rotationY(270).addModel()

				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.EAST).with(FurnaceSlabBlock.LIT, false)
				.modelForState().modelFile(topModel).rotationY(90).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.NORTH).with(FurnaceSlabBlock.LIT, false)
				.modelForState().modelFile(topModel).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.SOUTH).with(FurnaceSlabBlock.LIT, false)
				.modelForState().modelFile(topModel).rotationY(180).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.TOP).with(FacingMultiSlabBlock.FACING, Direction.WEST).with(FurnaceSlabBlock.LIT, false)
				.modelForState().modelFile(topModel).rotationY(270).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.EAST).with(FurnaceSlabBlock.LIT, false)
				.modelForState().modelFile(bottomModel).rotationY(90).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.NORTH).with(FurnaceSlabBlock.LIT, false)
				.modelForState().modelFile(bottomModel).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.SOUTH).with(FurnaceSlabBlock.LIT, false)
				.modelForState().modelFile(bottomModel).rotationY(180).addModel()
				.partialState().with(CustomSlabBlock.TYPE, CustomSlabType.BOTTOM).with(FacingMultiSlabBlock.FACING, Direction.WEST).with(FurnaceSlabBlock.LIT, false)
				.modelForState().modelFile(bottomModel).rotationY(270).addModel()
		;
	}
}
