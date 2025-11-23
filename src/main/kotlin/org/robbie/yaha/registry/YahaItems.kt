package org.robbie.yaha.registry

import at.petrak.hexcasting.common.items.storage.ItemThoughtKnot
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.robbie.yaha.Yaha
import org.robbie.yaha.features.bundles.IotaHolderBundle

object YahaItems {
    val TIME_BOMB = Item(Item.Settings())
    val SPINDLE = IotaHolderBundle(Item.Settings(), { it is ItemThoughtKnot }) // HexItems is not initialised! cant use THOUGHT_KNOT!!!

    fun register() {
        register("time_bomb", TIME_BOMB)
        register("spindle", SPINDLE)
    }

    private fun register(name: String, item: Item) {
        Registry.register(Registries.ITEM, Yaha.id(name), item)
    }
}