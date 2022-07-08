package com.org.admodule.render

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * INativeRender
 *
 * @author mmxm
 * @date 2022/7/5 9:40
 */
interface INativeRender {

    /**
     * 设置广告view
     */
    fun setView(view: View)

    /**
     * 获取广告view
     */
    fun getView(): View?

//    /**
//     * 获取原生广告控件
//     */
//    fun createView(context: Context, parent: ViewGroup, data: NativeData): View
//
//    /**
//     * 注册view
//     */
//    fun registerView(context: Context, parent: ViewGroup, data: NativeData)

}