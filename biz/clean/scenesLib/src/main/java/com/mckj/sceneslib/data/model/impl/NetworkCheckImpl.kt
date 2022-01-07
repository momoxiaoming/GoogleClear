package com.mckj.sceneslib.data.model.impl

import androidx.core.util.Consumer
import com.mckj.sceneslib.data.model.INetworkCheck
import com.mckj.sceneslib.entity.WifiDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Describe:
 *
 * Created By yangb on 2021/4/23
 */
class NetworkCheckImpl : INetworkCheck {

    private val mutex = Mutex()

    override suspend fun scanWifiDevice(hostIp: String, consumer: Consumer<WifiDevice>): Boolean {
        if (!checkIp(hostIp)) {
            return false
        }
        val ipPrefix = getIpPrefix(hostIp)
        return withContext(Dispatchers.IO) {
            val one = async { scanWifiDevice(ipPrefix, 0, 63, consumer) }
            val two = async { scanWifiDevice(ipPrefix, 64, 127, consumer) }
            val three = async { scanWifiDevice(ipPrefix, 128, 191, consumer) }
            val four = async { scanWifiDevice(ipPrefix, 192, 255, consumer) }
            one.await() && two.await() && three.await() && four.await()
        }
    }

    private suspend fun scanWifiDevice(
        ipPrefix: String,
        startIndex: Int,
        endIndex: Int,
        consumer: Consumer<WifiDevice>
    ): Boolean {
        for (index in startIndex..endIndex) {
            val ip = "$ipPrefix$index"
            if (isReachable(ip)) {
                mutex.withLock {
                    consumer.accept(WifiDevice("未知设备", ip))
                }
            }
        }
        return true
    }

    private fun isReachable(ip: String): Boolean {
        return try {
            val address = InetAddress.getByName(ip)
            address.isReachable(250)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 检验ip合法性
     */
    private fun checkIp(hostIp: String): Boolean {
        val ip =
            "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}"
        val pattern: Pattern = Pattern.compile(ip)
        val matcher: Matcher = pattern.matcher(hostIp)
        return matcher.matches()
    }

    /**
     * 获取ip前缀
     *
     * 192.168.1.1 -> 192.168.1.
     */
    private fun getIpPrefix(hostIp: String): String {
        return hostIp.substring(0, hostIp.lastIndexOf('.') + 1)
    }


}