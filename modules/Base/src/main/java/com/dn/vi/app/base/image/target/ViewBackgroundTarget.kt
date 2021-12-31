package com.dn.vi.app.base.image.target

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

/**
 * 通过Glide把图片加载到 View的Background里面
 * Created by holmes on 18-1-22.
 */
open class ViewBackgroundTarget : CustomViewTarget<View, Drawable> {

    var ignorePlaceholder = false
    var ignoreFail = false

    constructor(view: View) : super(view)
    constructor(view: View, ignorePlaceholder: Boolean, ignoreFail: Boolean) : super(view) {
        this.ignorePlaceholder = ignorePlaceholder
        this.ignoreFail = ignoreFail
    }


    override fun onLoadFailed(errorDrawable: Drawable?) {
        if (ignoreFail) {
            return
        }
        onDrawableChanged(errorDrawable)
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        onDrawableChanged(resource)
    }

    protected open fun onDrawableChanged(drawable: Drawable?) {
        ViewCompat.setBackground(view, drawable)
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        if (ignorePlaceholder) {
            return
        }
        onDrawableChanged(placeholder)
    }

}