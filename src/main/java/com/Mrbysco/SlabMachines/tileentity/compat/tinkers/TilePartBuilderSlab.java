package com.Mrbysco.SlabMachines.tileentity.compat.tinkers;

import com.Mrbysco.SlabMachines.blocks.compat.tinkers.BlockPartBuilderSlab;
import com.Mrbysco.SlabMachines.gui.compat.tcon.ContainerPartBuilderSlab;
import com.Mrbysco.SlabMachines.gui.compat.tcon.GuiPartBuilderSlab;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.shared.block.PropertyTableItem;
import slimeknights.tconstruct.tools.common.tileentity.TilePartBuilder;

public class TilePartBuilderSlab extends TilePartBuilder{
	
	@Override
	public Container createContainer(InventoryPlayer inventoryplayer, World world, BlockPos pos) {
		return new ContainerPartBuilderSlab(inventoryplayer, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(InventoryPlayer inventoryplayer, World world, BlockPos pos) {
	    return new GuiPartBuilderSlab(inventoryplayer, world, pos, this);
	}
	
	@Override
	public IExtendedBlockState writeExtendedBlockState(IExtendedBlockState state) {
	    state = setInventoryDisplay(state);
	    
		return state;
	}
	
	@Override
	protected IExtendedBlockState setInventoryDisplay(IExtendedBlockState state) {
	    PropertyTableItem.TableItems toDisplay = new PropertyTableItem.TableItems();
	    float c = 0.2125f;
	    float[] x = new float[]{c, -c, c, -c};
	    float[] y = new float[]{-c, -c, c, c};
	    for(int i = 0; i < 4; i++) {
	    	ItemStack stackInSlot = getStackInSlot(i);
	    	PropertyTableItem.TableItem item = getTableItem(stackInSlot, this.getWorld(), null);
	    	if(item != null) {
	    		item.x += x[i];
	    		item.z += y[i];
	    		item.s *= 0.46875f;

	        // correct itemblock because scaling
	        if(stackInSlot.getItem() instanceof ItemBlock && !(Block.getBlockFromItem(stackInSlot.getItem())  instanceof BlockPane)) {
	        	item.y = -(1f - item.s) / 2f;
	        }

	        //item.s *= 2/5f;
	        	toDisplay.items.add(item);
	    	}
	    }

	    // add inventory if needed
	    return state.withProperty(BlockPartBuilderSlab.INVENTORY, toDisplay);
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
