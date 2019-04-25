package com.mrbysco.slabmachines.tileentity.compat.tinkers;

import com.mrbysco.slabmachines.blocks.compat.tinkers.BlockCraftingStationSlab;
import com.mrbysco.slabmachines.config.SlabMachineConfigGen;
import com.mrbysco.slabmachines.init.SlabBlocks;
import mcmultipart.api.container.IMultipartContainer;
import mcmultipart.api.container.IPartInfo;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.slot.EnumFaceSlot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;

@SideOnly(Side.CLIENT)
public class TileCraftingStationSlabRenderer extends TileEntitySpecialRenderer<TileCraftingStationSlab> {	
	
	@Override
	public void render(TileCraftingStationSlab te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		IBlockState state = te.getWorld().getBlockState(te.getPos());
		if(SlabMachineConfigGen.modsupport.tconstruct.tinkersTopRendering)
		{
			if(Loader.isModLoaded("mcmultipart"))
			{
				Optional<IMultipartContainer> partInfo = MultipartHelper.getContainer(this.getWorld(), te.getPos());
				if(partInfo.isPresent())
				{
					IMultipartContainer container = partInfo.get();
					if(!container.getParts().isEmpty())
					{
						if(container.getParts().size() > 1)
						{
							for (IPartInfo info : container.getParts().values()) {
								if(info.getSlot() == EnumFaceSlot.UP && info.getState().getBlock() == SlabBlocks.craftingStationSlab && 
										info.getTile().getTileEntity() instanceof TileCraftingStationSlab && 
										info.getState().getProperties().containsKey(BlockCraftingStationSlab.FACING))
								{
									TileCraftingStationSlab tile = (TileCraftingStationSlab) info.getTile().getTileEntity();
									EnumFacing face = info.getState().getValue(BlockCraftingStationSlab.FACING);
									renderAll(tile, x, y, z, partialTicks, destroyStage, alpha, EnumBlockHalf.TOP, face);
								}
							}
						}
						else
						{
							if(state.getProperties().containsKey(BlockSlab.HALF))
							{
								renderAll(te, x, y, z, partialTicks, destroyStage, alpha, state.getValue(BlockSlab.HALF), te.getFacing());
							}			
						}
					}
				}
			}
			else
			{
				if(state.getProperties().containsKey(BlockSlab.HALF))
				{
					renderAll(te, x, y, z, partialTicks, destroyStage, alpha, state.getValue(BlockSlab.HALF), te.getFacing());
				}
			}
		}
	}

	private void renderAll(TileCraftingStationSlab te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, EnumBlockHalf side, EnumFacing facing)
	{
		GlStateManager.pushMatrix();
        GlStateManager.translate(x+0.5,y+0.95,z+0.5);
        if(side == EnumBlockHalf.BOTTOM)
        {
	        GlStateManager.translate(0, -0.5, 0);
        }
        switch (facing) {
		case EAST:
		    GlStateManager.rotate(-90, 0, 1, 0);
			break;
		case SOUTH:
		    GlStateManager.rotate(180, 0, 1, 0);
			break;
		case WEST:
		    GlStateManager.rotate(90, 0, 1, 0);
			break;
		default:
			break;
		}
                
	    float o = 3f / 16f;
	    for(int i = 0; i < 9; i++)
	    {
	    	ItemStack stack = te.getStackInSlot(i);
	    	
			if(stack != null && !stack.isEmpty()) {
				double xLoc = +o - (i % 3) * o;
				double Yloc = 0.5f / 32f;
				double Zloc = +o - (i / 3) * o;

			    renderItem(stack, xLoc, Yloc + 0.045, Zloc, 0.125f, te);
			}
	    }
	    
        GlStateManager.popMatrix();
	}
	
	private void renderItem(ItemStack stack, double x, double y, double z, float scale, TileCraftingStationSlab te)
    {
        if (!stack.isEmpty())
        {
            GlStateManager.pushMatrix();
		    GlStateManager.translate(x, y, z);
		    
		    int ambLight = getWorld().getCombinedLight(te.getPos().offset(EnumFacing.UP), 0);
			int lu = ambLight % 65536;
			int lv = ambLight / 65536;
		    OpenGlHelper.setLightmapTextureCoords (OpenGlHelper.lightmapTexUnit, lu / 1f, lv / 1f);
		    
		    if(stack.getItem() instanceof ItemBlock && !(Block.getBlockFromItem(stack.getItem()) instanceof BlockPane)) {
		    	GlStateManager.translate(0, 0.05, 0);
		    	GlStateManager.rotate(180, 0, 1, 0);
		    }
		    else
		    {
			    GlStateManager.rotate(180F, 0, 1, 0);
		        GlStateManager.rotate(-90F, 1, 0, 0);
		    }
		    
		    GlStateManager.scale(scale, scale, scale);
		    
	        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
            GlStateManager.popMatrix();
        }
    }
}
