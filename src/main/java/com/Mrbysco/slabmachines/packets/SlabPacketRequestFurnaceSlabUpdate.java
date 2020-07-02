package com.mrbysco.slabmachines.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SlabPacketRequestFurnaceSlabUpdate implements IMessage{
	private int dimension;
	private BlockPos pos;
	
	public SlabPacketRequestFurnaceSlabUpdate(TileEntity tile) {
		this.dimension = tile.getWorld().provider.getDimension();
		this.pos = tile.getPos();
	}
	
	public SlabPacketRequestFurnaceSlabUpdate() {
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.dimension = buf.readInt();
		this.pos = BlockPos.fromLong(buf.readLong());
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.dimension);
		buf.writeLong(this.pos.toLong());
	}
	
	public static class PacketHandler implements IMessageHandler<SlabPacketRequestFurnaceSlabUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(SlabPacketRequestFurnaceSlabUpdate message, MessageContext ctx) {
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile != null) {
				return new SlabPacketUpdateFurnaceMessage(tile);
			} else {
				return null;
			}
		}
	}
}