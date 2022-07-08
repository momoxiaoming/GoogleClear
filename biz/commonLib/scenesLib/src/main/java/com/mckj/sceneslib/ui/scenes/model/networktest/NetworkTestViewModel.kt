package com.mckj.sceneslib.ui.scenes.model.networktest

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.entity.SpeedTestServer
import com.mckj.sceneslib.util.Log
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.entity.ResponseLiveData
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.http.ProgressListener
import com.mckj.baselib.util.launch
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesTask
import com.mckj.sceneslib.entity.ScenesTaskData
import com.mckj.sceneslib.util.NetUtil
import com.mckj.sceneslib.util.ShellUtil
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
class NetworkTestViewModel : AbstractViewModel() {

    companion object {
        const val TAG = "NetworkTestViewModel"
    }

    /**
     * 网络测速开始标志
     * 用来标记埋点
     */
    var isSpeedScanStart: Boolean = false
    val mNetworkSpeedLiveData = MutableLiveData<ResponseLiveData<Long>>()

    /**
     * 网路下载管理器
     */
    private var mNetworkDownloadManager: NetworkDownloadManager? = null
    private var mDownloadTimeoutJob: Job? = null

    /**
     * 测试网络速度
     */
    private suspend fun downloadTask(lifecycleOwner: LifecycleOwner): Boolean {
        return suspendCoroutine { cont ->
            val url = getDownloadUrl()
            Log.i(TAG, "testNetworkSpeed: url:$url")
            mNetworkDownloadManager?.close()
            val time = System.currentTimeMillis()
            var updateTime = 0L
            mNetworkDownloadManager = NetworkDownloadManager(lifecycleOwner).also {
                it.downloadFileLoop(url, object : ProgressListener {
                    override fun update(updateSize: Long, downloadSize: Long, totalSize: Long) {
                        val newTime = System.currentTimeMillis()
                        //限制更新频率
                        if (newTime - updateTime < 50) {
                            return
                        }
                        updateTime = newTime
                        val speed: Long = (downloadSize / ((newTime - time) / 1000f)).toLong()
                        Log.i(TAG, "update: speed:$speed")
                        mNetworkSpeedLiveData.postValue(ResponseLiveData.success(speed))
                    }

                    override fun onSuccess(boolean: Boolean) {
                        Log.i(TAG, "onSuccess: ")
                        mNetworkDownloadManager?.close()
                    }

                    override fun onFailed(throwable: Throwable) {
                        Log.i(TAG, "onFailed: ")
                        mNetworkSpeedLiveData.value = ResponseLiveData.error(-1, throwable.message)
                        mNetworkDownloadManager?.close()
                    }

                    override fun onClose() {
                        cont.resume(true)
                    }
                })
            }
        }
    }

    private suspend fun downloadTimeout() {
        Log.i(TAG, "downloadTimeout: ")
        mDownloadTimeoutJob?.cancel()
        mDownloadTimeoutJob = launch {
            //测试10秒
            delay(5000)
            Log.i(TAG, "downloadTimeout: 时间到")
            mNetworkDownloadManager?.close()
        }
    }

    private fun getDownloadUrl(): String {
        var result: String = ""
        do {

            result =  "http://softdown1.hao123.com/hao123-soft-online-bcs/soft/S/2013-07-24_slsdzl.exe"
        } while (false)
        return result
    }

    fun getSpeedName(speed: Long): String {
        return when {
            speed < 50 * 1024 -> {
                ResourceUtil.getString(R.string.scenes_snail)
            }
            speed < 300 * 1024 -> {
                ResourceUtil.getString(R.string.scenes_bicycle)
            }
            speed < 1024 * 1024 -> {
                ResourceUtil.getString(R.string.scenes_car)
            }
            speed < 10 * 1024 * 1024L -> {
                ResourceUtil.getString(R.string.scenes_airplane)
            }
            else -> {
                ResourceUtil.getString(R.string.scenes_rocket)
            }
        }
    }

    /**
     * 百分比
     */
    private val mPercentLiveData = MutableLiveData<Float>()

    fun getTaskData(lifecycleOwner: LifecycleOwner): ScenesTaskData? {
        mPercentLiveData.value = 0f
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_testing_connect_latency)) {
            testNetDelay()
            mPercentLiveData.value = 30f
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_testing_download)) {
            downloadTimeout()
            downloadTask(lifecycleOwner)
            mPercentLiveData.value = 60f
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_testing_upload)) {

            //模拟任务
            delay(200)

            mPercentLiveData.value = 100f
            return@ScenesTask true
        })
        return ScenesTaskData(ResourceUtil.getString(R.string.scenes_wifi_testing), "", taskList)
    }


    /**
     * 网络延时
     */
    val hostDelay = MutableLiveData<Int>()

    private suspend fun testNetDelay() {
        val host = "www.baidu.com"
        //ping6次，10s超时
        val cmd = "/system/bin/ping -c 6 -w 10 $host"
        var execCmd: ShellUtil.CommandResult? = null
        withContext(Dispatchers.IO) {
            execCmd = ShellUtil.execCmd(cmd)
        }
        val successMsg = execCmd?.successMsg

        if (successMsg.isNullOrEmpty()) {
            hostDelay.value = -1
        } else {
            val parsPingResult = NetUtil.parsPingResult(host, successMsg)
            hostDelay.value = parsPingResult.avg
        }
    }

}