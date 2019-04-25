package com.mrbysco.slabmachines.tileentity.compat.tinkers;

import com.mrbysco.slabmachines.gui.compat.tcon.ContainerStencilTableSlab;
import com.mrbysco.slabmachines.gui.compat.tcon.GuiStencilTableSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.tools.common.tileentity.TileStencilTable;

public class TileStencilTableSlab extends TileStencilTable{

	@Override
	public Container createContainer(InventoryPlayer inventoryplayer, World world, BlockPos pos) {
		return new ContainerStencilTableSlab(inventoryplayer, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(InventoryPlayer inventoryplayer, World world, BlockPos pos) {
	    return new GuiStencilTableSlab(inventoryplayer, world, pos, this);
	}
	
	public void setHalf(EnumBlockHalf half) {
	    getTileData().setInteger("slabHalf", half == EnumBlockHalf.BOTTOM ? 0 : 1);
	}

	public EnumBlockHalf getHalf() {
	    return getTileData().getInteger("slabHalf") == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.getNbtCompound();
	    NBTBase feet = tag.getTag(FEET_TAG);
	    if(feet != null) {
	      getTileData().setTag(FEET_TAG, feet);
	    }
	    NBTBase facing = tag.getTag(FACE_TAG);
	    if(facing != null) {
	      getTileData().setTag(FACE_TAG, facing);
	    }
	    NBTBase half = tag.getTag("slabHalf");
	    if(half != null) {
		  getTileData().setTag("slabHalf", facing);
		}
	    
	    readFromNBT(tag);
	}
}
