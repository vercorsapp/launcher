package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.APP_VERSION
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun appHttpClient(json: Json) = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(json)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 3000
    }
    install(HttpRequestRetry) {
        retryOnServerErrors(1)
        retryOnException(1, true)
        constantDelay()
    }
    install(UserAgent) {
        agent = "skyecodes/vercors/$APP_VERSION (contact@skye.codes)"
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.INFO
    }
}

inline fun <reified T> HttpRequestBuilder.jsonBody(body: T) {
    setBody(body)
    contentType(ContentType.Application.Json)
    expectSuccess = true
}

