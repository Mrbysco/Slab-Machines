package com.mrbysco.slabmachines.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrbysco.slabmachines.entity.TNTSlabEntity;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TNTPrimeSlabRenderer extends EntityRenderer<TNTSlabEntity, TntRenderState> {
	private final BlockRenderDispatcher blockRenderer;

	public TNTPrimeSlabRenderer(Context context) {
		super(context);
		this.shadowRadius = 0.5F;
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(TntRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		poseStack.pushPose();
		poseStack.translate(0.0D, 0.5D, 0.0D);
		float remainingInTicks = renderState.fuseRemainingInTicks;
		if (remainingInTicks < 10.0F) {
			float f1 = 1.0F - remainingInTicks / 10.0F;
			f1 = Mth.clamp(f1, 0.0F, 1.0F);
			f1 *= f1;
			f1 *= f1;
			float f2 = 1.0F + f1 * 0.3F;
			poseStack.scale(f2, f2, f2);
		}

		poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
		poseStack.translate(-0.5D, -0.5D, 0.5D);
		poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
		renderTntFlash(SlabRegistry.TNT_SLAB.get().defaultBlockState(), poseStack, bufferSource, packedLight, remainingInTicks / 5 % 2 == 0);
		poseStack.popPose();
		super.render(renderState, poseStack, bufferSource, packedLight);
	}

	private void renderTntFlash(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, boolean doFullBright) {
		int i;
		if (doFullBright) {
			i = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
		} else {
			i = OverlayTexture.NO_OVERLAY;
		}

		blockRenderer.renderSingleBlock(state, poseStack, bufferSource, combinedLight, i);
	}

	@Override
	public TntRenderState createRenderState() {
		return new TntRenderState();
	}

	@Override
	public void extractRenderState(TNTSlabEntity slabEntity, TntRenderState renderState, float partialTick) {
		super.extractRenderState(slabEntity, renderState, partialTick);
		renderState.fuseRemainingInTicks = (float) slabEntity.getFuse() - partialTick + 1.0F;
		renderState.blockState = slabEntity.getBlockState();
	}
}