package com.example.kmpp.store.home

import com.example.kmpp.data.GithubOwner
import com.example.kmpp.data.getGithubUser

suspend fun getUser(username: String, isOrg: Boolean): User = getGithubUser(
        if (isOrg) GithubOwner.Organization(username) else GithubOwner.User(username)
).let { response ->
    val data = response.data
    val user = if (isOrg) data?.organization else data?.user
    val userDescription = if (isOrg) user?.description else user?.bio
    User(
            id = user?.id ?: "",
            login = user?.login ?: "",
            description = userDescription ?: "",
            avatarUrl = user?.avatarUrl ?: "",
            repositories = user?.repositories?.nodes?.map {
                Repo(
                        id = it.id ?: "",
                        name = it.name ?: "",
                        description = it.description ?: "",
                        stargazersCount = it.stargazers?.totalCount ?: 0,
                        contributors = it.assignableUsers?.nodes?.map {
                            User(
                                    id = it.id ?: "",
                                    login = it.login ?: "",
                                    description = it.description ?: "",
                                    avatarUrl = it.avatarUrl ?: "",
                                    repositories = emptyList()
                            )
                        } ?: emptyList()

                )
            } ?: emptyList()
    )
}