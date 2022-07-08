package com.mckj.api.data

import com.mckj.api.http.response.VersionInfo

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/7 10:22
 * @desc
 */
data class DealData(
    var queryData: String = "",
    var isFetchDataSuccess: Boolean = false,
    var responseFromRemote: VersionInfo? = null,
    var prepareLoadDb:Boolean = false
)