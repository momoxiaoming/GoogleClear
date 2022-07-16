package com.mckj.template.impl

import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.AbstractFragment
import com.mckj.template.abs.AbsMenuView
import com.mckj.template.entity.UIMenuBean


/**
 *  author : xx
 *  date : 2022/3/5 19:24
 *  description : 首页样式配置
 */
interface IHomeStyle {

    /**
     * 头部fragment
     */
    fun registerHeaderFragment(): Fragment

    fun registerMenuTemplate(): MutableMap<UIMenuBean, AbsMenuView>
    /**
     * 首页对应的菜单配置
     *
     */
    fun getMainMenu(): LinearLayout?

    fun getBusMenu(): LinearLayout?

    fun getBusPlusMenu(): LinearLayout?

    fun getRecommendMenu(): LinearLayout?


    /**
     * 陪伴提示
     */
    fun registerDayUsedView(): View?

}