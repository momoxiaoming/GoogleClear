package com.dn.baselib.http

import okhttp3.Interceptor
import okhttp3.Response

class ProgressInterceptor(private val listener: ProgressListener) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder().body(ProgressResponseBody(response.body!!, listener)).build()
    }
}