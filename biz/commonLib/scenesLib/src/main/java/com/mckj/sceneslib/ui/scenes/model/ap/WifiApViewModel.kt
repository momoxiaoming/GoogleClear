package com.mckj.sceneslib.ui.scenes.model.ap

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.arch.mvvm.AbsAppViewModel
import com.dn.vi.app.base.lifecycle.ResourceLiveData
import com.dn.vi.app.base.lifecycle.wrapLoading
import com.dn.vi.app.base.lifecycle.wrapSuccess
import com.dn.vi.app.cm.log.VLog
import com.mckj.sceneslib.entity.SecurityCheckEntity
import com.mckj.sceneslib.manager.network.ap.ApManager
import com.org.openlib.utils.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull


/**
 * WIFI ap 状态
 * Created by holmes on 2021/4/21.
 **/
class WifiApViewModel : AbsAppViewModel() {

    var adLoaded: Boolean = false
    val log: VLog.Logger
        get() = VLog.scoped("ap-hotspot")

    val statusItem: ResourceLiveData<MutableList<SecurityCheckEntity>> = ResourceLiveData()

    /**
     * 是否有setting permission。
     * [Resource.Success] and [value]=true
     */
    val settingPermission: ResourceLiveData<Boolean> = ResourceLiveData()

    val sharedAp: ResourceLiveData<ApManager.ApInfo> = ResourceLiveData()

    private var settingCheckingStarter: Runnable? = null

    fun updateStatusItems() {
        val statusItemList = mutableListOf(
            SecurityCheckEntity(0, 0, "优化网络配置", SecurityCheckEntity.STATE_LOADING),
            SecurityCheckEntity(0, 0, "开始分享热点", SecurityCheckEntity.STATE_LOADING),
            SecurityCheckEntity(0, 0, "热点分享成功", SecurityCheckEntity.STATE_LOADING),
        )
        statusItem.value = statusItemList.wrapSuccess()
    }

    suspend fun startOp() {
        log.i("start op")
        val startTime = System.currentTimeMillis()

        opStep1()
        finishStep(0)
        opStep2()
        finishStep(1)
        opStep3()

        val timeCount = System.currentTimeMillis() - startTime
        //等待广告加载超时8秒
        val waitAdTime = 8 * 1000L
        if (timeCount < waitAdTime) {
            val timeOut = waitAdTime - timeCount
            Log.d("WifiApViewModel","waitAdLoadEnd($timeOut)")
            waitAdLoadEnd(timeOut)
        }
        finishStep(2)
    }

    /**
     * 等待广告加载结束
     */
    private suspend fun waitAdLoadEnd(timeout: Long = 5000): Boolean {
        val withTimeoutOrNull = withTimeoutOrNull(timeout) {
            var result = false
            while (true) {
                if (adLoaded) {
                    result = true
                    break
                }
                delay(100)
            }
            result
        }
        val b = withTimeoutOrNull ?: false
        Log.d("WifiApViewModel","waitAdLoadEnd($timeout),return($b)")
        return b
    }

    private fun finishStep(index: Int) {
        val list = statusItem.value?.data?.toMutableList() ?: return
        val item = list.get(index)
        val newItem = item.copy(state = SecurityCheckEntity.STATE_RIGHT)
        list.set(index, newItem)
        statusItem.value = list.wrapSuccess()
    }

    /**
     * 要在[create]的时候调用
     *
     * setting permission预check。
     * @return 如果没有权限，则会返回一个[Runnable]用于随时启动checking。
     * 返回null，则说明有权限
     * @see [settingPermission]
     */
    fun preCheckPermission(activity: ActivityResultCaller): Runnable? {
        settingPermission.value = false.wrapLoading()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val context = AppMod.app
            if (!Settings.System.canWrite(context)) {
                val starter =
                    activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                        object : ActivityResultCallback<ActivityResult> {
                            override fun onActivityResult(result: ActivityResult?) {
                                if (result == null) {
                                    log.w("request settings permission, but null result return")
                                    return
                                }
                                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                                    log.i("request settings permission, OK")
                                    settingPermission.value = true.wrapSuccess()
                                } else {
                                    // fixme 授权了，结果还是 resultCode == 0
                                    log.w("request settings permission, error (${result.resultCode})")
                                    settingPermission.value = false.wrapSuccess()
                                }
                            }
                        })

                val runnable = Runnable {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + context.getPackageName())
                    )
                    starter.launch(intent)
                }
                settingCheckingStarter = runnable
                return runnable
            } else {
                settingPermission.value = true.wrapSuccess()
            }
        } else {
            settingPermission.value = true.wrapSuccess()
        }
        return null
    }

    fun hasPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val context = AppMod.app
            if (!Settings.System.canWrite(context)) {
                return false
            }
        }
        return true
    }

    /**
     * 先调用[preCheckPermission]
     */
    fun checkPermission() {
        settingCheckingStarter?.run()
    }

    suspend fun opStep1() {
        log.i("start op step1")
        ApManager.disableWifi()
        delay(1000L)
    }

    suspend fun opStep2() {
        log.i("start op step2")
        val apInfo = ApManager.ApInfo("wifiap-123456", "12345678a")
        val sharedAp = ApManager.openAp(apInfo)
        this.sharedAp.value = sharedAp.wrapSuccess()
        delay(1000L)
    }

    suspend fun opStep3() {
        log.i("start op step3")
        val state = ApManager.getApState()
        log.i("ap state: ${state}")
        delay(1000L)

    }

}