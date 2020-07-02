package com.mrbysco.slabmachines.blocks;

import com.mrbysco.slabmachines.SlabMachines;
import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.gui.SlabGuiHandler;
import com.mrbysco.slabmachines.tileentity.compat.pitweaks.TilePiTweakFurnaceSlab;
import com.mrbysco.slabmachines.tileentity.furnace.TileFurnaceSlab;
import com.mrbysco.slabmachines.tileentity.furnace.compat.TileFastFurnaceSlab;
import com.mrbysco.slabmachines.utils.SlabUtil;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFurnaceSlab extends BlockSlab {
	public static final PropertyEnum<BlockFurnaceSlab.Variant> VARIANT = PropertyEnum.<BlockFurnaceSlab.Variant>create("variant", BlockFurnaceSlab.Variant.class);
	public static final PropertyBool BURNING = PropertyBool.create("burning");
	private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockFurnaceSlab() {
		super(Material.ROCK);
		this.setTranslationKey(SlabReference.MOD_PREFIX + "furnace_slab".replaceAll("_", ""));
		this.setRegistryName("furnace_slab");
		this.setHardness(2.0F);
		this.setResistance(10.0F);
	    this.setSoundType(SoundType.STONE);
		
		useNeighborBrightness = true;
		setCreativeTab(SlabMachines.slabTab);

		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockFurnaceSlab.Variant.DEFAULT)
				.withProperty(HALF, EnumBlockHalf.BOTTOM).withProperty(BURNING, false).withProperty(FACING, EnumFacing.NORTH));
	    this.setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
			int id = state.getValue(HALF) == EnumBlockHalf.TOP ? SlabGuiHandler.GUI_FURNACE_SLAB_TOP : SlabGuiHandler.GUI_FURNACE_SLAB_BOTTOM;

        	if(Loader.isModLoaded("fastfurnace"))
			{
    			id = state.getValue(HALF) == EnumBlockHalf.TOP ? SlabGuiHandler.GUI_FASTFURNACE_SLAB_TOP : SlabGuiHandler.GUI_FASTFURNACE_SLAB_BOTTOM;
			}
			playerIn.openGui(SlabMachines.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
            playerIn.addStat(StatList.FURNACE_INTERACTION);

            return true;
        }
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!worldIn.isRemote) {
			TileFurnaceSlab te = SlabUtil.getTileSlab(worldIn, pos, state.getValue(BlockSlab.HALF), TileFurnaceSlab.class);
			if(te != null)
			{
				InventoryHelper.dropInventoryItems(worldIn, pos, te);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileFurnaceSlab te = SlabUtil.getTileSlab(worldIn, pos, stateIn.getValue(BlockSlab.HALF), TileFurnaceSlab.class);
		if(te != null && te.isBurning()) {
			EnumFacing enumfacing = stateIn.getValue(FACING);
			double d0 = (double)pos.getX() + 0.5D;
			double y = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			if (stateIn.getValue(HALF) == EnumBlockHalf.TOP) y += 0.5;
			double d2 = (double)pos.getZ() + 0.5D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;

			if (rand.nextDouble() < 0.1D)
			{
				worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
			
			switch (enumfacing)
			{
				case WEST:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, y, d2 + d4, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, y, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case EAST:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, y, d2 + d4, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52D, y, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case NORTH:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, y, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, y, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
					break;
				case SOUTH:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, y, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, y, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstone(SlabUtil.getTileSlab(worldIn, pos, blockState.getValue(BlockSlab.HALF), TileFurnaceSlab.class));
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
        return BlockFurnaceSlab.Variant.DEFAULT;
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public String getTranslationKey(int meta) {
		return this.getTranslationKey();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return VARIANT;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT, HALF, BURNING, FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(FACING).ordinal();
		if (state.getValue(HALF) == EnumBlockHalf.TOP) {
			meta |= 0b1000;
		}
		return meta;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.VALUES[meta & 0b0111];
		if (!EnumFacing.Plane.HORIZONTAL.apply(facing)) facing = EnumFacing.NORTH;
		boolean top = meta >> 3 == 1;
		return getDefaultState()
				.withProperty(FACING, facing)
				.withProperty(HALF, top ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntityFurnace te = SlabUtil.getTileSlab(worldIn, pos, state.getValue(HALF), TileFurnaceSlab.class);
		return te != null ? state.withProperty(BURNING, te.isBurning()) : state;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		if(Loader.isModLoaded("fastfurnace"))
			return new TileFastFurnaceSlab();
		else
		{
			if(Loader.isModLoaded("pitweaks"))
				return new TilePiTweakFurnaceSlab();
			else
				return new TileFurnaceSlab();
		}
	}
	
	public static enum Variant implements IStringSerializable
    {
        DEFAULT;

        public String getName()
        {
            return "default";
        }
    }
}
