package com.mrbysco.slabmachines.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrbysco.slabmachines.entity.TNTSlabEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;

public class TNTPrimeSlabRenderer extends EntityRenderer<TNTSlabEntity, TntRenderState> {
	public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
	private final BlockModelResolver blockModelResolver;

	public TNTPrimeSlabRenderer(Context context) {
		super(context);
		this.shadowRadius = 0.5F;
		this.blockModelResolver = context.getBlockModelResolver();
	}

	@Override
	public void submit(TntRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector,
	                   CameraRenderState cameraRenderState) {
		poseStack.pushPose();
		poseStack.translate(0.0F, 0.5F, 0.0F);
		float f = renderState.fuseRemainingInTicks;
		if (renderState.fuseRemainingInTicks < 10.0F) {
			float f1 = 1.0F - renderState.fuseRemainingInTicks / 10.0F;
			f1 = Mth.clamp(f1, 0.0F, 1.0F);
			f1 *= f1;
			f1 *= f1;
			float f2 = 1.0F + f1 * 0.3F;
			poseStack.scale(f2, f2, f2);
		}

		poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
		poseStack.translate(-0.5F, -0.5F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
		if (renderState.blockState != null) {
			TntMinecartRenderer.submitWhiteSolidBlock(
					renderState.blockState, poseStack, nodeCollector, renderState.lightCoords, (int) f / 5 % 2 == 0, renderState.outlineColor
			);
		}

		poseStack.popPose();

		super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
	}

	@Override
	public TntRenderState createRenderState() {
		return new TntRenderState();
	}

	@Override
	public void extractRenderState(TNTSlabEntity slabEntity, TntRenderState renderState, float partialTick) {
		super.extractRenderState(slabEntity, renderState, partialTick);
		renderState.fuseRemainingInTicks = (float) slabEntity.getFuse() - partialTick + 1.0F;
		this.blockModelResolver.update(renderState.blockState, slabEntity.getBlockState(), BLOCK_DISPLAY_CONTEXT);
	}
}