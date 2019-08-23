package com.example.kmpp.data

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.json.JsonSerializer
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.response.HttpResponseContainer
import io.ktor.client.response.HttpResponsePipeline
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.readRemaining

sealed class HttpRequestBody {
    data class Json<out T : Any>(val requestBody: T) : HttpRequestBody()
    data class GraphQL(val queryString: String) : HttpRequestBody()
    class File(val byteArray: ByteArray) : HttpRequestBody()
    data class FormData(val formData: List<Pair<String, String>>) : HttpRequestBody()
}


class GraphQLFeature(val serializer: JsonSerializer) {
    class Config {
        /**
         * Serialized that will be used for serializing requests bodies,
         * and de-serializing response bodies when Content-Type matches `application/json`.
         *
         * Default value is [defultSerializer]
         */
        var serializer: JsonSerializer? = null
    }

    companion object Feature : HttpClientFeature<Config, GraphQLFeature> {
        override val key: AttributeKey<GraphQLFeature> = AttributeKey("graphQL")

        override fun prepare(block: Config.() -> Unit): GraphQLFeature =
            Config().apply(block).let { GraphQLFeature(it.serializer ?: defaultSerializer()) }

        override fun install(feature: GraphQLFeature, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Transform) { payload ->
                (payload as? HttpRequestBody)?.let { requestBody ->
                    when (requestBody) {
                        is HttpRequestBody.Json<*> -> {
                            proceedWith(feature.serializer.write(requestBody.requestBody))
                        }
                        is HttpRequestBody.GraphQL -> {
                            proceedWith(TextContent(requestBody.queryString, ContentType.parse("application/graphql")))
                        }
                        is HttpRequestBody.File -> {
                            proceedWith(ByteArrayContent(requestBody.byteArray))
                        }
                        is HttpRequestBody.FormData -> {
                            proceedWith(FormDataContent(Parameters.build {
                                requestBody.formData.forEach { (key, value) ->
                                    set(key, value)
                                }
                            }))
                        }
                    }
                }
            }

            scope.responsePipeline.intercept(HttpResponsePipeline.Transform) { (info, body) ->
                if (body !is ByteReadChannel) return@intercept
                if (context.response.contentType()?.match(ContentType.Application.Json) != true) return@intercept
                proceedWith(HttpResponseContainer(info, feature.serializer.read(info, body.readRemaining())))
            }
        }
    }
}