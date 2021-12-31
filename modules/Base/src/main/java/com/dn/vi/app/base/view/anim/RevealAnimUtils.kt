package com.dn.vi.app.base.view.anim

import android.animation.Animator
import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.view.doOnLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator


/**
 * reveal 展开动画
 *
 * 参考:
 *
 * https://medium.com/@gabornovak/circular-reveal-animation-between-fragments-d8ed9011aec
 * Created by holmes on 2/26.
 */
object RevealAnimUtils {

    const val KEY_CONFIG = "revealConf"

    fun registerCircularRevealAnimation(
        context: Context, view: View, revealConfig: RevealAnimConfig,
        duration: Int,
        startColor: Int, endColor: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.doOnLayout {
                val cx: Int = revealConfig.centerX
                val cy: Int = revealConfig.centerY
                val width: Int = revealConfig.width
                val height: Int = revealConfig.height
                // val duration: Int = context.getResources().getInteger(R.integer.config_mediumAnimTime)
                //Simply use the diagonal of the view
                val finalRadius = Math.sqrt(width * width + height * height.toDouble()).toFloat()
                val anim: Animator =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
                        .setDuration(duration.toLong())
                anim.setInterpolator(FastOutSlowInInterpolator())
                anim.start()
            }
        }
    }

    fun getRevealConfigFromView(trigger: View, canvasView: View): RevealAnimConfig {
        return RevealAnimConfig(
            (trigger.x + trigger.width / 2).toInt(),
            (trigger.y + trigger.height / 2).toInt(),
            canvasView.width,
            canvasView.height
        )
    }

    /**
     * 传递的 动画参数
     */
    data class RevealAnimConfig(
        val centerX: Int, val centerY: Int,
        val width: Int, val height: Int
    ) : Parcelable {

        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(centerX)
            parcel.writeInt(centerY)
            parcel.writeInt(width)
            parcel.writeInt(height)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<RevealAnimConfig> {
            override fun createFromParcel(parcel: Parcel): RevealAnimConfig {
                return RevealAnimConfig(parcel)
            }

            override fun newArray(size: Int): Array<RevealAnimConfig?> {
                return arrayOfNulls(size)
            }
        }

    }
}