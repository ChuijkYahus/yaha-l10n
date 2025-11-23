package org.robbie.yaha.client.registry

import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.util.Identifier
import org.robbie.yaha.features.bundles.IotaHolderBundle
import org.robbie.yaha.registry.YahaItems

object YahaItemsClient {
    fun register() {
        ModelPredicateProviderRegistry.register(
            YahaItems.SPINDLE,
            Identifier("filled")
        ) { itemStack, clientWorld, livingEntity, seed ->
            IotaHolderBundle.getBundleOccupancy(itemStack).toFloat() / IotaHolderBundle.MAX_COUNT.toFloat()
        }
    }
}