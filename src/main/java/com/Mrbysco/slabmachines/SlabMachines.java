package com.mrbysco.slabmachines;

import com.mrbysco.slabmachines.compat.craftingtweaks.CraftTweaksCompat;
import com.mrbysco.slabmachines.config.SlabMachineConfigGen;
import com.mrbysco.slabmachines.gui.SlabGuiHandler;
import com.mrbysco.slabmachines.init.SlabTab;
import com.mrbysco.slabmachines.init.SlabTileEntities;
import com.mrbysco.slabmachines.packets.SlabPacketHandler;
import com.mrbysco.slabmachines.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = SlabReference.MOD_ID, 
	name = SlabReference.MOD_NAME, 
	version = SlabReference.VERSION, 
	acceptedMinecraftVersions = SlabReference.ACCEPTED_VERSIONS,
	dependencies = SlabReference.DEPENDENCIES)

public class SlabMachines {
	@Instance(SlabReference.MOD_ID)
	public static SlabMachines instance;
	
	@SidedProxy(clientSide = SlabReference.CLIENT_PROXY_CLASS, serverSide = SlabReference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final Logger logger = LogManager.getLogger(SlabReference.MOD_ID);
	
	public static SlabTab slabTab = new SlabTab();
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		logger.info("Registering Packet Handler");
		SlabPacketHandler.registerMessages();
		
		logger.info("Registering Config");
		MinecraftForge.EVENT_BUS.register(new SlabMachineConfigGen());
		
		logger.info("Registering TileEntities");
		SlabTileEntities.register();
		
		proxy.Preinit();
	}
	
	@EventHandler
    public void init(FMLInitializationEvent event)
	{
		logger.info("Registering gui handler");
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new SlabGuiHandler());
		
		logger.info("Registering Crafting Tweaks support");
		CraftTweaksCompat.register();
		
		proxy.Init();
    }
	
	@EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
		proxy.Postinit();
    }
}
