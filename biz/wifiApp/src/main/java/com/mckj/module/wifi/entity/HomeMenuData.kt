package com.mckj.module.wifi.entity

import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.entity.MenuJumpItem

/**
 * Describe:
 *
 * Created By yangb on 2021/3/2
 */
data class HomeMenuData(
    /**
     * 主菜单列表
     */
    var homeList: List<MenuItem>?,
    /**
     * 业务菜单列表
     */
    var businessList: List<MenuBusinessItem>?,
    /**
     * 跳转列表
     */
    var jumpList: List<MenuJumpItem>?,
    /**
     * 使用天数
     */
    val useDays: Int
) {
}