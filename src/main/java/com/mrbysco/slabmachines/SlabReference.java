package com.mrbysco.slabmachines;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SlabReference {
	public static final String MOD_ID = "slabmachines";
	public static final String MOD_PREFIX = MOD_ID + ":";

	public static final TagKey<Item> COBBLESTONE_SLABS = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "cobblestone_slabs"));

}
