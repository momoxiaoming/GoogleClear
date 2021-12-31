/**
 * Databinding 的扩展函数。
 *
 * @BindingAdapter里面定义的就是 xml里面的属性名
 *
 * [参数Databinding官方文档](https://developer.android.com/topic/libraries/data-binding/binding-adapters)
 *
 * 用于在 xml里面自定义的 bind属性
 *
 * ```
 * <Button
 *  ...
 *  android:text="@{btnText}"
 *  app:isAnimGone="@{!selected}"
 * />
 * ```
 *
 * Created by holmes on 2020/5/22.
 **/

package com.dn.vi.app.base.databinding

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.*
import com.dn.vi.app.base.image.GlideApp
import com.dn.vi.app.cm.R
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.cm.utils.DAttrUtil


@BindingAdapter("bindVerticalLayout", "stackFromEnd", requireAll = false)
fun RecyclerView.bindVerticalLayout(
    adapter: RecyclerView.Adapter<*>?,
    stackFromEnd: Boolean = false
) {
    if (adapter == null) return
    val layoutManager = LinearLayoutManager(context)
    layoutManager.stackFromEnd = stackFromEnd
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    setLayoutManager(layoutManager)
    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
    overScrollMode = View.OVER_SCROLL_NEVER
}

/**
 * 绑定 gone or visible
 */
@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

/**
 * 绑定gone or visible，并在改变的时候，放个动画
 */
@BindingAdapter("isAnimGone")
fun bindAnimateGone(view: View, isGone: Boolean) {
    val flag = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
    if (view.visibility != flag) {
        (view.parent as? ViewGroup)?.also { parent ->
            TransitionManager.beginDelayedTransition(parent)
        }
        view.visibility = flag
    }
}

/**
 * 绑定 selected
 */
@BindingAdapter("isSelected")
fun bindSelect(view: View, selected: Boolean) {
    view.isSelected = selected
}

/**
 * 绑定 activated
 */
@BindingAdapter("isActive")
fun bindActive(view: View, active: Boolean) {
    view.isActivated = active
}


/**
 * 从url，加载图片资源
 * "app:circleCrop"， 切圆圈 bool
 * "app:placeHolder"， 加载和错误的占位 drawable
 */
@BindingAdapter(value = ["imageFromUrl", "circleCrop", "placeHolder"], requireAll = false)
fun bindImageUrl(view: ImageView, url: String?, circle: Boolean? = false, ph: Drawable?) {
    if (url.isNullOrEmpty()) {
        view.setImageDrawable(ph)
        return
    }

    val glide = try {
        // fix : #3327519352135
        GlideApp.with(view)
    } catch (e: Throwable) {
        VLog.w("bind glide with view error. ${e.message}")
        return
    }

    glide.load(url)
        .let {
            if (ph != null) {
                it.placeholder(ph)
                    .error(ph)
            } else {
                it
            }
        }
//        .transition(DrawableTransitionOptions.withCrossFade())
        .let {
            if (circle == true) {
                it.circleCrop()
            } else {
                it.centerCrop()
            }
        }
        .into(view)

}

@BindingAdapter("enable", "enableBackground", "disEnableBackground", requireAll = false)
fun View.enableBackground(
    enable: Boolean,
    @DrawableRes enableBackground: Int = 0,
    @DrawableRes disEnableBackground: Int = 0
) {
    if (enable) {
        setBackgroundResource(enableBackground)
    } else {
        setBackgroundResource(disEnableBackground)
    }
}

@BindingAdapter(
    "url",
    "circle",
    "corners",
    "leftTopCorners",
    "leftBottomCorners",
    "rightBottomCorners",
    "rightTopCorners",
    "defResId",
    "errResId",
    "thumbnail",
    requireAll = false
)
fun ImageView.netImage(
    url: String?,
    circle: Boolean = false,
    corners: Int = 0,
    leftTopCorners: Int = 0,
    leftBottomCorners: Int = 0,
    rightBottomCorners: Int = 0,
    rightTopCorners: Int = 0,
    @DrawableRes defResId: Int = 0,
    @DrawableRes errResId: Int = defResId,
    thumbnail: Float = 1.0f
) {

    if ((context as? Activity)?.isDestroyed == true) {
        return
    }

    val transforms = when (scaleType) {
        ImageView.ScaleType.FIT_CENTER -> FitCenter()
        ImageView.ScaleType.CENTER_INSIDE -> CenterInside()
        else -> CenterCrop()
    }

    val glide = try {
        GlideApp.with(this)
    } catch (e: Throwable) {
        VLog.w("bind glide with view error. ${e.message}")
        return
    }
    glide.load(url)
        //.thumbnail(thumbnail)
        .transform(
            *when {
                circle -> arrayOf(CircleCrop())
                (corners > 0) -> arrayOf(RoundedCorners(corners))
                (leftTopCorners > 0 || leftBottomCorners > 0 || rightTopCorners > 0 || rightBottomCorners > 0) -> arrayOf(
                    RoundCorner(
                        context,
                        leftTopCorners.toFloat(),
                        leftBottomCorners.toFloat(),
                        rightTopCorners.toFloat(),
                        rightBottomCorners.toFloat()
                    )
                )
                else -> arrayOf(transforms)
            }
        )
        .placeholder(defResId).error(errResId)
        .into(this)
}

@BindingAdapter("android:src")
fun bindImageSrc(view: ImageView, bitmap: Bitmap) {
    view.setImageBitmap(bitmap)
}

@BindingAdapter("android:src")
fun bindImageSrc(view: ImageView, resId: Int) {
    view.setImageResource(resId)
}

@BindingAdapter("isDataTextColor")
fun isDataTextColor(view: TextView, select: Boolean) {
    if (select) {
        view.setTextColor(DAttrUtil.getPrimaryColor(view.context))
    } else {
        view.setTextColor(DAttrUtil.getPrimaryLightColor(view.context))
    }
}

@BindingAdapter("isDataHintColor")
fun isDataHintColor(view: TextView, select: Boolean) {
    if (select) {
        view.setTextColor(DAttrUtil.getPrimaryLightColor(view.context))
    } else {
        view.setTextColor(Color.parseColor("#999999"))
    }
}


