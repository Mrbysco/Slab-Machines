package com.mrbysco.slabmachines.compat.craftingtweaks;

import com.mrbysco.slabmachines.gui.compat.tcon.ContainerCraftingStationSlab;
import com.mrbysco.slabmachines.gui.workbench.ContainerWorkbenchSlab;
import com.mrbysco.slabmachines.gui.workbench.fast.ContainerFastWorkbenchSlab;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class CraftTweaksCompat {
	public static void register() {
		NBTTagCompound workbench = new NBTTagCompound();
		
		if(Loader.isModLoaded("fastbench"))
			workbench.setString("ContainerClass", ContainerFastWorkbenchSlab.class.getName());
		else
			workbench.setString("ContainerClass", ContainerWorkbenchSlab.class.getName());
		
		workbench.setString("AlignToGrid", "left");
		FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", workbench);
		
		if(Loader.isModLoaded("tconstruct"))
		{
			registerTinkers();
		}
	}
	
	@net.minecraftforge.fml.common.Optional.Method(modid = "tconstruct")
    public static void registerTinkers()
    {
		NBTTagCompound tinkers = new NBTTagCompound();
		tinkers.setString("ContainerClass", ContainerCraftingStationSlab.class.getName());
		tinkers.setString("AlignToGrid", "left");
		FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", tinkers);
    }
}
