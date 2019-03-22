package com.mrbysco.slabmachines.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.mrbysco.slabmachines.SlabMachines;
import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.tileentity.TileNoteSlab;
import com.mrbysco.slabmachines.utils.SlabUtil;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNoteblockSlab extends BlockSlab{
	public static final PropertyEnum<BlockNoteblockSlab.Variant> VARIANT = PropertyEnum.<BlockNoteblockSlab.Variant>create("variant", BlockNoteblockSlab.Variant.class);
    private static final List<SoundEvent> INSTRUMENTS = Lists.newArrayList(SoundEvents.BLOCK_NOTE_HARP, SoundEvents.BLOCK_NOTE_BASEDRUM, SoundEvents.BLOCK_NOTE_SNARE, SoundEvents.BLOCK_NOTE_HAT, SoundEvents.BLOCK_NOTE_BASS, SoundEvents.BLOCK_NOTE_FLUTE, SoundEvents.BLOCK_NOTE_BELL, SoundEvents.BLOCK_NOTE_GUITAR, SoundEvents.BLOCK_NOTE_CHIME, SoundEvents.BLOCK_NOTE_XYLOPHONE);

    public BlockNoteblockSlab() {
		super(Material.WOOD);
		
		this.setUnlocalizedName(SlabReference.MOD_PREFIX + "note_slab".replaceAll("_", ""));
		this.setRegistryName("note_slab");
		this.setHardness(2.5F);
	    this.setSoundType(SoundType.WOOD);
		
		useNeighborBrightness = true;
		setCreativeTab(SlabMachines.slabTab);
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockNoteblockSlab.Variant.DEFAULT).withProperty(HALF, EnumBlockHalf.BOTTOM));
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
        return BlockNoteblockSlab.Variant.DEFAULT;
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
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		if(state.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP)
		{
			TileNoteSlab te = SlabUtil.getTileSlab(worldIn, pos, EnumBlockHalf.TOP, TileNoteSlab.class);
			if(te != null)
			{
				if(te != null)
				{
					te.setMaterialInt(te.getMaterialIntFromBlockState(worldIn.getBlockState(pos.down())));
				}
			}
		}
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
			TileNoteSlab te = SlabUtil.getTileSlab(worldIn, pos, state.getValue(BlockSlab.HALF), TileNoteSlab.class);
            if (te != null)
            {
            	ItemStack stack = playerIn.getHeldItem(hand);
				if(stack.getItem() instanceof ItemBlock && state.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP)
				{
					IBlockState stackState = Block.getBlockFromItem(stack.getItem()).getDefaultState();
					
					if(te.getMaterialInt() != te.getMaterialIntFromBlockState(stackState))
					{
						te.setMaterialInt(te.getMaterialIntFromBlockState(stackState));
					}
				}
				
                int old = te.note;
                te.changePitch();
                if (old == te.note) return false;
                te.triggerNote(worldIn, pos);
                playerIn.addStat(StatList.NOTEBLOCK_TUNED);
            }

            return true;
		}
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		if (!worldIn.isRemote)
        {
			TileNoteSlab te = SlabUtil.getTileSlab(worldIn, pos, worldIn.getBlockState(pos).getValue(BlockSlab.HALF), TileNoteSlab.class);

            if (te != null)
            {
                te.triggerNote(worldIn, pos);
                playerIn.addStat(StatList.NOTEBLOCK_PLAYED);
            }
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
	
	@Override
	public boolean hasCustomBreakingProgress(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileNoteSlab();
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		boolean flag = worldIn.isBlockPowered(pos);
		TileNoteSlab te = SlabUtil.getTileSlab(worldIn, pos, state.getValue(BlockSlab.HALF), TileNoteSlab.class);

        if (te != null)
        {
            if (te.previousRedstoneState != flag)
            {
                if (flag)
                {
                	te.triggerNote(worldIn, pos);
                }

                te.previousRedstoneState = flag;
            }
        }
	}
	
	private SoundEvent getInstrument(int eventId)
    {
        if (eventId < 0 || eventId >= INSTRUMENTS.size())
        {
            eventId = 0;
        }

        return INSTRUMENTS.get(eventId);
    }
	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		net.minecraftforge.event.world.NoteBlockEvent.Play e = new net.minecraftforge.event.world.NoteBlockEvent.Play(worldIn, pos, state, param, id);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(e)) return false;
        id = e.getInstrument().ordinal();
        param = e.getVanillaNoteId();
        float f = (float)Math.pow(2.0D, (double)(param - 12) / 12.0D);
        worldIn.playSound((EntityPlayer)null, pos, this.getInstrument(id), SoundCategory.RECORDS, 3.0F, f);
        worldIn.spawnParticle(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, (double)param / 24.0D, 0.0D, 0.0D);
        return true;
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
