package com.mrbysco.slabmachines.blocks.base.enums;

import net.minecraft.util.StringRepresentable;

public enum CustomSlabType implements StringRepresentable {
	TOP("top"),
	BOTTOM("bottom");

	private final String name;

	CustomSlabType(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	public String getSerializedName() {
		return this.name;
	}
}