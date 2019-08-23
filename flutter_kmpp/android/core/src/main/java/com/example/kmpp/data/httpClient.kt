package com.example.kmpp.data

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp

actual fun httpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(OkHttp) {
    block()
}