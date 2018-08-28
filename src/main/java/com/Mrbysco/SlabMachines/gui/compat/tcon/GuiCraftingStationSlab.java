package com.Mrbysco.SlabMachines.gui.compat.tcon;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.tools.common.client.GuiCraftingStation;
import slimeknights.tconstruct.tools.common.tileentity.TileCraftingStation;

public class GuiCraftingStationSlab extends GuiCraftingStation{

	public GuiCraftingStationSlab(InventoryPlayer playerInv, World world, BlockPos pos, TileCraftingStation tile) {
		super(playerInv, world, pos, tile);
	}
}
