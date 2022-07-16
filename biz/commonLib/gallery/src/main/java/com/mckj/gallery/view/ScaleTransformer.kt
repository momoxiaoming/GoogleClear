package com.mckj.gallery.view

import android.view.View
import kotlin.math.abs


/**
 * @author xx
 * @version 1
 * @createTime 2021/7/22 9:28
 * @desc item切换
 */
class ScaleTransformer : GalleryLayoutManager.ItemTransformer {
    override fun transformItem(layoutManager: GalleryLayoutManager?, item: View?, fraction: Float) {
        item?.let {
            it.pivotX = it.width / 2f
            it.pivotY = it.height / 2.0f
            val scale = 1 - 0.20f * abs(fraction)
            it.scaleX = scale
            it.scaleY = scale
        }
    }
}