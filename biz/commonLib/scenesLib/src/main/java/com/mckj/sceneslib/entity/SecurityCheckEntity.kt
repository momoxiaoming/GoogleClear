package com.mckj.sceneslib.entity

/**
 * Describe:
 *
 * Created By yangb on 2020/10/22
 */
data class SecurityCheckEntity(
    var position: Int = 0,
    val type : Int,
    val name: String,
    var state: Int = STATE_NORMAL
) {

    companion object {

        const val STATE_NORMAL = 1
        const val STATE_LOADING = 2
        const val STATE_RIGHT = 3
        const val STATE_WRONG = 4

        //是否加密
        const val TYPE_ENCRYPT = 1

        //DNS攻击
        const val TYPE_DNS_ATTACK = 2

        //网络安全
        const val TYPE_NETWORK_SECURITY = 3

    }

}
