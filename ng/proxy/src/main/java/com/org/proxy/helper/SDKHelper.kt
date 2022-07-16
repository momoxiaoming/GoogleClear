package com.org.proxy.helper

import android.app.Activity
import com.org.admodule.type.splash.SplashManager

/**
 * SDKHelper
 *
 * @author mmxm
 * @date 2022/7/14 22:19
 */
object SDKHelper {

    fun initSplash(activity:Activity){
        SplashManager.init(activity)
        SplashManager.loadAd()
    }
}