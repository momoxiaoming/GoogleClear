package com.mckj.cleancore.http.response

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/4 10:32
 * @desc 数据库版本信息
 */
data class VersionInfo(
    val version: Int=0,
    val url: String? = "",
    val MD5: String? = ""
)