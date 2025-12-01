package org.robbie.yaha.features.sussify

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.BrushableBlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import org.robbie.yaha.registry.YahaBlockEntityTypes

class EntityBrushableBlockEntity(pos: BlockPos, state: BlockState) : BrushableBlockEntity(pos, state) {
    private var entityType: EntityType<*>? = null
    private var entityNbt: NbtCompound = NbtCompound()

    override fun spawnItem(player: PlayerEntity?) {
        if (world == null || world!!.server == null) return
        val direction = hitDirection ?: Direction.UP
        val blockPos = pos.offset(direction)
        entityType?.create(world)?.let {
            it.readNbt(entityNbt)
            it.setPosition(blockPos.toCenterPos())
            world!!.spawnEntity(it)
        }
    }

    override fun toInitialChunkDataNbt(): NbtCompound {
        val nbt = super.toInitialChunkDataNbt()

        if (entityType != null) {
            val entity = NbtCompound()
            entity.putString("entity_type", EntityType.getId(entityType).toString())
            entity.put("entity_nbt", entityNbt)
            nbt.put("entity", entity)
        }

        return nbt
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        if (!nbt.contains("entity")) return
        val entity = nbt.getCompound("entity")
        if (!entity.contains("entity_type") || !entity.contains("entity_nbt")) return

        val typeId = entity.getString("entity_type")
        entityType = Registries.ENTITY_TYPE.get(Identifier(typeId))
        entityNbt = entity.getCompound("entity_nbt")
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        if (entityType == null) return
        val entity = NbtCompound()
        entity.putString("entity_type", EntityType.getId(entityType).toString())
        entity.put("entity_nbt", entityNbt)
        nbt.put("entity", entity)
    }

    override fun getType(): BlockEntityType<*> = YahaBlockEntityTypes.ENTITY_BRUSHABLE_BLOCK

    fun getEntityType() = entityType
    fun getEntityNbt() = entityNbt

    companion object {
        fun saveEntity(entity: Entity, nbt: NbtCompound): Boolean {
            val entityType = EntityType.getId(entity.type).toString()
            val entityNbt = NbtCompound()
            if (!entity.saveNbt(entityNbt)) return false
            nbt.putString("entity_type", entityType)
            nbt.put("entity_nbt", entityNbt)
            return true
        }
    }
}