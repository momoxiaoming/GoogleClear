package com.mckj.sceneslib.util

import java.io.*
import java.net.NetworkInterface
import java.util.*

/**
 * Describe:
 *
 * Created By yangb on 2020/10/28
 */
object MacUtil {

    /**
     * 获取WiFi Mac地址
     */
    fun getAddressMac(): String? {
        return getAddressMacByInterface() ?: getAddressMacByFile()
    }

    private fun getAddressMacByInterface(): String? {
        try {
            val all: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (nif.name == "wlan0") {
                    val macBytes: ByteArray = nif.hardwareAddress ?: return ""
                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        res1.append(String.format("%02X:", b))
                    }
                    if (res1.isNotEmpty()) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getAddressMacByFile(): String? {
        var fileInputStream: FileInputStream? = null
        var writer: Writer? = null
        var reader: Reader? = null
        try {
            fileInputStream = FileInputStream("/sys/class/net/wlan0/address")
            writer = StringWriter()
            val charArray = CharArray(2048)
            reader = BufferedReader(InputStreamReader(fileInputStream, "UTF-8"))
            var counter: Int
            while (reader.read(charArray).also { counter = it } != -1) {
                writer.write(charArray, 0, counter)
            }
            return charArray.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            reader?.close()
            writer?.close()
            fileInputStream?.close()
        }
        return null
    }

}