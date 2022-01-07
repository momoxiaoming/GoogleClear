package com.mckj.sceneslib.util

import android.net.TrafficStats
import com.dn.baselib.ext.getApplicationContext
import com.mckj.sceneslib.gen.ScenesSp
import java.util.*
import kotlin.math.max

/**
 * Describe:网速工具类
 *
 * Created By yangb on 2020/11/4
 */
object NetworkSpeedUtil {

    const val TAG = "NetworkSpeedUtil"

    @Volatile
    private var lastTotalRxBytes: Long = 0

    @Volatile
    private var lastTimeStamp: Long = 0

    private fun getTotalRxBytes(): Long {
        val context = getApplicationContext()
        return if (TrafficStats.getUidRxBytes(context.applicationInfo.uid) == TrafficStats.UNSUPPORTED.toLong()) 0 else TrafficStats.getTotalRxBytes()
    }

    fun getNetSpeed(): Long {
        val nowTotalRxBytes = getTotalRxBytes()
        val nowTimeStamp = System.currentTimeMillis()
        val speed: Float =
            (nowTotalRxBytes - lastTotalRxBytes) * 1000f / (nowTimeStamp - lastTimeStamp) //毫秒转换秒
        Log.i(
            TAG,
            "getNetSpeed: size:${nowTotalRxBytes - lastTotalRxBytes}, time:${nowTimeStamp - lastTimeStamp} speed:$speed"
        )
        lastTimeStamp = nowTimeStamp
        lastTotalRxBytes = nowTotalRxBytes
        return speed.toLong()
    }

    fun getNetworkSpeedText(networkSpeed: Long): String {
        return when {
            networkSpeed > 1024 * 1024 * 1024 -> {
                String.format(Locale.getDefault(), "%1.1fGB/s", networkSpeed / 1024f / 1024 / 1024)
            }
            networkSpeed > 1024 * 1024 -> {
                String.format(Locale.getDefault(), "%1.1fM/s", networkSpeed / 1024f / 1024)
            }
            networkSpeed > 1024 -> {
                String.format(Locale.getDefault(), "%1.1fKB/s", networkSpeed / 1024f)
            }
            else -> {
                String.format(Locale.getDefault(), "%1.1fB/s", networkSpeed * 1f)
            }
        }
    }

    fun getNetworkSpeedStrengthText(networkSpeed: Long): String {
        return when {
            networkSpeed > 10 * 1024 * 1024 -> { //10M/s
                "当前网速很快"
            }
            networkSpeed > 1024 * 1024 -> { //1M/s
                "当前网速较快"
            }
            networkSpeed > 512 * 1024 -> {
                "当前网速一般"
            }
            networkSpeed > 100 * 1024 -> {
                "当前网速较慢"
            }
            networkSpeed > 1 * 1024 -> {
                "当前网速很慢"
            }
            else -> {
                "当前网速很弱"
            }
        }
    }

    fun getNetworkSpeedProgress(networkSpeed: Long): Int {
        val maxSpeed = max(ScenesSp.instance.networkSpeedMax, networkSpeed)
        //保存最大网速
        if (networkSpeed >= maxSpeed) {
            ScenesSp.instance.networkSpeedMax = maxSpeed
        }
        return (networkSpeed * 1f / maxSpeed * 100).toInt()
    }

}