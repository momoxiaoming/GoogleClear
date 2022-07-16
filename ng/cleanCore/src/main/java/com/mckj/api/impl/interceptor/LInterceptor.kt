package com.mckj.api.impl.interceptor

import com.mckj.api.data.DealData


/**
 * @author xx
 * @version 1
 * @createTime 2021/8/7 10:20
 * @desc
 */
interface LInterceptor {
    @Throws(Exception::class)
    fun intercept(chain: RealChain)

    class RealChain(
        private var interceptors: List<LInterceptor>,
        private var index: Int = 0,
        internal var mDealData: DealData
    ) {

        internal fun copy(
            index: Int = this.index,
            dealData: DealData = this.mDealData
        ) = RealChain(interceptors, index, dealData)

        fun proceed(data: DealData) {
            check(index < interceptors.size)
            val next = copy(index = index + 1, dealData = data)
            val interceptor = interceptors[index]
            interceptor.intercept(next)
        }
    }
}