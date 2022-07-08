package com.mc.cpyr.guide.config

import android.view.LayoutInflater
import android.view.View
import com.mc.cpyr.guide.view.MaskView

/**
 * * 遮罩系统中相对于目标区域而绘制一些图片或者文字等view需要实现的接口. <br>
 *  * <br>
 *  * {@link #getView(LayoutInflater)} <br>
 *  * {@link #getAnchor()} <br>
 *  * {@link #getFitPosition()} <br>
 *  * {@link #getXOffset()} <br>
 *  * {@link #getYOffset()}
 *  * <br>
 *  * 具体创建遮罩的说明请参加{@link GuideBuilder}
 * @author mmxm
 * @date 2021/3/8 17:03
 */
interface Component {


    companion object{

        val FIT_START = MaskView.LayoutParams.PARENT_START

        val FIT_END = MaskView.LayoutParams.PARENT_END

        val FIT_CENTER = MaskView.LayoutParams.PARENT_CENTER

        val ANCHOR_LEFT = MaskView.LayoutParams.ANCHOR_LEFT

        val ANCHOR_RIGHT = MaskView.LayoutParams.ANCHOR_RIGHT

        val ANCHOR_BOTTOM = MaskView.LayoutParams.ANCHOR_BOTTOM

        val ANCHOR_TOP = MaskView.LayoutParams.ANCHOR_TOP

        val ANCHOR_OVER = MaskView.LayoutParams.ANCHOR_OVER


        /**
         * 圆角矩形&矩形
         */
        const val ROUNDRECT = 0

        /**
         * 圆形
         */
        const val CIRCLE = 1

        /**
         * 特殊的圆角矩形,可以设置单独的每个角圆角,但是角度必须一致
         */
        const val ROUND_ALONE_RECT = 2
    }



    /**
     * 需要显示的view
     *
     * @param inflater use to inflate xml resource file
     * @return the component view
     */
    fun getView(inflater: LayoutInflater): View

    /**
     * 相对目标View的锚点
     *
     * @return could be [.ANCHOR_LEFT], [.ANCHOR_RIGHT],
     * [.ANCHOR_TOP], [.ANCHOR_BOTTOM], [.ANCHOR_OVER]
     */
    fun getAnchor(): Int

    /**
     * 相对目标View的对齐
     *
     * @return could be [.FIT_START], [.FIT_END],
     * [.FIT_CENTER]
     */
    fun getFitPosition(): Int

    /**
     * 相对目标View的X轴位移，在计算锚点和对齐之后。
     *
     * @return X轴偏移量, 单位 dp
     */
    fun getXOffset(): Int

    /**
     * 相对目标View的Y轴位移，在计算锚点和对齐之后。
     *
     * @return Y轴偏移量，单位 dp
     */
    fun getYOffset(): Int
}