package com.mrbysco.slabmachines;

import com.mrbysco.slabmachines.client.ClientHandler;
import com.mrbysco.slabmachines.config.SlabConfig;
import com.mrbysco.slabmachines.container.SlabBenchContainer;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SlabReference.MOD_ID)
public class SlabMachines {
	public static final Logger LOGGER = LogManager.getLogger(SlabReference.MOD_ID);

	public SlabMachines() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(Type.COMMON, SlabConfig.commonSpec);
		eventBus.register(SlabConfig.class);

		eventBus.addListener(this::interModEnqueueEvent);

		SlabRegistry.BLOCKS.register(eventBus);
		SlabRegistry.ITEMS.register(eventBus);
		SlabRegistry.ENTITIES.register(eventBus);
		SlabRegistry.BLOCK_ENTITIES.register(eventBus);
		SlabRegistry.CONTAINERS.register(eventBus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::registerRenders);
			eventBus.addListener(ClientHandler::registerEntityRenders);
		});
	}

	public void interModEnqueueEvent(InterModEnqueueEvent event) {
		InterModComms.sendTo("craftingtweaks", "RegisterProvider", () -> {
			CompoundTag tagCompound = new CompoundTag();
			tagCompound.putString("ContainerClass", SlabBenchContainer.class.getName());
			return tagCompound;
		});
	}
}
