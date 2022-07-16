package com.mckj.api.impl.interceptor

import android.util.Log
import com.mckj.api.db.JunkDatabase

/**
 * @author xx
 * @version 1
 * @createTime 2021/8/7 11:38
 * @desc加载数据库
 */
class LoadDbInterceptor : LInterceptor {
    companion object {
        const val TAG = "DbMonitor"
    }

    override fun intercept(chain: LInterceptor.RealChain) {
        val dealData = chain.mDealData
        if (!dealData.prepareLoadDb) return
        loadDb()
    }

    /**
     * load数据库
     */
    private fun loadDb() {
        try {
            val allList = JunkDatabase.getInstance().junkDbDao().getAllList()
            Log.d(TAG , "loadDb success:${allList?.size}")
        } catch (e: java.lang.Exception) {
            Log.d(TAG , "createNativeDB error :${e.message}")
        }
    }


}