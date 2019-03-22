package com.mrbysco.slabmachines.compat.mcmp;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class SlabPartTileTicking extends SlabPartTile implements ITickable {

	private ITickable tickable;

	public SlabPartTileTicking(TileEntity tile, ITickable tickable) {
		super(tile);
		this.tickable = tickable;
	}

	@Override
	public void update() {
		tickable.update();
	}
}