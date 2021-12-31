package com.mckj.module.wifi.ui.dialog.connecting

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.NetworkState
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.helper.showToast
import com.mckj.baselib.util.launch
import com.mckj.module.wifi.data.WifiRepository
import com.mckj.module.wifi.entity.ConnectingEntity
import com.mckj.module.wifi.utils.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2020/10/21
 */
class ConnectingViewModel(private val repository: WifiRepository) : AbstractViewModel() {

    companion object {
        const val TAG = "ConnectingViewModel"
    }

    /**
     * 检测列表
     */
    val mListLiveData = MutableLiveData<List<ConnectingEntity>>()

    /**
     * 状态监听
     */
    val mPositionLiveData = MutableLiveData<Int>()

    /**
     * 百分比
     */
    val mPercentLiveData = MutableLiveData<Float>()

    private var mConnectTimeJob: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        mListLiveData.value = getConnectingData()
    }

    /**
     * 获取连接状态列表
     */
    private fun getConnectingData(): List<ConnectingEntity> {
        val list = mutableListOf<ConnectingEntity>()
        list.add(ConnectingEntity(type = ConnectingEntity.TYPE_CREATE_CONNECTING, name = "正在建立连接"))
        list.add(ConnectingEntity(type = ConnectingEntity.TYPE_WRITE_PASSWORD, name = "正在写入密码"))
        list.add(ConnectingEntity(type = ConnectingEntity.TYPE_VERIFY_PASSWORD, name = "正在验证密码"))
        return list
    }

    fun connecting(
        fragment: DialogFragment,
        wifiInfo: WifiInfo,
        password: String
    ) {
        Log.i(TAG, "connect: ")

        launch {
            changeConnectStart()
            val result = repository.iWifiData.connect(wifiInfo, password)
            if (result) {
                changeConnecting()
            } else {
                connectFinish(fragment, false)
            }
        }
    }

    fun connectTimeout(fragment: ConnectingDialogFragment, timeoutMillis: Long) {
        mConnectTimeJob?.cancel()
        mConnectTimeJob = launch {
            val time = System.currentTimeMillis()
            while (true) {
                delay(1000)
                val timeout = System.currentTimeMillis() - time
                if (timeout >= timeoutMillis) {
                    break
                }
                mPercentLiveData.value = 100f * timeout / timeoutMillis
            }
            //连接超时
            Log.i(TAG, "connectTimeout: 连接超时")
            showToast("连接超时")
            fragment.dispatchButtonClickObserver(NetworkState.ERROR_TIMEOUT.ordinal)
            connectFinish(fragment, false)
        }
    }

    private fun changeConnectStart() {
        getList()?.forEach {
            when (it.type) {
                ConnectingEntity.TYPE_CREATE_CONNECTING -> {
                    it.state = ConnectingEntity.STATE_LOADING
                    mPositionLiveData.value = it.position
                }
            }
        }
    }

    private fun changeConnecting() {
        getList()?.forEach {
            when (it.type) {
                ConnectingEntity.TYPE_CREATE_CONNECTING,
                ConnectingEntity.TYPE_WRITE_PASSWORD -> {
                    it.state = ConnectingEntity.STATE_RIGHT
                    it.state = ConnectingEntity.STATE_RIGHT
                    mPositionLiveData.value = it.position
                }
                ConnectingEntity.TYPE_VERIFY_PASSWORD -> {
                    it.state = ConnectingEntity.STATE_LOADING
                    mPositionLiveData.value = it.position
                }
            }
        }
    }

    fun connectFinish(
        fragment: DialogFragment,
        result: Boolean
    ) {
        launch {
            getList()?.forEach {
                when (it.type) {
                    ConnectingEntity.TYPE_VERIFY_PASSWORD -> {
                        it.state = if (result) {
                            ConnectingEntity.STATE_RIGHT
                        } else {
                            ConnectingEntity.STATE_LOADING
                        }
                        mPositionLiveData.value = it.position
                    }
                }
            }
            fragment.dismissAllowingStateLoss()
        }
    }

    /**
     * 验证已连接的WiFi
     */
    fun verifyConnectWifi(
        fragment: ConnectingDialogFragment,
        wifiInfo: WifiInfo,
    ) {
        launch {
            val connectInfo = repository.iWifiData.getConnectInfo()
            Log.i(TAG, "verifyConnectWifi: connectInfo.name:${connectInfo.name} ssid:${wifiInfo.ssid}")
            if (connectInfo.networkType == ConnectInfo.NetworkType.WIFI && connectInfo.name == wifiInfo.ssid) {
                showToast("连接成功")
                fragment.dispatchButtonClickObserver(NetworkState.CONNECTED.ordinal)
                connectFinish(fragment, true)
            } else {
                showToast("连接失败")
                fragment.dispatchButtonClickObserver(NetworkState.ERROR_AUTHENTICATING.ordinal)
                connectFinish(fragment, false)
            }
        }
    }

    private fun getList(): List<ConnectingEntity>? {
        return mListLiveData.value
    }

    override fun onCleared() {
        super.onCleared()
        mConnectTimeJob?.cancel()
    }

}