package com.example.kmpp.data

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header

expect fun httpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient

fun provideGithubHttpClient() = httpClient {
    install(GraphQLFeature) {
        serializer = KotlinxSerializer().apply {
            register(GetGithubUserApiRequestEntity.serializer())
            register(GetGithubUserApiResponseEntity.serializer())
        }
    }

    install("HeaderInterceptor") {
        requestPipeline.intercept(HttpRequestPipeline.Before) {
            context.header("Authorization", "Bearer ")
        }
    }
    //TODO: control logging by debug flag
//    install(Logging) {
//        level = LogLevel.ALL
//    }
}