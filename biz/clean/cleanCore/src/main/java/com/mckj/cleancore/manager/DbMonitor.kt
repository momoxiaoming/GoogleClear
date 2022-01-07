package com.mckj.cleancore.manager

import com.mckj.cleancore.impl.interceptor.*
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
        val originDealData = com.mckj.cleancore.data.DealData()
        val chain = LInterceptor.RealChain(interceptors = interceptors, mDealData = originDealData)
        chain.proceed(originDealData)
    }

}