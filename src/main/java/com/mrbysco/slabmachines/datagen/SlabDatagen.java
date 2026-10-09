package com.mrbysco.slabmachines.datagen;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.datagen.assets.SlabLanguageProvider;
import com.mrbysco.slabmachines.datagen.assets.SlabModelProvider;
import com.mrbysco.slabmachines.datagen.data.SlabBlockTagProvider;
import com.mrbysco.slabmachines.datagen.data.SlabItemTagProvider;
import com.mrbysco.slabmachines.datagen.data.SlabLootProvider;
import com.mrbysco.slabmachines.datagen.data.SlabRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class SlabDatagen {

	public static final RegistrySetBuilder RELOADABLE_BUILDER = new RegistrySetBuilder()
			.add(Registries.LOOT_TABLE, new LootTableProvider(Set.of(), List.of(
					new LootTableProvider.SubProviderEntry(SlabLootProvider.SlabBlockLoot::new, LootContextParamSets.BLOCK)
			)))
			.add(SlabRecipeProvider.create());

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getReloadableLookupProvider();

		generator.addProvider(true, DatapackBuiltinEntriesProvider.forReloadableLayer(packOutput,"reload_slab_registries", event.getWorldLookupProvider(), event.getReloadableLookupProvider(), RELOADABLE_BUILDER, Set.of(SlabReference.MOD_ID)));
		generator.addProvider(true, new SlabBlockTagProvider(packOutput, lookupProvider));
		generator.addProvider(true, new SlabItemTagProvider(packOutput, lookupProvider));

		generator.addProvider(true, new SlabLanguageProvider(packOutput));
		generator.addProvider(true, new SlabModelProvider(packOutput));

	}
}
