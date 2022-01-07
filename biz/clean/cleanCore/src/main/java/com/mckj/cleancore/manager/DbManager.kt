package com.mckj.cleancore.manager

import com.mckj.cleancore.impl.junk.IJunkDb
import com.mckj.cleancore.impl.junk.JunkDbImpl


/**
 * @author leix
 * @version 1
 * @createTime 2021/8/7 16:21
 * @desc 操作数据库的管理类  所有和数据库相关的操作通过该类处理
 */
object DbManager {

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
        mDbMonitor?.execute()
    }

    /**
     * 获取垃圾数据实现类
     */
    fun getJunkDbImpl(): IJunkDb? {
        return mDbImpl
    }
}