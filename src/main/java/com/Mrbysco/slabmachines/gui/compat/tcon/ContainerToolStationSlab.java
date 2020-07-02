package com.mrbysco.slabmachines.gui.compat.tcon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.StringUtils;
import net.minecraft.world.WorldServer;
import slimeknights.mantle.util.ItemStackList;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.events.TinkerCraftingEvent;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tinkering.IModifyable;
import slimeknights.tconstruct.library.tinkering.IRepairable;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolBuilder;
import slimeknights.tconstruct.tools.common.inventory.ContainerToolStation;
import slimeknights.tconstruct.tools.common.tileentity.TileToolStation;

public class ContainerToolStationSlab extends ContainerToolStation{
	private final EntityPlayer player;

	public ContainerToolStationSlab(InventoryPlayer playerInventory, TileToolStation tile) {
		super(playerInventory, tile);
	    this.player = playerInventory.player;
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		// reset gui state
	    updateGUI();
	    try {
	      ItemStack result;
	      // 1. try repairing
	      result = repairTool(false);
	      // 2. try swapping tool parts
	      if(result.isEmpty()) {
	        result = replaceToolParts(false);
	      }
	      // 3. try modifying
	      if(result.isEmpty()) {
	        result = modifyTool(false);
	      }
	      // 4. try renaming
	      if(result.isEmpty()) {
	        result = renameTool();
	      }
	      // 5. try building a new tool
	      if(result.isEmpty()) {
	        result = buildTool();
	      }

	      out.inventory.setInventorySlotContents(0, result);
	      updateGUI();
	    } catch(TinkerGuiException e) {
	      // error ;(
	      out.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
	      this.error(e.getMessage());
	    }
	    // sync output with other open containers on the server
	    if(!this.world.isRemote) {
	      WorldServer server = null;
	    	if(this.world instanceof mcmultipart.util.MCMPWorldWrapper)
	    	{
	    		mcmultipart.util.MCMPWorldWrapper mcmpWorld = (mcmultipart.util.MCMPWorldWrapper)this.world;
	    		server = (WorldServer) mcmpWorld.getActualWorld();
	    	}
	    	else
	    	{
	  	        server = (WorldServer) this.world;
	    	}
	      for(EntityPlayer player : server.playerEntities) {
	        if(player.openContainer != this && player.openContainer instanceof ContainerToolStationSlab && this.sameGui((ContainerToolStationSlab) player.openContainer)) {
	          ((ContainerToolStationSlab) player.openContainer).out.inventory.setInventorySlotContents(0, out.getStack());
	        }
	      }
	    }
	}
	
	private ItemStack repairTool(boolean remove) {
	    ItemStack repairable = getToolStack();

	    // modifying possible?
	    if(repairable.isEmpty() || !(repairable.getItem() instanceof IRepairable)) {
	      return ItemStack.EMPTY;
	    }

	    return ToolBuilder.tryRepairTool(getInputs(), repairable, remove);
	  }

	  private ItemStack replaceToolParts(boolean remove) throws TinkerGuiException {
	    ItemStack tool = getToolStack();

	    if(tool.isEmpty() || !(tool.getItem() instanceof TinkersItem)) {
	      return ItemStack.EMPTY;
	    }

	    NonNullList<ItemStack> inputs = getInputs();
	    ItemStack result = ToolBuilder.tryReplaceToolParts(tool, inputs, remove);
	    if(!result.isEmpty()) {
	      TinkerCraftingEvent.ToolPartReplaceEvent.fireEvent(result, player, inputs);
	    }
	    return result;
	  }
	  
	  private ItemStack getToolStack() {
		    return inventorySlots.get(0).getStack();
	  }
	  
	  private NonNullList<ItemStack> getInputs() {
		    NonNullList<ItemStack> input = NonNullList.withSize(tile.getSizeInventory() - 1, ItemStack.EMPTY);
		    for(int i = 1; i < tile.getSizeInventory(); i++) {
		      input.set(i - 1, tile.getStackInSlot(i));
		    }

		    return input;
	  }
	  
	  private ItemStack modifyTool(boolean remove) throws TinkerGuiException {
	    ItemStack modifyable = getToolStack();

	    // modifying possible?
	    if(modifyable.isEmpty() || !(modifyable.getItem() instanceof IModifyable)) {
	      return ItemStack.EMPTY;
	    }

	    ItemStack result = ToolBuilder.tryModifyTool(getInputs(), modifyable, remove);
	    if(!result.isEmpty()) {
	      TinkerCraftingEvent.ToolModifyEvent.fireEvent(result, player, modifyable.copy());
	    }
	    return result;
	  }

	  private ItemStack renameTool() throws TinkerGuiException {
	    ItemStack tool = getToolStack();

	    // modifying possible?
	    if(tool.isEmpty() ||
	       !(tool.getItem() instanceof TinkersItem) ||
	       StringUtils.isNullOrEmpty(toolName) ||
	       tool.getDisplayName().equals(toolName)) {
	      return ItemStack.EMPTY;
	    }

	    ItemStack result = tool.copy();
	    if(TagUtil.getNoRenameFlag(result)) {
	      throw new TinkerGuiException(Util.translate("gui.error.no_rename"));
	    }

	    result.setStackDisplayName(toolName);

	    return result;
	  }

	  private ItemStack buildTool() throws TinkerGuiException {
	    NonNullList<ItemStack> input = ItemStackList.withSize(tile.getSizeInventory());
	    for(int i = 0; i < input.size(); i++) {
	      input.set(i, tile.getStackInSlot(i));
	    }

	    ItemStack result = ToolBuilder.tryBuildTool(input, toolName, getBuildableTools());
	    if(!result.isEmpty()) {
	      TinkerCraftingEvent.ToolCraftingEvent.fireEvent(result, player, input);
	    }
	    return result;
	  }
}
