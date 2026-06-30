package org.robbie.yaha.registry

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import org.robbie.yaha.Yaha

object YahaSounds {
    val PLANE_SHATTER = register("plane_shatter")
    val ANVIL_HIT = register("anvil_hit")
    val ANVIL_SHATTER = register("anvil_shatter")
    val TRIDENT_HIT = register("trident_hit")
    val TRIDENT_SHATTER = register("trident_shatter")

    fun register() {}

    private fun register(name: String): SoundEvent {
        val id = Yaha.id(name)
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id))
    }
}