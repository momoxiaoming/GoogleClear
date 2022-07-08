package com.mckj.sceneslib.util

import com.mckj.baselib.util.Log
import com.mckj.sceneslib.entity.PingResult
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*
import kotlin.math.roundToInt

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
     * 把ping结果文本转成ping结果对象
     * 如果数据解析失败，结果为-1
     *
     * PING www.a.shifen.com (14.215.177.38) 56(84) bytes of data.
     * 64 bytes from 14.215.177.38: icmp_seq=1 ttl=54 time=97.2 ms
     * 64 bytes from 14.215.177.38: icmp_seq=2 ttl=54 time=60.8 ms
     * 64 bytes from 14.215.177.38: icmp_seq=3 ttl=54 time=63.9 ms
     * 64 bytes from 14.215.177.38: icmp_seq=4 ttl=54 time=50.9 ms
     * --- www.a.shifen.com ping statistics ---
     * 4 packets transmitted, 4 received, 0% packet loss, time 3006ms
     * rtt min/avg/max/mdev = 50.959/68.257/97.208/17.396 ms
     *
     *
     * PING www.google.com (31.13.83.2) 56(84) bytes of data.
     * --- www.google.com ping statistics ---
     * 10 packets transmitted, 0 received, 100% packet loss, time 9189ms
     *
     * @param host ping的host
     * @param text ping的结果
     */
    fun parsPingResult(host: String, text: String?): PingResult {
        val pingResult = PingResult(host, "")
        if (text.isNullOrEmpty()) {
            pingResult.status = -1
            return pingResult
        }else{
            pingResult.status =0
        }
        /**
         * loss
         */
        var lostStart = text.lastIndexOf("received, ", ignoreCase = true)
        if (lostStart > 0) {
            //加上索引的字符串长度
            lostStart = lostStart + 10
        }
        val lossEnd = text.lastIndexOf("% packet loss", ignoreCase = true)
        if (lostStart > 0 && lossEnd > 0 && lostStart < lossEnd) {
            val loss = text.substring(lostStart, lossEnd)
            android.util.Log.i("PingResult", "loss:$loss")
            try {
                pingResult.loss = Integer.parseInt(loss)
            } catch (e: Exception){
                android.util.Log.e("PingResult","loss：${loss}转Int失败")
            }
        }

        /** 延时数据*/
        var msIndex = text.lastIndexOf("/mdev = ")
        //索引到
        if (msIndex > 0) {
            //加上索引字符串长度
            msIndex = msIndex + 8
            val msEnd = text.lastIndexOf("ms")
            val totalMs = text.substring(msIndex, msEnd)
            val split = totalMs.split("/")
            try {
                pingResult.min = split[0].toFloat().roundToInt()
                pingResult.avg = split[1].toFloat().roundToInt()
                pingResult.max = split[2].toFloat().roundToInt()
                pingResult.mdev = split[3].toFloat().roundToInt()
            } catch (e: Exception) {
                android.util.Log.e("PingResult","totalMs：${totalMs}\n解析失败")
            }
        }

        return pingResult
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