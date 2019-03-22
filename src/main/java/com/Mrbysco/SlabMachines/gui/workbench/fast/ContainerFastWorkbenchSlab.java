package com.mrbysco.slabmachines.gui.workbench.fast;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shadows.fastbench.gui.ContainerFastBench;

public class ContainerFastWorkbenchSlab extends ContainerFastBench{
    private final BlockPos pos;

	public ContainerFastWorkbenchSlab(EntityPlayer playerIn, World worldIn, BlockPos posIn) {
		super(playerIn, worldIn, posIn);
		this.pos = posIn;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
}
