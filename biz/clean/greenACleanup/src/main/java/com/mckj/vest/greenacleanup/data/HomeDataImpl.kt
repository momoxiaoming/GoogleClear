package com.mckj.vest.greenacleanup.data

import com.dn.vi.app.cm.utils.JsonUtil
import com.google.gson.reflect.TypeToken
import com.mckj.module.cleanup.data.model.IHomeData
import com.mckj.module.cleanup.gen.CleanupSp
import com.mckj.module.cleanup.util.Log
import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.entity.MenuJumpItem
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.vest.greenacleanup.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
class HomeDataImpl : IHomeData {

    companion object {
        const val TAG = "CleanupImpl"
    }

    override suspend fun getHomeMenuList(): List<MenuItem> {
        return withContext(Dispatchers.IO) {
            var result: List<MenuItem>? = null
            val text = CleanupSp.instance.cleanupHomeMenuConfig
            if (text.isNotEmpty()) {
                val type = object : TypeToken<List<MenuItem>>() {
                }.type
                result = JsonUtil.fromJson<List<MenuItem>>(text, type)
            }
            if (result == null || result.isEmpty()) {
                val list = mutableListOf<MenuItem>()
                list.add(MenuItem(ScenesType.TYPE_PHONE_SPEED, recommendAble = true))
                list.add(MenuItem(ScenesType.TYPE_POWER_SAVE))
                list.add(MenuItem(ScenesType.TYPE_CAMERA_CHECK, recommendAble = true))
                result = list
            }
            result
        }
    }

    override suspend fun saveHomeMenuList(list: List<MenuItem>): Boolean {
        return withContext(Dispatchers.IO) {
            val text = JsonUtil.toJson(list) ?: ""
            Log.d("leix","saveHomeMenuList:--->$text")
            CleanupSp.instance.cleanupHomeMenuConfig = text
            true
        }
    }

    override suspend fun getBusinessMenuList(): List<MenuBusinessItem> {
        return withContext(Dispatchers.IO) {
            var result: List<MenuBusinessItem>? = null
            val text = CleanupSp.instance.cleanupBusinessMenuConfig
            Log.i(TAG, "getBusinessMenuList: text:$text")
            if (text.isNotEmpty()) {
                val type = object : TypeToken<List<MenuBusinessItem>>() {
                }.type
                result = JsonUtil.fromJson<List<MenuBusinessItem>>(text, type)
            }
            if (result == null || result.isEmpty()) {
                val list = mutableListOf<MenuBusinessItem>()
                list.add(MenuBusinessItem(ScenesType.TYPE_SIGNAL_SPEED))
                list.add(MenuBusinessItem(ScenesType.TYPE_SHORT_VIDEO_CLEAN, recommendAble = true))
                list.add(MenuBusinessItem(ScenesType.TYPE_ZIP_MANAGER, isAuditConfig = true))
                list.add(MenuBusinessItem(ScenesType.TYPE_HOT_SHARING))
                list.add(MenuBusinessItem(ScenesType.TYPE_NETWORK_TEST))
                result = list
            }
            result
        }
    }

    override suspend fun saveBusinessMenuList(list: List<MenuBusinessItem>): Boolean {
        return withContext(Dispatchers.IO) {
            Log.d(TAG,"list.size:${list.size}")
            val text = JsonUtil.toJson(list) ?: ""
            Log.i(TAG, "getJumpMenuList: text:$text")
            CleanupSp.instance.cleanupBusinessMenuConfig = text
            true
        }
    }

    override suspend fun getJumpMenuList(): List<MenuJumpItem> {
        return withContext(Dispatchers.IO) {
            var result: List<MenuJumpItem>? = null
            val text = CleanupSp.instance.cleanupJumpMenuConfig
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
            CleanupSp.instance.cleanupJumpMenuConfig = text
            true
        }
    }

    override fun getMenuResId(type: Int): Int {
        return when (type) {
            ScenesType.TYPE_PHONE_SPEED-> R.drawable.cleanup_icon_scenes_phone_speed
            ScenesType.TYPE_COOL_DOWN-> R.drawable.cleanup_icon_scenes_cool_down
            ScenesType.TYPE_WECHAT_CLEAN-> R.drawable.cleanup_icon_scenes_wechat_clean
            ScenesType.TYPE_POWER_SAVE-> R.drawable.cleanup_icon_scenes_power_save
            ScenesType.TYPE_NETWORK_SPEED-> R.drawable.cleanup_icon_scenes_network_speed
            ScenesType.TYPE_ANTIVIRUS-> R.drawable.cleanup_icon_scenes_antivirus

            ScenesType.TYPE_SHORT_VIDEO_CLEAN-> R.drawable.cleanup_icon_scenes_video_clean
            ScenesType.TYPE_CAMERA_CHECK-> R.drawable.cleanup_icon_scenes_camera_check
            ScenesType.TYPE_QQ_CLEAN-> R.drawable.cleanup_icon_scenes_qq_clean
            ScenesType.TYPE_UNINSTALL_CLEAN-> R.drawable.cleanup_icon_scenes_uninstall_clean
            ScenesType.TYPE_SIGNAL_SPEED-> R.drawable.cleanup_icon_scenes_signal_speed
            ScenesType.TYPE_HOT_SHARING-> R.drawable.cleanup_icon_scenes_signal_speed
            ScenesType.TYPE_NETWORK_TEST-> R.drawable.cleanup_icon_scenes_network_speed

            ScenesType.TYPE_FILE_MANAGER-> R.drawable.cleanup_icon_scenes_file_manager
            else-> R.drawable.cleanup_icon_scenes_phone_speed
        }

    }
}
