package com.mrbysco.slabmachines.blocks.base;

import com.mrbysco.slabmachines.blocks.base.enums.CustomSlabType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class CustomSlabBlock extends Block implements IWaterLoggable {
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

	protected void createBlockStateDefinition(Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(new Property[]{TYPE, WATERLOGGED});
	}

	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		CustomSlabType slabType = (CustomSlabType)state.getValue(TYPE);
		switch(slabType) {
			case TOP:
				return TOP_SHAPE;
			default:
				return BOTTOM_SHAPE;
		}
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos pos = context.getClickedPos();
		FluidState fluidState = context.getLevel().getFluidState(pos);
		BlockState state = (BlockState)((BlockState)this.defaultBlockState().setValue(TYPE, CustomSlabType.BOTTOM)).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
		Direction direction = context.getClickedFace();

		return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double)pos.getY() > 0.5D)) ? state : (BlockState)state.setValue(TYPE, CustomSlabType.TOP);
	}

	public boolean canBeReplaced(BlockState state, BlockItemUseContext context) {
		return false;
	}

	public FluidState getFluidState(BlockState state) {
		return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if ((Boolean)stateIn.getValue(WATERLOGGED)) {
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		switch(type) {
			case LAND:
				return false;
			case WATER:
				return worldIn.getFluidState(pos).is(FluidTags.WATER);
			case AIR:
				return false;
			default:
				return false;
		}
	}
}