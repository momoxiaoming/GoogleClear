package com.mckj.sceneslib.manager.network

import com.dn.vi.app.cm.entity.SingleLiveData
import com.mckj.sceneslib.data.model.INetworkData
import com.mckj.sceneslib.data.model.impl.NetworkDataImp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Describe:网络数据
 *
 * Created By yangb on 2020/12/15
 */
class NetworkData private constructor() {

    companion object {
        const val TAG = "NetworkData"

        private val INSTANCE by lazy { NetworkData() }

        fun getInstance(): NetworkData = INSTANCE
    }

    /**
     * 网络数据接口
     */
    val iNetworkData: INetworkData by lazy { NetworkDataImp() }

    val mainScope by lazy { MainScope() }

    /**
     * 网络连接信息
     */
    val connectInfoLiveData = SingleLiveData<ConnectInfo>()

    /**
     * wifi状态
     */
    val wifiStateLiveData = SingleLiveData<WifiState>()

    /**
     * wifi列表
     */
    val wifiInfoListLiveData = SingleLiveData<List<WifiInfo>>()

    /**
     * 网络状态
     */
    val networkStateLiveData = SingleLiveData<NetworkStateEntity>()

    init {
        initLoad()
    }

    /**
     * 初始化加载
     */
    fun initLoad() {
        loadConnectInfo()
        loadWifiInfoList()
    }

    /**
     * 加载连接信息
     */
    fun loadConnectInfo() {
        mainScope.launch {
            val connectInfo = iNetworkData.getConnectInfo()
            connectInfoLiveData.setValue(connectInfo)
        }
    }

    /**
     * 加载wifi列表
     */
    fun loadWifiInfoList() {
        mainScope.launch {
            val list = iNetworkData.getWifiInfoList() ?: return@launch
            wifiInfoListLiveData.setValue(list)
        }
    }

    /**
     * 加载网络状态
     */
    fun loadNetworkState(
        networkState: NetworkState,
        isWifi: Boolean,
        errorCode: Int = 0,
        errorMsg: String = ""
    ) {
        if (networkState == NetworkState.CONNECTED || networkState == NetworkState.DISCONNECTED) {
            loadConnectInfo()
        }
        networkStateLiveData.setValue(NetworkStateEntity(networkState, isWifi, errorCode, errorMsg))
    }

    /**
     * 获取已连接的wifi信息
     */
    fun getConnectWifiInfo(): WifiInfo? {
        val list: List<WifiInfo>? = wifiInfoListLiveData.value
        return list?.find {
            it.isConnect
        }
    }

    /**
     * 加载wifi状态
     */
    fun loadWifiState(wifiState: WifiState) {
        wifiStateLiveData.setValue(wifiState)
    }

}