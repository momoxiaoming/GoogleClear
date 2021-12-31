package com.dn.vi.app.base.image.target

import android.graphics.drawable.Drawable
import android.view.Window
import java.lang.ref.WeakReference

/**
 * 加载图片到 Window的背景
 * Created by holmes on 18-1-22.
 */
class WindowBackgroundTarget(window: Window, width: Int, height: Int) :
    PreloadDrawableTarget(width, height) {

    private val windowRef: WeakReference<Window> = WeakReference(window)

    override fun onDrawableChanged(drawable: Drawable?) {
        super.onDrawableChanged(drawable)
        windowRef.get()?.setBackgroundDrawable(drawable)
    }

}