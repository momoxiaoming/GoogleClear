@file:Suppress("NOTHING_TO_INLINE")

/**
 * glide kt extensions
 * Created by holmes on 2020/5/27.
 **/
package com.dn.vi.app.base.image

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory


/**
 * GlideApp.with(context)
 */
inline fun Context.withGlide(): GlideRequests {
    return GlideApp.with(this)
}

/**
 * GlideApp.with(activity)
 */
inline fun Activity.withGlide(): GlideRequests {
    return GlideApp.with(this)
}

/**
 * GlideApp.with(fragment)
 */
inline fun Fragment.withGlide(): GlideRequests {
    return GlideApp.with(this)
}

/**
 * GlideApp.with(view)
 */
inline fun View.withGlide(): GlideRequests {
    return GlideApp.with(this)
}

/**
 * crossFade
 */
val crossFadeFactory: DrawableCrossFadeFactory =
    DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

/**
 * Glide 自带的crossFade
 */
inline fun GlideRequest<Drawable>.crossFade(): GlideRequest<Drawable> {
    return this.transition(DrawableTransitionOptions.withCrossFade(crossFadeFactory))
}