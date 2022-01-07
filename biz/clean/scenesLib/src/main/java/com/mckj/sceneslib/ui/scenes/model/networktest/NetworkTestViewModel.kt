package com.mckj.sceneslib.ui.scenes.model.networktest

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.entity.SpeedTestServer
import com.mckj.sceneslib.util.Log
import com.dn.baselib.base.databinding.AbstractViewModel
import com.dn.baselib.entity.ResponseLiveData
import com.dn.baselib.ext.idToString
import com.dn.baselib.ext.launch
import com.dn.baselib.http.ProgressListener
import com.mckj.sceneslib.R

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
class NetworkTestViewModel : AbstractViewModel() {

    companion object {
        const val TAG = "NetworkTestViewModel"
    }

    val mNetworkSpeedLiveData = MutableLiveData<ResponseLiveData<Long>>()

    /**
     * 网路下载管理器
     */
    private var mNetworkDownloadManager: NetworkDownloadManager? = null

    /**
     * 测试网络速度
     */
    fun testNetworkSpeed(lifecycleOwner: LifecycleOwner, block: (Boolean) -> Unit) {
        val url = getDownloadUrl()
        Log.i(TAG, "testNetworkSpeed: url:$url")
        mNetworkDownloadManager?.close()
        var time = 0L
        var updateTime = 0L
        var delayJob: Job? = null

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
                    delayJob?.cancel()
                    block(true)
                }

                override fun onFailed(throwable: Throwable) {
                    Log.i(TAG, "onFailed: ")
                    mNetworkSpeedLiveData.value = ResponseLiveData.error(-1, throwable.message)
                    delayJob?.cancel()
                    block(true)
                }
            })
        }
        time = System.currentTimeMillis()
        delayJob = launch {
            //测试10秒
            delay(10000)
            mNetworkDownloadManager?.close()
            block(true)
        }
    }


    private fun getDownloadUrl(): String {
        var result: String = ""
        do {
            result = "http://softdown1.hao123.com/hao123-soft-online-bcs/soft/S/2013-07-24_slsdzl.exe"
        } while (false)
        return result
    }

    fun getSpeedName(speed: Long): String {
        return when {
            speed < 50 * 1024 -> {
                idToString(R.string.scenes_wn)
            }
            speed < 300 * 1024 -> {
                idToString(R.string.scenes_net_zxc)
            }
            speed < 1024 * 1024 -> {
                idToString(R.string.scenes_net_qc)
            }
            speed < 10 * 1024 * 1024L -> {
                idToString(R.string.scenes_net_fj)
            }
            else -> {
                idToString(R.string.scenes_net_hj)
            }
        }
    }

}