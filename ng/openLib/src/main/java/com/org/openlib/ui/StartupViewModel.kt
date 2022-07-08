package com.org.openlib.ui

import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.base.arch.mvvm.AbsAppViewModel
import com.org.openlib.help.Consumer


/**
 * Describe:
 *
 * Created By yangb on 2021/6/11
 */
class StartupViewModel : AbsAppViewModel() {

    /**
     * 显示开屏广告
     */
    fun showSplashAd(activity: FragmentActivity, consumer: Consumer<Boolean>) {
        val adName = "splash"
//        if (!AdManager.getInstance().hasCacheAd(adName)) {
//            //没有缓存直接下一步
//            AdManager.getInstance().loadAd(adName)
//            consumer.accept(true)
//            return
//        }
//        AdManager.getInstance().showAd(adName, activity, activity) { adResult ->
//            SplashHelper.log.i("showSplashAd: adResult:$adResult")
//            when (adResult.adStatus) {
//                AdStatus.ERROR,
//                AdStatus.CLOSE -> {
//                    consumer.accept(true)
//                }
//                else -> {
//                }
//            }
//        }
        consumer.accept(true)
    }

}