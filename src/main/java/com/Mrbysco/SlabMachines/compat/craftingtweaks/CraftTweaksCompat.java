package com.Mrbysco.SlabMachines.compat.craftingtweaks;

import com.Mrbysco.SlabMachines.SlabMachines;
import com.Mrbysco.SlabMachines.gui.compat.tcon.ContainerCraftingStationSlab;
import com.Mrbysco.SlabMachines.gui.workbench.ContainerWorkbenchSlab;
import com.Mrbysco.SlabMachines.gui.workbench.fast.ContainerFastWorkbenchSlab;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class CraftTweaksCompat {
	public static void register() {
		NBTTagCompound workbench = new NBTTagCompound();
		
		if(SlabMachines.fastBenchLoaded)
			workbench.setString("ContainerClass", ContainerFastWorkbenchSlab.class.getName());
		else
			workbench.setString("ContainerClass", ContainerWorkbenchSlab.class.getName());
		
		workbench.setString("AlignToGrid", "left");
		FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", workbench);
		
		if(SlabMachines.tinkersLoaded)
		{
			NBTTagCompound tinkers = new NBTTagCompound();
			tinkers.setString("ContainerClass", ContainerCraftingStationSlab.class.getName());
			tinkers.setString("AlignToGrid", "left");
			FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", tinkers);
		}
	}
}
