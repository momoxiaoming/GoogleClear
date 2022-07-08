package com.mc.cpyr.guide.listener

import android.view.View

/**
 * GuideListener
 *
 * @author mmxm
 * @date 2021/3/8 17:33
 */
interface MaskViewClickListener {

    /**
     * 蒙版点击回调,只有开启[mOutsideTouchable]为false时会触发此监听
     * @param targetView Boolean 是否点击的目标view
     * @return Boolean  是否关闭蒙版
     */
    fun onClick(view : View, isTargetView: Boolean):Boolean
}