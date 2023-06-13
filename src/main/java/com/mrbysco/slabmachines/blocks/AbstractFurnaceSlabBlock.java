package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.blocks.base.FacingMultiSlabBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public abstract class AbstractFurnaceSlabBlock extends FacingMultiSlabBlock implements EntityBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public AbstractFurnaceSlabBlock(Properties properties) {
		super(properties.strength(2.0F, 10.0F).sound(SoundType.STONE));
		this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.valueOf(false)));
	}

	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof AbstractFurnaceBlockEntity furnaceBlockEntity) {
				Containers.dropContents(level, pos, furnaceBlockEntity);
				furnaceBlockEntity.getRecipesToAwardAndPopExperience((ServerLevel) level, Vec3.atCenterOf(pos));
				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(TYPE, FACING, LIT, WATERLOGGED);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends AbstractFurnaceBlockEntity> blockEntityType1) {
		return level.isClientSide ? null : createTickerHelper(blockEntityType, blockEntityType1, AbstractFurnaceBlockEntity::serverTick);
	}
}
