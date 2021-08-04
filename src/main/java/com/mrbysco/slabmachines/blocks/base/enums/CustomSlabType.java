package com.mrbysco.slabmachines.blocks.base.enums;

import net.minecraft.util.IStringSerializable;

public enum CustomSlabType implements IStringSerializable {
	TOP("top"),
	BOTTOM("bottom");

	private final String name;

	private CustomSlabType(String p_i49332_3_) {
		this.name = p_i49332_3_;
	}

	public String toString() {
		return this.name;
	}

	public String getSerializedName() {
		return this.name;
	}
}