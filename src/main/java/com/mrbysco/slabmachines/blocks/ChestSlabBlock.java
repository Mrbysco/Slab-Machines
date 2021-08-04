package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.blocks.base.FacingMultiSlabBlock;
import com.mrbysco.slabmachines.tileentity.TileChestSlab;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class ChestSlabBlock extends FacingMultiSlabBlock {
    public ChestSlabBlock(Properties properties) {
		super(properties.strength(2.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE).harvestLevel(0));
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isClientSide) {
			return ActionResultType.SUCCESS;
		} else {
			INamedContainerProvider inamedcontainerprovider = this.getMenuProvider(state, worldIn, pos);
			if (inamedcontainerprovider != null) {
				player.openMenu(inamedcontainerprovider);
				player.awardStat(this.getOpenStat());
				PiglinTasks.angerNearbyPiglins(player, true);
			}

			return ActionResultType.CONSUME;
		}
	}

	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState state, World worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider)tileentity : null;
	}

	protected Stat<ResourceLocation> getOpenStat() {
		return Stats.CUSTOM.get(Stats.OPEN_CHEST);
	}

	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof IInventory) {
				InventoryHelper.dropContents(worldIn, pos, (IInventory)tileentity);
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileChestSlab();
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, World worldIn, BlockPos pos) {
		return Container.getRedstoneSignalFromBlockEntity(worldIn.getBlockEntity(pos));
	}
}
