package org.robbie.yaha.features.sussify

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.command.argument.BlockStateArgument
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.property.Property
import net.minecraft.util.math.BlockPos
import org.robbie.yaha.registry.YahaBlocks
import org.robbie.yaha.registry.YahaCriteria

object OpSussifyBlock : SpellAction {
    override val argc = 2

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): SpellAction.Result {
        val pos = args.getBlockPos(0, argc)
        env.assertPosInRangeForEditing(pos)
        val entity = args.getEntity(1, argc)
        env.assertEntityInRange(entity)

        val block = env.world.getBlockState(pos).block
        val isItem = entity is ItemEntity
        val brushBlock = when (block to isItem) {
            Blocks.SAND to true -> Blocks.SUSPICIOUS_SAND
            Blocks.GRAVEL to true -> Blocks.SUSPICIOUS_GRAVEL
            Blocks.SAND to false -> YahaBlocks.ENTITY_SUS_SAND
            Blocks.GRAVEL to false -> YahaBlocks.ENTITY_SUS_GRAVEL
            else -> throw MishapBadBlock.Companion.of(pos, "yaha:sussifiable")
        }

        if (isItem && entity.stack.item == block.asItem() && env.castingEntity is ServerPlayerEntity)
            YahaCriteria.SUSCEPTION.trigger(env.castingEntity as ServerPlayerEntity)

        return SpellAction.Result(
            Spell(pos, brushBlock, entity),
            MediaConstants.DUST_UNIT / 8,
            listOf(ParticleSpray.Companion.cloud(pos.toCenterPos(), 1.0))
        )
    }

    private data class Spell(val pos: BlockPos, val brushBlock: Block, val entity: Entity) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            if (!env.canEditBlockAt(pos)) return

            if (!IXplatAbstractions.INSTANCE.isPlacingAllowed(
                    env.world,
                    pos,
                    ItemStack(brushBlock),
                    env.castingEntity as? ServerPlayerEntity
            )) return

            val blockNbt = NbtCompound()
            if (entity is ItemEntity) {
                blockNbt.put("item", entity.stack.writeNbt(NbtCompound()))
            } else {
                val entityNbt = NbtCompound()
                EntityBrushableBlockEntity.saveEntity(entity, entityNbt)
                blockNbt.put("entity", entityNbt)
            }

            val blockWithNbt = BlockStateArgument(
                brushBlock.defaultState,
                mutableSetOf<Property<*>>(),
                blockNbt
            )

            if (blockWithNbt.setBlockState(env.world, pos, Block.NOTIFY_ALL))
                entity.discard()
        }
    }
}