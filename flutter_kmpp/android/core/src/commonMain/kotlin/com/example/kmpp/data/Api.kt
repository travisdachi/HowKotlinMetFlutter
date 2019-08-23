package com.example.kmpp.data

import io.ktor.client.request.post


sealed class GithubOwner {
    abstract val value: String

    data class User(override val value: String) : GithubOwner()
    data class Organization(override val value: String) : GithubOwner()
}

val GithubOwner.queryType
    get() = if (this is GithubOwner.Organization) "organization" else "user"

val GithubOwner.queryDescription
    get() = if (this is GithubOwner.Organization) "description" else "bio"

suspend fun getGithubUser(owner: GithubOwner): GetGithubUserApiResponseEntity =
        provideGithubHttpClient().post("https://api.github.com/graphql") {
            val query = """query {
  ${owner.queryType}(login: "${owner.value}") {
    id
    login   
    ${owner.queryDescription}
    avatarUrl
    repositories(first: 5, orderBy: {field: STARGAZERS, direction: DESC}) {
      nodes {
        id
        name
        description
        stargazers {
          totalCount
        }
        assignableUsers(first: 10) {
          nodes {
            id
            login
            avatarUrl
          }
        }
      }
    }
  }
}
            """.trimIndent()
            body = HttpRequestBody.Json(GetGithubUserApiRequestEntity(query))
        }