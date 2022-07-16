package com.mckj.api.manager

import com.mckj.api.http.CleanUpApi
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author xx
 * @version 1
 * @createTime 2021/8/3 12:26
 * @desc
 */
class RetrofitManage {

    val service: CleanUpApi
        get() = retrofit.create(CleanUpApi::class.java)
    var mClient: OkHttpClient? = null

    // 获取retrofit的实例
    private val retrofit: Retrofit by lazy {
        mClient = OkHttpClient.Builder().build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(mClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun getClient():OkHttpClient?{
        return mClient
    }

    companion object {
        const val BASE_URL = "https://app.superclear.cn/"
        val instance: RetrofitManage by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManage()
        }
    }

}