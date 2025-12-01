package org.robbie.yaha.features.sussify

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BrushableBlock
import net.minecraft.sound.SoundEvent
import net.minecraft.util.math.BlockPos

class EntityBrushableBlock(
    baseBlock: Block,
    settings: Settings,
    brushingSound: SoundEvent,
    brushingCompleteSound: SoundEvent
) : BrushableBlock(
    baseBlock,
    settings,
    brushingSound,
    brushingCompleteSound
) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState) =
        EntityBrushableBlockEntity(pos, state)
}