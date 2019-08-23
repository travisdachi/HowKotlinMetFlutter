package com.example.kmpp.common.kreactive

import kotlin.properties.Delegates

typealias KObserver<T> = (T) -> Unit

interface KSubscription {
    fun addTo(compositeSubscriptions: KCompositeSubscriptions) {
        compositeSubscriptions += this
    }

    fun disposed(by: KCompositeSubscriptions) {
        by += this
    }

    fun unsubscribe()
}

class KCompositeSubscriptions {

    private val subscriptions: MutableList<KSubscription> = mutableListOf()

    fun add(subscription: KSubscription) {
        subscriptions += subscription
    }

    operator fun plusAssign(subscription: KSubscription) {
        add(subscription)
    }

    fun clear() {
        subscriptions.forEach {
            it.unsubscribe()
        }
        subscriptions.clear()
    }
}


interface KObservable<T> {
    val currentValue: T
    fun subscribe(observer: KObserver<T>): KSubscription
}

open class KStateRelay<T> : KObservable<T> {
    private var mutableValue: T? by Delegates.observable<T?>(null) { _, currentValue, newValue ->
        previousValue = currentValue
        newValue?.let {
            observers.forEach { observer -> observer.invoke(it) }
        }
    }

    var previousValue: T? = null
        private set

    var value: T
        get() = mutableValue!!
        set(value) {
            mutableValue = value
        }
    override val currentValue: T
        get() = value

    private val observers = mutableSetOf<KObserver<T>>()

    override fun subscribe(observer: KObserver<T>): KSubscription {
        mutableValue?.let(observer)
        observers.add(observer)
        return object : KSubscription {
            override fun unsubscribe() {
                observers.remove(observer)
            }
        }
    }

}

class KSingleEventRelay<T> : KObservable<T> {

    private var mutableValue: SingleEvent<T>? by Delegates.observable<SingleEvent<T>?>(null) { _, _, newValue ->
        newValue?.let {
            observers.forEach { observer -> observer.invoke(it) }
        }
    }

    var value: T
        get() = mutableValue!!.peekContent()
        set(value) {
            mutableValue = SingleEvent(value)
        }

    override val currentValue: T
        get() = value

    private val observers = mutableSetOf<ObserverWrapper<T>>()

    override fun subscribe(observer: KObserver<T>): KSubscription {
        val wrapObserver = ObserverWrapper(observer)
        mutableValue?.let(wrapObserver)
        observers.add(wrapObserver)
        return object : KSubscription {
            override fun unsubscribe() {
                observers.remove(wrapObserver)
            }
        }
    }

    open class SingleEvent<out T>(private val content: T) {

        private var hasBeenHandled = false

        fun getContentIfNotHandled(): T? {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }
        }

        fun peekContent(): T = content
    }


    private class ObserverWrapper<T>(private val observer: KObserver<T>) : KObserver<SingleEvent<T>> {

        override fun invoke(data: SingleEvent<T>) {
            data.getContentIfNotHandled()?.let(observer)
        }
    }
}

//Test
class TestKObserver<T> : KObserver<T> {
    private val mutableValues = mutableListOf<T>()

    val values: List<T> = mutableValues

    override fun invoke(t: T) {
        mutableValues.add(t)
    }
}

fun <T> KObservable<T>.test(): TestKObserver<T> = TestKObserver<T>().apply { subscribe(this) }