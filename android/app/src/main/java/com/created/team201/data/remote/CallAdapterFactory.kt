package com.created.team201.data.remote

import com.created.domain.model.response.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        check(returnType is ParameterizedType) {
            ERROR_TYPE_HAS_NOT_GENERIC
        }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != NetworkResponse::class.java) {
            return null
        }

        check(responseType is ParameterizedType) {
            ERROR_TYPE_HAS_NOT_NETWORK_RESPONSE
        }

        val successBodyType = getParameterUpperBound(0, responseType)
        return NetworkResponseCallAdapter<Any>(successBodyType)
    }

    companion object {
        private const val ERROR_TYPE_HAS_NOT_GENERIC = "반환 타입은 제네릭 인자를 가져야 합니다"
        private const val ERROR_TYPE_HAS_NOT_NETWORK_RESPONSE =
            "반환 타입은 NetworkResponse를 제네릭 인자로 가져야 합니다"
    }
}
