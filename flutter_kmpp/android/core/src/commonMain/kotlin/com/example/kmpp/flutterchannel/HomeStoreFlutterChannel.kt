package com.example.kmpp.flutterchannel

import com.example.kmpp.common.extensions.parse
import com.example.kmpp.common.flutterchannel.createStoreFlutterChannel
import com.example.kmpp.store.home.HomeStore
import com.example.kmpp.store.home.HomeStoreImpl

fun HomeStoreFlutterChannel() = createStoreFlutterChannel(
        storeProvider = { HomeStoreImpl() as HomeStore },
        actionDeserializerBuilder = {
            action { HomeStore.Action.Fetch.serializer().parse(it) }
        }
)