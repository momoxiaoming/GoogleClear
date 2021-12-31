package com.mckj.module.wifi.ui.detail

import android.os.Build
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.manager.network.NetworkState
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.WifiUtil
import com.mckj.baselib.util.launch
import com.mckj.module.wifi.data.WifiRepository
import com.mckj.module.wifi.entity.WifiDetailEntity
import com.mckj.module.wifi.ui.dialog.CommonDialogFragment
import com.mckj.module.wifi.ui.dialog.InputPasswordDialogFragment
import com.mckj.module.wifi.utils.Log
import io.reactivex.rxjava3.kotlin.subscribeBy

/**
 * Describe:
 *
 * Created By yangb on 2020/10/21
 */
class WifiDetailViewModel(private val repository: WifiRepository) : AbstractViewModel() {

    companion object {
        const val TAG = "WifiDetailViewModel"
    }

    /**
     * wifi详情
     */
    val mDetailLiveData = MutableLiveData<List<WifiDetailEntity>>()

    fun removeConnect(activity: FragmentActivity, wifiInfo: WifiInfo) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            CommonDialogFragment.newInstance()
                .setTitle("无法忘记WiFi")
                .setContent("由于安卓6.0以上系统限制,需要到系统设置中操作")
                .setOnCancelListener {
                }
                .setOnConfirmListener("立即前往") {
                    WifiUtil.openWifiSetting()
                }
                .rxShow(activity.supportFragmentManager, CommonDialogFragment.TAG)
                .subscribeBy { code -> }
        } else {
            launch {
                val result = repository.iWifiData.removeConnect(wifiInfo)
                if (result) {
                    isFinish.value = true
                }
            }
        }
    }

    fun connect(activity: FragmentActivity, wifiInfo: WifiInfo) {
        InputPasswordDialogFragment.newInstance(wifiInfo)
            .rxShow(activity.supportFragmentManager, InputPasswordDialogFragment.TAG)
            .subscribeBy { code ->
                Log.i(TAG, "connect: code:$code")
                if (code == NetworkState.CONNECTED.ordinal) {
                    isFinish.value = true
                }
            }
    }

    fun loadWifiDetails(wifiInfo: WifiInfo) {
        launch {
            val list = mutableListOf<WifiDetailEntity>()
            list.add(
                WifiDetailEntity(
                    type = WifiDetailEntity.TYPE_NAME,
                    name = "WiFi名称",
                    detail = wifiInfo.ssid
                )
            )
            list.add(
                WifiDetailEntity(
                    type = WifiDetailEntity.TYPE_SIGNAL,
                    name = "信号强度",
                    detail = wifiInfo.getSignalLevelText()
                )
            )
            list.add(
                WifiDetailEntity(
                    type = WifiDetailEntity.TYPE_ENCRYPT,
                    name = "加密方式",
                    detail = wifiInfo.getWifiEncryptTypeText()
                )
            )
            if (wifiInfo.isConnect) {
                val connectInfo = repository.iWifiData.getConnectInfo()
                if (connectInfo.name == wifiInfo.ssid) {
                    list.add(
                        WifiDetailEntity(
                            type = WifiDetailEntity.TYPE_SPEED,
                            name = "最大网速",
                            detail = connectInfo.speed
                        )
                    )
                    list.add(
                        WifiDetailEntity(
                            type = WifiDetailEntity.TYPE_IP,
                            name = "分配IP地址",
                            detail = connectInfo.ip
                        )
                    )
                    list.add(
                        WifiDetailEntity(
                            type = WifiDetailEntity.TYPE_MAC,
                            name = "WiFi MAC地址",
                            detail = connectInfo.mac
                        )
                    )
                }
            }
            mDetailLiveData.value = list
        }
    }

}