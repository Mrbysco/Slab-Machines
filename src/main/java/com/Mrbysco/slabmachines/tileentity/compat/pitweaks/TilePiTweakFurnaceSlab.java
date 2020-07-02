package com.mrbysco.slabmachines.tileentity.compat.pitweaks;

import com.mrbysco.slabmachines.tileentity.furnace.TileFurnaceSlab;
import net.minecraft.item.ItemStack;

public class TilePiTweakFurnaceSlab extends TileFurnaceSlab {
	@Override
    public int getCookTime(ItemStack stack) {
        return 200 / us.bemrose.mc.pitweaks.TweakConfig.furnace.multiplier;
    }

    // Decrement burn time by multiplier ticks (minus one because TileEntityFurnace.update() decrements one)
    public void update()
    {
        final int furnaceBurnTime_Field = 0;

        if (this.isBurning())
        {
            this.setField(furnaceBurnTime_Field, this.getField(furnaceBurnTime_Field) - us.bemrose.mc.pitweaks.TweakConfig.furnace.multiplier + 1);
        }
        super.update();
    }
}
