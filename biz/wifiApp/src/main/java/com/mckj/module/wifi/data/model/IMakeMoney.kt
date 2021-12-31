package com.mckj.module.wifi.data.model

/**
 * Describe:
 *
 * Created By yangb on 2021/5/19
 */
interface IMakeMoney {

    /**
     * 红包请求
     */
    suspend fun requestEnvelope(): Boolean

}