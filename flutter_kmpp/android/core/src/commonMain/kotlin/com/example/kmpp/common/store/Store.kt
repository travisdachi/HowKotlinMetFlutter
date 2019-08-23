package com.example.kmpp.common.store

import com.example.kmpp.common.kreactive.KObservable


interface Store<S : Store.State, E : Store.Event, A : Store.Action> {
    interface State {
        abstract fun stringify(): String
    }

    interface Event {
        fun stringify() = ""
    }

    interface Action


    val state: KObservable<S>
    val event: KObservable<E>
    val currentState: S

    fun dispatch(action: A)

    fun dispose()
}