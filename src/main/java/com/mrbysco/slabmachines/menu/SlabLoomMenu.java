package com.mrbysco.slabmachines.menu;

import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.LoomMenu;

public class SlabLoomMenu extends LoomMenu {
	private ContainerLevelAccess access;

	public SlabLoomMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
		super(containerId, playerInventory, access);
		this.access = access;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(this.access, player, SlabRegistry.LOOM_SLAB.get());
	}
}
