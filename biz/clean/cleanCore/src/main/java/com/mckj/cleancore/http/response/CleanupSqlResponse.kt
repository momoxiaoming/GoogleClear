package com.mckj.cleancore.http.response

/**
 * @author leix
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