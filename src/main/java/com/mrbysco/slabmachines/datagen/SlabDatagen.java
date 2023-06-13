package com.mrbysco.slabmachines.datagen;

import com.mrbysco.slabmachines.datagen.assets.SlabBlockModelProvider;
import com.mrbysco.slabmachines.datagen.assets.SlabBlockstateProvider;
import com.mrbysco.slabmachines.datagen.assets.SlabItemModelProvider;
import com.mrbysco.slabmachines.datagen.assets.SlabLanguageProvider;
import com.mrbysco.slabmachines.datagen.data.SlabBlockTagProvider;
import com.mrbysco.slabmachines.datagen.data.SlabItemTagProvider;
import com.mrbysco.slabmachines.datagen.data.SlabLootProvider;
import com.mrbysco.slabmachines.datagen.data.SlabRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlabDatagen {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(true, new SlabRecipeProvider(packOutput));
			generator.addProvider(true, new SlabLootProvider(packOutput));
			SlabBlockTagProvider blockTags;
			generator.addProvider(true, blockTags = new SlabBlockTagProvider(packOutput, lookupProvider, helper));
			generator.addProvider(true, new SlabItemTagProvider(packOutput, lookupProvider, blockTags.contentsGetter(), helper));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new SlabLanguageProvider(packOutput));
			generator.addProvider(true, new SlabBlockModelProvider(packOutput, helper));
			generator.addProvider(true, new SlabBlockstateProvider(packOutput, helper));
			generator.addProvider(true, new SlabItemModelProvider(packOutput, helper));
		}
	}
}
