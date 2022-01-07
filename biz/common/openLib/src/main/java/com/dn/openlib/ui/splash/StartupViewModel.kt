package com.dn.openlib.ui.splash

import androidx.core.util.Consumer
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.base.arch.mvvm.AbsAppViewModel

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
        consumer.accept(true)
    }

}