package org.robbie.yaha.features.anvil

import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.world.World

class AnvilEntity(
    entityType: EntityType<out AnvilEntity>,
    world: World
) : ProjectileEntity(entityType, world) {


    override fun initDataTracker() {}
}