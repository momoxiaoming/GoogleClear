package com.mckj.module.wifi.ui.wifi

import android.Manifest
import android.content.Intent
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.dn.vi.dex.utils.StringHexConfig
import com.mckj.api.biz.ad.AdManager
import com.mckj.api.biz.ad.entity.ad.AdStatus
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.helper.getApplication
import com.mckj.baselib.helper.showToast
import com.mckj.baselib.util.LocationUtil
import com.mckj.baselib.util.WifiUtil
import com.mckj.baselib.util.launch
import com.mckj.datalib.ui.permissionGuide.PermissionGuideActivity
import com.mckj.module.wifi.data.WifiRepository
import com.mckj.module.wifi.entity.WifiMenuItem
import com.mckj.module.wifi.gen.St
import com.mckj.module.wifi.helper.STHelper
import com.mckj.module.wifi.helper.WifiMenuHelper
import com.mckj.module.wifi.utils.Log
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.sceneslib.manager.network.WifiState
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.tbruyelle.rxpermissions3.RxPermissions
import com.tz.gg.appproxy.EvAgent
import com.tz.gg.appproxy.PrivacyUtils
import com.tz.gg.pipe.d.DSdkHelper
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class WifiViewModel(
    private val repository: WifiRepository
) : AbstractViewModel() {

    companion object {
        const val TAG = "WifiViewModel"
    }

    /**
     * wifi组装列表
     */
    val mWifiInfoListLiveData = MutableLiveData<List<Any>>()

    /**
     * 刷新状态
     */
    val mRefreshStateLiveData = MutableLiveData<Boolean>()


    /**
     * 刷新wifi列表
     */
    fun refreshWifiInfoList(connectInfo: ConnectInfo?, list: List<WifiInfo>?) {
        Log.i(TAG, "refreshWifiInfoList: ")
        launch {
            var newList: List<WifiInfo>? = list
            if (connectInfo != null && list != null) {
                newList = repository.refreshWifiInfoList(connectInfo, list)
            }
            warpWifiInfoList(newList)
        }
    }

    /**
     * wifi列表信息
     *
     * 判断定位权限->判断定位开关->判断wifi开关
     */
    private fun warpWifiInfoList(list: List<WifiInfo>?) {
        val items = mutableListOf<Any>()
        if (!LocationUtil.isLocationPermission()) {
            //判断定位权限是否打开
            items.add(WifiState.TYPE_LOCATION_PERMISSION)
        } else if (!LocationUtil.isLocationEnable()) {
            //定位功能是否打开
            items.add(WifiState.TYPE_LOCATION_DISABLE)
        } else if (!WifiUtil.isWifiEnable()) {
            //判断wifi是否打开
            items.add(WifiState.TYPE_WIFI_DISABLED)
        } else if (list.isNullOrEmpty()) {
            //列表为空时
            items.add(WifiState.TYPE_SCAN_EMPTY)
        } else {
            items.addAll(list)
        }
        mWifiInfoListLiveData.value = items
    }

    /**
     * 启动wifi扫描
     */
    fun startScan() {
        var result: Boolean = false
        do {
            /**
             * 判断定位权限
             */
            if (!LocationUtil.isLocationPermission()) {
                break
            }
            /**
             * 判断定位是否打开
             */
            if (!LocationUtil.isLocationEnable()) {
                break
            }
            /**
             * 判断wifi开开关
             */
            if (!WifiUtil.isWifiEnable()) {
                break
            }
            result = true
        } while (false)
        Log.i(TAG, "startScan: result:$result")
        if (result) {
            launch {
                val isScan = repository.iWifiData.startScan()
                Log.i(TAG, "startScan: isScan:$isScan")
                if (!result) {
                    mRefreshStateLiveData.value = false
                }
            }
        } else {
            warpWifiInfoList(null)
            mRefreshStateLiveData.value = false
        }
    }

    /**
     * 连接wifi
     */
    fun connect(wifiInfo: WifiInfo, password: String?) {
        Log.i(TAG, "connect: ")
        launch {
            val result = repository.iWifiData.connect(wifiInfo, password)
            Log.i(TAG, "connect: result:$result")
        }
    }

    /**
     * 断开连接
     */
    fun disconnect() {
        Log.i(TAG, "disconnect: ")
        launch {
            val result = repository.iWifiData.disconnect()
            Log.i(TAG, "disconnect: result:$result")
        }
    }

    fun removeConnect(wifiInfo: WifiInfo) {
        Log.i(TAG, "removeConnect: ")
        launch {
            val result = repository.iWifiData.removeConnect(wifiInfo)
            Log.i(TAG, "removeConnect: result:$result")
        }
    }

    /**
     * 申请定位权限，成功时开启扫描wifi，失败时打开app设置界面
     */
    fun requestLocationPermission(activity: FragmentActivity) {
        Log.i(TAG, "requestLocationPermission: ")
        St.stHomeLocationClick()
        val rxPermissions = RxPermissions(activity)
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { result ->
                if (result) {
                    //申请成功,刷新数据
                    St.stHomeLocationSuccess()
                    NetworkData.getInstance().initLoad()
                    startScan()
                } else {
                    //权限被拒绝 且禁止询问
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        //不能否再次请求
                        LocationUtil.openSelfSetting()
                        showPermissionGuideActivity()
                    }
                }
            }
    }

    /**
     * 显示权限引导
     */
    private fun showPermissionGuideActivity() {
        launch {
            //延迟显示
            delay(500)
            //显示权限提示
            val intent = Intent(getApplication(), PermissionGuideActivity::class.java)
            DSdkHelper.jumpActivity(getApplication(), intent)
        }
    }

    /**
     * 智能按钮点击
     */
    fun smartClickListener(activity: FragmentActivity) {
        /**
         * 判断定位权限
         */
        if (!LocationUtil.isLocationPermission()) {
            //vivo隐私检测系统比较傻逼，说这里不是位置权限使用的场景但是却申请了位置限
            if (PrivacyUtils.isInTestEnv()) {
                LocationUtil.openSelfSetting()
                showPermissionGuideActivity()
            } else {
                requestLocationPermission(activity)
            }
            return
        }
        /**
         * 判断定位是否打开
         */
        if (!LocationUtil.isLocationEnable()) {
            LocationUtil.locationEnable(true)
            return
        }
        /**
         * 判断wifi开开关
         */
        if (!WifiUtil.isWifiEnable()) {
            launch {
                val result = repository.setWifiAble(true)
                if (!result) {
                    showToast("打开wifi失败")
                }
            }
            return
        }
        menuClickListener(activity, MenuItem(ScenesType.TYPE_NETWORK_TEST))
    }

    /**
     * 菜单点击监听
     *
     * 判断定位权限->判断定位开关->判断wifi开关->判断有无网络
     */
    fun menuClickListener(
        activity: FragmentActivity,
        t: MenuItem,
        wifiInfo: WifiInfo? = null
    ): Boolean {
        //记录点击事件
        STHelper.menuClick(t, wifiInfo == null)
        val result: Boolean = when (t.type) {
            WifiMenuItem.TYPE_HOTSPOT_INFO -> {//热点信息
                WifiMenuHelper.jumpWifiDetail(activity, wifiInfo)
                true
            }
            WifiMenuItem.TYPE_REPORT_FISHING -> {//举报钓鱼
                WifiMenuHelper.showReportFishingDialog(activity, wifiInfo) {
                    reportFishing(it)
                }
                true
            }
            WifiMenuItem.TYPE_NETWORK_DISCONNECT -> {//断开网络
                disconnect()
                true
            }
            WifiMenuItem.TYPE_PWD_CONNECT -> {//密码连接
                WifiMenuHelper.showInputPasswordDialog(activity, wifiInfo)
                true
            }
            WifiMenuItem.TYPE_NETWORK_FORGET -> {//忘记网络
                WifiUtil.openWifiSetting()
                true
            }
            else -> {
                false
            }
        }
        Log.i(TAG, "wifiMenuClickListener: result:$result")
        if (!result) {
            ScenesManager.getInstance().jumpPage(activity, t.type)
        }
        return result
    }

    /**
     * 举报钓鱼
     */
    private fun reportFishing(wifiInfo: WifiInfo) {
        launch {
            val result = repository.iWifiData.reportFishing(wifiInfo)
            Log.i(TAG, "reportFishing: result:$result")
            if (result) {
                showToast("举报成功")
            } else {
                showToast("举报失败")
            }
        }
    }

    fun showAd(activity: FragmentActivity, name: String, parent: ViewGroup) {
        //加载广告
        AdManager.getInstance().showAd(name, parent, activity) { adResult ->
            when (adResult.adStatus) {
                AdStatus.CLICK -> {
                    EvAgent.sendEvent(StringHexConfig.news_ad_click)
                }
                AdStatus.SHOW_SUCCESS -> {
                    EvAgent.sendEvent(StringHexConfig.news_ad_show)
                }
                else -> {

                }
            }
        }
    }

}