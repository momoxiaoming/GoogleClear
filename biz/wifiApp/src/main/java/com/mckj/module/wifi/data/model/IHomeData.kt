package com.mckj.module.wifi.data.model

import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.entity.MenuJumpItem

/**
 * Describe:
 *
 * Created By yangb on 2021/4/22
 */
interface IHomeData {

    /**
     * 获取主菜单列表
     */
    suspend fun getHomeMenuList(): List<MenuItem>

    /**
     * 保存菜单列表
     */
    suspend fun saveHomeMenuList(list: List<MenuItem>): Boolean

    /**
     * 获取业务菜单列表
     */
    suspend fun getBusinessMenuList(): List<MenuBusinessItem>

    /**
     * 保存业务列表
     */
    suspend fun saveBusinessMenuList(list: List<MenuBusinessItem>): Boolean

    /**
     * 获取跳转列表
     */
    suspend fun getJumpMenuList(): List<MenuJumpItem>

    /**
     * 保存跳转列表
     */
    suspend fun saveJumpMenuList(list: List<MenuJumpItem>): Boolean

    /**
     * 获取菜单资源id
     */
    fun getMenuResId(type: Int): Int

}