package com.dn.baselib.http

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HttpUtil {

    private const val TAG = "HttpUtil"
    private const val BASE_URL = "https://pro.superclear.cn/"

    fun <T> getApi(clazz: Class<T>): T {
        return getRetrofit(getOkHttpClient()).create(clazz)
    }

    fun <T> getProcessApi(clazz: Class<T>, listener: ProgressListener): T {
        val builder = getOkHttpClientBuilder()
        builder.addInterceptor(ProgressInterceptor(listener))
        val retrofit = getRetrofit(builder.build())
        return retrofit.create(clazz)
    }

    fun getRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getOkHttpClient(): OkHttpClient {
        return getOkHttpClientBuilder()
            .build()
    }

    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)//连接超时设置
            .writeTimeout(5 * 60, TimeUnit.SECONDS)//写入超时设置，
            .readTimeout(5 * 60, TimeUnit.SECONDS)//读取超时设置
            .retryOnConnectionFailure(true)
    }

}