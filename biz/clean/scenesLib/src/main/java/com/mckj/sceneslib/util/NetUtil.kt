package com.mckj.sceneslib.util

import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*

/**
 * Describe:
 *
 * Created By yangb on 2020/10/27
 */
object NetUtil {

    const val TAG = "NetUtil"

    /**
     * 能否ping通
     */
    fun pingEnable(count: Int = 3): Boolean {
        Log.i(TAG, "pingEnable: count:$count")
        //ping的地址
        val url = "www.baidu.com"
        //ping网址3次
        val pingCommand = "ping -c $count -w 100 $url"
        val cmdResult : ShellUtil.CommandResult = ShellUtil.execCmd(pingCommand)
        Log.i(TAG, "pingEnable: cmdResult:$cmdResult")
        return cmdResult.result == 0
    }

    /**
     * 域名解析是否正常
     */
    fun dnsEnable(): Boolean {
        Log.i(TAG, "dnsEnable: ")
        val url = "www.baidu.com"
        val ipArray = arrayOf("14.215.177.38", "14.215.177.39")
        val parseArray = parseHostGetIPAddress(url)
        ipArray.sort()
        parseArray?.sort()
        Log.i(TAG, "dnsEnable: ipArray:${ipArray.contentToString()}")
        Log.i(TAG, "dnsEnable: parseArray:${parseArray.contentToString()}")
        val result = Arrays.equals(ipArray, parseArray)
        Log.i(TAG, "dnsEnable: result:$result")
        return result
    }

    /**
     * 解析域名获取IP数组
     * @param host
     * @return
     */
    private fun parseHostGetIPAddress(host: String): Array<String?>? {
        var result: Array<String?>? = null
        try {
            val inetAddressArr: Array<InetAddress>? = InetAddress.getAllByName(host)
            if (inetAddressArr != null && inetAddressArr.isNotEmpty()) {
                result = arrayOfNulls(inetAddressArr.size)
                for (i in inetAddressArr.indices) {
                    result[i] = inetAddressArr[i].hostAddress
                }
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
        return result
    }

}