package com.mrbysco.slabmachines.client;

import com.mrbysco.slabmachines.menu.SlabBenchMenu;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
	public static void registerRenders(FMLClientSetupEvent event) {
		MenuScreens.register(SlabRegistry.SLAB_WORKBENCH_CONTAINER.get(), new Factory());
	}

	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SlabRegistry.TNT_SLAB_ENTITY.get(), TNTPrimeSlabRenderer::new);
	}

	private static class Factory implements ScreenConstructor {
		@Override
		public CraftingScreen create(AbstractContainerMenu container, Inventory pInv, Component name) {
			return new CraftingScreen((SlabBenchMenu) container, pInv, name);
		}
	}
}
