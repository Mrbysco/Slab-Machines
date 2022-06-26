package com.mrbysco.slabmachines.blockentity;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blocks.TrappedChestSlabBlock;
import com.mrbysco.slabmachines.config.SlabConfig;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;

public class ChestSlabBlockEntity extends ChestBlockEntity {

	private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
		protected void onOpen(Level level, BlockPos pos, BlockState state) {
			ChestSlabBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
		}

		protected void onClose(Level level, BlockPos pos, BlockState state) {
			ChestSlabBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
		}

		protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int p_155364_, int p_155365_) {
			ChestSlabBlockEntity.this.signalOpenCount(level, pos, state, p_155364_, p_155365_);
		}

		protected boolean isOwnContainer(Player player) {
			if (!(player.containerMenu instanceof ChestMenu)) {
				return false;
			} else {
				Container container = ((ChestMenu) player.containerMenu).getContainer();
				return container == ChestSlabBlockEntity.this || container instanceof CompoundContainer && ((CompoundContainer) container).contains(ChestSlabBlockEntity.this);
			}
		}
	};

	public ChestSlabBlockEntity(BlockPos pos, BlockState state) {
		super(SlabRegistry.CHEST_SLAB_BE.get(), pos, state);
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getContainerSize() {
		return 27;
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
	 */
	public int getMaxStackSize() {
		return SlabConfig.COMMON.slabChestSlotLimit.get();
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable(SlabReference.MOD_PREFIX + "container.chest");
	}

	@Override
	protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int p_155336_, int p_155337_) {
		Block block = this.getBlockState().getBlock();
		if (block instanceof TrappedChestSlabBlock) {
			level.updateNeighborsAt(this.worldPosition, block);
		}
		level.blockEvent(pos, block, 1, p_155337_);
	}

	static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent soundEvent) {
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.5D;
		double d2 = (double) pos.getZ() + 0.5D;

		level.playSound((Player) null, d0, d1, d2, soundEvent, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
	}

	public static int getOpenCount(BlockGetter blockGetter, BlockPos pos) {
		BlockState blockstate = blockGetter.getBlockState(pos);
		if (blockstate.hasBlockEntity()) {
			BlockEntity blockentity = blockGetter.getBlockEntity(pos);
			if (blockentity instanceof ChestSlabBlockEntity) {
				return ((ChestSlabBlockEntity) blockentity).openersCounter.getOpenerCount();
			}
		}

		return 0;
	}

	public void startOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	public void stopOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	public void recheckOpen() {
		if (!this.remove) {
			this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}
}