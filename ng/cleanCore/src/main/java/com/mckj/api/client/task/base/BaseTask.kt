package com.mckj.api.client.task.base

import com.mckj.api.entity.AppJunk
import io.reactivex.rxjava3.functions.Consumer

/**
 * @author xx
 * @version 1
 * @createTime 2021/10/28 15:48
 * @desc 基础任务:
 */
abstract class BaseTask {
    /**
     * 名称
     */
    abstract fun getName(): String

    /**
     * 扫描
     */
    abstract fun scan(consumer: Consumer<AppJunk>): Boolean

    /**
     * 清理
     */
    abstract fun clean(appjunk: AppJunk): Boolean


    abstract fun stop()
    /**
     * 扫描和清理一起上
     */
    open fun scanAndClean() {
        scan {
            clean(it)
        }
    }
}