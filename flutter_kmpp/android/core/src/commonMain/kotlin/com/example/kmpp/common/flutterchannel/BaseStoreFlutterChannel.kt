package com.example.kmpp.common.flutterchannel

import com.example.kmpp.common.kreactive.KCompositeSubscriptions
import com.example.kmpp.common.kreactive.KObservable
import com.example.kmpp.common.kreactive.KSingleEventRelay
import com.example.kmpp.common.kreactive.KStateRelay
import com.example.kmpp.common.store.Store
import kotlin.reflect.KClass

data class FlutterMethod(val method: String, val argument: String)

inline fun <
        reified STORE : Store<STATE, EVENT, ACTION>,
        reified STATE : Store.State,
        reified EVENT : Store.Event,
        reified ACTION : Store.Action
        > createStoreFlutterChannel(
        noinline storeProvider: () -> STORE,
        actionDeserializerBuilder: FlutterStoreActionBuilder<ACTION>.() -> Unit
) = StoreFlutterChannel(
        storeProvider,
        actionCreator(actionDeserializerBuilder),
        STORE::class
)

class StoreFlutterChannel<
        STORE : Store<STATE, EVENT, ACTION>,
        STATE : Store.State,
        EVENT : Store.Event,
        ACTION : Store.Action>(
        private val storeProvider: () -> STORE,
        private val actionDeserializer: Map<String, (argument: String) -> ACTION>,
        private val storeClass: KClass<STORE>) {

    val storeName: String = storeClass.simpleName!!

    val methodChannelName get() = "com.example.kmpp/store/$storeName"

    private val subscriptions = KCompositeSubscriptions()

    private val mutableFlutterState = KStateRelay<FlutterMethod>()
    private val mutableFlutterEvent = KSingleEventRelay<FlutterMethod>()

    val flutterState: KObservable<FlutterMethod> = mutableFlutterState
    val flutterEvent: KObservable<FlutterMethod> = mutableFlutterEvent

    private val store = storeProvider()

    init {
        store.state.observe { state ->
            mutableFlutterState.value = FlutterMethod(state::class.qualifiedName!!
                    .split('.').takeLast(2).joinToString("."), state.stringify())
        }
        store.event.observe { event ->
            mutableFlutterEvent.value = FlutterMethod(event::class.qualifiedName!!
                    .split('.').takeLast(3).joinToString("."), event.stringify())
        }
    }

    fun invokeMethod(method: FlutterMethod): Boolean =
            actionDeserializer[method.method]
                    ?.invoke(method.argument)
                    ?.let { action ->
                        store.dispatch(action)
                        true
                    } ?: false

    fun dispose() {
        subscriptions.clear()
        store.dispose()
    }

    private inline fun <T> KObservable<T>.observe(
            crossinline onNext: (T) -> Unit
    ) {
        subscriptions += subscribe {
            onNext(it)
        }
    }
}