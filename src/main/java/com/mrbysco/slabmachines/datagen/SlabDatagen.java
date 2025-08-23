package com.mrbysco.slabmachines.datagen;

import com.mrbysco.slabmachines.datagen.assets.SlabLanguageProvider;
import com.mrbysco.slabmachines.datagen.assets.SlabModelProvider;
import com.mrbysco.slabmachines.datagen.data.SlabBlockTagProvider;
import com.mrbysco.slabmachines.datagen.data.SlabItemTagProvider;
import com.mrbysco.slabmachines.datagen.data.SlabLootProvider;
import com.mrbysco.slabmachines.datagen.data.SlabRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class SlabDatagen {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new SlabRecipeProvider.Runner(packOutput, lookupProvider));
		generator.addProvider(true, new SlabLootProvider(packOutput, lookupProvider));
		SlabBlockTagProvider blockTags;
		generator.addProvider(true, blockTags = new SlabBlockTagProvider(packOutput, lookupProvider));
		generator.addProvider(true, new SlabItemTagProvider(packOutput, lookupProvider, blockTags.contentsGetter()));

		generator.addProvider(true, new SlabLanguageProvider(packOutput));
		generator.addProvider(true, new SlabModelProvider(packOutput));

	}
}
