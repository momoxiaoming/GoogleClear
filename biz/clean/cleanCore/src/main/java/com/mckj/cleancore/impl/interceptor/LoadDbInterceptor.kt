package com.mckj.cleancore.impl.interceptor

import com.dn.vi.app.cm.log.VLog
import com.mckj.cleancore.db.JunkDatabase

/**
 * @author leix
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
            VLog.d(TAG + "loadDb success:${allList?.size}")
        } catch (e: java.lang.Exception) {
            VLog.d(TAG + "createNativeDB error :${e.message}")
        }
    }


}