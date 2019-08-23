package com.example.kmpp.common.coroutine

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

expect var ApplicationDispatcher: CoroutineDispatcher

@InternalCoroutinesApi
object TestDispatcher : CoroutineDispatcher(), Delay {

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        continuation.resume(Unit)
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        block.run()
    }
}