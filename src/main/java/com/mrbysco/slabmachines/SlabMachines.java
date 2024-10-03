package com.mrbysco.slabmachines;

import com.mojang.logging.LogUtils;
import com.mrbysco.slabmachines.client.ClientHandler;
import com.mrbysco.slabmachines.config.SlabConfig;
import com.mrbysco.slabmachines.init.SlabRegistry;
import com.mrbysco.slabmachines.menu.SlabBenchMenu;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(SlabReference.MOD_ID)
public class SlabMachines {
	public static final Logger LOGGER = LogUtils.getLogger();

	public SlabMachines(IEventBus eventBus, Dist dist, ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, SlabConfig.commonSpec);
		eventBus.register(SlabConfig.class);

		eventBus.addListener(this::commonSetupEvent);
		eventBus.addListener(this::interModEnqueueEvent);
		eventBus.addListener(SlabRegistry::registerCapabilities);

		SlabRegistry.BLOCKS.register(eventBus);
		SlabRegistry.ITEMS.register(eventBus);
		SlabRegistry.CREATIVE_MODE_TABS.register(eventBus);
		SlabRegistry.ENTITY_TYPES.register(eventBus);
		SlabRegistry.BLOCK_ENTITY_TYPES.register(eventBus);
		SlabRegistry.MENU_TYPES.register(eventBus);

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
			eventBus.addListener(ClientHandler::registerMenuScreens);
			eventBus.addListener(ClientHandler::registerEntityRenders);
		}
	}

	public void interModEnqueueEvent(InterModEnqueueEvent event) {
		InterModComms.sendTo("craftingtweaks", "RegisterProvider", () -> {
			CompoundTag tagCompound = new CompoundTag();
			tagCompound.putString("ContainerClass", SlabBenchMenu.class.getName());
			return tagCompound;
		});
	}

	public void commonSetupEvent(FMLCommonSetupEvent event) {
		event.enqueueWork(SlabRegistry::registerPointOfInterests);
	}
}
