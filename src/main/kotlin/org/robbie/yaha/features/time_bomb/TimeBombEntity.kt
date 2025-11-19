package org.robbie.yaha.features.time_bomb

import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.world.World

class TimeBombEntity(
    entityType: EntityType<out TimeBombEntity>,
    world: World
) : ProjectileEntity(entityType, world) {
    override fun initDataTracker() {}
}