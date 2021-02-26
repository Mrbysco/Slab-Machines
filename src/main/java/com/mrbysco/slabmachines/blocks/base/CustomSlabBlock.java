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
	protected static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public CustomSlabBlock(Properties builder) {
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(TYPE, CustomSlabType.BOTTOM).with(WATERLOGGED, Boolean.valueOf(false)));
	}

	public boolean isTransparent(BlockState state) {
		return true;
	}

	protected void fillStateContainer(Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(new Property[]{TYPE, WATERLOGGED});
	}

	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		CustomSlabType slabType = (CustomSlabType)state.get(TYPE);
		switch(slabType) {
			case TOP:
				return TOP_SHAPE;
			default:
				return BOTTOM_SHAPE;
		}
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos pos = context.getPos();
		FluidState fluidState = context.getWorld().getFluidState(pos);
		BlockState state = (BlockState)((BlockState)this.getDefaultState().with(TYPE, CustomSlabType.BOTTOM)).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
		Direction direction = context.getFace();

		return direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double)pos.getY() > 0.5D)) ? state : (BlockState)state.with(TYPE, CustomSlabType.TOP);
	}

	public boolean isReplaceable(BlockState state, BlockItemUseContext context) {
		return false;
	}

	public FluidState getFluidState(BlockState state) {
		return (Boolean)state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if ((Boolean)stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		switch(type) {
			case LAND:
				return false;
			case WATER:
				return worldIn.getFluidState(pos).isTagged(FluidTags.WATER);
			case AIR:
				return false;
			default:
				return false;
		}
	}
}