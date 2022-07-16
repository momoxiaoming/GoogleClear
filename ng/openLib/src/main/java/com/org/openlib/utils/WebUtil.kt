package com.org.openlib.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.cm.log.VLog
import com.org.openlib.R
import com.org.proxy.AppProxy
import com.org.openlib.ui.web.WebHelper

/**
 * Describe:
 *
 * Created By yangb on 2020/12/15
 */
object WebUtil {


    fun openFeed() {
        // https://pro.77pin.net/feedback/questionFeedback.html?value=base64(appid=xxxpid=xxxlsn=xxx
//        WebHelper.openWeb(null, WebHelper.Request(
//            webUrl = finalUrl,
//            title = "反馈",
//            contentKey = "feedback",
//        ))
    }



    /**
     * 打开用户协议
     */
    fun openUserAgreementByWeb() {


//        WebHelper.openWeb(null, WebHelper.Request(
//            webUrl = url,
//            title = "用户协议",
//            contentKey = "agreement",
//        ))

    }

    /**
     * 打开隐私政策
     */
    fun openProtocolByWeb(activity: Activity) {
        val intent=Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ebaylo.top/privacy.htm"))
        activity.startActivity(intent)

//        WebHelper.openWeb(null, WebHelper.Request(
//            webUrl = "https://www.ebaylo.top/privacy.htm",
//            title = AppMod.app.getString(R.string.string_open_privacy),
//            contentKey = "agreement",
//        ))
    }



    private fun getAppName(): String {
        return AppMod.getString(R.string.app_name)
    }

    private fun getChan(): String {
        return AppProxy.channel
    }

}