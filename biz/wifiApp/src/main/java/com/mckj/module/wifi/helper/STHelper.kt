package com.mckj.module.wifi.helper

import com.mckj.module.wifi.entity.WifiMenuItem
import com.mckj.module.wifi.gen.St
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.network.WifiInfo

/**
 * Describe:
 *
 * Created By yangb on 2020/10/29
 */
object STHelper {

    /**
     * 记录菜单点击事件
     */
    fun menuClick(menu: MenuItem, isHome: Boolean) {
        when (menu.type) {
            WifiMenuItem.TYPE_HOTSPOT_INFO -> St.stWifilistGetpopHotspotinfoClick()
            WifiMenuItem.TYPE_REPORT_FISHING -> St.stWifilistGetpopAccusalClick()
            WifiMenuItem.TYPE_NETWORK_DISCONNECT -> St.stWifilistGetpopDisconnectClick()
        }
    }

    /**
     * wifi列表点击事件记录
     */
    fun itemClick(wifiInfo: WifiInfo) {
        if (wifiInfo.isConnect) {
            St.stWifilistGetClick()
        } else {
            St.stWifilistNoClick()
        }
    }

}