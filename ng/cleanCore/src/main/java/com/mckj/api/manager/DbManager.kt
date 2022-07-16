package com.mckj.api.manager

import com.mckj.api.impl.junk.IJunkDb
import com.mckj.api.impl.junk.JunkDbImpl
import com.org.openlib.utils.SpUtil


/**
 * @author xx
 * @version 1
 * @createTime 2021/8/7 16:21
 * @desc 操作数据库的管理类  所有和数据库相关的操作通过该类处理
 */
object DbManager {

    private const val ONE_DAY = 1000 * 60 * 60 * 24

    private var mDbMonitor: DbMonitor? = null
    private var mDbImpl: JunkDbImpl? = null

    init {
        mDbMonitor = DbMonitor()
        mDbImpl = JunkDbImpl.instance
    }

    /**
     * 检查数据库
     */
    fun checkDb() {
        val lastTime = SpUtil.getLong("last_time")
        if (System.currentTimeMillis() - lastTime > ONE_DAY) {
            mDbMonitor?.execute()
            SpUtil.put("last_time", System.currentTimeMillis())
        }
    }

    /**
     * 获取垃圾数据实现类
     */
    fun getJunkDbImpl(): IJunkDb? {
        return mDbImpl
    }
}