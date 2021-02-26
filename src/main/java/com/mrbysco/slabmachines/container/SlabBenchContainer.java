package com.mrbysco.slabmachines.container;

import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.util.IWorldPosCallable;

public class SlabBenchContainer extends WorkbenchContainer {
	public SlabBenchContainer(int id, PlayerInventory playerInventory) {
		super(id, playerInventory);
	}

	public SlabBenchContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
		super(id, playerInventory, worldPosCallable);
	}

	@Override
	public ContainerType<?> getType() {
		return SlabRegistry.SLAB_WORKBENCH_CONTAINER.get();
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(this.worldPosCallable, playerIn, SlabRegistry.CRAFTING_TABLE_SLAB.get());
	}
}
