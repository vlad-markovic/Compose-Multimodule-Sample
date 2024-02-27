package com.vladmarkovic.sample.shared_data.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import java.io.IOException

suspend inline fun <reified T> HttpResponse.unpack(): T {
    if (status.value in 200..299) {
        return body<T>()
    } else {
        // TODO Exceptions
        throw IOException("Failed to get response for ${T::class.simpleName}")
    }
}
