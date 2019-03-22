package com.mrbysco.slabmachines.gui;

import com.mrbysco.slabmachines.gui.workbench.ContainerWorkbenchSlab;
import com.mrbysco.slabmachines.gui.workbench.GuiWorkbenchSlab;
import com.mrbysco.slabmachines.gui.workbench.fast.ContainerFastWorkbenchSlab;
import com.mrbysco.slabmachines.gui.workbench.fast.GuiFastWorkbenchSlab;
import com.mrbysco.slabmachines.tileentity.TileChestSlab;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TileCraftingStationSlab;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TilePartBuilderSlab;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TilePartChestSlab;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TilePatternChestSlab;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TileStencilTableSlab;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TileToolForgeSlab;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TileToolStationSlab;
import com.mrbysco.slabmachines.tileentity.furnace.TileFurnaceSlab;
import com.mrbysco.slabmachines.tileentity.furnace.compat.TileFastFurnaceSlab;
import com.mrbysco.slabmachines.utils.SlabUtil;

import net.minecraft.block.BlockSlab;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import slimeknights.mantle.common.IInventoryGui;

public class SlabGuiHandler implements IGuiHandler {

	public static final int GUI_WORKBENCH_SLAB_TOP = 0;
	public static final int GUI_WORKBENCH_SLAB_BOTTOM = 1;
	public static final int GUI_FURNACE_SLAB_TOP = 2;
	public static final int GUI_FURNACE_SLAB_BOTTOM = 3;
	public static final int GUI_CHEST_SLAB_TOP = 4;
	public static final int GUI_CHEST_SLAB_BOTTOM = 5;
	public static final int GUI_NOTEBLOCK_SLAB_TOP = 6;
	public static final int GUI_NOTEBLOCK_SLAB_BOTTOM = 7;
	
	public static final int GUI_FASTWORKBENCH_SLAB_TOP = 8;
	public static final int GUI_FASTWORKBENCH_SLAB_BOTTOM = 9;
	public static final int GUI_FASTFURNACE_SLAB_TOP = 10;
	public static final int GUI_FASTFURNACE_SLAB_BOTTOM = 11;
	
	public static final int GUI_CRAFTINGSTATION_SLAB_TOP = 12;
	public static final int GUI_CRAFTINGSTATION_SLAB_BOTTOM = 13;
	public static final int GUI_STENCILTABLE_SLAB_TOP = 14;
	public static final int GUI_STENCILTABLE_SLAB_BOTTOM = 15;
	public static final int GUI_PARTBUILDER_SLAB_TOP = 16;
	public static final int GUI_PARTBUILDER_SLAB_BOTTOM = 17;
	public static final int GUI_TOOLSTATION_SLAB_TOP = 18;
	public static final int GUI_TOOLSTATION_SLAB_BOTTOM = 19;
	public static final int GUI_PATTERNCHEST_SLAB_TOP = 20;
	public static final int GUI_PATTERNCHEST_SLAB_BOTTOM = 21;
	public static final int GUI_TOOLFORGE_SLAB_TOP = 22;
	public static final int GUI_TOOLFORGE_SLAB_BOTTOM = 23;
	public static final int GUI_PARTCHEST_SLAB_TOP = 24;
	public static final int GUI_PARTCHEST_SLAB_BOTTOM = 25;
	 
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {		
		if(ID == GUI_WORKBENCH_SLAB_TOP)
		{
			 return new ContainerWorkbenchSlab(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_WORKBENCH_SLAB_BOTTOM)
		{
			return new ContainerWorkbenchSlab(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_FURNACE_SLAB_TOP)
		{
			return new ContainerFurnace(player.inventory, SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileFurnaceSlab.class));
		}
		else if(ID == GUI_FURNACE_SLAB_BOTTOM)
		{
			return new ContainerFurnace(player.inventory, SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileFurnaceSlab.class));
		}
		else if(ID == GUI_FASTWORKBENCH_SLAB_TOP)
		{
			return new ContainerFastWorkbenchSlab(player, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_FASTWORKBENCH_SLAB_BOTTOM)
		{
			return new ContainerFastWorkbenchSlab(player, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_FASTFURNACE_SLAB_TOP)
		{
			return new ContainerFurnace(player.inventory, SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileFastFurnaceSlab.class));
		}
		else if(ID == GUI_FASTFURNACE_SLAB_BOTTOM)
		{
			return new ContainerFurnace(player.inventory, SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileFastFurnaceSlab.class));
		}
		else if(ID == GUI_CHEST_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileChestSlab.class) != null)
		{
			return SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileChestSlab.class).createContainer(player.inventory, player);
		}
		else if(ID == GUI_CHEST_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileChestSlab.class) != null)
		{
			return SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileChestSlab.class).createContainer(player.inventory, player);
		}
		else if(ID == GUI_CRAFTINGSTATION_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileCraftingStationSlab.class) instanceof IInventoryGui)
		{
		    return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileCraftingStationSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_CRAFTINGSTATION_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileCraftingStationSlab.class) instanceof IInventoryGui)
		{
		    return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileCraftingStationSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));	
		}
		else if(ID == GUI_STENCILTABLE_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileStencilTableSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileStencilTableSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_STENCILTABLE_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileStencilTableSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileStencilTableSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));	
		}
		else if(ID == GUI_PARTBUILDER_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePartBuilderSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePartBuilderSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PARTBUILDER_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePartBuilderSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePartBuilderSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_TOOLSTATION_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileToolStationSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileToolStationSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_TOOLSTATION_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileToolStationSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileToolStationSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PATTERNCHEST_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePatternChestSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePatternChestSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PATTERNCHEST_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePatternChestSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePatternChestSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_TOOLFORGE_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileToolForgeSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileToolForgeSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_TOOLFORGE_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileToolForgeSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileToolForgeSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PARTCHEST_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePartChestSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePartChestSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PARTCHEST_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePartChestSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePartChestSlab.class)).createContainer(player.inventory, world, new BlockPos(x, y, z));
		}
		else
		{
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUI_WORKBENCH_SLAB_TOP)
		{
			 return new GuiWorkbenchSlab(player.inventory, world);
		}
		else if(ID == GUI_WORKBENCH_SLAB_BOTTOM)
		{
			return new GuiWorkbenchSlab(player.inventory, world);
		}
		else if(ID == GUI_FURNACE_SLAB_TOP)
		{
			return new GuiFurnace(player.inventory, SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileFurnaceSlab.class));
		}
		else if(ID == GUI_FURNACE_SLAB_BOTTOM)
		{
			return new GuiFurnace(player.inventory, SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileFurnaceSlab.class));
		}
		else if(ID == GUI_FASTWORKBENCH_SLAB_TOP)
		{
			return new GuiFastWorkbenchSlab(player.inventory, world);
		}
		else if(ID == GUI_FASTWORKBENCH_SLAB_BOTTOM)
		{
			return new GuiFastWorkbenchSlab(player.inventory, world);
		}
		else if(ID == GUI_FASTFURNACE_SLAB_TOP)
		{
			return new GuiFurnace(player.inventory, SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileFastFurnaceSlab.class));
		}
		else if(ID == GUI_FASTFURNACE_SLAB_BOTTOM)
		{
			return new GuiFurnace(player.inventory, SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileFastFurnaceSlab.class));
		}
		else if(ID == GUI_CHEST_SLAB_TOP)
		{
			return SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileChestSlab.class).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_CHEST_SLAB_BOTTOM)
		{
			return SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileChestSlab.class).createGui(player.inventory, world,  new BlockPos(x, y, z));
		}
		else if(ID == GUI_CRAFTINGSTATION_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileCraftingStationSlab.class) instanceof IInventoryGui)
		{
		    return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileCraftingStationSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_CRAFTINGSTATION_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileCraftingStationSlab.class) instanceof IInventoryGui)
		{
		    return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileCraftingStationSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_STENCILTABLE_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileStencilTableSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileStencilTableSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_STENCILTABLE_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileStencilTableSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileStencilTableSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PARTBUILDER_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePartBuilderSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePartBuilderSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PARTBUILDER_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePartBuilderSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePartBuilderSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_TOOLSTATION_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileToolStationSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileToolStationSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_TOOLSTATION_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileToolStationSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileToolStationSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PATTERNCHEST_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePatternChestSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePatternChestSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PATTERNCHEST_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePatternChestSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePatternChestSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_TOOLFORGE_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileToolForgeSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TileToolForgeSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_TOOLFORGE_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileToolForgeSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TileToolForgeSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PARTCHEST_SLAB_TOP && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePartChestSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.TOP, TilePartChestSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else if(ID == GUI_PARTCHEST_SLAB_BOTTOM && SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePartChestSlab.class) instanceof IInventoryGui)
		{
			return ((IInventoryGui) SlabUtil.getTileSlab(world, new BlockPos(x, y, z), BlockSlab.EnumBlockHalf.BOTTOM, TilePartChestSlab.class)).createGui(player.inventory, world, new BlockPos(x, y, z));
		}
		else
		{
			return null;
		}
	}

}
