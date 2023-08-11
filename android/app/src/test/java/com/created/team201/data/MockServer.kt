package com.created.team201.data

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

object MockServer {
    val server = MockWebServer()
    private const val HEADER_NAME_TYPE: String = "Content-Type"
    private const val HEADER_VALUE_JSON: String = "application/json"
    private const val HEADER_LOCATION: String = "Location"
    private const val HEADER_VALUE_LOCATION: String = "location/studyId/1"

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val path = request.path ?: return MockResponse().setResponseCode(404)
            return when {
                path == "/v1/studies" && request.method == "POST" -> {
                    MockResponse()
                        .setHeader(HEADER_NAME_TYPE, HEADER_VALUE_JSON)
                        .setHeader(HEADER_LOCATION, HEADER_VALUE_LOCATION)
                        .setResponseCode(201)
                }

                path == "/v1/members/my" && request.method == "GET" -> {
                    MockResponse()
                        .setHeader(HEADER_NAME_TYPE, HEADER_VALUE_JSON)
                        .setResponseCode(200)
                        .setBody(myProfile)
                }

                path.startsWith("/v1/members/exists?") && request.method == "GET" -> {
                    MockResponse()
                        .setHeader(HEADER_NAME_TYPE, HEADER_VALUE_JSON)
                        .setResponseCode(200)
                        .setBody(nicknameValidationResponse)
                }

                path == "/v1/members/check-onboarding-is-done" && request.method == "GET" -> {
                    MockResponse()
                        .setHeader(HEADER_NAME_TYPE, HEADER_VALUE_JSON)
                        .setResponseCode(200)
                        .setBody(onBoardingIsDoneResponse)
                }

                else -> {
                    MockResponse().setResponseCode(404)
                }
            }
        }
    }

    private val myProfile: String =
        """ 
            {
                "id": 1,
                "nickname": "최강전사김진우",
                "githubId": "kimjinwoo",
                "profileImageUrl": null,
                "successRate": 77.4,
                "successfulRoundCount": 24,
                "tierProgress": 50, 
                "tier": 1,
                "introduction": "안녕하세요, 김진우입니다."
            }
        """.trimIndent()

    private val nicknameValidationResponse: String =
        """
            {
                "exists": false
            }
        """.trimIndent()

    private val onBoardingIsDoneResponse: String =
        """
            {
                "isOnboardingDone" : true
            }
        """.trimIndent()

    init {
        server.dispatcher = dispatcher
        server.start()
    }
}
