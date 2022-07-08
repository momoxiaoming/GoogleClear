package com.mckj.sceneslib.data

import com.mckj.sceneslib.data.model.INetworkCheck
import com.mckj.sceneslib.data.model.impl.NetworkCheckImpl
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.NetworkData

/**
 * Describe:
 *
 * Created By yangb on 2021/4/23
 */
class NetworkCheckRepository {

    companion object {
        const val TAG = "NetworkCheckRepository"
    }

    /**
     * 网络检测接口
     */
    val iNetworkCheck: INetworkCheck by lazy { NetworkCheckImpl() }

    /**
     * 获取网络信息
     */
    fun getConnectInfo(): ConnectInfo? {
        return NetworkData.getInstance().connectInfoLiveData.value
    }

}