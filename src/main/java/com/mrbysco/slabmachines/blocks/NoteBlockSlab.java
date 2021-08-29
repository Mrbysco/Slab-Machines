package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.blocks.base.CustomSlabBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;

public class NoteBlockSlab extends CustomSlabBlock {
	public static final EnumProperty<NoteBlockInstrument> INSTRUMENT = BlockStateProperties.NOTEBLOCK_INSTRUMENT;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final IntegerProperty NOTE = BlockStateProperties.NOTE;

    public NoteBlockSlab(Properties properties) {
		super(properties.strength(2.5F).sound(SoundType.WOOD));
		this.registerDefaultState(this.defaultBlockState().setValue(INSTRUMENT, NoteBlockInstrument.HARP).setValue(NOTE, Integer.valueOf(0)).setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(INSTRUMENT, NoteBlockInstrument.byState(context.getLevel().getBlockState(context.getClickedPos().below())));
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN ? stateIn.setValue(INSTRUMENT, NoteBlockInstrument.byState(facingState)) : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			int _new = net.minecraftforge.common.ForgeHooks.onNoteChange(level, pos, state, state.getValue(NOTE), state.cycle(NOTE).getValue(NOTE));
			if (_new == -1) return InteractionResult.FAIL;
			state = state.setValue(NOTE, _new);
			level.setBlock(pos, state, 3);
			this.triggerNote(level, pos);
			player.awardStat(Stats.TUNE_NOTEBLOCK);
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void attack(BlockState state, Level level, BlockPos pos, Player player) {
		if (!level.isClientSide) {
			this.triggerNote(level, pos);
			player.awardStat(Stats.PLAY_NOTEBLOCK);
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		boolean flag = level.hasNeighborSignal(pos);
		if (flag != state.getValue(POWERED)) {
			if (flag) {
				this.triggerNote(level, pos);
			}

			level.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(flag)), 3);
		}
	}

	private void triggerNote(Level level, BlockPos pos) {
		if (level.isEmptyBlock(pos.above())) {
			level.blockEvent(pos, this, 0, 0);
		}

	}
	
	@Override
	public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
		net.minecraftforge.event.world.NoteBlockEvent.Play e = new net.minecraftforge.event.world.NoteBlockEvent.Play(level, pos, state, state.getValue(NOTE), state.getValue(INSTRUMENT));
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(e)) return false;
		state = state.setValue(NOTE, e.getVanillaNoteId()).setValue(INSTRUMENT, e.getInstrument());
		int i = state.getValue(NOTE);
		float f = (float)Math.pow(2.0D, (double)(i - 12) / 12.0D);
		level.playSound((Player)null, pos, state.getValue(INSTRUMENT).getSoundEvent(), SoundSource.RECORDS, 3.0F, f);
		level.addParticle(ParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1D, (double)pos.getZ() + 0.5D, (double)i / 24.0D, 0.0D, 0.0D);
		return true;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(TYPE, WATERLOGGED, INSTRUMENT, POWERED, NOTE);
	}
}
