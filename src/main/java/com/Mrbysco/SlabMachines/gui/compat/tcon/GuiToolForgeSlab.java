package com.mrbysco.slabmachines.gui.compat.tcon;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.tools.common.tileentity.TileToolStation;

import java.util.Set;

public class GuiToolForgeSlab extends GuiToolStationSlab{

	public GuiToolForgeSlab(InventoryPlayer playerInv, World world, BlockPos pos, TileToolStation tile) {
		super(playerInv, world, pos, tile);

	    metal();
	}

	@Override
	public Set<ToolCore> getBuildableItems() {
	    return TinkerRegistry.getToolForgeCrafting();
	}

}
