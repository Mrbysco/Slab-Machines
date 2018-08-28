package com.Mrbysco.SlabMachines.config;

import com.Mrbysco.SlabMachines.SlabReference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = SlabReference.MOD_ID)
@Config.LangKey("slabmachines.config.title")
public class SlabMachineConfigGen {
	@Config.Comment({"General settings"})
	public static General general = new General();
	
	@Config.Comment({"Mod Support settings"})
	public static ModSupport modsupport = new ModSupport();
	
	public static class General{
		@Config.Comment("This setting limits the slab furnace max stacksize to 32 [default: false]")
		public boolean nerfSlabFurnace = true;
		
		@Config.Comment("This setting limits the slab chest max stacksize to 32 [default: false]")
		public boolean nerfSlabChest = true;
		
		@Config.Comment("Changing this to true will cause the Etho slab to also drop onto Villagers [default: false]")
		public boolean ethoSlabVillagers = true;
	}

	public static class ModSupport{
		public final TinkersSupport tconstruct = new TinkersSupport();
		
		public final class TinkersSupport{
			@Config.Comment("This setting enables rendering of items on the appropiate tinkers slabs [default: true]")
			public boolean tinkersTopRendering = true;
		}
	}
	
	@Mod.EventBusSubscriber(modid = SlabReference.MOD_ID)
	private static class EventHandler {

		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(SlabReference.MOD_ID)) {
				ConfigManager.sync(SlabReference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
