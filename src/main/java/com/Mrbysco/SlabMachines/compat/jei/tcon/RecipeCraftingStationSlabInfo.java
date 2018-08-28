package com.Mrbysco.SlabMachines.compat.jei.tcon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.Mrbysco.SlabMachines.gui.compat.tcon.ContainerCraftingStationSlab;

import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;

public class RecipeCraftingStationSlabInfo implements IRecipeTransferInfo<ContainerCraftingStationSlab> {

	  @Nonnull
	  @Override
	  public Class<ContainerCraftingStationSlab> getContainerClass() {
	    return ContainerCraftingStationSlab.class;
	  }

	  @Nonnull
	  @Override
	  public String getRecipeCategoryUid() {
	    return VanillaRecipeCategoryUid.CRAFTING;
	  }

	  @Override
	  public boolean canHandle(ContainerCraftingStationSlab container) {
	    return true;
	  }

	  @Nonnull
	  @Override
	  public List<Slot> getRecipeSlots(ContainerCraftingStationSlab container) {
	    List<Slot> slots = new ArrayList<>();
	    for(int i = 1; i < 10; i++) {
	      slots.add(container.getSlot(i));
	    }
	    return slots;
	  }

	  @Nonnull
	  @Override
	  public List<Slot> getInventorySlots(ContainerCraftingStationSlab container) {
	    List<Slot> slots = new ArrayList<>();

	    // skip the actual slots of the crafting table
	    for(int i = 10; i < container.inventorySlots.size(); i++) {
	      slots.add(container.getSlot(i));
	    }
	    return slots;
	  }
	}