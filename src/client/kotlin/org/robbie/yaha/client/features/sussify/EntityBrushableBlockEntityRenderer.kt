package org.robbie.yaha.client.features.sussify

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import net.minecraft.util.math.RotationAxis
import org.joml.Quaternionf
import org.robbie.yaha.features.sussify.EntityBrushableBlockEntity

class EntityBrushableBlockEntityRenderer : BlockEntityRenderer<EntityBrushableBlockEntity> {
    private val entityRenderDispatcher: EntityRenderDispatcher

    constructor(context: BlockEntityRendererFactory.Context) {
        entityRenderDispatcher = context.entityRenderDispatcher
    }

    override fun render(
        blockEntity: EntityBrushableBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val dusted = blockEntity.cachedState.get(Properties.DUSTED)
        if (dusted == 0) return
        val direction = blockEntity.hitDirection ?: return
        val entity = blockEntity.getEntityType()?.create(blockEntity.world ?: return) ?: return

        val isWide = entity.width > entity.height
        val scale = 0.5f / (if (isWide) entity.height else entity.width)

        matrices.push()
        matrices.translate(0.5f, 0.5f, 0.5f)
        matrices.multiply(getRotation(direction))
        matrices.translate(
            0.0f,
            1.0f + (if (isWide) entity.width else entity.height) * scale * (dusted.toFloat() / 6.0f - 1.0f),
            0.0f
        )
        if (isWide) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f))
        matrices.scale(scale, scale, scale)

        val blockPos = blockEntity.pos.offset(direction)
        val light = WorldRenderer.getLightmapCoordinates(
            blockEntity.world,
            blockEntity.cachedState,
            blockPos
        )

        val renderer = entityRenderDispatcher.getRenderer(entity)
        renderer.render(
            entity,
            0.0f,
            0.0f,
            matrices,
            vertexConsumers,
            light
        )
        matrices.pop()
    }

    private fun getRotation(direction: Direction): Quaternionf = when (direction) {
            Direction.DOWN -> RotationAxis.POSITIVE_X.rotationDegrees(180f)
            Direction.UP -> Quaternionf()
            Direction.NORTH -> RotationAxis.POSITIVE_X.rotationDegrees(-90f)
            Direction.SOUTH -> RotationAxis.POSITIVE_X.rotationDegrees(90f)
            Direction.WEST -> RotationAxis.POSITIVE_Z.rotationDegrees(90f)
            Direction.EAST -> RotationAxis.POSITIVE_Z.rotationDegrees(-90f)
    }
}