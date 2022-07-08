package com.mckj.sceneslib.ui.scenes.model.securitycheck

import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.data.SecurityCheckRepository
import com.mckj.sceneslib.entity.ScenesTask
import com.mckj.sceneslib.entity.ScenesTaskData
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.baselib.base.databinding.AbstractViewModel

/**
 * Describe:
 *
 * Created By yangb on 2020/10/21
 */
class SecurityCheckViewModel(private val repository: com.mckj.sceneslib.data.SecurityCheckRepository) :
    AbstractViewModel() {

    companion object {
        const val TAG = "WifiCheckViewModel"
    }

    /**
     * 百分比
     */
    val mPercentLiveData = MutableLiveData<Float>()

    /**
     * 异常数量
     */
    var mExceptionCountLiveData = MutableLiveData<Int>()

    fun getTaskData(): ScenesTaskData? {
        mPercentLiveData.value = 0f
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask("是否加密") {
            val result = repository.checkEncrypt(NetworkData.getInstance().getConnectWifiInfo())
            if (!result) {
                mExceptionCountLiveData.value = (mExceptionCountLiveData.value ?: 0) + 1
            }
            mPercentLiveData.value = 30f
            return@ScenesTask true
        })
        taskList.add(ScenesTask("DNS劫持") {
            val result = repository.checkDnsState(NetworkData.getInstance().getConnectWifiInfo())
            if (!result) {
                mExceptionCountLiveData.value = (mExceptionCountLiveData.value ?: 0) + 1
            }
            mPercentLiveData.value = 60f
            return@ScenesTask true
        })
        taskList.add(ScenesTask("网络安全") {
            val result =
                repository.checkNetworkSecurity(NetworkData.getInstance().getConnectWifiInfo())
            if (!result) {
                mExceptionCountLiveData.value = (mExceptionCountLiveData.value ?: 0) + 1
            }
            mPercentLiveData.value = 100f
            return@ScenesTask true
        })
        return ScenesTaskData("正在检测中...", "", taskList)
    }

}