package com.mckj.module.wifi.entity

import android.annotation.SuppressLint
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
@SuppressLint("ParcelCreator")
class WifiMenuItem(
    type: Int,
    update: Long = 0L,
    recommendAble: Boolean = false,
    isAuditConfig: Boolean = false
) : MenuItem(type, update, recommendAble, isAuditConfig) {

    companion object {

        //网络测速
        const val TYPE_NETWORK_TEST = ScenesType.TYPE_NETWORK_TEST
//        const val TYPE_NETWORK_TEST = 1001


        //安全检测
        const val TYPE_SECURITY_CHECK = ScenesType.TYPE_SECURITY_CHECK
//        const val TYPE_NETWORK_TEST = 1002

        //信号增强
        const val TYPE_NETWORK_SIGNAL_BOOST = ScenesType.TYPE_SIGNAL_BOOST
//        const val TYPE_NETWORK_SIGNAL_BOOST = 1003

        //一键加速
        const val TYPE_NETWORK_SPEED_UP = ScenesType.TYPE_NETWORK_SPEED
//        const val TYPE_NETWORK_SPEED_UP = 1004

        //热点信息
        const val TYPE_HOTSPOT_INFO = 1005

        //举报钓鱼
        const val TYPE_REPORT_FISHING = 1006

        //断开网络
        const val TYPE_NETWORK_DISCONNECT = 1007

        //密码连接
        const val TYPE_PWD_CONNECT = 1008

        //忘记网络
        const val TYPE_NETWORK_FORGET = 1009
    }

}