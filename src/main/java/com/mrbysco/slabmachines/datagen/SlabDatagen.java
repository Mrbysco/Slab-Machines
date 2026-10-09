package com.mrbysco.slabmachines.datagen;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.datagen.assets.SlabLanguageProvider;
import com.mrbysco.slabmachines.datagen.assets.SlabModelProvider;
import com.mrbysco.slabmachines.datagen.data.SlabBlockTagProvider;
import com.mrbysco.slabmachines.datagen.data.SlabItemTagProvider;
import com.mrbysco.slabmachines.datagen.data.SlabLootProvider;
import com.mrbysco.slabmachines.datagen.data.SlabRecipeProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

@EventBusSubscriber
public class SlabDatagen {

	@SubscribeEvent
	static void onGatherRegistries(GatherDataRegistryEntriesEvent event) {
		event.gatherFor(SlabReference.MOD_ID)
				.add(RecipeProvider.asBootstrap(SlabRecipeProvider::new))
				.add(Registries.LOOT_TABLE, SlabLootProvider.create());
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		event.createProvider(SlabBlockTagProvider::new);
		event.createProvider(SlabItemTagProvider::new);

		event.createProvider(SlabLanguageProvider::new);
		event.createProvider(SlabModelProvider::new);
	}
}
