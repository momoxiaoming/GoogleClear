package com.mckj.vest.defaultwifi.data

import com.google.gson.reflect.TypeToken
import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.entity.MenuJumpItem
import com.mckj.module.wifi.data.model.IHomeData
import com.mckj.baselib.util.JsonUtil
import com.mckj.module.wifi.gen.WifiSp
import com.mckj.module.wifi.utils.Log
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.vest.defaultwifi.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Describe:
 *
 * Created By yangb on 2021/4/22
 */
class HomeDataImpl : IHomeData {

    companion object {
        const val TAG = "HomeDataImpl"
    }

    override suspend fun getHomeMenuList(): List<MenuItem> {
        return withContext(Dispatchers.IO) {
            var result: List<MenuItem>? = null
            val text = WifiSp.instance.homeMenuConfig
            if (text.isNotEmpty()) {
                val type = object : TypeToken<List<MenuItem>>() {
                }.type
                result = JsonUtil.fromJson<List<MenuItem>>(text, type)
            }
            if (result == null || result.isEmpty()) {
                val list = mutableListOf<MenuItem>()
                list.add(MenuItem(ScenesType.TYPE_NETWORK_TEST, recommendAble = true))
                list.add(MenuItem(ScenesType.TYPE_POWER_SPEED))
                list.add(MenuItem(ScenesType.TYPE_HOT_SHARING))
                result = list
            }
            result
        }
    }

    override suspend fun saveHomeMenuList(list: List<MenuItem>): Boolean {
        return withContext(Dispatchers.IO) {
            val text = JsonUtil.toJson(list) ?: ""
            WifiSp.instance.homeMenuConfig = text
            true
        }
    }

    override suspend fun getBusinessMenuList(): List<MenuBusinessItem> {
        return withContext(Dispatchers.IO) {
            var result: List<MenuBusinessItem>? = null
            val text = WifiSp.instance.businessMenuConfig
            Log.i(TAG, "getBusinessMenuList: text:$text")
            if (text.isNotEmpty()) {
                val type = object : TypeToken<List<MenuBusinessItem>>() {
                }.type
                result = JsonUtil.fromJson<List<MenuBusinessItem>>(text, type)
            }
            if (result == null || result.isEmpty()) {
                val list = mutableListOf<MenuBusinessItem>()
                list.add(MenuBusinessItem(ScenesType.TYPE_WECHAT_SPEED, isAuditConfig = true, recommendAble = true))
//                list.add(MenuBusinessItem(ScenesType.TYPE_COOL_DOWN))
//                list.add(MenuBusinessItem(ScenesType.TYPE_ANTIVIRUS, isAuditConfig = true))
                list.add(MenuBusinessItem(ScenesType.TYPE_SHORT_VIDEO_CLEAN, isAuditConfig = true))
//                list.add(MenuBusinessItem(ScenesType.TYPE_CAMERA_CHECK))
                list.add(MenuBusinessItem(ScenesType.TYPE_QQ_CLEAN, isAuditConfig = true))
                list.add(MenuBusinessItem(ScenesType.TYPE_UNINSTALL_CLEAN, isAuditConfig = true))
//                list.add(MenuBusinessItem(ScenesType.TYPE_SIGNAL_SPEED))
                result = list
            }
            result
        }
    }

    override suspend fun saveBusinessMenuList(list: List<MenuBusinessItem>): Boolean {
        return withContext(Dispatchers.IO) {
            val text = JsonUtil.toJson(list) ?: ""
            WifiSp.instance.businessMenuConfig = text
            true
        }
    }

    override suspend fun getJumpMenuList(): List<MenuJumpItem> {
        return withContext(Dispatchers.IO) {
            var result: List<MenuJumpItem>? = null
            val text = WifiSp.instance.jumpMenuConfig
            Log.i(TAG, "getJumpMenuList: text:$text")
            if (text.isNotEmpty()) {
                val type = object : TypeToken<List<MenuJumpItem>>() {
                }.type
                result = JsonUtil.fromJson<List<MenuJumpItem>>(text, type)
            }
            if (result == null || result.isEmpty()) {
                val list = mutableListOf<MenuJumpItem>()
//                list.add(MenuJumpItem(ScenesType.TYPE_FILE_MANAGER, isAuditConfig = true))
                result = list
            }
            result
        }
    }

    override suspend fun saveJumpMenuList(list: List<MenuJumpItem>): Boolean {
        return withContext(Dispatchers.IO) {
            val text = JsonUtil.toJson(list) ?: ""
            WifiSp.instance.jumpMenuConfig = text
            true
        }
    }

    override fun getMenuResId(type: Int): Int {
        return when (type) {
            ScenesType.TYPE_SIGNAL_BOOST -> R.drawable.wifi_icon_scenes_signal_boost
            ScenesType.TYPE_NETWORK_TEST -> R.drawable.wifi_icon_scenes_network_test
            ScenesType.TYPE_SECURITY_CHECK -> R.drawable.wifi_icon_scenes_security_check
            ScenesType.TYPE_POWER_SPEED -> R.drawable.wifi_icon_scenes_power
            ScenesType.TYPE_NETWORK_CHECK -> R.drawable.wifi_icon_scenes_network_check
            ScenesType.TYPE_HOT_SHARING -> R.drawable.wifi_icon_scenes_hot_sharing

            ScenesType.TYPE_WECHAT_SPEED -> R.drawable.wifi_icon_scenes_wechat
            ScenesType.TYPE_COOL_DOWN -> R.drawable.wifi_icon_scenes_cool_down
            ScenesType.TYPE_ANTIVIRUS -> R.drawable.wifi_icon_scenes_antivirus
            ScenesType.TYPE_SHORT_VIDEO_CLEAN -> R.drawable.wifi_icon_scenes_short_video
            ScenesType.TYPE_CAMERA_CHECK -> R.drawable.wifi_icon_scenes_camera_check
            ScenesType.TYPE_QQ_CLEAN -> R.drawable.wifi_icon_scenes_qq
            ScenesType.TYPE_UNINSTALL_CLEAN -> R.drawable.wifi_icon_scenes_uninstall
            ScenesType.TYPE_SIGNAL_SPEED -> R.drawable.wifi_icon_scenes_signal_speed

            ScenesType.TYPE_FILE_MANAGER -> R.drawable.wifi_icon_scenes_file_manager
            else -> R.drawable.wifi_icon_scenes_signal_boost
        }
    }

}
