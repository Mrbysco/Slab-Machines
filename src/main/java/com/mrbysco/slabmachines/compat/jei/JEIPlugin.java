package com.mrbysco.slabmachines.compat.jei;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.menu.SlabBenchMenu;
import com.mrbysco.slabmachines.init.SlabRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
	private static final ResourceLocation UID = new ResourceLocation(SlabReference.MOD_ID, "jei_plugin");

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(SlabRegistry.CRAFTING_TABLE_SLAB.get()), RecipeTypes.CRAFTING);
		registration.addRecipeCatalyst(new ItemStack(SlabRegistry.FURNACE_SLAB.get()), RecipeTypes.SMELTING);
		registration.addRecipeCatalyst(new ItemStack(SlabRegistry.BLAST_FURNACE_SLAB.get()), RecipeTypes.BLASTING);
		registration.addRecipeCatalyst(new ItemStack(SlabRegistry.SMOKER_SLAB.get()), RecipeTypes.SMOKING);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(SlabBenchMenu.class, MenuType.CRAFTING, RecipeTypes.CRAFTING, 1, 9, 10, 36);
	}
}
