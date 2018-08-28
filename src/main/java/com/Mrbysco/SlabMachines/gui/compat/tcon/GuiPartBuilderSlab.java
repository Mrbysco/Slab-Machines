package com.Mrbysco.SlabMachines.gui.compat.tcon;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.tools.common.client.GuiPartBuilder;
import slimeknights.tconstruct.tools.common.tileentity.TilePartBuilder;

public class GuiPartBuilderSlab extends GuiPartBuilder{

	public GuiPartBuilderSlab(InventoryPlayer playerInv, World world, BlockPos pos, TilePartBuilder tile) {
		super(playerInv, world, pos, tile);
	}

}
