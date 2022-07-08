package com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean

import android.graphics.drawable.Drawable

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.util.main.network
 * @data  2022/3/28 10:35
 */
data class NetAppInfo(
    val appName: String,
    val appLogo: Drawable?,
    val appUid: Int,
    var totalRxBytesValue: Long = 0,
    var totalTxBytesValue:Long=0,
    var rxBytesValue: Long = 0,
    var txBytesValue: Long = 0,
    var readTime:Long=0
)
