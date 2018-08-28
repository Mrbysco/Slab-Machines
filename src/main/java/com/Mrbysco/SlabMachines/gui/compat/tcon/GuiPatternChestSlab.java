package com.Mrbysco.SlabMachines.gui.compat.tcon;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.tools.common.client.GuiPatternChest;
import slimeknights.tconstruct.tools.common.tileentity.TilePatternChest;

public class GuiPatternChestSlab extends GuiPatternChest{

	public GuiPatternChestSlab(InventoryPlayer playerInv, World world, BlockPos pos, TilePatternChest tile) {
		super(playerInv, world, pos, tile);
	}

}
