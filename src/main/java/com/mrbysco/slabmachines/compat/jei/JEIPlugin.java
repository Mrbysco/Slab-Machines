package com.mrbysco.slabmachines.compat.jei;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.init.SlabRegistry;
import com.mrbysco.slabmachines.menu.SlabBenchMenu;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
	private static final Identifier UID = Identifier.fromNamespaceAndPath(SlabReference.MOD_ID, "jei_plugin");

	@Override
	public Identifier getPluginUid() {
		return UID;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addCraftingStation(RecipeTypes.CRAFTING, new ItemStack(SlabRegistry.CRAFTING_TABLE_SLAB.get()));
		registration.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(SlabRegistry.FURNACE_SLAB.get()));
		registration.addCraftingStation(RecipeTypes.BLASTING, new ItemStack(SlabRegistry.BLAST_FURNACE_SLAB.get()));
		registration.addCraftingStation(RecipeTypes.SMOKING, new ItemStack(SlabRegistry.SMOKER_SLAB.get()));
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(SlabBenchMenu.class, MenuType.CRAFTING, RecipeTypes.CRAFTING, 1, 9, 10, 36);
	}
}
