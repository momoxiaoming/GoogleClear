package com.org.proxy.helper

import android.app.Activity
import androidx.core.util.Consumer
import com.dn.vi.app.cm.log.VLog
import com.org.admodule.type.splash.SplashLoadCallback
import com.org.admodule.type.splash.SplashManager

/**
 * SDKHelper
 *
 * @author mmxm
 * @date 2022/7/14 22:19
 */
object SDKHelper {

    fun initSplash(activity:Activity,consumer: Consumer<Boolean>){
        SplashManager.loadAd(object : SplashLoadCallback{
            override fun loadEnd() {
                VLog.i("开屏加载结束")
                consumer.accept(true)
            }
        })
    }
}