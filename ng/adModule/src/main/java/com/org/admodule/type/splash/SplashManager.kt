package com.org.admodule.type.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdExtraInfo
import com.anythink.splashad.api.ATSplashAdListener
import com.dn.vi.app.cm.log.VLog

/**
 * <pre>
 *     author:
 *     time  : 2022/7/10
 *     desc  : new class
 * </pre>
 */
object SplashManager {
    const val TAG="SplashManager"
    @SuppressLint("StaticFieldLeak")
    var splashAd: ATSplashAd? = null
    const val splash_placementId="b5bea7cc9a4497"

    val listener:ATSplashAdListener=object :ATSplashAdListener{
        override fun onAdLoaded(p0: Boolean) {
            VLog.scoped(TAG).i("onAdLoaded-->$p0")
        }

        override fun onAdLoadTimeout() {
            VLog.scoped(TAG).i("onAdLoadTimeout")

        }

        override fun onNoAdError(p0: AdError?) {
            VLog.scoped(TAG).i("onNoAdError-->$p0")

        }

        override fun onAdShow(p0: ATAdInfo?) {
            VLog.scoped(TAG).i("onAdShow-->$p0")

        }

        override fun onAdClick(p0: ATAdInfo?) {
            VLog.scoped(TAG).i("onAdClick-->$p0")
        }

        override fun onAdDismiss(p0: ATAdInfo?, p1: ATSplashAdExtraInfo?) {
            VLog.scoped(TAG).i("onAdDismiss-->$p0")
        }

    }

    fun init(context: Context, ) {
        val defaultConfig = ""
        splashAd = ATSplashAd(context, splash_placementId, listener, 5000, defaultConfig)
        ATSplashAd.entryAdScenario(splash_placementId, "")
    }


    fun loadAd() {
        splashAd?.loadAd()
    }

    fun isReadly(): Boolean {
        return splashAd?.isAdReady() == true
    }

    fun show(activity:Activity){
        if(isReadly()){
            val intent = Intent(activity, SplashAdShowActivity::class.java)
            intent.putExtra("placementId", splash_placementId)
        }
    }



}