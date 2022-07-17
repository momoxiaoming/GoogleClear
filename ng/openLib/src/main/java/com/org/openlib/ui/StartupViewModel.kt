package com.org.openlib.ui

import androidx.core.util.Consumer
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.base.arch.mvvm.AbsAppViewModel
import com.org.proxy.helper.SDKHelper


/**
 * Describe:
 *
 */
class StartupViewModel : AbsAppViewModel() {

    /**
     * 显示开屏广告
     */
    fun showSplashAd(activity: FragmentActivity, consumer: Consumer<Boolean>) {
        SDKHelper.initSplash(activity,consumer)
    }
}