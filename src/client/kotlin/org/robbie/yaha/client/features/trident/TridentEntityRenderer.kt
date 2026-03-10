package org.robbie.yaha.client.features.trident

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.model.TridentEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RotationAxis
import org.robbie.yaha.Yaha
import org.robbie.yaha.features.trident.TridentEntity

class TridentEntityRenderer(context: EntityRendererFactory.Context) : EntityRenderer<TridentEntity>(context) {
    val model = TridentEntityModel(context.getPart(EntityModelLayers.TRIDENT))

    override fun render(
        entity: TridentEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        matrices.push()
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.yaw)))
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch) - 90f))

        val vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(getTexture(entity)))
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f)

        matrices.pop()
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

    override fun getTexture(tridentEntity: TridentEntity) = Yaha.id("textures/entity/trident.png")
    override fun getBlockLight(entity: TridentEntity, pos: BlockPos) = 15
}