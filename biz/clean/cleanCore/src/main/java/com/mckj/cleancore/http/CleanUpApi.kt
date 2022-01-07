package com.mckj.cleancore.http

import com.mckj.cleancore.http.response.CleanupSqlResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/4 10:32
 * @desc
 */
interface CleanUpApi {

    @GET("get/cleandb")
    suspend fun getCleanDb(@Query("values") value: String): CleanupSqlResponse
}