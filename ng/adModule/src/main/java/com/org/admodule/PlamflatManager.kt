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
import com.dn.vi.app.cm.utils.MetaData
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


    fun init(application: Application) {
        //Android 9 or above must be set
        val appid = MetaData.getMetaValue(application,"TOP_ON_APPID","")
        val appKey = MetaData.getMetaValue(application,"TOP_ON_APPKEY","")

        val channel = MetaData.getMetaValue(application,"TOP_ON_CHANNEL","")
        val sub_channel = MetaData.getMetaValue(application,"TOP_ON_SUBCHANNEL","")

        ATSDK.setNetworkLogDebug(BuildConfig.DEBUG)  //是否开启日志
        ATSDK.integrationChecking(application)  //检查平台集成状态
        ATSDK.testModeDeviceInfo(application, object : DeviceInfoCallback {
            override fun deviceInfo(p0: String?) {
                VLog.i("设备信息：$p0")
            }
        })


        ATSDK.setChannel(channel)  //设置渠道信息，用于TopOn后台区分广告数据
        ATSDK.setSubChannel(sub_channel)//设置子渠道信息，用于TopOn后台区分广告数据

        ATSDK.init(
            application,
            appid,
            appKey
        )
    }
}