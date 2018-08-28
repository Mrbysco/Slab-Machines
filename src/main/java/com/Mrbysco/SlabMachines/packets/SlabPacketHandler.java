package com.Mrbysco.SlabMachines.packets;

import com.Mrbysco.SlabMachines.SlabReference;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class SlabPacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SlabReference.MOD_ID);
	
	public static void registerMessages() {
		INSTANCE.registerMessage(SlabPacketUpdateFurnaceMessage.PacketHandler.class, SlabPacketUpdateFurnaceMessage.class, 0, Side.CLIENT);
		INSTANCE.registerMessage(SlabPacketRequestFurnaceSlabUpdate.PacketHandler.class, SlabPacketRequestFurnaceSlabUpdate.class, 1, Side.SERVER);
	}
}
