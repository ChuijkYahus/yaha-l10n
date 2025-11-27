package org.robbie.yaha.registry

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.robbie.yaha.Yaha
import org.robbie.yaha.features.sussify.EntityBrushableBlockEntity

object YahaBlockEntityTypes {

    val ENTITY_BRUSHABLE_BLOCK = register(
        "entity_brushable_block",
        FabricBlockEntityTypeBuilder.create(
            ::EntityBrushableBlockEntity,
            YahaBlocks.ENTITY_SUS_SAND,
            YahaBlocks.ENTITY_SUS_GRAVEL
        ).build()
    )

    private fun <T : BlockEntityType<*>> register(name: String, blockEntityType: T): T = Registry.register(
        Registries.BLOCK_ENTITY_TYPE, Yaha.id(name), blockEntityType
    )

    fun register() {}
}