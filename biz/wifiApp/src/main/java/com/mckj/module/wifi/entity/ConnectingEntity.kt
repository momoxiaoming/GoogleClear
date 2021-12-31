package com.mckj.module.wifi.entity

/**
 * Describe:
 *
 * Created By yangb on 2020/10/22
 */
data class ConnectingEntity(
    var position: Int = 0,
    val type: Int,
    val name: String,
    var state: Int = STATE_NORMAL
) {

    companion object {

        const val STATE_NORMAL = 1
        const val STATE_LOADING = 2
        const val STATE_RIGHT = 3

        //建立连接
        const val TYPE_CREATE_CONNECTING = 1

        //写入密码
        const val TYPE_WRITE_PASSWORD = 2

        //验证密码
        const val TYPE_VERIFY_PASSWORD = 3

    }

}
