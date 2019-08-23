package com.example.kmpp.data

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


@Serializable
data class GetGithubUserApiRequestEntity(
        @SerialName("query")
        val query: String
)

@Serializable
data class GetGithubUserApiResponseEntity(
        @SerialName("data")
        val `data`: Data? = null
) {
    @Serializable
    data class Data(
            @SerialName("user")
            val user: User? = null,
            @SerialName("organization")
            val organization: User? = null
    ) {
        @Serializable
        data class User(
                @SerialName("avatarUrl")
                val avatarUrl: String? = null,
                @SerialName("description")
                val description: String? = null,
                @SerialName("bio")
                val bio: String? = null,
                @SerialName("id")
                val id: String? = null,
                @SerialName("login")
                val login: String? = null,
                @SerialName("repositories")
                val repositories: Repositories? = null
        ) {
            @Serializable
            data class Repositories(
                    @SerialName("nodes")
                    val nodes: List<Node>? = null
            ) {
                @Serializable
                data class Node(
                        @SerialName("assignableUsers")
                        val assignableUsers: AssignableUsers? = null,
                        @SerialName("description")
                        val description: String? = null,
                        @SerialName("id")
                        val id: String? = null,
                        @SerialName("name")
                        val name: String? = null,
                        @SerialName("stargazers")
                        val stargazers: Stargazers? = null
                ) {
                    @Serializable
                    data class AssignableUsers(
                            @SerialName("nodes")
                            val nodes: List<Node>? = null
                    ) {
                        @Serializable
                        data class Node(
                                @SerialName("avatarUrl")
                                val avatarUrl: String? = null,
                                @SerialName("id")
                                val id: String? = null,
                                @SerialName("login")
                                val login: String? = null,
                                @SerialName("description")
                                val description: String? = null
                        )
                    }

                    @Serializable
                    data class Stargazers(
                            @SerialName("totalCount")
                            val totalCount: Int? = null
                    )
                }
            }
        }
    }
}