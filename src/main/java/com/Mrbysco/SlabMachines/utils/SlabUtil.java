package com.mrbysco.slabmachines.utils;

import java.util.Optional;

import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import mcmultipart.util.MCMPWorldWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class SlabUtil {
	@net.minecraftforge.fml.common.Optional.Method(modid = "mcmultipart")
	public static <TE extends TileEntity> TE detectSlabTileentity(World world, BlockPos pos, Class<TE> clazz) {
		World worldIn = world;
		if(world instanceof MCMPWorldWrapper)
		{
			worldIn = ((MCMPWorldWrapper)world).getActualWorld();
		}
		
		java.util.Optional<IMultipartTile> tile = MultipartHelper.getPartTile(worldIn, pos, EnumFaceSlot.UP);
		java.util.Optional<IMultipartTile> tile2 = MultipartHelper.getPartTile(worldIn, pos, EnumFaceSlot.DOWN);

        if(tile.isPresent())
        {
        	IMultipartTile theTile = tile.orElse(null);

        	if(theTile != null)
        	{
        		TileEntity te = theTile.getTileEntity();
            	
            	if(te != null && clazz.isAssignableFrom(te.getClass())) {
          	      return (TE) te;
          	    }
        	}
        }
        else if(tile2.isPresent())
        {
        	IMultipartTile theTile = tile2.orElse(null);
        	
        	if(theTile != null)
        	{
        		TileEntity te = theTile.getTileEntity();
            	
            	if(te != null && clazz.isAssignableFrom(te.getClass())) {
          	      return (TE) te;
          	    }
        	}
        }
        
		return null;
	}
	
	@net.minecraftforge.fml.common.Optional.Method(modid = "mcmultipart")
	public static <TE extends TileEntity> TE detectLeveledSlab(World world, BlockPos pos, Class<TE> clazz, EnumBlockHalf half) {
		World worldIn = world;
		if(world instanceof MCMPWorldWrapper)
		{
			worldIn = ((MCMPWorldWrapper)world).getActualWorld();
		}
		EnumFaceSlot slot = half == EnumBlockHalf.BOTTOM ? EnumFaceSlot.DOWN : EnumFaceSlot.UP;
		
		java.util.Optional<IMultipartTile> tile = MultipartHelper.getPartTile(worldIn, pos, slot);

        if(tile.isPresent())
        {
        	IMultipartTile theTile = tile.orElse(null);
        	
        	if(theTile != null)
        	{
        		TileEntity te = theTile.getTileEntity();
            	
            	if(te != null && clazz.isAssignableFrom(te.getClass())) {
          	      return (TE) te;
          	    }
        	}
        }
        
		return null;
	}
	
	public static <TE extends TileEntity> TE getTileSlab(IBlockAccess world, BlockPos pos, BlockSlab.EnumBlockHalf half, Class<TE> clazz) {
		TileEntity te = world.getTileEntity(pos);
		if(te != null && clazz.isAssignableFrom(te.getClass())) {
			return (TE) te;
    	}
		else 
		{
			if (Loader.isModLoaded("mcmultipart")) 
				return getPartTileSlab(world, pos, half, clazz);
		}
		return null;
	}
	
	@net.minecraftforge.fml.common.Optional.Method(modid = "mcmultipart")
	private static <TE extends TileEntity> TE getPartTileSlab(IBlockAccess world, BlockPos pos, BlockSlab.EnumBlockHalf half, Class<TE> clazz) {
		IPartSlot slot = half == BlockSlab.EnumBlockHalf.TOP ? EnumFaceSlot.UP : EnumFaceSlot.DOWN;
		Optional<IMultipartTile> tile = MultipartHelper.getPartTile(world, pos, slot);
		if(tile.isPresent())
		{
			TileEntity te = tile.get().getTileEntity();
			if(te != null && clazz.isAssignableFrom(te.getClass()))
			{
				return (TE) te;
			}
		}
		return null;
	}
	
	public static IBlockState getStateSlab(IBlockAccess world, BlockPos pos, Block block) {
		IBlockState state = world.getBlockState(pos);
		if(state != null && state.getBlock() == block){
			return state;
    	}
		else 
		{
			if (Loader.isModLoaded("mcmultipart")) 
				return getPartStateSlab(world, pos, block);
		}
		return null;
	}
	
	@net.minecraftforge.fml.common.Optional.Method(modid = "mcmultipart")
	public static IBlockState getPartStateSlab(IBlockAccess world, BlockPos pos, Block block) {
		Optional<IBlockState> down = MultipartHelper.getPartState(world, pos, EnumFaceSlot.DOWN);
		if (down.isPresent() && down.get().getBlock() == block) 
		{
			return down.get();
		} 
		else 
		{
			Optional<IBlockState> up = MultipartHelper.getPartState(world, pos, EnumFaceSlot.UP);
			if (up.isPresent() && up.get().getBlock() == block) {
				return up.get();
			}
		}	
		return null;
	}
}
