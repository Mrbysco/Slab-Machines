package com.mrbysco.slabmachines.gui.compat.tcon;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.tools.common.client.GuiToolStation;
import slimeknights.tconstruct.tools.common.tileentity.TileToolStation;

public class GuiToolStationSlab extends GuiToolStation{

	public GuiToolStationSlab(InventoryPlayer playerInv, World world, BlockPos pos, TileToolStation tile) {
		super(playerInv, world, pos, tile);
	}
}
