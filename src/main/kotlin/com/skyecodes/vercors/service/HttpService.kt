package com.skyecodes.vercors.service

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

interface HttpService {
    val client: HttpClient
}

inline fun <reified T> HttpRequestBuilder.jsonBody(body: T) {
    setBody(body)
    contentType(ContentType.Application.Json)
}