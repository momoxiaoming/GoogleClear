package com.mckj.sceneslib.entity

import com.mckj.cleancore.entity.IJunkEntity

/**
 * Describe:
 *
 * Created By yangb on 2021/3/2
 */
data class ScanData(
    var state: Int = STATE_NORMAL,
    val list: MutableList<IJunkEntity> = mutableListOf(),
    var size: Long = 0,
) {

    companion object {

        /**
         * 默认状态
         */
        const val STATE_NORMAL = 0

        /**
         * 扫描中
         */
        const val STATE_SCANNING = 1

        /**
         * 扫描完成
         */
        const val STATE_SCANNED = 2

        /**
         * 清理中
         */
        const val STATE_CLEANING = 3

        /**
         * 清理完成
         */
        const val STATE_CLEANED = 4

    }

    /**
     * 添加扫描数据
     */
    fun add(entity: IJunkEntity) {
        if (list.add(entity)) {
            size += entity.getJunkSize()
        }
    }

    /**
     * 添加扫描数据
     */
    fun remove(entity: IJunkEntity) {
        if (list.remove(entity)) {
            size -= entity.getJunkSize()
        }
    }

    /**
     * 重置状态
     */
    fun resetState(state: Int) {
        this.state = state
    }

    /**
     * 重置
     */
    fun reset() {
        state = STATE_NORMAL
        list.clear()
        size = 0
    }

}