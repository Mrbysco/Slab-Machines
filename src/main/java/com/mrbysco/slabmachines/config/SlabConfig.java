package com.mrbysco.slabmachines.config;

import com.mrbysco.slabmachines.SlabMachines;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class SlabConfig {

	public static class Common {
		public final IntValue slabFurnaceSlotLimit;
		public final IntValue slabChestSlotLimit;
		public final BooleanValue ethoSlabVillagers;

		Common(net.neoforged.neoforge.common.ModConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("General");

			slabFurnaceSlotLimit = builder
					.comment("This setting limits the slab furnace max stacksize to what's filled in [default: 64]")
					.defineInRange("slabFurnaceSlotLimit", 64, 0, 64);

			slabChestSlotLimit = builder
					.comment("This setting limits the slab chest max stacksize to what's filled in [default: 64]")
					.defineInRange("slabChestSlotLimit", 64, 0, 64);

			ethoSlabVillagers = builder
					.comment("Changing this to true will cause the Etho slab to also drop onto Villagers [default: false]")
					.define("ethoSlabVillagers", false);

			builder.pop();
		}
	}

	public static final net.neoforged.neoforge.common.ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, net.neoforged.neoforge.common.ModConfigSpec> specPair = new net.neoforged.neoforge.common.ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		SlabMachines.LOGGER.debug("Loaded Slab Machines' config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		SlabMachines.LOGGER.debug("Slab Machines' config just got changed on the file system!");
	}
}
