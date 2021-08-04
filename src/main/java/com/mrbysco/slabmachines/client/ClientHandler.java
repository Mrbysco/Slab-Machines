package com.mrbysco.slabmachines.client;

import com.mrbysco.slabmachines.container.SlabBenchContainer;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
    public static void registerRenders(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(SlabRegistry.TNT_SLAB_ENTITY.get(), TNTPrimeSlabRenderer::new);

        ScreenManager.register(SlabRegistry.SLAB_WORKBENCH_CONTAINER.get(), new Factory());
    }

    private static class Factory implements IScreenFactory {
        @Override
        public CraftingScreen create(Container container, PlayerInventory pInv, ITextComponent name) {
            return new CraftingScreen((SlabBenchContainer) container, pInv, name);
        }
    }
}
