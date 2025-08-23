package com.mrbysco.slabmachines.blocks.base;

import com.mrbysco.slabmachines.blocks.base.enums.CustomSlabType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CustomSlabBlock extends Block implements SimpleWaterloggedBlock {
	public static final EnumProperty<CustomSlabType> TYPE = EnumProperty.create("type", CustomSlabType.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape BOTTOM_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public CustomSlabBlock(Properties builder) {
		super(builder);
		this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, CustomSlabType.BOTTOM).setValue(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(TYPE, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		CustomSlabType slabType = state.getValue(TYPE);
		return switch (slabType) {
			case TOP -> TOP_SHAPE;
			default -> BOTTOM_SHAPE;
		};
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		FluidState fluidState = context.getLevel().getFluidState(pos);
		BlockState state = this.defaultBlockState().setValue(TYPE, CustomSlabType.BOTTOM).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
		Direction direction = context.getClickedFace();

		return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) pos.getY() > 0.5D)) ? state : (BlockState) state.setValue(TYPE, CustomSlabType.TOP);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return false;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (Boolean) state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return switch (type) {
			case WATER -> state.getFluidState().is(FluidTags.WATER);
			default -> false;
		};
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
			BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker
	) {
		return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
	}
}