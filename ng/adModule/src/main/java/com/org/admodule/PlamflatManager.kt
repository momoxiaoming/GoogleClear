package com.org.admodule

import android.app.Application
import android.os.Build
import android.util.Log
import android.webkit.WebView
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATDetectionResultCallback
import com.anythink.core.api.ATSDK
import com.anythink.core.api.DeviceInfoCallback
import com.dn.vi.app.cm.log.VLog
import java.util.*

/**
 * <pre>
 *     author:
 *     time  : 2022/7/9
 *     desc  : new class
 * </pre>
 */
object PlamflatManager {
    const val appid = "a5aa1f9deda26d"
    const val appKey = "4f7b9ac17decb9babec83aac078742c7"


    fun init(application: Application){
        //Android 9 or above must be set


        ATSDK.setNetworkLogDebug(BuildConfig.DEBUG)  //是否开启日志
        ATSDK.integrationChecking(application)  //检查平台集成状态
        ATSDK.testModeDeviceInfo(application,object :DeviceInfoCallback{
            override fun deviceInfo(p0: String?) {
                VLog.i("设备信息：$p0")
            }
        })


        ATSDK.setChannel("testChannle")  //设置渠道信息，用于TopOn后台区分广告数据
        ATSDK.setSubChannel("testSubChannle")//设置子渠道信息，用于TopOn后台区分广告数据

        ATSDK.init(
            application,
            appid,
            appKey
        )

//        val excludelist: kotlin.collections.MutableList = ArrayList<Any?>()
//        excludelist.add("com.exclude.myoffer1")
//        excludelist.add("com.exclude.myoffer2")
//        ATSDK.setExcludePackageList(excludelist)
//
//        Log.i("Demoapplication", "isChinaSDK:" + ATSDK.isCnSDK())
//        Log.i("Demoapplication", "SDKVersionName:" + ATSDK.getSDKVersionName())
//
//        val custommap: kotlin.collections.MutableMap<String, Any> = HashMap<String, Any>()
//        custommap.put("key1", "initCustomMap1")
//        custommap.put("key2", "initCustomMap2")
//        ATSDK.initCustomMap(custommap)
//
//        val subcustommap: kotlin.collections.MutableMap<String, Any> = HashMap<String, Any>()
//        subcustommap.put("key1", "initPlacementCustomMap1")
//        subcustommap.put("key2", "initPlacementCustomMap2")
//        ATSDK.initPlacementCustomMap("b5aa1fa4165ea3", subcustommap) //native  facebook
//
//
//
//        ATSDK.init(
//            this,
//            com.test.ad.demo.DemoApplication.appid,
//            com.test.ad.demo.DemoApplication.appKey
//        )


    }
}