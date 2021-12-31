package com.mckj.module.wifi.entity

/**
 * Describe:悬浮控件数据
 *
 * Created By yangb on 2021/1/25
 */
data class FloatEntity(
    val index: Int,
    val type: Int,
    var time: Long = 0,
    var dayCount: Int = 0,
    var totalCount: Int = 0
) {

    companion object {

        /**
         * 类型-悬浮金币
         */
        const val TYPE_GOLD = 0

        /**
         * 类型-悬浮抽奖
         */
        const val TYPE_LOTTERY = 1

        /**
         * 类型-悬浮大转盘
         */

        const val TYPE_WHEEL = 2

    }

}