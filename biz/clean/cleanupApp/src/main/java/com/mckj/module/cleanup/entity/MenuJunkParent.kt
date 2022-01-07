package com.mckj.module.cleanup.entity

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
data class MenuJunkParent(
    val name: String,
    /**
     * 大小
     */
    val size: Long,
    /**
     * 是否展开
     */
    var isExpand: Boolean,

    /**
     * 是否选中
     */
    var select: Boolean,
    var childList: List<MenuJunkChild>? = null,
) {
}