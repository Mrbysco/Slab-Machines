package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.blockentity.furnace.SmokerSlabBlockEntity;
import com.mrbysco.slabmachines.blocks.base.CustomSlabBlock;
import com.mrbysco.slabmachines.blocks.base.enums.CustomSlabType;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class SmokerSlabBlock extends AbstractFurnaceSlabBlock {

	public SmokerSlabBlock(Properties properties) {
		super(properties.strength(2.0F, 10.0F).sound(SoundType.STONE));
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			if (level.getBlockEntity(pos) instanceof SmokerSlabBlockEntity smokerBlockEntity) {
				player.openMenu(smokerBlockEntity);
				player.awardStat(Stats.INTERACT_WITH_SMOKER);
			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
		if (stateIn.getValue(LIT)) {
			double posX = (double) pos.getX() + 0.5D;
			double posY = (double) pos.getY() + ((stateIn.getValue(CustomSlabBlock.TYPE) == CustomSlabType.TOP) ? 0.5D : 0);
			double posZ = (double) pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D) {
				level.playLocalSound(posX, posY, posZ, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			level.addParticle(ParticleTypes.SMOKE, posX, posY + 1.1D, posZ, 0.0D, 0.0D, 0.0D);
		}
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createFurnaceTicker(level, blockEntityType, SlabRegistry.SMOKER_SLAB_BE.get());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SmokerSlabBlockEntity(pos, state);
	}
}
