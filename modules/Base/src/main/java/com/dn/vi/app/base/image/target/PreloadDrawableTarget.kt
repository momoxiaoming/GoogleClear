package com.dn.vi.app.base.image.target

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.BaseTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.transition.Transition

/**
 * 预先加载图片，缓存到一个Drawable里面
 * Created by holmes on 18-1-22.
 */
open class PreloadDrawableTarget(val width: Int = 0, val height: Int = 0) : BaseTarget<Drawable>() {

    var drawable: Drawable? = null

    override fun getSize(cb: SizeReadyCallback) {
        if (width > 0 && height > 0) {
            cb.onSizeReady(width, height)
        }
    }

    override fun removeCallback(cb: SizeReadyCallback) {
    }

    override fun onLoadStarted(placeholder: Drawable?) {
        drawable = placeholder
        onDrawableChanged(placeholder)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        drawable = errorDrawable
        onDrawableChanged(errorDrawable)
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        drawable = resource
        onDrawableChanged(resource)
    }

    open fun onDrawableChanged(drawable: Drawable?) {

    }

}