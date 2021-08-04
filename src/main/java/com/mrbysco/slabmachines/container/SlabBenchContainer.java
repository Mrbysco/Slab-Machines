package com.mrbysco.slabmachines.container;

import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.MenuType;

public class SlabBenchContainer extends CraftingMenu {
	public SlabBenchContainer(int id, Inventory playerInventory) {
		super(id, playerInventory);
	}

	public SlabBenchContainer(int id, Inventory playerInventory, ContainerLevelAccess worldPosCallable) {
		super(id, playerInventory, worldPosCallable);
	}

	@Override
	public MenuType<?> getType() {
		return SlabRegistry.SLAB_WORKBENCH_CONTAINER.get();
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return stillValid(this.access, playerIn, SlabRegistry.CRAFTING_TABLE_SLAB.get());
	}
}
