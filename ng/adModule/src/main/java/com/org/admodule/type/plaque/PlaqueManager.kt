package com.org.admodule.type.plaque

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.anythink.interstitial.api.ATInterstitial
import com.anythink.interstitial.api.ATInterstitialAutoAd
import com.org.admodule.utils.PlacementIdUtil

/**
 * <pre>
 *     author:
 *     time  : 2022/7/10
 *     desc  : new class
 * </pre>
 */
object PlaqueManager {
    @SuppressLint("StaticFieldLeak")
    lateinit var mInterstitialAd: ATInterstitial
    fun init(context: Context) {
        val placementId = PlacementIdUtil.getInterstitialPlacements(context)["All"]
        ATInterstitialAutoAd.addPlacementId(placementId)
        mInterstitialAd = ATInterstitial(context, placementId)
        ATInterstitial.entryAdScenario(placementId, "")
    }

    fun load() {
        mInterstitialAd.load()
    }

    fun show(activity: Activity) {
        mInterstitialAd.show(activity)
    }

}