package com.mrbysco.slabmachines.tileentity.furnace;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.config.SlabConfig;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TileFurnaceSlab extends AbstractFurnaceTileEntity implements ISlabFurnace {

    public TileFurnaceSlab() {
        super(SlabRegistry.FURNACE_SLAB_TILE.get(), IRecipeType.SMELTING);
    }

    @Override
    public int getMaxStackSize() {
    	return SlabConfig.COMMON.slabFurnaceSlotLimit.get();
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent(SlabReference.MOD_PREFIX + "container.furnace");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new FurnaceContainer(id, player, this, this.dataAccess);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

//    @Override public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) { return oldState.getBlock() != newState.getBlock(); }
}
