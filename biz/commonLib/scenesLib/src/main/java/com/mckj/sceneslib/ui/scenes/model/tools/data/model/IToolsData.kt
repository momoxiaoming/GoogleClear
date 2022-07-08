package com.mckj.sceneslib.ui.scenes.model.tools.data.model

import com.mckj.sceneslib.ui.scenes.model.tools.data.entity.MenuToolsItem

/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
interface IToolsData {

    /**
     * 获取主菜单列表
     */
    suspend fun getToolsMenuList(): List<MenuToolsItem>

    /**
     * 保存菜单列表
     */
    suspend fun saveToolsMenuList(list: List<MenuToolsItem>): Boolean

    /**
     * 获取菜单资源id
     */
    fun getMenuResId(type: Int): Int

}