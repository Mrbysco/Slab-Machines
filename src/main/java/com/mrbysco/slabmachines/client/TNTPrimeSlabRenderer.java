package com.mrbysco.slabmachines.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mrbysco.slabmachines.entity.TNTSlabEntity;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TNTPrimeSlabRenderer extends EntityRenderer<TNTSlabEntity> {
	public TNTPrimeSlabRenderer(Context context) {
		super(context);
		this.shadowRadius = 0.5F;
	}

	public void render(TNTSlabEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
		poseStack.pushPose();
		poseStack.translate(0.0D, 0.5D, 0.0D);
		int var7 = entityIn.getFuse();
		if ((float) var7 - partialTicks + 1.0F < 10.0F) {
			float var8 = 1.0F - ((float) var7 - partialTicks + 1.0F) / 10.0F;
			var8 = Mth.clamp(var8, 0.0F, 1.0F);
			var8 *= var8;
			var8 *= var8;
			float var9 = 1.0F + var8 * 0.3F;
			poseStack.scale(var9, var9, var9);
		}

		poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
		poseStack.translate(-0.5D, -0.5D, 0.5D);
		poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
		renderTntFlash(SlabRegistry.TNT_SLAB.get().defaultBlockState(), poseStack, bufferSource, packedLightIn, var7 / 5 % 2 == 0);
		poseStack.popPose();
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferSource, packedLightIn);
	}

	public static void renderTntFlash(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, boolean doFullBright) {
		int i;
		if (doFullBright) {
			i = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
		} else {
			i = OverlayTexture.NO_OVERLAY;
		}

		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource, combinedLight, i);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	public ResourceLocation getTextureLocation(TNTSlabEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}