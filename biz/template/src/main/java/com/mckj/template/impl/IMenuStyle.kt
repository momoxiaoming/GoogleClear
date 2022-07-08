package com.mckj.template.impl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

/**
 *  author : leix
 *  date : 2022/3/5 18:33
 *  description :
 */
interface IMenuStyle {
    fun getRootView(inflater: LayoutInflater, parent: ViewGroup): View?

    fun getItemName(): TextView?

    fun getItemIcon(): ImageView?


    fun getItemDesc(): TextView?

    fun getItemTip(): TextView?

    fun getRecommendView(): View?

    fun getMenuArrowView(): TextView?
}