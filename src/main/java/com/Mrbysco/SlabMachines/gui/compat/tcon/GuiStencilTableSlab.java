package com.Mrbysco.SlabMachines.gui.compat.tcon;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.tools.common.client.GuiStencilTable;
import slimeknights.tconstruct.tools.common.tileentity.TileStencilTable;

public class GuiStencilTableSlab extends GuiStencilTable{

	public GuiStencilTableSlab(InventoryPlayer playerInv, World world, BlockPos pos, TileStencilTable tile) {
		super(playerInv, world, pos, tile);
	}
}
