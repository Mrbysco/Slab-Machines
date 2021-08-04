package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.blocks.base.CustomSlabBlock;
import com.mrbysco.slabmachines.blocks.base.FacingMultiSlabBlock;
import com.mrbysco.slabmachines.blocks.base.enums.CustomSlabType;
import com.mrbysco.slabmachines.tileentity.furnace.TileFurnaceSlab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class FurnaceSlabBlock extends FacingMultiSlabBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public FurnaceSlabBlock(Properties properties) {
		super(properties.strength(2.0F, 10.0F).sound(SoundType.STONE).harvestLevel(0).harvestTool(ToolType.PICKAXE));
		this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.valueOf(false)));
	}

	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isClientSide) {
			return ActionResultType.SUCCESS;
		} else {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof TileFurnaceSlab) {
				player.openMenu((INamedContainerProvider)tileentity);
				player.awardStat(Stats.INTERACT_WITH_FURNACE);
			}
			return ActionResultType.CONSUME;
		}
	}
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof TileFurnaceSlab) {
				InventoryHelper.dropContents(worldIn, pos, (TileFurnaceSlab)tileentity);
				((TileFurnaceSlab)tileentity).getRecipesToAwardAndPopExperience(worldIn, Vector3d.atCenterOf(pos));
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(LIT)) {
			double posX = (double)pos.getX() + 0.5D;
			double posY = (double)pos.getY() + ((stateIn.getValue(CustomSlabBlock.TYPE) == CustomSlabType.TOP) ? 0.5D : 0);
			double posZ = (double)pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D) {
				worldIn.playLocalSound(posX, posY, posZ, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = stateIn.getValue(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52D : d4;
			double d6 = rand.nextDouble() * 6.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52D : d4;
			worldIn.addParticle(ParticleTypes.SMOKE, posX + d5, posY + d6, posZ + d7, 0.0D, 0.0D, 0.0D);
			worldIn.addParticle(ParticleTypes.FLAME, posX + d5, posY + d6, posZ + d7, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
		return Container.getRedstoneSignalFromBlockEntity(worldIn.getBlockEntity(pos));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(TYPE, FACING, LIT, WATERLOGGED);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileFurnaceSlab();
	}
}
