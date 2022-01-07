package com.mckj.cleancore.impl.interceptor

import android.os.Build
import com.dn.baselib.util.AESUtil


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
        val commonParams = mutableListOf<Pair<String, String>>()

        return commonParams
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