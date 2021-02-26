package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blocks.base.CustomSlabBlock;
import com.mrbysco.slabmachines.container.SlabBenchContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class CraftingTableSlabBlock extends CustomSlabBlock {
	private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent(SlabReference.MOD_PREFIX + "container.crafting");

	public CraftingTableSlabBlock(Properties properties) {
		super(properties.hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD).harvestTool(ToolType.AXE).harvestLevel(0));
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote) {
			return ActionResultType.SUCCESS;
		} else {
			player.openContainer(state.getContainer(worldIn, pos));
			player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
			return ActionResultType.CONSUME;
		}
	}

	public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
		return new SimpleNamedContainerProvider((id, inventory, player) -> {
			return new SlabBenchContainer(id, inventory, IWorldPosCallable.of(worldIn, pos));
		}, CONTAINER_NAME);
	}
}
