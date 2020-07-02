package com.mrbysco.slabmachines.compat.jei;

import com.mrbysco.slabmachines.compat.jei.tcon.RecipeCraftingStationSlabInfo;
import com.mrbysco.slabmachines.gui.workbench.ContainerWorkbenchSlab;
import com.mrbysco.slabmachines.gui.workbench.fast.ContainerFastWorkbenchSlab;
import com.mrbysco.slabmachines.init.SlabBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

@JEIPlugin
public class JeiPlugin implements IModPlugin{
	@Override
	public void register(IModRegistry registry) {
		registry.addRecipeCatalyst(new ItemStack(SlabBlocks.workbenchSlab), VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeCatalyst(new ItemStack(SlabBlocks.furnaceSlab), VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL);
		
		if(Loader.isModLoaded("tconstruct"))
		{
			registry.addRecipeCatalyst(new ItemStack(SlabBlocks.craftingStationSlab), VanillaRecipeCategoryUid.CRAFTING);
			registry.getRecipeTransferRegistry().addRecipeTransferHandler(new RecipeCraftingStationSlabInfo());
		}
		
		if(Loader.isModLoaded("fastbench"))
		{
			registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerFastWorkbenchSlab.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		}
		else
		{
			registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerWorkbenchSlab.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		}
	}
}
