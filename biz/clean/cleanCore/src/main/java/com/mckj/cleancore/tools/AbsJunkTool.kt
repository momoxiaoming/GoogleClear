package com.mckj.cleancore.tools

import com.mckj.cleancore.entity.IJunkEntity
import io.reactivex.rxjava3.functions.Consumer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Describe:垃圾管理接口
 *
 * Created By yangb on 2021/3/2
 */
abstract class AbsJunkTool {

    /**
     * 是否允许操作
     */
    private val mOptEnable = AtomicBoolean(false)

    /**
     * 名称
     */
    abstract fun getName(): String

    /**
     * 扫描
     */
    abstract fun scan(consumer: Consumer<IJunkEntity>): Boolean

    /**
     * 清理
     */
    abstract fun clean(entity: IJunkEntity): Boolean

    /**
     * 设置是否可以操作
     */
    open fun setOptEnable(enable: Boolean) {
        mOptEnable.set(enable)
    }

    /**
     * 能否扫描
     */
    protected fun isOptEnable(): Boolean {
        return mOptEnable.get()
    }

    /**
     * 扫描和清理一起上
     */
    open fun scanAndClean() {
        scan {
            clean(it)
        }
    }

}