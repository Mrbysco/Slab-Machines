package com.mrbysco.slabmachines.gui.compat.tcon;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.tools.common.client.GuiPartChest;
import slimeknights.tconstruct.tools.common.tileentity.TilePartChest;

public class GuiPartChestSlab extends GuiPartChest{

	public GuiPartChestSlab(InventoryPlayer playerInv, World world, BlockPos pos, TilePartChest tile) {
		super(playerInv, world, pos, tile);
	}

}
