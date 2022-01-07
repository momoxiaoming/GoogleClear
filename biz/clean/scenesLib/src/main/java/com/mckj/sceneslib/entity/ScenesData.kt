package com.mckj.sceneslib.entity

/**
 * Describe:
 *
 * Created By yangb on 2021/3/25
 */
data class ScenesData(
    /**
     * 功能类型
     */
    val type: Int,

    /**
     * key值
     */
    val key: String,

    /**
     * 名称
     */

    val name: String,

    /**
     * 获取描述
     */

    var desc: String,

    /**
     * 高亮提示
     */

    var tip: String,

    /**
     * 获取按钮文本
     */

    var btnText: String,

    /**
     * 获取落地页名称
     */

    var landingName: CharSequence,

    /**
     * 获取落地页描述
     */

    var landingDesc: CharSequence,

    /**
     * 点击事件
     */
    var clickEvent: String = ""
) {
}