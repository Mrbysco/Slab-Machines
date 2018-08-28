package com.Mrbysco.SlabMachines.init.recipe;

import java.util.function.BooleanSupplier;

import com.Mrbysco.SlabMachines.SlabMachines;
import com.google.gson.JsonObject;

import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class TinkersInstalledRecipeCondition implements IConditionFactory{

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return () -> SlabMachines.tinkersLoaded;
	}
}
