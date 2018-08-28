package com.Mrbysco.SlabMachines.compat.mcmp;

import mcmultipart.api.multipart.IMultipartTile;
import net.minecraft.tileentity.TileEntity;

public class SlabPartTile implements IMultipartTile {

	private TileEntity tile;

	public SlabPartTile(TileEntity tile) {
		this.tile = tile;
	}

	@Override
	public TileEntity getTileEntity() {
		return tile;
	}
}
