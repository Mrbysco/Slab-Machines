package com.mrbysco.slabmachines.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.slabmachines.entity.TNTSlabEntity;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TNTPrimeSlabRenderer extends EntityRenderer<TNTSlabEntity> {
    public TNTPrimeSlabRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowRadius = 0.5F;
    }

    public void render(TNTSlabEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, 0.5D, 0.0D);
        if ((float)entityIn.getLife() - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)entityIn.getLife() - partialTicks + 1.0F) / 10.0F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            matrixStackIn.scale(f1, f1, f1);
        }

        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStackIn.translate(-0.5D, -0.5D, 0.5D);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        renderTntFlash(SlabRegistry.TNT_SLAB.get().defaultBlockState(), matrixStackIn, bufferIn, packedLightIn, entityIn.getLife() / 5 % 2 == 0);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public static void renderTntFlash(BlockState blockStateIn, MatrixStack matrixStackIn, IRenderTypeBuffer renderTypeBuffer, int combinedLight, boolean doFullBright) {
        int i;
        if (doFullBright) {
            i = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
        } else {
            i = OverlayTexture.NO_OVERLAY;
        }

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockStateIn, matrixStackIn, renderTypeBuffer, combinedLight, i);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    public ResourceLocation getTextureLocation(TNTSlabEntity entity)
    {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}