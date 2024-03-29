package com.mrbysco.slabmachines.blockentity.furnace;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.config.SlabConfig;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SmokerSlabBlockEntity extends AbstractFurnaceBlockEntity implements ISlabFurnace {

	public SmokerSlabBlockEntity(BlockPos pos, BlockState state) {
		super(SlabRegistry.SMOKER_SLAB_BE.get(), pos, state, RecipeType.SMOKING);
	}

	@Override
	public int getMaxStackSize() {
		return SlabConfig.COMMON.slabFurnaceSlotLimit.get();
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable(SlabReference.MOD_PREFIX + "container.smoker");
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return new FurnaceMenu(id, player, this, this.dataAccess);
	}
}
