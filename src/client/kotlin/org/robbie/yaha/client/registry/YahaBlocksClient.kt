package org.robbie.yaha.client.registry

import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import org.robbie.yaha.client.features.sussify.EntityBrushableBlockEntityRenderer
import org.robbie.yaha.registry.YahaBlockEntityTypes

object YahaBlocksClient {
    fun register() {
        BlockEntityRendererFactories.register(
            YahaBlockEntityTypes.ENTITY_BRUSHABLE_BLOCK,
            ::EntityBrushableBlockEntityRenderer
        )
    }
}