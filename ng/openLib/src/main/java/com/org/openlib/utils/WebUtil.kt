package com.org.openlib.utils

import com.dn.vi.app.base.app.AppMod
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
    fun openProtocolByWeb() {

//        WebHelper.openWeb(null, WebHelper.Request(
//            webUrl = url,
//            title = "隐私政策",
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