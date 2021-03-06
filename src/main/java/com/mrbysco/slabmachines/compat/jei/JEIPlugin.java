package com.mrbysco.slabmachines.compat.jei;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.container.SlabBenchContainer;
import com.mrbysco.slabmachines.init.SlabRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
	private static final ResourceLocation UID = new ResourceLocation(SlabReference.MOD_ID, "jei_plugin");

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(SlabRegistry.CRAFTING_TABLE_SLAB.get()), VanillaRecipeCategoryUid.CRAFTING);
		registration.addRecipeCatalyst(new ItemStack(SlabRegistry.FURNACE_SLAB.get()), VanillaRecipeCategoryUid.FURNACE);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(SlabBenchContainer.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
	}
}
