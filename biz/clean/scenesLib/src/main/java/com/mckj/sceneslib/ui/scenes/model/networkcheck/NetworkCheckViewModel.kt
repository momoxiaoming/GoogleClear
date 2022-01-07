package com.mckj.sceneslib.ui.scenes.model.networkcheck

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.data.NetworkCheckRepository
import com.mckj.sceneslib.entity.WifiDevice
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.dn.baselib.base.databinding.AbstractViewModel
import com.dn.baselib.ext.idToString
import com.dn.baselib.ext.launch
import com.dn.openlib.callback.LiveDataResponse
import com.mckj.sceneslib.R

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
                LiveDataResponse.failed(msg = idToString(R.string.scenes_plase_con_wifi))
            }
        } else {
            LiveDataResponse.failed(msg = idToString(R.string.scenes_plase_con_net))
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
            val list = mutableListOf<WifiDevice>()
            val result = repository.iNetworkCheck.scanWifiDevice(ip) {
                Log.i(TAG, "scanWifiDevice: it:$it")
                list.add(it)
                mWifiDeviceListLiveData.postValue(list)
            }
            block(result)
        }
    }

}