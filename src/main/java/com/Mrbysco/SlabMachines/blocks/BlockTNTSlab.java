package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.SlabMachines;
import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.entity.EntityTNTPrimeSlab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNTSlab extends BlockSlab{
	public static final PropertyEnum<BlockTNTSlab.Variant> VARIANT = PropertyEnum.<BlockTNTSlab.Variant>create("variant", BlockTNTSlab.Variant.class);
    public static final PropertyBool EXPLODE = PropertyBool.create("explode");
    public static final PropertyBool ETHOSLAB = PropertyBool.create("etho");

    public BlockTNTSlab() {
		super(Material.TNT);
		
		this.setUnlocalizedName(SlabReference.MOD_PREFIX + "tnt_slab".replaceAll("_", ""));
		this.setRegistryName("tnt_slab");
		this.setHardness(0.0F);
	    this.setSoundType(SoundType.PLANT);
		
		useNeighborBrightness = true;
		setCreativeTab(SlabMachines.slabTab);
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockTNTSlab.Variant.DEFAULT)
				.withProperty(HALF, EnumBlockHalf.BOTTOM).withProperty(EXPLODE, false).withProperty(ETHOSLAB, false));
	    this.setHarvestLevel("axe", 0);
	}
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
    		float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    	ItemStack stack = placer.getHeldItem(hand);
    	if(stack.getItem() == Item.getItemFromBlock(this))
    	{
        	return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(ETHOSLAB, Boolean.valueOf(stack.getDisplayName().toLowerCase().equals("Etho slab".toLowerCase())));
    	}

    	return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }
    
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    	super.onBlockAdded(worldIn, pos, state);

        if (worldIn.isBlockPowered(pos))
        {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(EXPLODE, Boolean.valueOf(true)));
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
    	if (!world.isRemote)
        {
    		IBlockState state = world.getBlockState(pos);
    		boolean etho = false;
    		if(state.getProperties().containsKey(ETHOSLAB))
    		{
    			etho = state.getValue(ETHOSLAB);
    		}
    		EntityTNTPrimeSlab slabtntprimed = new EntityTNTPrimeSlab(world, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), explosion.getExplosivePlacedBy(), etho);
    		slabtntprimed.setFuse((short)(world.rand.nextInt(slabtntprimed.getFuse() / 4) + slabtntprimed.getFuse() / 8));
    		world.spawnEntity(slabtntprimed);
        }
    	
    	super.onBlockExploded(world, pos, explosion);
    }
    
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        this.explode(worldIn, pos, state, (EntityLivingBase)null);
    }
    
    public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter)
    {
        if (!worldIn.isRemote)
        {
            if (((Boolean)state.getValue(EXPLODE)).booleanValue())
            {
            	boolean etho = false;
        		if(state.getProperties().containsKey(ETHOSLAB))
        		{
        			etho = state.getValue(ETHOSLAB);
        		}
        		
        		EntityTNTPrimeSlab slabtntprimed = new EntityTNTPrimeSlab(worldIn, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), igniter, etho);
                worldIn.spawnEntity(slabtntprimed);
                worldIn.playSound((EntityPlayer)null, slabtntprimed.posX, slabtntprimed.posY, slabtntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
    
	@Override
	public String getUnlocalizedName(int meta) {
		return this.getUnlocalizedName();
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return VARIANT;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
        return BlockTNTSlab.Variant.DEFAULT;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
    {
		return getDefaultState().withProperty(HALF, EnumBlockHalf.values()[meta]);
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
		return state.getValue(HALF).ordinal();
    }
	
	@Override
    protected BlockStateContainer createBlockState()
    {
		return new BlockStateContainer(this, VARIANT, HALF, EXPLODE, ETHOSLAB);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = playerIn.getHeldItem(hand);

        if (!itemstack.isEmpty() && (itemstack.getItem() == Items.FLINT_AND_STEEL || itemstack.getItem() == Items.FIRE_CHARGE))
        {
            this.explode(worldIn, pos, state.withProperty(EXPLODE, Boolean.valueOf(true)), playerIn);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

            if (itemstack.getItem() == Items.FLINT_AND_STEEL)
            {
                itemstack.damageItem(1, playerIn);
            }
            else if (!playerIn.capabilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }

            return true;
        }
        else
        {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote && entityIn instanceof EntityArrow)
        {
            EntityArrow entityarrow = (EntityArrow)entityIn;

            if (entityarrow.isBurning())
            {
                this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(EXPLODE, Boolean.valueOf(true)), entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
                worldIn.setBlockToAir(pos);
            }
        }
	}
	
	@Override
	public boolean canDropFromExplosion(Explosion explosionIn) {
		return false;
	}
	
	public static enum Variant implements IStringSerializable
    {
        DEFAULT;

        public String getName()
        {
            return "default";
        }
    }
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (worldIn.isBlockPowered(pos))
        {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(EXPLODE, Boolean.valueOf(true)));
            worldIn.setBlockToAir(pos);
        }
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
	public boolean isTopSolid(IBlockState state) {
		return state.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP;	
	}
}
