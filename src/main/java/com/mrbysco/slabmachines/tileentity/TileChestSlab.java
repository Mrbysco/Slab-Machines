package com.mrbysco.slabmachines.tileentity;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blocks.TrappedChestSlabBlock;
import com.mrbysco.slabmachines.config.SlabConfig;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class TileChestSlab extends ChestTileEntity {
    private int ticksSinceSync;
    public int numPlayersUsing;
    protected float lidAngle;

    public TileChestSlab() {
        super(SlabRegistry.CHEST_SLAB_TILE.get());
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return 27;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit() {
        return SlabConfig.COMMON.slabChestSlotLimit.get();
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent(SlabReference.MOD_PREFIX + "container.chest");
    }

    @Override
    public void tick() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;
        this.numPlayersUsing = calculatePlayersUsingSync(this.world, this, this.ticksSinceSync, i, j, k, this.numPlayersUsing);
        this.prevLidAngle = this.lidAngle;
        float f = 0.1F;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f1 = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                this.lidAngle += 0.1F;
            } else {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;
            if (this.lidAngle < 0.5F && f1 >= 0.5F) {
                this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    private void playSound(SoundEvent soundIn) {
        double d0 = (double)this.pos.getX() + 0.5D;
        double d1 = (double)this.pos.getY() + 0.5D;
        double d2 = (double)this.pos.getZ() + 0.5D;

        this.world.playSound((PlayerEntity)null, d0, d1, d2, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
    }

    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.onOpenOrClose();
        }

    }

    public void closeInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onOpenOrClose();
        }

    }

    protected void onOpenOrClose() {
        Block block = this.getBlockState().getBlock();
        if (block instanceof TrappedChestSlabBlock) {
            this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, block);
        }

    }

    public static int getPlayersUsing(IBlockReader reader, BlockPos posIn) {
        BlockState blockstate = reader.getBlockState(posIn);
        if (blockstate.hasTileEntity()) {
            TileEntity tileentity = reader.getTileEntity(posIn);
            if (tileentity instanceof ChestTileEntity) {
                return ((ChestTileEntity)tileentity).numPlayersUsing;
            }
        }

        return 0;
    }

    public static int calculatePlayersUsingSync(World worldIn, LockableTileEntity tile, int p_213977_2_, int x, int y, int z, int users) {
        if (!worldIn.isRemote && users != 0 && (p_213977_2_ + x + y + z) % 200 == 0) {
            users = calculatePlayersUsing(worldIn, tile, x, y, z);
        }

        return users;
    }

    public static int calculatePlayersUsing(World worldIn, LockableTileEntity tile, int x, int y, int z) {
        int i = 0;
        float f = 5.0F;

        for (PlayerEntity playerentity : worldIn.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB((double) ((float) x - 5.0F), (double) ((float) y - 5.0F), (double) ((float) z - 5.0F), (double) ((float) (x + 1) + 5.0F), (double) ((float) (y + 1) + 5.0F), (double) ((float) (z + 1) + 5.0F)))) {
            if (playerentity.openContainer instanceof ChestContainer) {
                IInventory iinventory = ((ChestContainer) playerentity.openContainer).getLowerChestInventory();
                if (iinventory == tile)
                    ++i;
            }
        }

        return i;
    }
    //    @Override public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) { return oldState.getBlock() != newState.getBlock(); }
}