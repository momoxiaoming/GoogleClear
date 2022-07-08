package com.mckj.template.impl

import com.mckj.sceneslib.entity.MenuItem

/**
 *  author : leix
 *  date : 2022/3/5 22:56
 *  description :
 */
interface IHomeData {
    fun getMenuData(): MutableList<MenuItem>?
    fun getBusMenuData(): MutableList<MenuItem>?
    fun getBusPlusMenuData(): MutableList<MenuItem>?
    fun getRecommendMenuData(): MutableList<MenuItem>?
}