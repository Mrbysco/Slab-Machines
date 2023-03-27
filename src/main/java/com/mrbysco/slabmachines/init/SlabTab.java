package com.mrbysco.slabmachines.init;

import com.mrbysco.slabmachines.SlabReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class SlabTab {
	public static CreativeModeTab SLAB_TAB;

	@SubscribeEvent
	public void registerCreativeTabs(final CreativeModeTabEvent.Register event) {
		SLAB_TAB = event.registerCreativeModeTab(new ResourceLocation(SlabReference.MOD_ID, "tab"), builder ->
				builder.icon(() -> new ItemStack(SlabRegistry.CRAFTING_TABLE_SLAB.get()))
						.title(Component.translatable("itemGroup.statues.items"))
						.displayItems((displayParameters, output) -> {
							List<ItemStack> stacks = SlabRegistry.ITEMS.getEntries().stream()
									.map(reg -> new ItemStack(reg.get())).toList();
							output.acceptAll(stacks);
						}));
	}
}
