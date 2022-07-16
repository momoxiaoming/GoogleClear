package com.org.openlib.ui

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.base.app.kt.resumedContStep
import com.dn.vi.app.base.helper.StepRunner
import com.dn.vi.app.base.helper.runOnce
import com.dn.vi.app.cm.kt.printTimeMillisSysLog
import com.dn.vi.app.cm.log.VLog
import com.org.openlib.help.SplashHelper

import com.org.openlib.utils.RomUtils
import com.org.proxy.AppProxy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 * 统一流程的入口activity
 * 可由子类自己继承
 *
 * Created by holmes on 2020/12/23.
 **/
open class SimpleStartupActivity : ViActivity() {

    companion object {
        /**
         * "1" or "0"
         */
        const val EXTRA_KEY_WALLPAPER_PASS = "wpPassed"
    }

    private val vm: StartupViewModel by lazy {
        ViewModelProvider(this).get(StartupViewModel::class.java)
    }

    /**
     * 壁纸流程是否已经过了。
     * 主要是避免activity不停的重建
     *
     * 1 or 0
     */
    private var wallpaperPassedState = "0"

    override fun initLayout() {
    }

    /**
     * 需要的权限
     */
    open fun getRequirePermissions(): Array<String> {

        return arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    /**
     * 获取权限
     */
    private fun getPermissions(): Array<String> {
        val permissions = getRequirePermissions()
        //审核状态下不能申请sd卡权限
        val hasSd: Boolean = permissions.find {
            it == Manifest.permission.WRITE_EXTERNAL_STORAGE
        } != null
        return if (hasSd) {
            permissions
        } else {
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, *permissions)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        printTimeMillisSysLog("startup, onCreate") {
            super.onCreate(savedInstanceState)
        }

        savedInstanceState?.getString(EXTRA_KEY_WALLPAPER_PASS)?.also { passed ->
            if (passed.isNotEmpty()) {
                wallpaperPassedState = passed
            }
        }


        // 屏蔽返回
        onBackPressedDispatcher.addCallback(ShieldBackpressCallback())
        StepRunner.runner(scope) {

            finishedAction = Runnable {
                onFinishAction()
            }
            breakAction = Runnable {
                onBreakAction()
            }

            resumedContStep(this) { cont ->
                SplashHelper.requestPermissions(
                    this@SimpleStartupActivity,
                    getPermissions(),
                    { phoneResult ->
                    }) { result ->
                    cont.resume(true)
                }
            }


            resumedContStep(this) { cont ->
                //显示开屏广告
                AppProxy.postInitSdk()
                cont.resume(true)
            }


            resumedContStep(this) { cont ->

                //显示开屏广告
                vm.showSplashAd(this@SimpleStartupActivity) { result ->
                    cont.resume(result)
                }
            }

            step {

                redirectToMain()
                true
            }
        }
    }

    open fun doSomeBySplash(cont: Continuation<Boolean>) {
        cont.resume(true)
    }

    override fun onDestroy() {
        super.onDestroy()


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_KEY_WALLPAPER_PASS, wallpaperPassedState)
    }


    /**
     * 转到主页
     *
     * 子类处理
     */
    protected open fun redirectToMain() {

    }

    /**
     * 中断
     */
    protected open fun onBreakAction() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * 结束到本页
     */
    protected open fun onFinishAction() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private inner class ShieldBackpressCallback : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
            // do nothing
        }

    }

}