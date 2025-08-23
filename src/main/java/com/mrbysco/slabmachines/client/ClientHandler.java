package com.mrbysco.slabmachines.client;

import com.mrbysco.slabmachines.init.SlabRegistry;
import com.mrbysco.slabmachines.menu.SlabBenchMenu;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientHandler {
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(SlabRegistry.SLAB_WORKBENCH_CONTAINER.get(), new Factory());
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
