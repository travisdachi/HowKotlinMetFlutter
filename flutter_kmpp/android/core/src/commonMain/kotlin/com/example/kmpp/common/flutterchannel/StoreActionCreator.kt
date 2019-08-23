package com.example.kmpp.common.flutterchannel

import com.example.kmpp.common.store.Store
import kotlin.reflect.KClass

class FlutterStoreActionBuilder<
        ACTION : Store.Action> {
    val actions = mutableListOf<Pair<KClass<*>, (String) -> ACTION>>()

    inline fun <reified T : ACTION> action(noinline creator: (argument: String) -> T) {
        actions.add(T::class to creator)
    }

    fun build(): Map<String, (argument: String) -> ACTION> =
            actions.associate {
                val actionName= it.first.qualifiedName!!.split('.').takeLast(3).joinToString(".")
                actionName to it.second
            }

}

inline fun <reified ACTION : Store.Action> actionCreator(builder: FlutterStoreActionBuilder<ACTION>.() -> Unit) =
        FlutterStoreActionBuilder<ACTION>().apply(builder).build()