package com.mrbysco.slabmachines.tileentity;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.config.SlabMachineConfigGen;
import com.mrbysco.slabmachines.gui.chest.ContainerChestSlab;
import com.mrbysco.slabmachines.gui.chest.GuiChestSlab;
import com.mrbysco.slabmachines.init.SlabBlocks;
import com.mrbysco.slabmachines.utils.SlabUtil;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileChestSlab extends TileEntityLockableLoot implements ITickable
{
    private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
    /** The number of players currently using this chest */
    public int numPlayersUsing;
    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;
    
    public TileChestSlab()
    {
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 27;
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.chestContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.customName : SlabReference.MOD_PREFIX + "container.chest";
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return SlabMachineConfigGen.general.nerfSlabChest ? 32 : 64;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)i - 5.0F), (double)((float)j - 5.0F), (double)((float)k - 5.0F), (double)((float)(i + 1) + 5.0F), (double)((float)(j + 1) + 5.0F), (double)((float)(k + 1) + 5.0F))))
            {
                if (entityplayer.openContainer instanceof ContainerChest)
                {
                    IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this)
                        ++this.numPlayersUsing;
                }
            }
        }
    }

    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
            
            double d1 = (double)this.pos.getX() + 0.5D;
            double d2 = (double)this.pos.getZ() + 0.5D;
            
        	d1 += 0.5D;
            d2 += 0.5D;
            
            this.world.playSound((EntityPlayer)null, d1, (double)this.pos.getY() + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            
            if (this.isTrappedSlab())
            {
                this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
            }
        }
    }
    
    public boolean isTrappedSlab()
    {
    	if(SlabUtil.getStateSlab(world, pos, SlabBlocks.trappedChestSlab) != null)
    	{
    		return true;
    	}
    	return false;
    }

    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockChest)
        {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
            
            double d3 = (double)this.pos.getX() + 0.5D;
            double d0 = (double)this.pos.getZ() + 0.5D;

            d0 += 0.5D;
            d3 += 0.5D;
            this.world.playSound((EntityPlayer)null, d3, (double)this.pos.getY() + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            
            if (this.isTrappedSlab())
            {
                this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
            }
        }
    }

    public net.minecraftforge.items.IItemHandler getSingleChestHandler()
    {
        return super.getCapability(net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    public String getGuiID()
    {
        return SlabReference.MOD_PREFIX + "slabchest";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    	return new ContainerChestSlab(playerInventory, this, playerIn);
    }
    
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(InventoryPlayer inventoryplayer, World world, BlockPos pos) {
		return new GuiChestSlab(inventoryplayer, this);
	}

    protected NonNullList<ItemStack> getItems()
    {
        return this.chestContents;
    }

    @Override public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) { return oldState.getBlock() != newState.getBlock(); }
}