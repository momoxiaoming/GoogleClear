package com.mckj.module.wifi.data

import android.os.Build
import android.provider.Settings
import com.dn.vi.app.base.app.AppMod
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.sceneslib.manager.network.ap.ApManager
import com.mckj.sceneslib.data.model.impl.WifiDataImpl
import com.mckj.baselib.util.WifiUtil
import com.mckj.module.wifi.utils.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.Comparator

/**
 * Describe:
 *
 * Created By yangb on 2020/11/18
 */
class WifiRepository {

    companion object {
        const val TAG = "WifiRepository"
    }

    /**
     * wifi数据接口
     */
    val iWifiData = WifiDataImpl.getInstance()

    /**
     * 刷新列表状态
     */
    fun refreshWifiInfoList(
        connectInfo: ConnectInfo,
        list: List<WifiInfo>
    ): List<WifiInfo> {
        list.forEach {
            it.isConnect = it.ssid == connectInfo.name
            it.isExists = WifiUtil.getWifiConfiguration(it.ssid) != null
        }
        return list.sortedWith(Comparator { o1, o2 ->
            if (o1.isConnect) {
                return@Comparator -1
            }
            if (o2.isConnect) {
                return@Comparator 1
            }
            return@Comparator o2.level.compareTo(o1.level)
        })
    }


    /**
     * wifi开关
     */
    suspend fun setWifiAble(enable: Boolean): Boolean {
        return withContext(Dispatchers.Main) {
            closeHotSharing() && WifiUtil.setWifiEnable(enable)
        }
    }

    /**
     * 关闭热点分享
     */
    private suspend fun closeHotSharing(): Boolean {
        //先关闭热点分享，再打开wifi
        var result = false
        do {
            if (ApManager.getApState() == ApManager.STATE_AP_OFF) {
                result = true
                break
            }
            //是否有系统写权限
            if (!isSystemWrite()) {
                Log.i(TAG, "closeHotSharing: NoSystemWritePermission")
                break
            }
            ApManager.closeAp()
            result = withTimeout(3000) {
                repeat(1000) {
                    Log.i(TAG, "setWifiAble: state:${ApManager.getApState()}")
                    if (ApManager.getApState() == ApManager.STATE_AP_OFF) {
                        return@withTimeout true
                    }
                    delay(100)
                }
                false
            }
        } while (false)
        return result
    }

    /**
     * 是否有系统修改权限
     */
    private fun isSystemWrite(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val context = AppMod.app
            Settings.System.canWrite(context)
        } else {
            true
        }
    }

}