package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.blockentity.ChestSlabBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class TrappedChestSlabBlock extends ChestSlabBlock {
	public TrappedChestSlabBlock(Properties properties) {
		super(properties.strength(2.5F).sound(SoundType.WOOD));
	}

	@Override
	protected Stat<ResourceLocation> getOpenStat() {
		return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
	}

	public boolean isSignalSource(BlockState state) {
		return true;
	}

	public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos pos, Direction side) {
		return Mth.clamp(ChestSlabBlockEntity.getOpenCount(blockGetter, pos), 0, 15);
	}

	public int getDirectSignal(BlockState blockState, BlockGetter level, BlockPos pos, Direction side) {
		return side == Direction.UP ? level.getBlockState(pos).getSignal(level, pos, side) : 0;
	}
}
