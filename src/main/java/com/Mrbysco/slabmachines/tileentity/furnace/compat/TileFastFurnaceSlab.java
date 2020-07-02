package com.mrbysco.slabmachines.tileentity.furnace.compat;

import com.mrbysco.slabmachines.packets.SlabPacketHandler;
import com.mrbysco.slabmachines.packets.SlabPacketRequestFurnaceSlabUpdate;
import com.mrbysco.slabmachines.packets.SlabPacketUpdateFurnaceMessage;
import com.mrbysco.slabmachines.tileentity.furnace.TileFurnaceSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ItemStackHolder;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import shadows.fastfurnace.FastFurnace;

import java.util.Iterator;
import java.util.Map.Entry;

public class TileFastFurnaceSlab extends TileFurnaceSlab {
	public static final int INPUT = 0;
	public static final int FUEL = 1;
	public static final int OUTPUT = 2;
	protected ItemStack recipeKey;
	protected ItemStack recipeOutput;
	protected ItemStack failedMatch;
	@ItemStackHolder( value = "minecraft:sponge",  meta = 1)
	public static final ItemStack WET_SPONGE = ItemStack.EMPTY;

	public TileFastFurnaceSlab() {
		this.recipeKey = ItemStack.EMPTY;
		this.recipeOutput = ItemStack.EMPTY;
		this.failedMatch = ItemStack.EMPTY;
		this.totalCookTime = 200;
	}

	@Override
	public void onLoad() {
		if (world.isRemote) { SlabPacketHandler.INSTANCE.sendToServer(new SlabPacketRequestFurnaceSlabUpdate(this)); }
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setInteger("BurnTime", this.furnaceBurnTime);
		compound.setInteger("CookTime", this.cookTime);
		compound.setInteger("CookTimeTotal", this.totalCookTime);
		return compound;
	}

	@Override
	public void update()
	{
		if (this.world.isRemote && this.isBurning()) {
			--this.furnaceBurnTime;
		} else if (!this.world.isRemote) {
			ItemStack fuel = ItemStack.EMPTY;
			boolean canSmelt = this.canSmelt();
			if (!this.isBurning() && !(fuel = (ItemStack)this.furnaceItemStacks.get(FUEL)).isEmpty() && canSmelt) {
				this.burnFuel(fuel, false);
			}

			boolean wasBurning = this.isBurning();
			if (this.isBurning()) {
				--this.furnaceBurnTime;
				if (canSmelt) {
					this.smelt();
				} else {
					this.cookTime = 0;
				}
			}

			if (!this.isBurning()) {
				if (!(fuel = (ItemStack)this.furnaceItemStacks.get(FUEL)).isEmpty()) {
					if (this.canSmelt()) {
						this.burnFuel(fuel, wasBurning);
					}
				} else if (this.cookTime > 0) {
					this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
				}
			}

			if (wasBurning && !isBurning()) {
				NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
				SlabPacketHandler.INSTANCE.sendToAllAround(new SlabPacketUpdateFurnaceMessage(this), target);
			}
		}
	}

	protected void smelt() {
		++this.cookTime;
		if (this.cookTime == this.totalCookTime) {
			this.cookTime = 0;
			this.totalCookTime = this.getCookTime((ItemStack)this.furnaceItemStacks.get(INPUT));
			this.smeltItem();
		}
	}

	protected void burnFuel(ItemStack fuel, boolean burnedThisTick) {
		this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(fuel);
		if (this.isBurning()) {
			Item item = fuel.getItem();
			fuel.shrink(1);
			if (fuel.isEmpty()) {
				this.furnaceItemStacks.set(FUEL, item.getContainerItem(fuel));
			}

			if (!burnedThisTick) {
				NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
				SlabPacketHandler.INSTANCE.sendToAllAround(new SlabPacketUpdateFurnaceMessage(this), target);
			}
		}
	}

	@Override
	protected boolean canSmelt() {
		ItemStack input = (ItemStack)this.furnaceItemStacks.get(0);
		ItemStack output = (ItemStack)this.furnaceItemStacks.get(2);
		if (!input.isEmpty() && input != this.failedMatch) {
			if (this.recipeKey.isEmpty() || !OreDictionary.itemMatches(this.recipeKey, input, false)) {
				boolean matched = false;
				Iterator var4 = FurnaceRecipes.instance().getSmeltingList().entrySet().iterator();

				while(var4.hasNext()) {
					Entry<ItemStack, ItemStack> e = (Entry)var4.next();
					if (OreDictionary.itemMatches((ItemStack)e.getKey(), input, false)) {
						this.recipeKey = (ItemStack)e.getKey();
						this.recipeOutput = (ItemStack)e.getValue();
						matched = true;
						this.failedMatch = ItemStack.EMPTY;
						break;
					}
				}

				if (!matched) {
					this.recipeKey = ItemStack.EMPTY;
					this.recipeOutput = ItemStack.EMPTY;
					this.failedMatch = input;
					return false;
				}
			}

			return !this.recipeOutput.isEmpty() && (output.isEmpty() || this.itemsMatch(this.recipeOutput, output) && this.recipeOutput.getCount() + output.getCount() <= output.getMaxStackSize());
		} else {
			return false;
		}
	}

	@Override
	public void smeltItem() {
		ItemStack input = (ItemStack)this.furnaceItemStacks.get(INPUT);
		ItemStack recipeOutput = (ItemStack)FurnaceRecipes.instance().getSmeltingList().get(recipeKey);
		ItemStack output = (ItemStack)this.furnaceItemStacks.get(OUTPUT);
		if (output.isEmpty()) {
			this.furnaceItemStacks.set(OUTPUT, recipeOutput.copy());
		} else if (this.itemsMatch(output, recipeOutput)) {
			output.grow(recipeOutput.getCount());
		}

		if (input.isItemEqual(WET_SPONGE) && ((ItemStack)this.furnaceItemStacks.get(FUEL)).getItem() == Items.BUCKET) {
			this.furnaceItemStacks.set(FUEL, new ItemStack(Items.WATER_BUCKET));
		}

		input.shrink(1);
	}

	boolean itemsMatch(ItemStack a, ItemStack b) {
		return FastFurnace.useStrictMatching ? ItemHandlerHelper.canItemStacksStack(a, b) : a.isItemEqual(b) && ItemStack.areItemStackTagsEqual(a, b);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) { return oldState.getBlock() != newState.getBlock(); }
}
