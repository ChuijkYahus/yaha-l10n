package org.robbie.yaha.features.trident

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.robbie.yaha.YahaUtils
import org.robbie.yaha.features.paper_plane.PaperPlaneEntity
import org.robbie.yaha.registry.YahaDamageTypes
import org.robbie.yaha.registry.YahaEntities
import kotlin.math.pow

const val MAX_AGE = 600
const val GRAVITY = -0.05
const val DRAG = 0.99

class TridentEntity(
    entityType: EntityType<out TridentEntity>,
    world: World
) : ProjectileEntity(entityType, world) {
    constructor(
        world: World,
        owner: Entity?,
        pos: Vec3d
    ) : this(YahaEntities.TRIDENT_ENTITY, world) {
        this.owner = owner
        setPosition(pos)
    }

    val piercedEntities = hashSetOf<Int>()

    override fun tick() {
        super.tick()

        if (!world.isClient && age > MAX_AGE) shatter()

        if (velocity.lengthSquared() != 0.0) YahaUtils.pitchYawFromRotVec(velocity)?.let {
            pitch = it.first
            yaw = it.second
        }

        velocity = velocity.multiply(DRAG)
        if (!hasNoGravity()) velocity = velocity.add(0.0, GRAVITY, 0.0)
        setPosition(pos.add(velocity))

        while (!isRemoved) {
            val hitResult = ProjectileUtil.getCollision(this, ::canHit)
            if (hitResult.type == HitResult.Type.MISS) break
            onCollision(hitResult)
        }

        checkBlockCollision()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        shatter()
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val entity = entityHitResult.entity

        playHitSound()
        spawnParticles()

        val damage = 20 - 20 * (velocity.lengthSquared() / 15 + 1).pow(-2)
        if (entity !is ProjectileEntity) {
            entity.damage(world.damageSources.create(
                YahaDamageTypes.TRIDENT,
                this,
                owner
            ), damage.toFloat())
        }
        entity.velocity = velocity.negate()

        piercedEntities.add(entity.id)
        if (piercedEntities.size == 3) shatter()
    }

    private fun shatter() {
        playShatterSound()
        spawnParticles()
        discard()
    }

    private fun playHitSound() {
        playSound(SoundEvents.ITEM_TRIDENT_HIT, 1f, 1f)
        playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, 0.5f, 1f)
    }

    private fun playShatterSound() {
        playSound(SoundEvents.ITEM_TRIDENT_THUNDER, 0.3f, 1.5f)
        playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, 1.0f, 0.5f)
    }

    private fun spawnParticles() {
        (world as? ServerWorld)?.let {
            val particleParam = ItemStackParticleEffect(ParticleTypes.ITEM, ItemStack(Items.AMETHYST_BLOCK, 1))
            it.spawnParticles(
                particleParam,
                x, y, z,
                8,
                0.0, 0.0, 0.0,
                0.1
            )
        }
    }

    override fun canHit() = false
    override fun canHit(entity: Entity) = super.canHit(entity)
            && !piercedEntities.contains(entity.id)
            && (entity !is PaperPlaneEntity || entity.owner != owner)
    override fun initDataTracker() {}
}