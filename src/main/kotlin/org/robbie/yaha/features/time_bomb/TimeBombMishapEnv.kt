package org.robbie.yaha.features.time_bomb

import at.petrak.hexcasting.api.casting.eval.MishapEnvironment
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d

class TimeBombMishapEnv(bomb: TimeBombEntity, world: ServerWorld) : MishapEnvironment(world, bomb.owner as? ServerPlayerEntity) {
    override fun yeetHeldItemsTowards(targetPos: Vec3d?) {}

    override fun dropHeldItems() {}

    override fun drown() {}

    override fun damage(healthProportion: Float) {}

    override fun removeXp(amount: Int) {}

    override fun blind(ticks: Int) {}
}