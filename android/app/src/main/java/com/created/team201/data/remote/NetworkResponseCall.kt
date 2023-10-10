package com.created.team201.data.remote

import com.created.domain.model.response.NetworkResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class NetworkResponseCall<T : Any>(
    private val call: Call<T>,
) : Call<NetworkResponse<T>> {
    override fun clone(): Call<NetworkResponse<T>> = NetworkResponseCall(call.clone())

    override fun execute(): Response<NetworkResponse<T>> {
        throw UnsupportedOperationException(ERROR_DO_NOT_SUPPORT_EXECUTE)
    }

    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Success(responseBody)),
                        )
                    } ?: run {
                        @Suppress("UNCHECKED_CAST")
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Success(Unit as T)),
                        )
                    }
                } else {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(
                            NetworkResponse.Failure(
                                response.code(),
                                response.errorBody()?.string()
                            ),
                        ),
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val response = when (t) {
                    is IOException -> NetworkResponse.NetworkError(t)
                    else -> NetworkResponse.Unexpected(t)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(response))
            }
        })
    }

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()


    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()

    companion object {
        private const val ERROR_DO_NOT_SUPPORT_EXECUTE = "NetworkResponseCall은 execute를 지원하지 않습니다"
    }
}
