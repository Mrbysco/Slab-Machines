package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.blocks.base.CustomSlabBlock;
import com.mrbysco.slabmachines.entity.TNTSlabEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class TNTSlabBlock extends CustomSlabBlock {
    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
    public static final BooleanProperty ETHOSLAB = BooleanProperty.create("etho");

    public TNTSlabBlock(Properties properties) {
        super(properties.instabreak().sound(SoundType.GRASS).harvestTool(ToolType.AXE).harvestLevel(0));
        this.registerDefaultState(this.defaultBlockState().setValue(UNSTABLE, Boolean.valueOf(false)).setValue(ETHOSLAB, Boolean.valueOf(false)));
    }

    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable net.minecraft.util.Direction face, @Nullable LivingEntity igniter) {
        explode(world, pos, igniter);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        ItemStack stack = context.getItemInHand();
        return super.getStateForPlacement(context).setValue(ETHOSLAB, Boolean.valueOf(stack.getHoverName().getContents().equalsIgnoreCase("Etho slab")));
    }

    @Override
    public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (worldIn.hasNeighborSignal(pos)) {
                catchFire(state, worldIn, pos, null, null);
                worldIn.removeBlock(pos, false);
            }
        }
    }

    public boolean isEthoSlab(World worldIn, BlockPos pos) {
        BlockState partState = worldIn.getBlockState(pos);
        boolean etho = false;
        if(partState.getProperties().contains(ETHOSLAB)) {
            etho = partState.getValue(ETHOSLAB);
        }
        return etho;
    }

    public void wasExploded(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isClientSide) {
            TNTSlabEntity tntentity = new TNTSlabEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, explosionIn.getSourceMob(), isEthoSlab(worldIn, pos));
            tntentity.setFuse((short)(worldIn.random.nextInt(tntentity.getLife() / 4) + tntentity.getLife() / 8));
            worldIn.addFreshEntity(tntentity);
        }
    }

    public void explode(World worldIn, BlockPos pos, LivingEntity igniter) {
        if (!worldIn.isClientSide) {
            TNTSlabEntity tntentity = new TNTSlabEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, igniter, isEthoSlab(worldIn, pos));
            worldIn.addFreshEntity(tntentity);
            worldIn.playSound((PlayerEntity)null, tntentity.getX(), tntentity.getY(), tntentity.getZ(), SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED, UNSTABLE, ETHOSLAB);
    }

    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getItemInHand(handIn);
        Item item = itemstack.getItem();
        if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.use(state, worldIn, pos, player, handIn, hit);
        } else {
            catchFire(state, worldIn, pos, hit.getDirection(), player);
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            if (!player.isCreative()) {
                if (item == Items.FLINT_AND_STEEL) {
                    itemstack.hurtAndBreak(1, player, (player1) -> {
                        player1.broadcastBreakEvent(handIn);
                    });
                } else {
                    itemstack.shrink(1);
                }
            }

            return ActionResultType.sidedSuccess(worldIn.isClientSide);
        }
    }

    public void onProjectileHit(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
        if (!worldIn.isClientSide) {
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire()) {
                BlockPos blockpos = hit.getBlockPos();
                catchFire(state, worldIn, blockpos, null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                worldIn.removeBlock(blockpos, false);
            }
        }

    }

	@Override
	public boolean dropFromExplosion(Explosion explosionIn) {
		return false;
	}
	
	@Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.hasNeighborSignal(pos)) {
            catchFire(state, worldIn, pos, null, null);
            worldIn.removeBlock(pos, false);
        }
    }
	
	@Override
	public BlockRenderType getRenderShape(BlockState state)
    {
        return BlockRenderType.MODEL;
    }
}
