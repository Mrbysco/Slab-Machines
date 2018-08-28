package com.Mrbysco.SlabMachines.packets;

import com.Mrbysco.SlabMachines.blocks.BlockFurnaceSlab;
import com.Mrbysco.SlabMachines.init.SlabBlocks;
import com.Mrbysco.SlabMachines.tileentity.furnace.TileFurnaceSlab;
import com.Mrbysco.SlabMachines.utils.SlabUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SlabPacketUpdateFurnaceMessage implements IMessage{
	private BlockPos pos;
	private BlockSlab.EnumBlockHalf half;
	private NBTTagCompound tag;
	
	public SlabPacketUpdateFurnaceMessage(TileEntity tile) {
		this.pos = tile.getPos();
		this.half = SlabUtil.getStateSlab(tile.getWorld(), tile.getPos(), SlabBlocks.furnaceSlab).getValue(BlockFurnaceSlab.HALF);
		this.tag = tile.writeToNBT(new NBTTagCompound());
	}
	
	public SlabPacketUpdateFurnaceMessage() {
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.half = buf.readBoolean() ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
		this.tag = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeBoolean(this.half == EnumBlockHalf.TOP);
		ByteBufUtils.writeTag(buf, this.tag);
	}
	
	public static class PacketHandler implements IMessageHandler<SlabPacketUpdateFurnaceMessage, IMessage>
	{
		@Override
		public IMessage onMessage(SlabPacketUpdateFurnaceMessage message, MessageContext ctx) {
			TileFurnaceSlab te = SlabUtil.getTileSlab(Minecraft.getMinecraft().world, message.pos, message.half, TileFurnaceSlab.class);
			if(te != null)
			{
				te.readFromNBT(message.tag);
				Minecraft.getMinecraft().world.markBlockRangeForRenderUpdate(message.pos, message.pos);
			}
		    return null;
		}
	}
}
