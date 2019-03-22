package com.mrbysco.slabmachines.init;

import java.util.ArrayList;

import com.mrbysco.slabmachines.SlabMachines;
import com.mrbysco.slabmachines.blocks.BlockChestSlab;
import com.mrbysco.slabmachines.blocks.BlockFurnaceSlab;
import com.mrbysco.slabmachines.blocks.BlockNoteblockSlab;
import com.mrbysco.slabmachines.blocks.BlockTNTSlab;
import com.mrbysco.slabmachines.blocks.BlockTrappedChestSlab;
import com.mrbysco.slabmachines.blocks.BlockWorkbenchSlab;
import com.mrbysco.slabmachines.blocks.compat.tinkers.BlockCraftingStationSlab;
import com.mrbysco.slabmachines.blocks.compat.tinkers.BlockPartBuilderSlab;
import com.mrbysco.slabmachines.blocks.compat.tinkers.BlockPartChestSlab;
import com.mrbysco.slabmachines.blocks.compat.tinkers.BlockPatternChestSlab;
import com.mrbysco.slabmachines.blocks.compat.tinkers.BlockStencilTableSlab;
import com.mrbysco.slabmachines.blocks.compat.tinkers.BlockToolForgeSlab;
import com.mrbysco.slabmachines.blocks.compat.tinkers.BlockToolStationSlab;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class SlabBlocks {
	public static BlockSlab workbenchSlab, furnaceSlab, chestSlab, trappedChestSlab, noteSlab, tntSlab;
	
	//Tinkers Stuff
	public static BlockSlab craftingStationSlab, partBuilderSlab, partChestSlab, patternChestSlab, stencilTableSlab, toolForgeSlab, toolStationSlab;
		
	public static ArrayList<Block> BLOCKS = new ArrayList<>();
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
    	IForgeRegistry<Block> registry = event.getRegistry();
    	
    	workbenchSlab = registerBlock(new BlockWorkbenchSlab());
    	furnaceSlab = registerBlock(new BlockFurnaceSlab());
    	chestSlab = registerBlock(new BlockChestSlab());
    	trappedChestSlab = registerBlock(new BlockTrappedChestSlab());
    	noteSlab = registerBlock(new BlockNoteblockSlab());
    	tntSlab = registerBlock(new BlockTNTSlab());
    	
    	if(SlabMachines.tinkersLoaded)
    	{
    		registerTinkers();
    	}
    	
    	registry.registerAll(BLOCKS.toArray(new Block[0]));
    }
    
	@net.minecraftforge.fml.common.Optional.Method(modid = "tconstruct")
    public static void registerTinkers()
    {
		craftingStationSlab = registerBlock(new BlockCraftingStationSlab());
		partBuilderSlab = registerBlock(new BlockPartBuilderSlab());
		partChestSlab = registerBlock(new BlockPartChestSlab());
		patternChestSlab = registerBlock(new BlockPatternChestSlab());
		stencilTableSlab = registerBlock(new BlockStencilTableSlab());	
		toolForgeSlab = registerBlock(new BlockToolForgeSlab());
		toolStationSlab = registerBlock(new BlockToolStationSlab());
    }
    
    public static <T extends Block> T registerBlock(T block)
    {
        return registerBlock(block, new ItemBlock(block));
    }
    
    public static <T extends Block> T registerBlock(T block, ItemBlock item)
    {
    	item.setRegistryName(block.getRegistryName());
    	SlabItems.ITEMS.add(item);
        BLOCKS.add(block);
        return block;
    }
}
