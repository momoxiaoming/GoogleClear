package com.mckj.sceneslib.ui.scenes.model.securitycheck

import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.data.SecurityCheckRepository
import com.mckj.sceneslib.entity.ScenesTask
import com.mckj.sceneslib.entity.ScenesTaskData
import com.mckj.sceneslib.manager.network.NetworkData
import com.dn.baselib.base.databinding.AbstractViewModel
import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R

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
        taskList.add(ScenesTask(idToString(R.string.scenes_sec_aes)) {
            val result = repository.checkEncrypt(NetworkData.getInstance().getConnectWifiInfo())
            if (!result) {
                mExceptionCountLiveData.value = (mExceptionCountLiveData.value ?: 0) + 1
            }
            mPercentLiveData.value = 30f
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_sec_dns)) {
            val result = repository.checkDnsState(NetworkData.getInstance().getConnectWifiInfo())
            if (!result) {
                mExceptionCountLiveData.value = (mExceptionCountLiveData.value ?: 0) + 1
            }
            mPercentLiveData.value = 60f
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_sec_aq)) {
            val result =
                repository.checkNetworkSecurity(NetworkData.getInstance().getConnectWifiInfo())
            if (!result) {
                mExceptionCountLiveData.value = (mExceptionCountLiveData.value ?: 0) + 1
            }
            mPercentLiveData.value = 100f
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_sec_scanning), "", taskList)
    }

}