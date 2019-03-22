package com.mrbysco.slabmachines.compat.mcmp;

import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SlabPart implements IMultipart{
	
	private BlockSlab block;
	
	public SlabPart(BlockSlab block) {
		this.block = block;
	}

	@Override
	public Block getBlock() {
		return block;
	}

	@Override
	public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
		return state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP ? EnumFaceSlot.UP : EnumFaceSlot.DOWN;
	}

	@Override
	public IPartSlot getSlotFromWorld(IBlockAccess world, BlockPos pos, IBlockState state) {
		return state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP ? EnumFaceSlot.UP : EnumFaceSlot.DOWN;
	}

	@Override
	public IMultipartTile convertToMultipartTile(TileEntity tile) {
		if (tile instanceof ITickable) {
			return new SlabPartTileTicking(tile, (ITickable)tile);
		} else {
			return new SlabPartTile(tile);
		}
	}
}
