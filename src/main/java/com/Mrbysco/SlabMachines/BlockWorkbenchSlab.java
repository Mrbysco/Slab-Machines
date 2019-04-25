package com.mrbysco.slabmachines;

import com.mrbysco.slabmachines.gui.SlabGuiHandler;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class BlockWorkbenchSlab extends BlockSlab{
	public static final PropertyEnum<BlockWorkbenchSlab.Variant> VARIANT = PropertyEnum.<BlockWorkbenchSlab.Variant>create("variant", BlockWorkbenchSlab.Variant.class);
	
    public BlockWorkbenchSlab() {
		super(Material.WOOD);
		
		this.setUnlocalizedName(SlabReference.MOD_PREFIX + "workbench_slab".replaceAll("_", ""));
		this.setRegistryName("workbench_slab");
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		
		useNeighborBrightness = true;
		setCreativeTab(SlabMachines.slabTab);
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockWorkbenchSlab.Variant.DEFAULT).withProperty(HALF, EnumBlockHalf.BOTTOM));
	    this.setHarvestLevel("axe", 0);
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
        return BlockWorkbenchSlab.Variant.DEFAULT;
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
		return new BlockStateContainer(this, VARIANT, HALF);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		}
		else
		{
			int id = state.getValue(HALF) == EnumBlockHalf.TOP ? SlabGuiHandler.GUI_WORKBENCH_SLAB_TOP : SlabGuiHandler.GUI_WORKBENCH_SLAB_BOTTOM;
			if(Loader.isModLoaded("fastfurnace"))
			{
    			id = state.getValue(HALF) == EnumBlockHalf.TOP ? SlabGuiHandler.GUI_FASTWORKBENCH_SLAB_TOP : SlabGuiHandler.GUI_FASTWORKBENCH_SLAB_BOTTOM;
			}
			playerIn.addStat(StatList.CRAFTING_TABLE_INTERACTION);
			playerIn.openGui(SlabMachines.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
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
