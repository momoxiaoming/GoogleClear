package com.mckj.cleancore.entity

import android.graphics.drawable.Drawable
import com.mckj.cleancore.entity.IJunkEntity
import com.mckj.cleancore.entity.JunkDetailEntity
import com.mckj.cleancore.tools.JunkType

/**
 * Describe:内存缓存
 *
 * Created By yangb on 2021/3/2
 */
class RamCacheEntity(
    val name: String,
    val desc: String,
    val packageName: String,
    val iconDrawable: Drawable?,
    val details: List<JunkDetailEntity>?,
    val size: Long,
) : IJunkEntity {

    override fun getJunkType(): Int {
        return JunkType.RAM_CACHE
    }

    override fun getJunkName(): String {
        return name
    }

    override fun getJunDesc(): String {
        return desc
    }

    override fun getJunkIconDrawable(): Drawable? {
        return iconDrawable
    }

    override fun getJunkDetails(): List<JunkDetailEntity>? {
        return details
    }

    override fun getJunkSize(): Long {
        return size
    }

}