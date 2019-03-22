package com.mrbysco.slabmachines.blocks.compat.tinkers;

import java.util.List;

import javax.annotation.Nonnull;

import com.mrbysco.slabmachines.SlabMachines;
import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.gui.SlabGuiHandler;
import com.mrbysco.slabmachines.tileentity.compat.tinkers.TilePartBuilderSlab;
import com.mrbysco.slabmachines.utils.SlabUtil;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.inventory.BaseContainer;
import slimeknights.mantle.property.PropertyUnlistedDirection;
import slimeknights.mantle.tileentity.TileInventory;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.shared.block.PropertyTableItem;
import slimeknights.tconstruct.shared.tileentity.TileTable;
import slimeknights.tconstruct.tools.common.block.ITinkerStationBlock;

public class BlockPartBuilderSlab extends BlockSlab implements ITinkerStationBlock {
	public static final PropertyEnum<BlockPartBuilderSlab.Variant> VARIANT = PropertyEnum.<BlockPartBuilderSlab.Variant>create("variant", BlockPartBuilderSlab.Variant.class);
	public static final PropertyTableItem INVENTORY = new PropertyTableItem();
	public static final PropertyDirection FACING = new PropertyUnlistedDirection("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockPartBuilderSlab() {
		super(Material.WOOD);
		
		this.setUnlocalizedName(SlabReference.MOD_PREFIX + "part_builder_slab".replaceAll("_", ""));
		this.setRegistryName("part_builder_slab");
		this.setHardness(2.0F);
		this.setResistance(5.0F);
	    this.setSoundType(SoundType.WOOD);
		useNeighborBrightness = true;
		setCreativeTab(SlabMachines.slabTab);
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockPartBuilderSlab.Variant.DEFAULT).withProperty(HALF, EnumBlockHalf.BOTTOM).withProperty(FACING, EnumFacing.NORTH));
	    this.setHarvestLevel("axe", 0);
	}
    
    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
      return BlockRenderLayer.CUTOUT;
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
        return BlockPartBuilderSlab.Variant.DEFAULT;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
    {
		EnumFacing facing = EnumFacing.VALUES[meta & 0b0111];
		if (!EnumFacing.Plane.HORIZONTAL.apply(facing)) facing = EnumFacing.NORTH;
		boolean top = meta >> 3 == 1;
		return getDefaultState()
				.withProperty(FACING, facing)
				.withProperty(HALF, top ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
	}

	@Override
    public int getMetaFromState(IBlockState state)
    {
		int meta = state.getValue(FACING).ordinal();
		if (state.getValue(HALF) == EnumBlockHalf.TOP) {
			meta |= 0b1000;
		}
		return meta;
    }
	
	@Override
    protected BlockStateContainer createBlockState()
    {
	    return new ExtendedBlockState(this, new IProperty[] {VARIANT, HALF, FACING}, new IUnlistedProperty[]{INVENTORY});
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		NBTTagCompound tag = TagUtil.getTagSafe(stack);
		TilePartBuilderSlab te = SlabUtil.getTileSlab(worldIn, pos, state.getValue(HALF), TilePartBuilderSlab.class);

	    if(te != null) {
		    te.setFacing(placer.getHorizontalFacing().getOpposite());
		    te.setHalf(state.getValue(BlockSlab.HALF));
		    
		      // check if we also have an inventory
		    if(tag.hasKey("inventory")) {
		    	te.readInventoryFromNBT(tag.getCompoundTag("inventory"));
		    }
	
		      // check if we have a custom name
		    if (stack.hasDisplayName()) {
		    	te.setCustomName(stack.getDisplayName());
		    }
	    }
	}
	
	@Override
	public boolean openGui(EntityPlayer player, World world, BlockPos pos) {
		int id = world.getBlockState(pos).getValue(HALF) == EnumBlockHalf.TOP ? SlabGuiHandler.GUI_PARTBUILDER_SLAB_TOP : SlabGuiHandler.GUI_PARTBUILDER_SLAB_BOTTOM;
		player.openGui(SlabMachines.instance, id, world, pos.getX(), pos.getY(), pos.getZ());
        if(player.openContainer instanceof BaseContainer) {
          ((BaseContainer) player.openContainer).syncOnOpen((EntityPlayerMP) player);
        }
	    return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if(playerIn.isSneaking()) {
		      return false;
	    }

	    if(!worldIn.isRemote) {
	    	this.openGui(playerIn, worldIn, pos);
	    }

		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
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
	public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePartBuilderSlab();
	}

	@Override
	public int getGuiNumber(IBlockState state) {
        return 20;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedState = (IExtendedBlockState) state;

		TilePartBuilderSlab te = SlabUtil.getTileSlab(world, pos, state.getValue(HALF), TilePartBuilderSlab.class);
	    if(te != null) 
	    {
    		return te.writeExtendedBlockState(extendedState);
	    }

	    return super.getExtendedState(state, world, pos);
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		TilePartBuilderSlab table = SlabUtil.getTileSlab(world, pos, world.getBlockState(pos).getValue(HALF), TilePartBuilderSlab.class);
	    if(table != null) {
	    	table.setFacing(table.getFacing().rotateY());
	    	IBlockState state = world.getBlockState(pos);
	    	world.notifyBlockUpdate(pos, state, state, 0);
	    	return true;
	    }
	    return false;
	}
	
	private void writeDataOntoItemstack(@Nonnull ItemStack item, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean inventorySave) {
	    // get block data from the block
		TilePartBuilderSlab te = SlabUtil.getTileSlab(world, pos, state.getValue(HALF), TilePartBuilderSlab.class);
	    if(te != null) {
	    	TileTable table = (TileTable) te;
	      	NBTTagCompound tag = TagUtil.getTagSafe(item);

	      	// save inventory, if not empty
	      	if(inventorySave && keepInventory()) {
	    	  if (!table.isInventoryEmpty()) {
	        		NBTTagCompound inventoryTag = new NBTTagCompound();
	          		table.writeInventoryToNBT(inventoryTag);
	          		tag.setTag("inventory", inventoryTag);
	          		table.clear();
	        	}
	      	}

	      	if (!tag.hasNoTags()) {
	    	  item.setTagCompound(tag);
	      	}
	    }
	}
	
	@Override
	public void dropBlockAsItemWithChance(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, float chance, int fortune) {
		if(!worldIn.isRemote && !worldIn.restoringBlockSnapshots) {
		    List<ItemStack> items = this.getDrops(worldIn, pos, state, fortune);
		    chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, harvesters.get());
		    
		    for(ItemStack item : items) {
		        // save the data from the block onto the item
		        if(item.getItem() == Item.getItemFromBlock(this)) {
		        	writeDataOntoItemstack(item, worldIn, pos, state, chance >= 1f);
		        }
		    }
		      // also drop inventory contents (that hasn't been written on the TE above)
			TilePartBuilderSlab te = SlabUtil.getTileSlab(worldIn, pos, state.getValue(HALF), TilePartBuilderSlab.class);
		    if(te != null && te instanceof TileInventory) {
		        TileInventory tileInventory = (TileInventory) te;
	
		        for(int i = 0; i < tileInventory.getSizeInventory(); i++) {
		        	ItemStack itemStack = tileInventory.getStackInSlot(i);
		        	if(!itemStack.isEmpty()) {
		        		items.add(itemStack);
		        	}
		        }
		        // clear since otherwise we might dupe
		        tileInventory.clear();
	
		        // if the TE had a custom name, let's propagate that to the ItemStack
		        if (tileInventory.hasCustomName()) {
		        	for(ItemStack item : items) {
		        		if(item.getItem() == Item.getItemFromBlock(this)) {
		        			item.setStackDisplayName(tileInventory.getName());
		        			item.setRepairCost(0); // seems to be always added when renaming in anvil, need to restore it here or won't stack
		            	}
		        	}
		        }
		    }
	
		    for(ItemStack item : items) {
		        if(worldIn.rand.nextFloat() <= chance) {
		        	spawnAsEntity(worldIn, pos, item);
		        }
		    }
	    }
	}
	
	@Override
	public boolean removedByPlayer(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
	    // we pull up a few calls to this point in time because we still have the TE here
	    // the execution otherwise is equivalent to vanilla order
	    this.onBlockDestroyedByPlayer(world, pos, state);
	    if(willHarvest) {
	    	this.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());
	    }

	    // clear the inventory if we kept it on the item
	    // otherwise we'd dupe it since it'd also spill when we set the block to air
		TilePartBuilderSlab te = SlabUtil.getTileSlab(world, pos, state.getValue(HALF), TilePartBuilderSlab.class);
    	if(te != null && te instanceof TileInventory) {
    		((TileInventory) te).clear();
    	}

	    world.setBlockToAir(pos);
	    // return false to prevent the above called functions to be called again
	    // side effect of this is that no xp will be dropped. but it shoudln't anyway from a table :P
	    return false;
	}
	
	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
		List<ItemStack> drops = getDrops(world, pos, world.getBlockState(pos), 0);
	    if(drops.size() > 0) {
	    	ItemStack stack = drops.get(0);
	    	writeDataOntoItemstack(stack, world, pos, state, false);
	    	return stack;
	    }
	
	    return super.getPickBlock(state, target, world, pos, player);
	}
	
	private boolean keepInventory() {
	    return false;
	}
}