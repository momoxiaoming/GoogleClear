package com.org.admodule.type.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdExtraInfo
import com.anythink.splashad.api.ATSplashAdListener
import com.anythink.splashad.api.IATSplashEyeAd
import com.dn.vi.app.cm.log.VLog
import com.org.admodule.utils.PlacementIdUtil

/**
 * <pre>
 *     author:
 *     time  : 2022/7/10
 *     desc  : new class
 * </pre>
 */
@SuppressLint("StaticFieldLeak")
object SplashManager {
    const val TAG = "SplashManager"

    private lateinit var splashAd: ATSplashAd
    private var loadCallback: SplashLoadCallback? = null
    private var container:ViewGroup?=null

    val listener: ATSplashAdListener = object : ATSplashAdListener {
        override fun onAdLoaded(p0: Boolean) {
            VLog.scoped(TAG).i("onAdLoaded-->$p0")
            loadCallback?.loadEnd()
        }

        override fun onAdLoadTimeout() {
            VLog.scoped(TAG).i("onAdLoadTimeout")
            loadCallback?.loadEnd()
        }

        override fun onNoAdError(p0: AdError?) {
            VLog.scoped(TAG).i("onNoAdError-->$p0")
            loadCallback?.loadEnd()
        }

        override fun onAdShow(p0: ATAdInfo?) {
            VLog.scoped(TAG).i("onAdShow-->$p0")

        }

        override fun onAdClick(p0: ATAdInfo?) {
            VLog.scoped(TAG).i("onAdClick-->$p0")
        }

        override fun onAdDismiss(p0: ATAdInfo?, p1: ATSplashAdExtraInfo?) {
            VLog.scoped(TAG).i("onAdDismiss-->$p0")
            dismiss()
        }
    }

    fun init(context: Context) {
        val placementId=PlacementIdUtil.getSplashPlacements(context)["All"]
        splashAd = ATSplashAd(context, placementId, listener, 5000, "")
        ATSplashAd.entryAdScenario(placementId, "")
    }


    fun loadAd(callback: SplashLoadCallback) {
        this.loadCallback = callback
        splashAd.loadAd()
    }
    fun loadAd() {
        splashAd.loadAd()
    }
    fun isReadly(): Boolean {
        return splashAd.isAdReady()
    }

    fun show(activity: Activity) {
        loadAd()
        if (isReadly()) {
            val container = activity.findViewById<ViewGroup>(android.R.id.content)
            splashAd.show(activity, container)
        } else {
            VLog.i("无缓存")
        }
    }

    fun show(activity: Activity,container:ViewGroup) {
        loadAd()
        if (isReadly()) {
            container.visibility = View.GONE
            this.container=container
            splashAd.show(activity, container)
        } else {
            VLog.i("无缓存")
        }
    }

    fun dismiss(){
        if (container != null) {
            container!!.removeAllViews()
            container!!.visibility = View.GONE
        }
    }
}