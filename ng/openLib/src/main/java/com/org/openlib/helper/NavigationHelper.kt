package com.org.openlib.helper

import android.content.Context
import com.org.openlib.path.ARouterPath

/**
 * Describe:
 *
 * Created By yangb on 2020/12/22
 */
object NavigationHelper {

    /**
     * 打开设置界面
     */
    fun openSetting(context: Context) {
        context.startFragment(ARouterPath.ACTIVITY_CONTAINER_TITLE, ARouterPath.FRAGMENT_SETTING)
    }

    /**
     * 打开关于界面
     */
    fun openAbout(context: Context) {
        context.startFragment(ARouterPath.ACTIVITY_CONTAINER_TITLE, ARouterPath.FRAGMENT_ABOUT)
    }

}