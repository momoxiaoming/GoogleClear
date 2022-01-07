package com.mckj.module.cleanup.entity

import com.mckj.cleancore.entity.IJunkEntity

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
data class MenuJunkChild(
    /**
     * 是否选中
     */
    var select: Boolean,
    val iJunkEntity: IJunkEntity,
    val parent : MenuJunkParent

) {
}