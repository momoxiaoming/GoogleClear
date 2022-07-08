package com.mckj.sceneslib.entity

import com.mckj.api.entity.AppJunk


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
    val iJunkEntity: AppJunk,
    val parent: MenuJunkParent
)