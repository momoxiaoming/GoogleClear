package com.mckj.cleancore.entity

import android.graphics.drawable.Drawable

/**
 * Describe:垃圾文件集合
 *
 * Created By yangb on 2021/3/2
 */
interface IJunkEntity {

    /**
     * 获取类型
     */
    fun getJunkType(): Int

    /**
     * 获取垃圾名称
     */
    fun getJunkName(): String

    /**
     * 获取垃圾描述
     */
    fun getJunDesc(): String

    /**
     * 获取垃圾icon
     */
    fun getJunkIconDrawable(): Drawable?

    /**
     * 获取垃圾详情列表
     */
    fun getJunkDetails(): List<JunkDetailEntity>?

    /**
     * 获取垃圾文件大小
     */
    fun getJunkSize(): Long

}