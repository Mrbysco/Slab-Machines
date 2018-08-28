package com.Mrbysco.SlabMachines.proxy;

import com.Mrbysco.SlabMachines.SlabMachines;
import com.Mrbysco.SlabMachines.entity.EntityTNTPrimeSlab;
import com.Mrbysco.SlabMachines.entity.TNTPrimeSlabRenderer;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TileCraftingStationSlab;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TileCraftingStationSlabRenderer;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TilePartBuilderSlab;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TilePartBuilderSlabRenderer;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TileToolForgeSlab;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TileToolStationRenderer;
import com.Mrbysco.SlabMachines.tileentity.compat.tinkers.TileToolStationSlab;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Optional;

public class ClientProxy extends CommonProxy{
	@Override
	public void Preinit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityTNTPrimeSlab.class, TNTPrimeSlabRenderer.FACTORY);
		
		if(SlabMachines.tinkersLoaded)
		{
			tinkersTESR();
		}
	}
	
	@Override
	public void Init() {
		
	}
	
	@Override
	public void Postinit() {
		
	}
	
	@Optional.Method(modid = "tconstruct")
	public void tinkersTESR()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileCraftingStationSlab.class, new TileCraftingStationSlabRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePartBuilderSlab.class, new TilePartBuilderSlabRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileToolStationSlab.class, new TileToolStationRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileToolForgeSlab.class, new TileToolStationRenderer());
	}
}
