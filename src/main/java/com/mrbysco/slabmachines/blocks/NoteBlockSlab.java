package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.blocks.base.CustomSlabBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.NoteBlockInstrument;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class NoteBlockSlab extends CustomSlabBlock {
	public static final EnumProperty<NoteBlockInstrument> INSTRUMENT = BlockStateProperties.NOTEBLOCK_INSTRUMENT;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final IntegerProperty NOTE = BlockStateProperties.NOTE;

    public NoteBlockSlab(Properties properties) {
		super(properties.strength(2.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE).harvestLevel(0));
		this.registerDefaultState(this.defaultBlockState().setValue(INSTRUMENT, NoteBlockInstrument.HARP).setValue(NOTE, Integer.valueOf(0)).setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).setValue(INSTRUMENT, NoteBlockInstrument.byState(context.getLevel().getBlockState(context.getClickedPos().below())));
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN ? stateIn.setValue(INSTRUMENT, NoteBlockInstrument.byState(facingState)) : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isClientSide) {
			return ActionResultType.SUCCESS;
		} else {
			int _new = net.minecraftforge.common.ForgeHooks.onNoteChange(worldIn, pos, state, state.getValue(NOTE), state.cycle(NOTE).getValue(NOTE));
			if (_new == -1) return ActionResultType.FAIL;
			state = state.setValue(NOTE, _new);
			worldIn.setBlock(pos, state, 3);
			this.triggerNote(worldIn, pos);
			player.awardStat(Stats.TUNE_NOTEBLOCK);
			return ActionResultType.CONSUME;
		}
	}

	@Override
	public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		if (!worldIn.isClientSide) {
			this.triggerNote(worldIn, pos);
			player.awardStat(Stats.PLAY_NOTEBLOCK);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		boolean flag = worldIn.hasNeighborSignal(pos);
		if (flag != state.getValue(POWERED)) {
			if (flag) {
				this.triggerNote(worldIn, pos);
			}

			worldIn.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(flag)), 3);
		}
	}

	private void triggerNote(World worldIn, BlockPos pos) {
		if (worldIn.isEmptyBlock(pos.above())) {
			worldIn.blockEvent(pos, this, 0, 0);
		}

	}
	
	@Override
	public boolean triggerEvent(BlockState state, World worldIn, BlockPos pos, int id, int param) {
		net.minecraftforge.event.world.NoteBlockEvent.Play e = new net.minecraftforge.event.world.NoteBlockEvent.Play(worldIn, pos, state, state.getValue(NOTE), state.getValue(INSTRUMENT));
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(e)) return false;
		state = state.setValue(NOTE, e.getVanillaNoteId()).setValue(INSTRUMENT, e.getInstrument());
		int i = state.getValue(NOTE);
		float f = (float)Math.pow(2.0D, (double)(i - 12) / 12.0D);
		worldIn.playSound((PlayerEntity)null, pos, state.getValue(INSTRUMENT).getSoundEvent(), SoundCategory.RECORDS, 3.0F, f);
		worldIn.addParticle(ParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1D, (double)pos.getZ() + 0.5D, (double)i / 24.0D, 0.0D, 0.0D);
		return true;
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED, INSTRUMENT, POWERED, NOTE);
	}
}
