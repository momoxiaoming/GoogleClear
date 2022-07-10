package com.org.admodule.ad

import android.content.Context
import com.org.admodule.data.AdItem
import com.org.admodule.data.AdResult
import com.org.admodule.data.AdStatus
import java.util.function.Consumer

/**
 *
 * Created by yangb on 2022/2/11.
 */
object NewsAdHelper {

    /**
     * 加载广告
     */
    fun loadAd(adName: String, consumer: Consumer<AdResult<AdItem>>? = null) {

    }

//    /**
//     * 是否缓存广告
//     */
//    fun isCacheAd(adName: String): Boolean {
//        return AdManager.getInstance().hasCacheAd(adName)
//    }

}