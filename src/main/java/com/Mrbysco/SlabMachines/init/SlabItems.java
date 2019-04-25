package com.mrbysco.slabmachines.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

@EventBusSubscriber
public class SlabItems {
	public static ArrayList<Item> ITEMS = new ArrayList<>();
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();
        
        registry.registerAll(ITEMS.toArray(new Item[0]));
    }
    
    public static <T extends Item> T registerItem(T item)
    {
        ITEMS.add(item);
        return item;
    }
}
