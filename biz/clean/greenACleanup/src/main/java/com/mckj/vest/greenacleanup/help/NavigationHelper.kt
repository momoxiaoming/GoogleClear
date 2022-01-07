package com.mckj.vest.greenacleanup.help

import android.content.Context
import com.dn.openlib.OpenLibARouterPath
import com.dn.openlib.ui.startFragment


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
        context.startFragment(OpenLibARouterPath.ACTIVITY_CONTAINER_TITLE, OpenLibARouterPath.FRAGMENT_SETTING)
    }

    /**
     * 打开关于界面
     */
    fun openAbout(context: Context) {
        context.startFragment(OpenLibARouterPath.ACTIVITY_CONTAINER_TITLE, OpenLibARouterPath.FRAGMENT_ABOUT)
    }

}