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
	public static final EnumProperty<NoteBlockInstrument> INSTRUMENT = BlockStateProperties.NOTE_BLOCK_INSTRUMENT;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final IntegerProperty NOTE = BlockStateProperties.NOTE_0_24;

    public NoteBlockSlab(Properties properties) {
		super(properties.hardnessAndResistance(2.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE).harvestLevel(0));
		this.setDefaultState(this.getDefaultState().with(INSTRUMENT, NoteBlockInstrument.HARP).with(NOTE, Integer.valueOf(0)).with(POWERED, Boolean.valueOf(false)));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).with(INSTRUMENT, NoteBlockInstrument.byState(context.getWorld().getBlockState(context.getPos().down())));
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN ? stateIn.with(INSTRUMENT, NoteBlockInstrument.byState(facingState)) : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote) {
			return ActionResultType.SUCCESS;
		} else {
			int _new = net.minecraftforge.common.ForgeHooks.onNoteChange(worldIn, pos, state, state.get(NOTE), state.func_235896_a_(NOTE).get(NOTE));
			if (_new == -1) return ActionResultType.FAIL;
			state = state.with(NOTE, _new);
			worldIn.setBlockState(pos, state, 3);
			this.triggerNote(worldIn, pos);
			player.addStat(Stats.TUNE_NOTEBLOCK);
			return ActionResultType.CONSUME;
		}
	}

	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		if (!worldIn.isRemote) {
			this.triggerNote(worldIn, pos);
			player.addStat(Stats.PLAY_NOTEBLOCK);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		boolean flag = worldIn.isBlockPowered(pos);
		if (flag != state.get(POWERED)) {
			if (flag) {
				this.triggerNote(worldIn, pos);
			}

			worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag)), 3);
		}
	}

	private void triggerNote(World worldIn, BlockPos pos) {
		if (worldIn.isAirBlock(pos.up())) {
			worldIn.addBlockEvent(pos, this, 0, 0);
		}

	}
	
	@Override
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
		net.minecraftforge.event.world.NoteBlockEvent.Play e = new net.minecraftforge.event.world.NoteBlockEvent.Play(worldIn, pos, state, state.get(NOTE), state.get(INSTRUMENT));
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(e)) return false;
		state = state.with(NOTE, e.getVanillaNoteId()).with(INSTRUMENT, e.getInstrument());
		int i = state.get(NOTE);
		float f = (float)Math.pow(2.0D, (double)(i - 12) / 12.0D);
		worldIn.playSound((PlayerEntity)null, pos, state.get(INSTRUMENT).getSound(), SoundCategory.RECORDS, 3.0F, f);
		worldIn.addParticle(ParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, (double)i / 24.0D, 0.0D, 0.0D);
		return true;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED, INSTRUMENT, POWERED, NOTE);
	}
}
