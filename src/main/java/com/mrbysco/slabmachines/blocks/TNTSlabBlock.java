package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.blocks.base.CustomSlabBlock;
import com.mrbysco.slabmachines.entity.TNTSlabEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class TNTSlabBlock extends CustomSlabBlock {
	public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
	public static final BooleanProperty ETHOSLAB = BooleanProperty.create("etho");

	public TNTSlabBlock(Properties properties) {
		super(properties.instabreak().sound(SoundType.GRASS));
		this.registerDefaultState(this.defaultBlockState().setValue(UNSTABLE, Boolean.valueOf(false)).setValue(ETHOSLAB, Boolean.valueOf(false)));
	}

	public void catchFire(BlockState state, Level world, BlockPos pos, @Nullable net.minecraft.core.Direction face, @Nullable LivingEntity igniter) {
		explode(world, pos, igniter);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		ItemStack stack = context.getItemInHand();
		return super.getStateForPlacement(context).setValue(ETHOSLAB, Boolean.valueOf(stack.getHoverName().getContents().equalsIgnoreCase("Etho slab")));
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!oldState.is(state.getBlock())) {
			if (level.hasNeighborSignal(pos)) {
				catchFire(state, level, pos, null, null);
				level.removeBlock(pos, false);
			}
		}
	}

	public boolean isEthoSlab(Level level, BlockPos pos) {
		BlockState partState = level.getBlockState(pos);
		boolean etho = false;
		if (partState.getProperties().contains(ETHOSLAB)) {
			etho = partState.getValue(ETHOSLAB);
		}
		return etho;
	}

	public void wasExploded(Level level, BlockPos pos, Explosion explosionIn) {
		if (!level.isClientSide) {
			TNTSlabEntity tntentity = new TNTSlabEntity(level, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, explosionIn.getSourceMob(), isEthoSlab(level, pos));
			tntentity.setFuse((short) (level.random.nextInt(tntentity.getFuse() / 4) + tntentity.getFuse() / 8));
			level.addFreshEntity(tntentity);
		}
	}

	public void explode(Level level, BlockPos pos, LivingEntity igniter) {
		if (!level.isClientSide) {
			TNTSlabEntity tntentity = new TNTSlabEntity(level, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, igniter, isEthoSlab(level, pos));
			level.addFreshEntity(tntentity);
			level.playSound((Player) null, tntentity.getX(), tntentity.getY(), tntentity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(TYPE, WATERLOGGED, UNSTABLE, ETHOSLAB);
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		ItemStack itemstack = player.getItemInHand(handIn);
		Item item = itemstack.getItem();
		if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
			return super.use(state, level, pos, player, handIn, hit);
		} else {
			catchFire(state, level, pos, hit.getDirection(), player);
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
			if (!player.isCreative()) {
				if (item == Items.FLINT_AND_STEEL) {
					itemstack.hurtAndBreak(1, player, (player1) -> {
						player1.broadcastBreakEvent(handIn);
					});
				} else {
					itemstack.shrink(1);
				}
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
		if (!level.isClientSide) {
			Entity entity = projectile.getOwner();
			if (projectile.isOnFire()) {
				BlockPos blockpos = hit.getBlockPos();
				catchFire(state, level, blockpos, null, entity instanceof LivingEntity ? (LivingEntity) entity : null);
				level.removeBlock(blockpos, false);
			}
		}

	}

	@Override
	public boolean dropFromExplosion(Explosion explosionIn) {
		return false;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (level.hasNeighborSignal(pos)) {
			catchFire(state, level, pos, null, null);
			level.removeBlock(pos, false);
		}
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
}
