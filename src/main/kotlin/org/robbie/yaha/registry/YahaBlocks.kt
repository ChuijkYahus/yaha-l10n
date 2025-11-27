package org.robbie.yaha.registry

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.MapColor
import net.minecraft.block.enums.Instrument
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import org.robbie.yaha.Yaha
import org.robbie.yaha.features.sussify.EntityBrushableBlock

object YahaBlocks {
    val ENTITY_SUS_SAND: Block = register(
        EntityBrushableBlock(
            Blocks.SAND,
            AbstractBlock.Settings.create()
                .mapColor(MapColor.PALE_YELLOW)
                .instrument(Instrument.SNARE)
                .strength(0.25F)
                .sounds(BlockSoundGroup.SUSPICIOUS_SAND)
                .pistonBehavior(PistonBehavior.DESTROY),
            SoundEvents.ITEM_BRUSH_BRUSHING_SAND,
            SoundEvents.ITEM_BRUSH_BRUSHING_SAND_COMPLETE
        ),
        "entity_sus_sand"
    )

    val ENTITY_SUS_GRAVEL: Block = register(
        EntityBrushableBlock(
            Blocks.GRAVEL,
            AbstractBlock.Settings.create()
				.mapColor(MapColor.STONE_GRAY)
				.instrument(Instrument.SNARE)
				.strength(0.25F)
				.sounds(BlockSoundGroup.SUSPICIOUS_GRAVEL)
				.pistonBehavior(PistonBehavior.DESTROY),
			SoundEvents.ITEM_BRUSH_BRUSHING_GRAVEL,
			SoundEvents.ITEM_BRUSH_BRUSHING_GRAVEL_COMPLETE
        ),
        "entity_sus_gravel"
    )

    private fun register(block: Block, name: String) = Registry.register(
        Registries.BLOCK,
        Yaha.id(name),
        block
    )

    fun register() {}
}