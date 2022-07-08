package com.mckj.sceneslib.manager.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import com.org.openlib.utils.Log
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Describe:
 *
 * Created By yangb on 2020/12/16
 */
class NetworkReceiver : BroadcastReceiver() {

    companion object {

        const val TAG = "NetworkReceiver"

        private val isRegister = AtomicBoolean(false)

        fun register(context: Context): NetworkReceiver? {
            if (isRegister.compareAndSet(false, true)) {
                val receiver = NetworkReceiver()
                val intentFilter = IntentFilter()
                //wifi连接验证
                intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)
                //网络连接
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
                //wifi状态变化
                intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
                //wifi扫描结果通知
                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                context.registerReceiver(receiver, intentFilter)
                return receiver
            }
            return null
        }

        fun unregister(context: Context, receiver: NetworkReceiver) {
            if (isRegister.compareAndSet(true, false)) {
                context.unregisterReceiver(receiver)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        Log.i(TAG, "onReceive: action:$action")
        when (action) {
            WifiManager.SUPPLICANT_STATE_CHANGED_ACTION -> {
                val errorResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1)
                if (errorResult == -1) {
                    return
                }
                when (errorResult) {
                    WifiManager.ERROR_AUTHENTICATING -> {
                        Log.i(TAG, "onReceive: password Verification Error")
                        NetworkData.getInstance()
                            .loadNetworkState(
                                NetworkState.ERROR_AUTHENTICATING,
                                true,
                                errorResult,
                                "password Verification Error"
                            )
                    }
                    else -> {
                        Log.i(TAG, "onReceive: unknownError errorResult:$errorResult")
                        NetworkData.getInstance()
                            .loadNetworkState(
                                NetworkState.ERROR_AUTHENTICATING,
                                true,
                                errorResult,
                                "unknownError"
                            )
                    }
                }
            }
            ConnectivityManager.CONNECTIVITY_ACTION -> {
                val networkInfo =
                    intent.getParcelableExtra<NetworkInfo>(ConnectivityManager.EXTRA_NETWORK_INFO)
                        ?: return
                Log.i(TAG, "onReceive: state:${networkInfo.state} type:${networkInfo.type}")
                handleNetworkState(networkInfo)
            }
            WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                val wifiState =
                    intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                Log.i(TAG, "onReceive: wifiState:$wifiState")
                handleWifiState(wifiState)
            }
            WifiManager.SCAN_RESULTS_AVAILABLE_ACTION -> {
                Log.i(TAG, "onReceive: SCAN_RESULTS_AVAILABLE_ACTION")
                NetworkData.getInstance().loadWifiInfoList()
            }
        }
    }

    private fun handleNetworkState(networkInfo: NetworkInfo) {
        val networkState = when (networkInfo.state) {
            NetworkInfo.State.CONNECTING -> {//连接中
                NetworkState.CONNECTING
            }
            NetworkInfo.State.DISCONNECTING -> {//断开中
                NetworkState.DISCONNECTING
            }
            NetworkInfo.State.CONNECTED -> {//连接成功
                //加载连接信息
                NetworkData.getInstance().loadConnectInfo()
                NetworkState.CONNECTED
            }
            NetworkInfo.State.DISCONNECTED -> {//断开
                NetworkState.DISCONNECTED
            }
            else ->
                NetworkState.UNKNOWN
        }
        NetworkData.getInstance()
            .loadNetworkState(networkState, networkInfo.type == ConnectivityManager.TYPE_WIFI)
    }

    /**
     * 加载wifi状态
     */
    fun handleWifiState(state: Int) {
        val wifiState = when (state) {
            WifiManager.WIFI_STATE_ENABLED -> {
                Log.i(TAG, "onReceive: wifiIsOn")
                WifiState.TYPE_WIFI_ENABLED
            }
            WifiManager.WIFI_STATE_ENABLING -> {
                Log.i(TAG, "onReceive: wifiOpening")
                WifiState.TYPE_WIFI_ENABLING
            }
            WifiManager.WIFI_STATE_DISABLED -> {
                Log.i(TAG, "onReceive: wifiClosed")
                WifiState.TYPE_WIFI_DISABLED
            }
            WifiManager.WIFI_STATE_DISABLING -> {
                Log.i(TAG, "onReceive: wifiClosing")
                WifiState.TYPE_WIFI_DISABLING
            }
            else -> {
                Log.i(TAG, "onReceive: unknownStatus")
                WifiState.TYPE_WIFI_UNKNOWN
            }
        }
        NetworkData.getInstance().loadWifiState(wifiState)
    }

}