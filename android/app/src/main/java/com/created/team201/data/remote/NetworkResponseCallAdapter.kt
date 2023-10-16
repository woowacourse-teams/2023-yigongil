package com.created.team201.data.remote

import com.created.domain.model.response.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkResponseCallAdapter<T : Any>(private val responseType: Type) :
    CallAdapter<T, Call<NetworkResponse<T>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Call<NetworkResponse<T>> = NetworkResponseCall(call)
}
