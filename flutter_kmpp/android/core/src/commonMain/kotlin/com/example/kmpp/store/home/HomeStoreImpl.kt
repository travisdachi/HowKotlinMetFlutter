package com.example.kmpp.store.home

import com.example.kmpp.common.store.BaseStore
import com.example.kmpp.store.home.HomeStore.*
import com.example.kmpp.store.home.HomeStore.Action.Fetch
import kotlinx.coroutines.launch

class HomeStoreImpl()
    : BaseStore<State, Event, Action>(State()), HomeStore {
    override fun dispatch(action: Action) {
        super.dispatch(action)
        when (action) {
            is Fetch -> {
                launch {
                    try {
                        setState { copy(loading = true) }
                        val user = getUser(action.username, action.isOrg)
                        setState { copy(loading = false, user = user) }
                    } catch (e: Throwable) {
                        setState { copy(loading = false) }
                        event.value = Event.FetchError(e.toString())
                    }
                }
            }
        }
    }

}