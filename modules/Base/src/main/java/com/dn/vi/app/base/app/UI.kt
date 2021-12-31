package com.dn.vi.app.base.app

import android.content.Context
import android.content.res.Resources
import com.dn.vi.app.base.R
import org.jetbrains.anko.dip
import kotlin.math.roundToInt

/**
 * UI 相关常量
 * Created by holmes on 17-7-11.
 */
object UI {

    private val deviceContext: Context by lazy { AppMod.app }

    /**
     * 大边距
     * [R.dimen.container_padding]
     */
    var containerPadding: Int = 0
        get() {
            if (field == 0) {
                field = deviceContext.resources.getDimensionPixelOffset(R.dimen.broadPadding)
            }
            return field
        }

    /**
     * 用于屏幕边框的padding
     * [R.dimen.bound_padding]
     */
    var boundPadding: Int = 0
        get() {
            if (field == 0) {
                field = deviceContext.dip(24)
            }
            return field
        }

    /**
     * 小边距，内边
     * [R.dimen.view_padding]
     */
    var viewPadding: Int = 0
        get() {
            if (field == 0) {
                field = deviceContext.resources.getDimensionPixelOffset(R.dimen.viewPadding)
            }
            return field
        }

    /**
     * 最小边距，内边
     * [R.dimen.view_card_padding]
     */
    var viewCardPadding: Int = 0
        get() {
            if (field == 0) {
                field = deviceContext.dip(1)
            }
            return field
        }

    val titleBarHeight = deviceContext.dip(48)

    var screenWidth: Int = 0
        get() {
            if (field == 0) {
                field = deviceContext.resources.displayMetrics.widthPixels
            }
            return field
        }

    var screenHeight: Int = 0
        get() {
            if (field == 0) {
                field = deviceContext.resources.displayMetrics.heightPixels
            }
            return field
        }

    fun computeRecycleViewGridItemWith(
        gridCount: Int,
        dividerSize: Int,
        paddingLeft: Int,
        paddingRight: Int
    ): Int {
        return computeRecycleViewGridItemWith(
            screenWidth,
            gridCount, dividerSize, paddingLeft, paddingRight
        )
    }

    fun computeRecycleViewGridItemWith(
        containerWidth: Int,
        gridCount: Int, dividerSize: Int, paddingLeft: Int, paddingRight: Int
    ): Int {
        val itemWith =
            ((containerWidth - ((gridCount - 1) * dividerSize) - paddingLeft - paddingRight) / gridCount.toFloat()).roundToInt()
        return itemWith
    }

    /**
     * Return the status bar's height.
     *
     * @return the status bar's height
     */
    fun getStatusBarHeight(): Int {
        val res: Resources = Resources.getSystem()
        val resourceId: Int = res.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    object Color {
        val primary: Int by lazy {
            0xff00e676.toInt()
        }

        val textPrimary: Int by lazy {
            0xff000000.toInt()
        }
        val textSecondary: Int by lazy {
            0xff000000.toInt()
        }

        const val red: Int = 0xffF44336.toInt()
        const val blue: Int = 0xff1E88E5.toInt()
        const val gray: Int = 0xFFF2F2F2.toInt()
        const val dividerGray: Int = 0x19292A2B.toInt()

        /**
         * opaque divider
         */
        const val dividerGrayOpa: Int = 0xffE9E9E9.toInt()

        /**
         * percent 10. 10%
         */
        const val p0: Int = 0x00

        /**
         * percent 10. 10%
         */
        const val p10: Int = 0x19

        /**
         * 20%
         */
        const val p20: Int = 0x33

        /**
         * 30%
         */
        const val p30: Int = 0x4c

        /**
         * 40%
         */
        const val p40: Int = 0x66

        /**
         * 50%
         */
        const val p50: Int = 0x7f

        /**
         * 60%
         */
        const val p60: Int = 0x99

        /**
         * 70%
         */
        const val p70: Int = 0xB2

        /**
         * 80%
         */
        const val p80: Int = 0xCC

        /**
         * 90%
         */
        const val p90: Int = 0xE5

        /**
         * 90%
         */
        const val p100: Int = 0xFF
    }

}


