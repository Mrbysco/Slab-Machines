package com.mrbysco.slabmachines.init;

import com.mrbysco.slabmachines.SlabReference;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SlabTab {
	public static final CreativeModeTab SLAB_TAB = (new CreativeModeTab(SlabReference.MOD_ID) {
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(SlabRegistry.CRAFTING_TABLE_SLAB.get());
		}
	});
}
