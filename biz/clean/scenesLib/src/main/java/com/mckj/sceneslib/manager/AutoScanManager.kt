package com.mckj.sceneslib.manager

import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.entity.ScanData
import com.mckj.cleancore.entity.IJunkEntity

/**
 * Describe:扫描管理
 *
 * Created By yangb on 2021/3/3
 */
class AutoScanManager() {

    companion object {
        const val TAG = "AutoScanManager"

        private val INSTANCE by lazy { AutoScanManager() }

        fun getInstance(): AutoScanManager = INSTANCE

    }

    /**
     * 扫描数据
     */
    val scanDataLiveData = MutableLiveData<ScanData>(ScanData())

    /**
     * 添加扫描数据
     */
    fun add(entity: IJunkEntity) {
        val scanData = getScanData()
        scanData.add(entity)
        scanDataLiveData.value = scanData
    }

    /**
     * 添加扫描数据
     */
    fun postAdd(entity: IJunkEntity) {
        val scanData = getScanData()
        scanData.add(entity)
        scanDataLiveData.postValue(scanData)
    }

    fun setState(state: Int) {
        val scanData = getScanData()
        scanData.resetState(state)
        scanDataLiveData.value = scanData
    }

    fun resetData() {
        val scanData = getScanData()
        scanData.reset()
        scanDataLiveData.value = scanData
    }

    fun getScanData(): ScanData {
        return scanDataLiveData.value ?: ScanData()
    }

}