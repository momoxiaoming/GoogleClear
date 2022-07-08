package com.org.proxy

import com.dn.vi.app.cm.kt.timeNowDelta
import com.dn.vi.app.repo.kv.KvSp

/**
 * 平台SDK，相关的环境参数
 */
object AppProxyEnv {
    /**
     * int
     */
    private val KEY_INIT_COUNT = "appSdk:init:ct"

    /**
     * long
     */
    private val KEY_FIRST_INIT_AT = "appSdk:init:at"

    /**
     * 初始化状态
     * @param count 初始化次数
     * @param firstInitAt 第一次初始化时间
     */
    data class InitState(val count: Int, val firstInitAt: Long) {

        /**
         * 是不是第一次初始化
         */
        fun isFirst(): Boolean {
            return firstInitAt < 3000L || timeNowDelta(firstInitAt) < 3000L
        }

        fun isFirstInit():Boolean{
            return count<=0
        }

        fun isNotFirst():Boolean{
            return count>1
        }
    }

    fun getInitState(): InitState {
        return InitState(
            KvSp.getInt(KEY_INIT_COUNT, 0),
            KvSp.getLong(KEY_FIRST_INIT_AT, 0L)
        )
    }

    fun increaseInitState() {
        val s = getInitState()
        KvSp.putInt(KEY_INIT_COUNT, s.count + 1)
        if (s.firstInitAt < 3000L || timeNowDelta(s.firstInitAt) < 3000L) {
            KvSp.putLong(KEY_FIRST_INIT_AT, System.currentTimeMillis())
        }
    }


}