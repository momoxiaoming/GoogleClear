package com.mckj.sceneslib.ui.scenes.model.networkcheck

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.data.NetworkCheckRepository
import com.mckj.sceneslib.entity.WifiDevice
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.launch
import com.org.openlib.help.LiveDataResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*

/**
 * Describe:
 *
 * Created By yangb on 2021/4/23
 */
class NetworkCheckViewModel(private val repository: com.mckj.sceneslib.data.NetworkCheckRepository) : AbstractViewModel() {

    companion object {
        const val TAG = "NetworkCheckViewModel"
    }

    var adCached: Boolean = false

    /**
     * wifi设备列表
     */
    val mWifiDeviceListLiveData: MutableLiveData<List<WifiDevice>> = MutableLiveData()

    /**
     * 网络信息
     */
    val mConnectInfoLiveData: MutableLiveData<LiveDataResponse<ConnectInfo>> = MutableLiveData()

    init {
        loadData()
    }

    fun loadData() {
        val connectInfo = repository.getConnectInfo()
        val liveData = if (connectInfo != null) {
            if (connectInfo.networkType == ConnectInfo.NetworkType.WIFI) {
                LiveDataResponse.success(connectInfo)
            } else {
                LiveDataResponse.failed(msg = "请连接WiFi")
            }
        } else {
            LiveDataResponse.failed(msg = "请连接网络")
        }
        mConnectInfoLiveData.value = liveData
    }

    fun scanWifiDevice(block: (Boolean) -> Unit) {
        launch {
            val liveData = mConnectInfoLiveData.value
            val ip: String? = liveData?.t?.ip
            if (ip.isNullOrEmpty()) {
                block(false)
                return@launch
            }

            val startTime = System.currentTimeMillis()

            val list = mutableListOf<WifiDevice>()
            val result = repository.iNetworkCheck.scanWifiDevice(ip) {
                Log.i(TAG, "scanWifiDevice: it:$it")
                list.add(it)
                mWifiDeviceListLiveData.postValue(list)
            }

            val taskCountTime = System.currentTimeMillis() - startTime
            val waitAdTime = 8*1000L
            //如果任务时间不足8秒，等待请求广告超时
            if (taskCountTime<waitAdTime){
                waitAdLoadEnd(waitAdTime-taskCountTime)
            }
            block(result)
        }
    }

    /**
     * 等待广告加载结束直到超时
     * @param timeout 超时时间
     * @return 广告加载完毕返回true，超时false
     */
    private suspend fun waitAdLoadEnd(timeout: Long = 5000): Boolean {
        return withTimeoutOrNull(timeout) {
            var result = false
            while (true) {
                if (adCached) {
                    result = true
                    break
                }
                delay(100)
            }
            result
        } ?: false
    }

}