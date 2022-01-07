package com.mckj.sceneslib.manager.network

/**
 * Describe:
 *
 * Created By yangb on 2020/10/21
 */
data class NetworkStateEntity(
    val networkState: NetworkState,
    val isWifi: Boolean,
    val errorCode: Int = 0, //错误码，默认为0
    val errorMsg: String = "" //错误描述，默认为空
)