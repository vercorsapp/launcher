package com.skyecodes.vercors.service.impl

import com.skyecodes.vercors.APP_VERSION
import com.skyecodes.vercors.appJson
import com.skyecodes.vercors.service.HttpService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*

class HttpServiceImpl : HttpService {
    override val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(appJson)
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 1000
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(3)
            retryOnException(3, true)
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
}