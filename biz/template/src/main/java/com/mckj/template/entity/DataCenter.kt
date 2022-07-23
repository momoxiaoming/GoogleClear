package com.mckj.template.entity

import com.dn.vi.app.base.app.AppMod
import com.google.gson.reflect.TypeToken
import com.mckj.baselib.util.JsonUtil
import com.mckj.template.sp.CleanSpUtil
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.template.R

/**
 *  author : xx
 *  date : 2022/3/5 23:07
 *  description :数据处理
 */
class DataCenter {


    fun getMenuItem(
        type: Int,
        isRecommend: Boolean = false,
        isAuditConfig: Boolean = false,
        menuType: Int
    ): MenuItem {
        return MenuItem(
            type,
            isRecommend = isRecommend,
            isAuditConfig = isAuditConfig,
            resId = getResId(type),
            menuType = menuType
        )
    }

    //设置默认图标
    fun getResId(type: Int): Int {
        return when (type) {
            ScenesType.TYPE_PHONE_SPEED -> R.drawable.ic_func_phone_speed
            ScenesType.TYPE_COOL_DOWN -> R.drawable.ic_func_cool_down
            ScenesType.TYPE_WECHAT_SPEED -> R.drawable.ic_func_wechat
            ScenesType.TYPE_WX_CLEAN -> R.drawable.ic_func_wechat
            ScenesType.TYPE_QQ_SPEED -> R.drawable.ic_func_qq
            ScenesType.TYPE_QQ_CLEAN -> R.drawable.ic_func_qq
            ScenesType.TYPE_POWER_SAVE -> R.drawable.ic_func_power_save
            ScenesType.TYPE_ANTIVIRUS -> R.drawable.ic_func_moblie_antivirus
            ScenesType.TYPE_SHORT_VIDEO_CLEAN -> R.drawable.ic_func_shortvideo
            ScenesType.TYPE_UNINSTALL_CLEAN -> R.drawable.ic_func_esidual_clean
            ScenesType.TYPE_SIGNAL_SPEED -> R.drawable.ic_func_signal_opt
            ScenesType.TYPE_JUNK_CLEAN -> R.drawable.ic_func_junk_clean
            ScenesType.TYPE_NETWORK_TEST -> R.drawable.ic_func_network_test//图标缺少
            ScenesType.TYPE_CATON_SPEED -> R.drawable.ic_func_caton_opt
            ScenesType.TYPE_TOOLS -> R.drawable.ic_func_tools//
            ScenesType.TYPE_ALBUM_CLEAN -> R.drawable.ic_func_album
            ScenesType.TYPE_ENVELOPE_TEST -> R.drawable.ic_func_envelope_test
            ScenesType.TYPE_APP_MANAGER -> R.drawable.ic_func_app_manager
            ScenesType.TYPE_CAMERA_CHECK->R.drawable.ic_func_camera_check
            ScenesType.TYPE_ONE_KEY_SPEED -> R.drawable.ic_func_phone_speed
            ScenesType.TYPE_NETWORK_SPEED->R.drawable.ic_func_network_speed
            ScenesType.TYPE_DUST->R.drawable.ic_func_dust_clean
            ScenesType.TYPE_AUDIO->R.drawable.ic_func_vol_adjust
            ScenesType.TYPE_NOTIFY->R.drawable.ic_func_notice_clean
            ScenesType.TYPE_MAGNIFIER->R.drawable.ic_func_larger
            else -> R.drawable.ic_func_phone_speed
        }
    }


    fun updateRecommend(type: Int) {
        loadHomeConfig()?.let {
            var list = it.list
            if (list.isNullOrEmpty()) {
                list = mutableListOf()
            }
//            if (!list.contains(type)) {
//                list.add(HomeRecommendConfig(System.currentTimeMillis(), type))
//            }
            it.list = list
            saveHomeConfig(it)
        } ?: let {
            val list = mutableListOf<HomeRecommendConfig>()
            list.add(HomeRecommendConfig(System.currentTimeMillis(), type))
            val homeConfig = HomeConfig(list)
            saveHomeConfig(homeConfig)
        }
    }

    fun getRecommendConfig(): MutableList<HomeRecommendConfig>? {
        loadHomeConfig()?.let {
            return it.list
        }
        return null
    }

    fun loadHomeConfig(): HomeConfig? {
        val str = CleanSpUtil[AppMod.app, TepConstants.Sp.RECOMMEND_INFO, ""]
        if (str.isNotEmpty()) {
            val type = object : TypeToken<HomeConfig>() {
            }.type
            return JsonUtil.fromJson<HomeConfig>(str, type)
        }
        return null
    }

    fun saveHomeConfig(homeConfig: HomeConfig) {
        val str = JsonUtil.toJson(homeConfig) ?: ""
        CleanSpUtil.put(AppMod.app, TepConstants.Sp.RECOMMEND_INFO, str)
    }


}