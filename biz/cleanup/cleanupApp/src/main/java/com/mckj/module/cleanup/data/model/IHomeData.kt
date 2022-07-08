package com.mckj.module.cleanup.data.model

import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.entity.MenuJumpItem

/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
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
    suspend fun saveBusinessMenuList(list: List<MenuItem>): Boolean

    /**
     * 获取特色菜单列表
     */
    suspend fun getCharaMenuList(): List<MenuBusinessItem>? = null

    /**
     * 保存特色列表
     */
    suspend fun saveCharaMenuList(list: List<MenuBusinessItem>): Boolean = true

    /**
     * 获取特色菜单(包含工具)列表
     */
    suspend fun getCharaToolsMenuList(): List<MenuItem>? = null

    /**
     * 保存特色(包含工具)列表
     */
    suspend fun saveCharaToolsMenuList(list: List<MenuItem>): Boolean = true


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