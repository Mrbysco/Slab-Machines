package com.mrbysco.slabmachines.menu;

import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class SlabCartographyTableMenu extends CartographyTableMenu {
	private ContainerLevelAccess access;

	public SlabCartographyTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
		super(containerId, playerInventory, access);
		this.access = access;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(this.access, player, SlabRegistry.CARTOGRAPHY_TABLE_SLAB.get());
	}
}
