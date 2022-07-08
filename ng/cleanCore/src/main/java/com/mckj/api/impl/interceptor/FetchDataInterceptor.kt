package com.mckj.api.impl.interceptor

import android.util.Log
import com.dn.vi.app.cm.utils.JsonUtil
import com.google.gson.reflect.TypeToken
import com.mckj.api.http.response.VersionInfo
import com.mckj.api.manager.RetrofitManage
import com.org.proxy.utils.AESUtil


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/7 11:16
 * @desc 拉取服务器数据
 */
class FetchDataInterceptor : LInterceptor {
    companion object {
        const val TAG = "DbMonitor"
    }

    private val service by lazy {
        RetrofitManage.instance.service
    }

    override fun intercept(chain: LInterceptor.RealChain) {
        val dealData = chain.mDealData
        dealData.queryData.let {
            GlobalScope.launch {
                try {
                    service.getCleanDb(it).let { response ->
                        if (response.code == 200 && !response.data.isNullOrEmpty()) {
                            response.data.let { data ->
                                AESUtil.d(data).let { signResponse ->
                                    try {
                                        val type = object : TypeToken<VersionInfo>() {}.type
                                        JsonUtil.fromJson<VersionInfo>(signResponse, type)
                                            ?.let { dbVersionInfo ->
                                                dealData.isFetchDataSuccess = true
                                                dealData.responseFromRemote = dbVersionInfo
                                                chain.proceed(dealData)
                                            }
                                    } catch (e: Exception) {
                                        dealData.isFetchDataSuccess = false
                                        chain.proceed(dealData)
                                        Log.d(TAG, "parse data error ${e.message}")
                                    }
                                }
                            }
                        } else {
                            dealData.isFetchDataSuccess = false
                            chain.proceed(dealData)
                        }
                    }
                } catch (e: Exception) {
                    dealData.isFetchDataSuccess = false
                    chain.proceed(dealData)
                }
            }
        }
    }
}