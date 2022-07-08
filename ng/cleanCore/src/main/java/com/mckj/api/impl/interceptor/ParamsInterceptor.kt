package com.mckj.api.impl.interceptor

import android.os.Build
import com.org.proxy.AppProxy
import com.org.proxy.utils.AESUtil


/**
 * @author leix
 * @version 1
 * @createTime 2021/8/7 10:31
 * @desc 拼接请求参数并且加密
 */
class ParamsInterceptor : LInterceptor {
    override fun intercept(chain: LInterceptor.RealChain) {
        val dealData = chain.mDealData
        val queryData = AESUtil.e(getQueryData())
        dealData.queryData = queryData
        chain.proceed(dealData)
    }

    fun getQueryData(): String {
        val pairs = getParams()
        pairs.sortWith(object : Comparator<Pair<String, String>> {
            override fun compare(o1: Pair<String, String>?, o2: Pair<String, String>?): Int {
                if (o1 == null && o2 == null) {
                    return 0
                }
                if (o1 == null) {
                    return 1
                }
                if (o2 == null) {
                    return -1
                }
                return o1.first.compareTo(o2.first)
            }
        })
        return pairs.toQuery(true)
    }

    fun getParams(): MutableList<Pair<String, String>> {
        val osversion = Build.VERSION.RELEASE
        val model = Build.MODEL
        val commonParams = getCommonParams()
        commonParams.add("pkg" to AppProxy.packageName)
        commonParams.add("osversion" to osversion)
        commonParams.add("model" to model)
        return commonParams
    }

    private fun getCommonParams(): MutableList<Pair<String, String>> {
        // 下面toQuery的时候，要忽略isDebug
        val pairs: MutableList<Pair<String, String>> = mutableListOf(
            "appid" to AppProxy.appId,
            "prjid" to AppProxy.projectId,
            "platform" to "android",
            "timestamp" to System.currentTimeMillis().toString(),
            "cha" to AppProxy.channel,
            "imei" to AppProxy.deviceId,
            "oaid" to AppProxy.oaid,
            "lsn" to AppProxy.lsn
        )
        return pairs
    }


    private fun MutableList<Pair<String, String>>.toQuery(ignoreEmpty: Boolean): String {
        var src: MutableList<Pair<String, String>> = this
        if (ignoreEmpty) {
            val tmp = ArrayList<Pair<String, String>>(this.size)
            this.forEach { item ->
                if (item.second.isNotEmpty()) {
                    tmp.add(item)
                }
            }
            src = tmp
        }
        return src.joinToString(separator = "&") { pair ->
            "${pair.first}=${pair.second}"
        }
    }
}