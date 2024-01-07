package com.skyecodes.snowball.service

import com.skyecodes.snowball.APP_VERSION
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

val HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
    install(HttpTimeout) {
        connectTimeoutMillis = 1000

    }
    install(HttpRequestRetry) {
        retryOnServerErrors(3)
        retryOnException(3, true)
        constantDelay()
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
}

fun HttpRequestBuilder.initGlobal() {
    userAgent("skyecodes/snowball-launcher/$APP_VERSION (contact@skye.codes)")
}

inline fun <reified T> HttpRequestBuilder.jsonBody(body: T) {
    setBody(body)
    contentType(ContentType.Application.Json)
}