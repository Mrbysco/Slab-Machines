package com.mrbysco.slabmachines.datagen.assets;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class SlabItemModelProvider extends ItemModelProvider {
	public SlabItemModelProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, SlabReference.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		for (RegistryObject<Item> registryObject : SlabRegistry.ITEMS.getEntries()) {
			if (registryObject.get() instanceof BlockItem) {
				withBlockParent(registryObject.getId());
			}
		}
	}

	private void withBlockParent(ResourceLocation location) {
		withExistingParent(location.getPath(), modLoc("block/" + location.getPath()));
	}
}
