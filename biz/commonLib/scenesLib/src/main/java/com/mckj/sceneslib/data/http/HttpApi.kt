package com.mckj.sceneslib.data.http

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Describe:
 *
 * Created By yangb on 2021/3/12
 */
interface HttpApi {

    /**
     * 下载文件
     */
    @Streaming
    @GET
    fun downloadFile(@Url url: String): Observable<ResponseBody>

}