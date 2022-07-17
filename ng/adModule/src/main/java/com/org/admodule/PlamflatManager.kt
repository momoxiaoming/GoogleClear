package com.org.admodule

import android.app.Application
import android.content.Context
import android.util.Log
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATDetectionResultCallback
import com.anythink.core.api.ATSDK
import com.org.admodule.type.plaque.PlaqueManager
import com.org.admodule.type.splash.SplashManager
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
    fun init(application: Application) {
        //Android 9 or above must be set
//        val appid = MetaData.getMetaValue(application,"TOP_ON_APPID","")
//        val appKey = MetaData.getMetaValue(application,"TOP_ON_APPKEY","")
//
//        val channel = MetaData.getMetaValue(application,"TOP_ON_CHANNEL","")
//        val sub_channel = MetaData.getMetaValue(application,"TOP_ON_SUBCHANNEL","")
//
//        ATSDK.setNetworkLogDebug(BuildConfig.DEBUG)  //是否开启日志
//        ATSDK.integrationChecking(application)  //检查平台集成状态
//        ATSDK.testModeDeviceInfo(application, object : DeviceInfoCallback {
//            override fun deviceInfo(p0: String?) {
//                VLog.i("设备信息：$p0")
//            }
//        })
//
//
//        ATSDK.setChannel(channel)  //设置渠道信息，用于TopOn后台区分广告数据
//        ATSDK.setSubChannel(sub_channel)//设置子渠道信息，用于TopOn后台区分广告数据
//
//        ATSDK.init(
//            application,
//            appid,
//            appKey
//        )

        //        ATSDK.deniedUploadDeviceInfo(
//                DeviceDataInfo.DEVICE_SCREEN_SIZE
//                , DeviceDataInfo.ANDROID_ID
//                , DeviceDataInfo.APP_PACKAGE_NAME
//                , DeviceDataInfo.APP_VERSION_CODE
//                , DeviceDataInfo.APP_VERSION_NAME
//                , DeviceDataInfo.BRAND
//                , DeviceDataInfo.GAID
//                , DeviceDataInfo.LANGUAGE
//                , DeviceDataInfo.MCC
//                , DeviceDataInfo.MNC
//                , DeviceDataInfo.MODEL
//                , DeviceDataInfo.ORIENTATION
//                , DeviceDataInfo.OS_VERSION_CODE
//                , DeviceDataInfo.OS_VERSION_NAME
//                , DeviceDataInfo.TIMEZONE
//                , DeviceDataInfo.USER_AGENT
//                , DeviceDataInfo.NETWORK_TYPE
//                , ChinaDeviceDataInfo.IMEI
//                , ChinaDeviceDataInfo.MAC
//                , ChinaDeviceDataInfo.OAID
//                , DeviceDataInfo.INSTALLER
//
//        );

        ATSDK.setNetworkLogDebug(true)
        ATSDK.integrationChecking(application)

        ATSDK.setChannel("testChannle")
        ATSDK.setSubChannel("testSubChannle")

        val excludelist: MutableList<String> = ArrayList<String>()
        excludelist.add("com.exclude.myoffer1")
        excludelist.add("com.exclude.myoffer2")
        ATSDK.setExcludePackageList(excludelist)

        Log.i("Demoapplication", "isChinaSDK:" + ATSDK.isCnSDK())
        Log.i("Demoapplication", "SDKVersionName:" + ATSDK.getSDKVersionName())

        val custommap: MutableMap<String, Any> = HashMap()
        custommap["key1"] = "initCustomMap1"
        custommap["key2"] = "initCustomMap2"
        ATSDK.initCustomMap(custommap)

        val subcustommap: MutableMap<String, Any> = HashMap()
        subcustommap["key1"] = "initPlacementCustomMap1"
        subcustommap["key2"] = "initPlacementCustomMap2"
        ATSDK.initPlacementCustomMap("b5aa1fa4165ea3", subcustommap) //native  facebook

        ATSDK.setDetectionListener(object : ATDetectionResultCallback {
            override fun onSucess(rdid: String) {
                Log.i("Demoapplication", "Detection id:$rdid")
            }

            override fun onError(errorMsg: String) {
                Log.e("Demoapplication", "Detection init error: $errorMsg")
            }
        })
        ATSDK.setPersonalizedAdStatus(ATAdConst.PRIVACY.PERSIONALIZED_ALLOW_STATUS)
        ATSDK.init(
            application,
            appid,
            appKey
        )

        ATSDK.testModeDeviceInfo(application, null)
        initAd(application)
    }

    private fun initAd(context: Context){
        SplashManager.init(context)
        PlaqueManager.init(context)
    }
}