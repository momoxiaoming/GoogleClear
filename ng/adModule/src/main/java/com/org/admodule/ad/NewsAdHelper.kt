package com.org.admodule.ad

import android.content.Context
import com.google.oia.IKots
import com.mckj.api.biz.ad.AdActivity
import com.mckj.api.biz.ad.AdManager
import com.mckj.api.biz.ad.entity.AdResult
import com.mckj.api.biz.ad.entity.ad.AdItem
import com.mckj.api.biz.ad.entity.ad.AdStatus
import com.mckj.api.entity.Consumer
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
        if (consumer != null) {
            AdManager.getInstance().loadAd(adName, consumer)
        } else {
            AdManager.getInstance().loadAd(adName)
        }
    }

    /**
     * 是否缓存广告
     */
    fun isCacheAd(adName: String): Boolean {
        return AdManager.getInstance().hasCacheAd(adName)
    }

    fun showAd(
        context: Context,
        adName: String,
        callback: Consumer<AdStatus>? = null
    ): Boolean {
        return showAd(context, adName, true, callback)
    }

    fun showAdx(
        context: Context,
        adName: String,
        callback: Consumer<AdResult<AdItem>>? = null
    ): Boolean {
        return showAdx(context, adName, true, callback)
    }

    fun showAd(
        context: Context,
        adName: String,
        isWaitLoad: Boolean = true,
        callback: Consumer<AdStatus>? = null
    ): Boolean {
        val config = AdActivity.Config(adName)
        return showAd(context, config, isWaitLoad, callback)
    }

    /**
     * 显示广告
     *
     * @param adName 广告名
     * @param isWaitLoad 是否等待加载
     * @param callback 广告回调
     */
    fun showAdx(
        context: Context,
        adName: String,
        isWaitLoad: Boolean = true,
        callback: Consumer<AdResult<AdItem>>? = null
    ): Boolean {
        val config = AdActivity.Config(adName)
        return showAdx(context, config, isWaitLoad, callback)
    }

    fun showAd(
        context: Context,
        config: AdActivity.Config,
        isWaitLoad: Boolean = true,
        callback: Consumer<AdStatus>? = null
    ): Boolean {
        return showAdx(context, config, isWaitLoad) { adResult ->
            callback?.accept(adResult.adStatus)
        }
    }

    /**
     * 显示广告
     *
     * @param context 上下文
     * @param config 广告显示配置
     * @param isWaitLoad 是否等待加载
     * @param callback 广告回调
     */
    fun showAdx(
        context: Context,
        config: AdActivity.Config,
        isWaitLoad: Boolean = true,
        callback: Consumer<AdResult<AdItem>>? = null
    ): Boolean {
        //不等待加载，判断是否有缓存
        if (!isWaitLoad && !AdManager.getInstance().hasCacheAd(config.name)) {
            callback?.accept(AdResult(AdItem(config.name), AdStatus.ERROR, "实时显示失败，没有缓存"))
            return false
        }
        AdActivity.startActivity(context, config, AdCallback(context, callback))
        return true
    }

    private class AdCallback(
        private val context: Context,
        private val callback: Consumer<AdResult<AdItem>>?
    ) : Consumer<AdResult<AdItem>> {

        override fun accept(t: AdResult<AdItem>) {
            callback?.accept(t)
            when (t.adStatus) {
                AdStatus.CLICK -> {
                    //如果容器实现了抢占接口，则广告点击后置位false
                    if (context is IKots) {
                        context.setKotsEnable(false)
                    }
                }
                else -> {
                }
            }
        }
    }

}