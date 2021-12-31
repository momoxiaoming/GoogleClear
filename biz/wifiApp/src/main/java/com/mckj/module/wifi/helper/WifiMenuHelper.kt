package com.mckj.module.wifi.helper

import androidx.fragment.app.FragmentActivity
import com.mckj.baselib.helper.showToast
import com.mckj.baselib.util.WifiUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.module.wifi.ui.dialog.CommonDialogFragment
import com.mckj.module.wifi.ui.dialog.InputPasswordDialogFragment
import com.mckj.module.wifi.ui.dialog.MenuDialogFragment
import com.mckj.module.wifi.ui.dialog.connecting.ConnectingDialogFragment
import com.mckj.module.wifi.utils.Log
import com.mckj.openlib.helper.startFragment
import com.mckj.sceneslib.manager.network.WifiInfo
import io.reactivex.rxjava3.kotlin.subscribeBy

/**
 * Describe:Wifi菜单帮助类
 *
 * Created By yangb on 2020/10/22
 */
object WifiMenuHelper {

    const val TAG = "WifiMenuHelper"

    /**
     * 跳转热点信息
     */
    fun jumpWifiDetail(activity: FragmentActivity, wifiInfo: WifiInfo?): Boolean {
        if (wifiInfo == null) {
            showToast("wifi信息不能为空")
            return false
        }
        activity.startFragment(ARouterPath.Wifi.FRAGMENT_DETAIL) {
            it.withParcelable("wifi_info", wifiInfo)
        }
        return true
    }

    /**
     * 显示智能连接
     */
    fun showSmartConnectDialog(activity: FragmentActivity, wifiInfo: WifiInfo?): Boolean {
        if (wifiInfo == null) {
            showToast("wifi信息不能为空")
            return false
        }
        STHelper.itemClick(wifiInfo)
        if (wifiInfo.isConnect) {
            Log.e(TAG, "connect: 已连接,弹出详情")
            MenuDialogFragment.newInstance(wifiInfo)
                .rxShow(activity.supportFragmentManager, MenuDialogFragment.TAG)
                .subscribeBy { code -> }
            return true
        }
        return if (wifiInfo.isExists || wifiInfo.getWifiEncryptType() == WifiUtil.WIFI_CIPHER_NO_PASS) {
            Log.i(TAG, "connect: 不需要密码连接")
            showConnectDialog(activity, wifiInfo)
        } else {
            Log.i(TAG, "connect: 需要密码连接")
            showInputPasswordDialog(activity, wifiInfo)
        }
    }

    /**
     * 显示密码连接
     */
    fun showInputPasswordDialog(activity: FragmentActivity, wifiInfo: WifiInfo?): Boolean {
        if (wifiInfo == null) {
            showToast("wifi信息不能为空")
            return false
        }
        InputPasswordDialogFragment.newInstance(wifiInfo)
            .rxShow(activity.supportFragmentManager, InputPasswordDialogFragment.TAG)
            .subscribeBy { code -> }
        return true
    }

    /**
     * 显示正在连接
     */
    fun showConnectDialog(activity: FragmentActivity, wifiInfo: WifiInfo?): Boolean {
        if (wifiInfo == null) {
            showToast("wifi信息不能为空")
            return false
        }
        ConnectingDialogFragment.newInstance(wifiInfo, "")
            .rxShow(activity.supportFragmentManager, ConnectingDialogFragment.TAG)
            .subscribeBy { code -> }
        return true
    }


    /**
     * 显示举报钓鱼
     */
    fun showReportFishingDialog(
        activity: FragmentActivity,
        wifiInfo: WifiInfo?,
        block: (WifiInfo) -> Unit
    ): Boolean {
        if (wifiInfo == null) {
            showToast("wifi信息不能为空")
            return false
        }
        CommonDialogFragment.newInstance()
            .setTitle("举报钓鱼WiFi")
            .setContent("确定要举报 ${wifiInfo.ssid} 为钓鱼WiFi吗")
            .setOnCancelListener {
            }
            .setOnConfirmListener("确定举报") {
                block(wifiInfo)
            }
            .rxShow(activity.supportFragmentManager, CommonDialogFragment.TAG)
            .subscribeBy { code -> }
        return true
    }

}