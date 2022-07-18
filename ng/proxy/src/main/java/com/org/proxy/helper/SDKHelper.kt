package com.org.proxy.helper

import android.app.Activity
import android.app.Application
import androidx.core.util.Consumer


/**
 * SDKHelper
 *
 * @author mmxm
 * @date 2022/7/14 22:19
 */
object SDKHelper {

    fun initSplash(activity:Activity,consumer: Consumer<Boolean>){
//        SplashManager.loadAd(object : SplashLoadCallback{
//            override fun loadEnd() {
//                VLog.i("开屏加载结束")
//                consumer.accept(true)
//            }
//        })
        consumer.accept(true)
    }

    fun init(application: Application){
//        PlamflatManager.init(application)
    }
}