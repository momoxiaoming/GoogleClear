package com.dn.openlib.ui.splash

import android.Manifest
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.dn.openlib.time.TimeElapse
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.base.app.kt.resumedContStep
import com.dn.vi.app.base.helper.StepRunner
import com.dn.vi.app.base.helper.runOnce
import com.dn.vi.app.cm.kt.printTimeMillisSysLog
import java.util.concurrent.atomic.AtomicInteger

import kotlin.coroutines.resume

/**
 * 统一流程的入口activity
 * 可由子类自己继承
 *
 * Created by holmes on 2020/12/23.
 **/
open abstract class SimpleStartupActivity : ViActivity(){

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

    override fun onCreate(savedInstanceState: Bundle?) {
        printTimeMillisSysLog("startup, onCreate") {
            super.onCreate(savedInstanceState)
        }

        savedInstanceState?.getString(EXTRA_KEY_WALLPAPER_PASS)?.also { passed ->
            if (passed.isNotEmpty()) {
                wallpaperPassedState = passed
            }
        }
        // === $ ===

        // 屏蔽返回
        onBackPressedDispatcher.addCallback(ShieldBackpressCallback())
        StepRunner.runner(scope) {

            finishedAction = Runnable {
                onFinishAction()
            }
            breakAction = finishedAction

            val time = TimeElapse()
            time.begin()

            resumedContStep(this) { cont ->

                //协议页
                cont.resume(true)
            }

            resumedContStep(this) { cont ->
                //权限申请
                val runTaskSize = AtomicInteger(2)
                val checkRunnable = Runnable {
                    SplashHelper.log.i("show permission, runTaskSize:${runTaskSize.get()}")
                    if (runTaskSize.get() <= 0) {
                        cont.resume(true)
                    }
                }
                SplashHelper.requestPermissions(
                    this@SimpleStartupActivity,
                    getRequirePermissions(),
                    { phoneResult ->
                        //phone permission
                        runTaskSize.decrementAndGet()
                        checkRunnable.run()
                    }) { result ->
                    runTaskSize.decrementAndGet()
                    checkRunnable.run()
                }

            }

            resumedContStep(this) { cont ->


                // 显示壁纸

                cont.resume(true)
            }

            resumedContStep(this) { cont ->

                //显示开屏广告
                vm.showSplashAd(this@SimpleStartupActivity) { result ->
                    cont.resume(result)
                }
            }

            resumedContStep(this) { cont ->
                //显示小工具

                cont.resume(true)

            }

            step {

                redirectToMain()
                true
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // 单纯标一下退出
        // 看一下页面正常关闭情况，是否与 "app_start_show" 对应

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
     abstract fun redirectToMain()

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