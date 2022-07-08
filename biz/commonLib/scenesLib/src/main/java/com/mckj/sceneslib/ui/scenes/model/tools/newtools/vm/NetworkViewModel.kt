package com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NetAppInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.CheckNetWorkHelper
import com.mckj.sceneslib.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm
 * @data  2022/4/11 15:55
 */

class NetworkViewModel : AbstractViewModel() {

    private val allAppInfo by lazy { arrayListOf<NetAppInfo>() }

    val currentAppList by lazy { MutableLiveData<List<NetAppInfo>>() }

    val currentNetSpeed by lazy { MutableLiveData<Array<Long>>() }

    val currentNetWork by lazy { MutableLiveData<String>() }

    private var oldRxBytesValue = 0L
    private var oldTxBytesValue = 0L
    private var lastTime = 0L


    @RequiresApi(Build.VERSION_CODES.M)
    fun getCurrentSpeed() {
        val nowTime = System.currentTimeMillis()
        val totalRxBytes = CheckNetWorkHelper.getTotalRxBytes()
        val totalTxBytes = CheckNetWorkHelper.getTotalTxBytes()
//        val allMonthMobile = CheckNetWorkHelper.getAllMonthMobile()
//        val totalRxBytes = allMonthMobile[0]
//        val totalTxBytes = allMonthMobile[1]

        val time = (nowTime - lastTime)/1000
        Log.d("9999","---------------time---------$time")
        if (time>0){
            val totalRxInternal = (totalRxBytes - oldRxBytesValue)
            val totalTxInternal = (totalTxBytes - oldTxBytesValue)
            Log.d("9999","----------------$totalRxInternal--------$totalTxInternal")
            val currentRxSpeed = if (totalRxInternal > 0) totalRxInternal / time  else 0
            val currentTxSpeed = if (totalTxInternal > 0) totalTxInternal / time else  0
            lastTime = nowTime
            oldRxBytesValue = totalRxBytes
            oldTxBytesValue = totalTxBytes
            currentNetSpeed.postValue(arrayOf(currentRxSpeed, currentTxSpeed))
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun showChangeNetwork() {
        viewModelScope.launch(Dispatchers.Default) {
            //提前显示
            val totalBytesArray = arrayOf(0L, 0L)
            allAppInfo.addAll(CheckNetWorkHelper.getCurrentAppData())
            currentAppList.postValue(allAppInfo)
            getCurrentSpeed()
            // currentNetSpeed.postValue(totalBytesArray)
            do {
                currentNetWork.postValue(CheckNetWorkHelper.showCurrentNetWork())
                var totalRxBytes = 0L
                var totalTxBytes = 0L
                allAppInfo.forEach {
                    val bytesValue = CheckNetWorkHelper.getBytesValue(it.appUid)
                    val totalRxValue = bytesValue[0]
                    val totalTxValue = bytesValue[1]

                    val nowTime = System.currentTimeMillis()
                    val second = (nowTime - it.readTime) / 1000
                    if (it.totalRxBytesValue > 0) {
                        it.rxBytesValue = (totalRxValue - it.totalRxBytesValue) / second
                        it.txBytesValue = (totalTxValue - it.totalTxBytesValue) / second
                    }
                    totalRxBytes += it.rxBytesValue
                    totalTxBytes += it.txBytesValue

                    it.totalRxBytesValue = totalRxValue
                    it.totalTxBytesValue = totalTxValue
                    it.readTime = nowTime
                }

                allAppInfo.sortByDescending { it.rxBytesValue }
                totalBytesArray[0] = totalRxBytes
                totalBytesArray[1] = totalTxBytes
                getCurrentSpeed()
                if (totalRxBytes > 0 || totalTxBytes > 0) currentNetSpeed.postValue(totalBytesArray)
                currentAppList.postValue(allAppInfo)
                delay(1000)
            } while (true)

        }
    }

}