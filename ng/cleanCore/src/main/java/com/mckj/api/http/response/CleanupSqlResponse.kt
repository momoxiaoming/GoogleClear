package com.mckj.api.http.response

/**
 * @author xx
 * @version 1
 * @createTime 2021/8/3 12:26
 * @desc
 */
data class CleanupSqlResponse(
    val code: Int,
    val data: String? = "",
    val message: String? = "",
    val timestamp: Long
)