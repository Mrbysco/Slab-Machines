package com.mrbysco.slabmachines.proxy;

import com.mrbysco.slabmachines.entity.EntityTNTPrimeSlab;
import com.mrbysco.slabmachines.entity.TNTPrimeSlabRenderer;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TileCraftingStationSlab;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TileCraftingStationSlabRenderer;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TilePartBuilderSlab;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TilePartBuilderSlabRenderer;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TileToolForgeSlab;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TileToolStationRenderer;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TileToolStationSlab;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

public class ClientProxy extends CommonProxy{
	@Override
	public void Preinit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityTNTPrimeSlab.class, TNTPrimeSlabRenderer.FACTORY);
		
		if(Loader.isModLoaded("tconstruct"))
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
