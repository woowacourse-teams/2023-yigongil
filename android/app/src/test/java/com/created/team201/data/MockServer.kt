package com.created.team201.data

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

object MockServer {
    val server = MockWebServer()

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val path = request.path ?: return MockResponse().setResponseCode(404)
            return when {
                path == "/v1/studies" && request.method == "POST" -> {
                    MockResponse()
                        .setHeader("Content-Type", "application/json")
                        .setResponseCode(201)
                }

                else -> {
                    MockResponse().setResponseCode(404)
                }
            }
        }
    }

    init {
        server.dispatcher = dispatcher
        server.start()
    }
}
