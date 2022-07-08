package com.mckj.sceneslib.manager.scenes.model

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import java.lang.Exception

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
open class AppManagerScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_APP_MANAGER,
            "app_manager",
            ResourceUtil.getString(R.string.scenes_app_manager),
            ResourceUtil.getString(R.string.scenes_manage_less_used_apps),
            ResourceUtil.getString(R.string.scenes_too_many_applications),
            ResourceUtil.getString(R.string.scenes_view_now),
            ResourceUtil.getString(R.string.scenes_app_manage_completed),
            ResourceUtil.getString(R.string.scenes_app_optimized),
            "home_management_click",
            recommendDesc = ResourceUtil.getString(R.string.scenes_app_manager_recommend_desc)
        )
    }

    override fun getGuideIconResId(): Int {
        return R.drawable.ic_land_app_manager
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(ResourceUtil.getString(R.string.scenes_haven_not_manage_your_apps_in_a_long_time), ResourceUtil.getString(
                    R.string.scenes_rarely_used_apps_taking_up_storage_space))
    }

    override fun jumpPage(context: Context, invoke: ((accept: Boolean) -> Unit)?): Boolean {
        try{
            handleClickEvent()
            ARouter.getInstance().build(ARouterPath.Cleanup.ACTIVITY_APP_MANAGER).navigation()
        }catch (e : Exception){
            return false
        }
        return true
    }
    override fun isRequestPermission(): Boolean {
        return true
    }
}