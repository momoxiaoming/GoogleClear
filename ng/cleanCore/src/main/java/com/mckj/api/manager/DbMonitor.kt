package com.mckj.api.manager

import com.mckj.api.data.DealData
import com.mckj.api.impl.interceptor.FetchDataInterceptor
import com.mckj.api.impl.interceptor.InspectDbInterceptor
import com.mckj.api.impl.interceptor.LInterceptor
import com.mckj.api.impl.interceptor.LoadDbInterceptor
import com.mckj.api.impl.interceptor.ParamsInterceptor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/5 14:26
 * @desc 检查DB状态
 */
class DbMonitor {

    fun execute() {
        GlobalScope.launch {
            startWithInterceptorChain()
        }
    }

    private fun startWithInterceptorChain() {
        val interceptors = mutableListOf<LInterceptor>()
        interceptors += ParamsInterceptor()
        interceptors += FetchDataInterceptor()
        interceptors += InspectDbInterceptor()
        interceptors += LoadDbInterceptor()
        val originDealData = DealData()
        val chain = LInterceptor.RealChain(interceptors = interceptors, mDealData = originDealData)
        chain.proceed(originDealData)
    }

}