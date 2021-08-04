package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.tileentity.TileChestSlab;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class TrappedChestSlabBlock extends ChestSlabBlock {
    public TrappedChestSlabBlock(Properties properties) {
		super(properties.strength(2.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE).harvestLevel(0));
	}

	@Override
	protected Stat<ResourceLocation> getOpenStat() {
		return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
	}

	public boolean isSignalSource(BlockState state) {
		return true;
	}

	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return MathHelper.clamp(getPlayersUsing(blockAccess, blockState, pos), 0, 15);
	}

	public int getPlayersUsing(IBlockReader worldIn, BlockState state, BlockPos pos) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if (tileentity instanceof TileChestSlab) {
			return ((TileChestSlab)tileentity).numPlayersUsing;
		}

		return 0;
	}

	public int getDirectSignal(BlockState blockState, IBlockReader worldIn, BlockPos pos, Direction side) {
		return side == Direction.UP ? worldIn.getBlockState(pos).getSignal(worldIn, pos, side) : 0;
	}
}
