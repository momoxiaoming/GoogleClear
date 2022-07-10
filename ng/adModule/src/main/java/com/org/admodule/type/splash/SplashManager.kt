package com.org.admodule.type.splash

import android.content.Context
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdListener
import com.org.admodule.utils.PlacementIdUtil

/**
 * <pre>
 *     author:
 *     time  : 2022/7/10
 *     desc  : new class
 * </pre>
 */
object SplashManager {
    var splashAd: ATSplashAd? = null




    private fun init(context: Context,listtener:ATSplashAdListener) {
//       val placementIdMap: MutableMap<String, String> =PlacementIdUtil.getSplashPlacements(context)
        val placementId=""
        val defaultConfig=""
        splashAd = ATSplashAd(context, placementId, listtener, 5000, defaultConfig)
        ATSplashAd.entryAdScenario(placementId, "")
    }


    fun loadAd(){
        splashAd?.loadAd()
    }

    fun isReadly():Boolean{
        return splashAd?.isAdReady()==true
    }

}