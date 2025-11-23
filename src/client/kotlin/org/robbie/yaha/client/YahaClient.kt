package org.robbie.yaha.client

import net.fabricmc.api.ClientModInitializer
import org.robbie.yaha.client.registry.YahaEntitiesClient
import org.robbie.yaha.client.registry.YahaItemsClient

class YahaClient : ClientModInitializer {

    override fun onInitializeClient() {
        YahaEntitiesClient.register()
        YahaItemsClient.register()
    }
}
