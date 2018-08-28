package com.Mrbysco.SlabMachines.init;

import com.Mrbysco.SlabMachines.SlabMachines;
import com.Mrbysco.SlabMachines.SlabReference;
import com.Mrbysco.SlabMachines.entity.EntityTNTPrimeSlab;
import com.Mrbysco.SlabMachines.tileentity.TileChestSlab;
import com.Mrbysco.SlabMachines.tileentity.TileNoteSlab;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TileCraftingStationSlab;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TilePartBuilderSlab;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TilePartChestSlab;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TilePatternChestSlab;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TileStencilTableSlab;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TileToolForgeSlab;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TileToolStationSlab;
import com.Mrbysco.SlabMachines.tileentity.furnace.TileFurnaceSlab;
import com.Mrbysco.SlabMachines.tileentity.furnace.compat.TileFastFurnaceSlab;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SlabTileEntities {
	static int ID = 0;
	
	public static void register() {				
		registerEntity("slab_tnt_primed", EntityTNTPrimeSlab.class, "slabtntprimed", 80, 3, true);
		
		registerTileEntity(TileChestSlab.class, "slab_chest");	
		registerTileEntity(TileNoteSlab.class, "slab_note");	

		if(SlabMachines.fastFurnaceLoaded)
		{
			registerTileEntity(TileFastFurnaceSlab.class, "slab_furnace");
		}
		else
		{
			registerTileEntity(TileFurnaceSlab.class, "slab_furnace");	
		}
		
		if(SlabMachines.tinkersLoaded)
		{
			registerTileEntity(TileCraftingStationSlab.class, "slab_crafting_station");	
			registerTileEntity(TilePartBuilderSlab.class, "slab_part_builder");	
			registerTileEntity(TilePartChestSlab.class, "slab_part_chest");	
			registerTileEntity(TilePatternChestSlab.class, "slab_pattern_chest");	
			registerTileEntity(TileStencilTableSlab.class, "slab_stencil_table");	
			registerTileEntity(TileToolForgeSlab.class, "slab_tool_forge");	
			registerTileEntity(TileToolStationSlab.class, "slab_tool_station");	
		}
	}
	
	public static void registerEntity(String registryName, Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(new ResourceLocation(SlabReference.MOD_ID, registryName), entityClass, SlabReference.MOD_PREFIX + entityName, ID, SlabMachines.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		ID++;
	}
	
	public static void registerTileEntity(Class<? extends TileEntity> tileentityClass, String tilename) {
		GameRegistry.registerTileEntity(tileentityClass, new ResourceLocation(SlabReference.MOD_ID, tilename.replaceAll("_", "")));
	}
}
