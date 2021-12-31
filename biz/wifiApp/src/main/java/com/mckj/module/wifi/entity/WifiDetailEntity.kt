package com.mckj.module.wifi.entity

/**
 * Describe:
 *
 * Created By yangb on 2020/10/23
 */
data class WifiDetailEntity(
    var position: Int = 0,
    val type: Int,
    val name: String,
    val detail: String
) {
    companion object {
        //WiFi名称
        const val TYPE_NAME = 1

        //信号强度
        const val TYPE_SIGNAL = 2

        //加密方式
        const val TYPE_ENCRYPT = 3

        //最大连接速度
        const val TYPE_SPEED = 4

        //IP地址
        const val TYPE_IP = 5

        //MAC地址
        const val TYPE_MAC = 6
    }

}