package com.mrbysco.slabmachines.blocks.base;

import com.mrbysco.slabmachines.blocks.base.enums.CustomSlabType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CustomSlabBlock extends Block implements SimpleWaterloggedBlock {
	public static final EnumProperty<CustomSlabType> TYPE = EnumProperty.create("type", CustomSlabType.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape BOTTOM_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public CustomSlabBlock(Properties builder) {
		super(builder);
		this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, CustomSlabType.BOTTOM).setValue(WATERLOGGED, Boolean.valueOf(false)));
	}

	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	protected void createBlockStateDefinition(Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(new Property[]{TYPE, WATERLOGGED});
	}

	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		CustomSlabType slabType = (CustomSlabType)state.getValue(TYPE);
		return switch (slabType) {
			case TOP -> TOP_SHAPE;
			default -> BOTTOM_SHAPE;
		};
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		FluidState fluidState = context.getLevel().getFluidState(pos);
		BlockState state = (BlockState)((BlockState)this.defaultBlockState().setValue(TYPE, CustomSlabType.BOTTOM)).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
		Direction direction = context.getClickedFace();

		return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double)pos.getY() > 0.5D)) ? state : (BlockState)state.setValue(TYPE, CustomSlabType.TOP);
	}

	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return false;
	}

	public FluidState getFluidState(BlockState state) {
		return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if ((Boolean)stateIn.getValue(WATERLOGGED)) {
			level.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
	}

	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		switch(type) {
			case LAND:
				return false;
			case WATER:
				return level.getFluidState(pos).is(FluidTags.WATER);
			case AIR:
				return false;
			default:
				return false;
		}
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
		return p_152134_ == p_152133_ ? (BlockEntityTicker<A>) p_152135_ : null;
	}
}