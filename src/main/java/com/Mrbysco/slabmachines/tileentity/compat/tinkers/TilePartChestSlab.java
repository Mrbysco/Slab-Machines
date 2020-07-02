package com.mrbysco.slabmachines.tileentity.compat.tinkers;

import com.mrbysco.slabmachines.gui.compat.tcon.ContainerPartChestSlab;
import com.mrbysco.slabmachines.gui.compat.tcon.GuiPartChestSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.tools.common.tileentity.TilePartChest;

public class TilePartChestSlab extends TilePartChest{

	@Override
	public Container createContainer(InventoryPlayer inventoryplayer, World world, BlockPos pos) {
		return new ContainerPartChestSlab(inventoryplayer, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(InventoryPlayer inventoryplayer, World world, BlockPos pos) {
	    return new GuiPartChestSlab(inventoryplayer, world, pos, this);
	}

	@Override public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) { return oldState.getBlock() != newState.getBlock(); }
}
