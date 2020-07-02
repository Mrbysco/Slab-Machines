package com.mrbysco.slabmachines.tileentity.furnace;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.config.SlabMachineConfigGen;
import com.mrbysco.slabmachines.packets.SlabPacketHandler;
import com.mrbysco.slabmachines.packets.SlabPacketRequestFurnaceSlabUpdate;
import com.mrbysco.slabmachines.packets.SlabPacketUpdateFurnaceMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileFurnaceSlab extends TileEntityFurnace implements ISlabFurnace {
    @Override
	public void onLoad() {
		if (world.isRemote) { SlabPacketHandler.INSTANCE.sendToServer(new SlabPacketRequestFurnaceSlabUpdate(this)); }
	}
    
    @Override
    public int getInventoryStackLimit() {
    	return SlabMachineConfigGen.general.nerfSlabFurnace ? 32 : 64;
    }

    @Override
    public String getName()
    {
        return SlabReference.MOD_PREFIX + "container.furnace";
    }

    @Override
    public void update()
    {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning())
        {
            --this.furnaceBurnTime;
        }

        if (!this.world.isRemote)
        {
            ItemStack itemstack = this.furnaceItemStacks.get(1);

            if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack)this.furnaceItemStacks.get(0)).isEmpty())
            {
                if (!this.isBurning() && this.canSmelt())
                {
                    this.furnaceBurnTime = getItemBurnTime(itemstack);
                    this.currentItemBurnTime = this.furnaceBurnTime;

                    if (this.isBurning())
                    {
                        flag1 = true;

                        if (!itemstack.isEmpty())
                        {
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);

                            if (itemstack.isEmpty())
                            {
                                ItemStack item1 = item.getContainerItem(itemstack);
                                this.furnaceItemStacks.set(1, item1);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.cookTime = 0;
                }
            }
            else if (!this.isBurning() && this.cookTime > 0)
            {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (flag != this.isBurning())
            {
                flag1 = true;
                NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
				SlabPacketHandler.INSTANCE.sendToAllAround(new SlabPacketUpdateFurnaceMessage(this), target);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public String getGuiID() {
        return SlabReference.MOD_PREFIX + "slabfurnace";
    }

    @Override public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) { return oldState.getBlock() != newState.getBlock(); }
}
