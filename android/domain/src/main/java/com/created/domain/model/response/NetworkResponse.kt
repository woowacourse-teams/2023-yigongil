package com.created.domain.model.response

import java.io.IOException

sealed class NetworkResponse<out T : Any> {
    data class Success<T : Any>(val body: T) : NetworkResponse<T>()
    data class Failure(val responseCode: Int, val error: String?) : NetworkResponse<Nothing>()
    data class NetworkError(val exception: IOException) : NetworkResponse<Nothing>()
    data class Unexpected(val t: Throwable?) : NetworkResponse<Nothing>()
}
