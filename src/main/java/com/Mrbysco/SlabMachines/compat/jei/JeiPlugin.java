package com.Mrbysco.SlabMachines.compat.jei;

import com.Mrbysco.SlabMachines.SlabMachines;
import com.Mrbysco.SlabMachines.gui.workbench.ContainerWorkbenchSlab;
import com.Mrbysco.SlabMachines.gui.workbench.fast.ContainerFastWorkbenchSlab;
import com.Mrbysco.SlabMachines.init.SlabBlocks;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.plugin.jei.CraftingStationRecipeTransferInfo;

@JEIPlugin
public class JeiPlugin implements IModPlugin{
	@Override
	public void register(IModRegistry registry) {
		registry.addRecipeCatalyst(new ItemStack(SlabBlocks.workbenchSlab), VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeCatalyst(new ItemStack(SlabBlocks.furnaceSlab), VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL);
		
		if(SlabMachines.tinkersLoaded)
		{
			registry.addRecipeCatalyst(new ItemStack(SlabBlocks.craftingStationSlab), VanillaRecipeCategoryUid.CRAFTING);
			registry.getRecipeTransferRegistry().addRecipeTransferHandler(new CraftingStationRecipeTransferInfo());
		}
		
		if(SlabMachines.fastBenchLoaded)
			registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerFastWorkbenchSlab.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		else
			registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerWorkbenchSlab.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
	}
}
