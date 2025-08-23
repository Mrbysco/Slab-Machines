package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blocks.base.CustomSlabBlock;
import com.mrbysco.slabmachines.menu.SlabCartographyTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CartographyTableSlabBlock extends CustomSlabBlock {
	private static final Component CONTAINER_NAME = Component.translatable(SlabReference.MOD_PREFIX + "container.cartography");

	public CartographyTableSlabBlock(Properties properties) {
		super(properties.strength(2.0F, 5.0F).sound(SoundType.WOOD));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			player.openMenu(state.getMenuProvider(level, pos));
			player.awardStat(Stats.INTERACT_WITH_CARTOGRAPHY_TABLE);
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		return new SimpleMenuProvider(
				(id, inventory, player) ->
						new SlabCartographyTableMenu(id, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_NAME
		);
	}
}
