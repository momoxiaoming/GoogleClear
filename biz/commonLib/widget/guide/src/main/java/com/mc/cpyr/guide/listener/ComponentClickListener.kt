package com.mc.cpyr.guide.listener

import android.view.View

/**
 * 罩系统中相对于目标区域而绘制一些view的点击事件处理
 *
 * @author mmxm
 * @date 2021/3/8 17:33
 */
interface ComponentClickListener {

    /**
     *
     * @param view  View 点击的view
     * @return Boolean  是否关闭蒙版
     */
    fun onClick(view: View):Boolean
}