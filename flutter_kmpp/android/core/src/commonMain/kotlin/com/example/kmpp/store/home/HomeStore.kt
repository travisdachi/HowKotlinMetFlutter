package com.example.kmpp.store.home

import com.example.kmpp.common.extensions.stringify
import com.example.kmpp.common.store.Store
import kotlinx.serialization.Serializable


interface HomeStore : Store<HomeStore.State, HomeStore.Event, HomeStore.Action> {
    @Serializable
    data class State(
            val loading: Boolean = false,
            val user: User? = null
    ) : Store.State {
        override fun stringify(): String = serializer().stringify(this)
    }

    sealed class Event : Store.Event {
        @Serializable
        data class FetchError(val message: String) : Event() {
            override fun stringify(): String = serializer().stringify(this)
        }
    }

    sealed class Action : Store.Action {
        @Serializable
        data class Fetch(val username: String, val isOrg: Boolean) : Action()
    }
}

@Serializable
data class User(
        val id: String,
        val login: String,
        val description: String,
        val avatarUrl: String,
        val repositories: List<Repo>
)

@Serializable
data class Repo(
        val id: String,
        val name: String,
        val description: String,
        val stargazersCount: Int,
        val contributors: List<User>
)
