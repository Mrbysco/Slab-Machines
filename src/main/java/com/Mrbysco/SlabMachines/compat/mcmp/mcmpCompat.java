package com.Mrbysco.SlabMachines.compat.mcmp;

import com.Mrbysco.SlabMachines.SlabMachines;
import com.Mrbysco.SlabMachines.init.SlabBlocks;

import mcmultipart.api.addon.IMCMPAddon;
import mcmultipart.api.addon.IWrappedBlock;
import mcmultipart.api.addon.MCMPAddon;
import mcmultipart.api.multipart.IMultipartRegistry;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@MCMPAddon
public class mcmpCompat implements IMCMPAddon {
	@Override
	public void registerParts(IMultipartRegistry registry) {
		registry.registerPartWrapper(SlabBlocks.workbenchSlab, new SlabPart(SlabBlocks.workbenchSlab));
		IWrappedBlock craftingSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.workbenchSlab), stack -> true, SlabBlocks.workbenchSlab);
		craftingSlab.setPlacementInfo(this::getSlabState);

		registry.registerPartWrapper(SlabBlocks.furnaceSlab, new SlabPart(SlabBlocks.furnaceSlab));
		IWrappedBlock furnaceSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.furnaceSlab), stack -> true, SlabBlocks.furnaceSlab);
		furnaceSlab.setPlacementInfo(this::getSlabState);
		
		registry.registerPartWrapper(SlabBlocks.chestSlab, new SlabPart(SlabBlocks.chestSlab));
		IWrappedBlock chestSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.chestSlab), stack -> true, SlabBlocks.chestSlab);
		chestSlab.setPlacementInfo(this::getSlabState);
		
		registry.registerPartWrapper(SlabBlocks.trappedChestSlab, new SlabPart(SlabBlocks.trappedChestSlab));
		IWrappedBlock trappedChestSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.trappedChestSlab), stack -> true, SlabBlocks.trappedChestSlab);
		trappedChestSlab.setPlacementInfo(this::getSlabState);
		
		registry.registerPartWrapper(SlabBlocks.noteSlab, new SlabPart(SlabBlocks.noteSlab));
		IWrappedBlock noteSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.noteSlab), stack -> true, SlabBlocks.noteSlab);
		noteSlab.setPlacementInfo(this::getSlabState);
		
		registry.registerPartWrapper(SlabBlocks.tntSlab, new SlabPart(SlabBlocks.tntSlab));
		IWrappedBlock tntSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.tntSlab), stack -> true, SlabBlocks.tntSlab);
		tntSlab.setPlacementInfo(this::getSlabState);
		
		if(SlabMachines.tinkersLoaded)
		{
			registry.registerPartWrapper(SlabBlocks.craftingStationSlab, new SlabPart(SlabBlocks.craftingStationSlab));
			IWrappedBlock craftingstationSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.craftingStationSlab), stack -> true, SlabBlocks.craftingStationSlab);
			craftingstationSlab.setPlacementInfo(this::getSlabState);
			
			registry.registerPartWrapper(SlabBlocks.partBuilderSlab, new SlabPart(SlabBlocks.partBuilderSlab));
			IWrappedBlock partBuilderSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.partBuilderSlab), stack -> true, SlabBlocks.partBuilderSlab);
			partBuilderSlab.setPlacementInfo(this::getSlabState);
			
			registry.registerPartWrapper(SlabBlocks.partChestSlab, new SlabPart(SlabBlocks.partChestSlab));
			IWrappedBlock partChestSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.partChestSlab), stack -> true, SlabBlocks.partChestSlab);
			partChestSlab.setPlacementInfo(this::getSlabState);
			
			registry.registerPartWrapper(SlabBlocks.patternChestSlab, new SlabPart(SlabBlocks.patternChestSlab));
			IWrappedBlock patternChestSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.patternChestSlab), stack -> true, SlabBlocks.patternChestSlab);
			patternChestSlab.setPlacementInfo(this::getSlabState);
			
			registry.registerPartWrapper(SlabBlocks.stencilTableSlab, new SlabPart(SlabBlocks.stencilTableSlab));
			IWrappedBlock stencilTableSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.stencilTableSlab), stack -> true, SlabBlocks.stencilTableSlab);
			stencilTableSlab.setPlacementInfo(this::getSlabState);
			
			registry.registerPartWrapper(SlabBlocks.toolForgeSlab, new SlabPart(SlabBlocks.toolForgeSlab));
			IWrappedBlock toolForgeSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.toolForgeSlab), stack -> true, SlabBlocks.toolForgeSlab);
			toolForgeSlab.setPlacementInfo(this::getSlabState);
			
			registry.registerPartWrapper(SlabBlocks.toolStationSlab, new SlabPart(SlabBlocks.toolStationSlab));
			IWrappedBlock toolStationSlab = registry.registerStackWrapper(Item.getItemFromBlock(SlabBlocks.toolStationSlab), stack -> true, SlabBlocks.toolStationSlab);
			toolStationSlab.setPlacementInfo(this::getSlabState);
		}
	}

	private IBlockState getSlabState(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand, IBlockState state) {
		if (facing.getAxis() == EnumFacing.Axis.Y
				&& Math.abs(hitX * facing.getFrontOffsetX() + hitY * facing.getFrontOffsetY() + hitZ * facing.getFrontOffsetZ()) == 0.5) {
			return state.cycleProperty(BlockSlab.HALF);
		}
		return state;
	}
}
