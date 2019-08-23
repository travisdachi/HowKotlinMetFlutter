package com.example.kmpp.common.extensions

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

inline fun <reified T> KSerializer<T>.parse(jsonString: String) = Json.parse(this, jsonString)

inline fun <reified T> KSerializer<T>.stringify(obj: T) = Json.stringify(this, obj)