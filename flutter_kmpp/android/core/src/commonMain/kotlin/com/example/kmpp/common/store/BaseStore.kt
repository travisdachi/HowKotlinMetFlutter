package com.example.kmpp.common.store

import com.example.kmpp.common.coroutine.ApplicationDispatcher
import com.example.kmpp.common.kreactive.KObservable
import com.example.kmpp.common.kreactive.KSingleEventRelay
import com.example.kmpp.common.kreactive.KStateRelay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseStore<S : Store.State, E : Store.Event, A : Store.Action>(
        initialState: S
) : Store<S, E, A>, CoroutineScope {
    private val job = SupervisorJob()
    protected val mutableState = KStateRelay<S>().apply { value = initialState }
    override val state: KObservable<S> = mutableState
    override val event = KSingleEventRelay<E>()
    override val currentState get() = mutableState.value!!
    override val coroutineContext: CoroutineContext get() = job + ApplicationDispatcher

    protected inline fun setState(newState: S.() -> S) {
        mutableState.value = mutableState.value!!.newState()
    }

    override fun dispatch(action: A) {

    }

    override fun dispose() {
        job.cancel()
    }
}