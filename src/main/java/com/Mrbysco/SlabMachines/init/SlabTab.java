package com.mrbysco.slabmachines.init;

import com.mrbysco.slabmachines.SlabReference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class SlabTab extends CreativeTabs{

	public SlabTab() {
		super(SlabReference.MOD_ID);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(SlabBlocks.workbenchSlab);
	}

}
