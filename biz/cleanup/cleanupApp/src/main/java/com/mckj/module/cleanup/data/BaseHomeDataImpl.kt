package com.mckj.module.cleanup.data

import com.mckj.module.cleanup.data.model.IHomeData
import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.entity.MenuJumpItem
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType

/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
open class BaseHomeDataImpl : IHomeData {

    private val toolsMenuList = mutableListOf<Int>()

    companion object {
        const val TAG = "CleanupImpl"
    }


    //菜单项在下面列表中,不隐藏
    protected fun isHideMenu(type: Int): Boolean {
        val noHideMenuList = mutableListOf<Int>()
        noHideMenuList.let {
            //工具菜单
            it.add(ToolsType.TYPE_CALENDAR)
            it.add(ToolsType.TYPE_ALARM_CLOCK)
            it.add(ToolsType.TYPE_WEATHER)
            it.add(ToolsType.TYPE_FLOW_USING)
            it.add(ToolsType.TYPE_PHONE_PARAMETERS)
            it.add(ToolsType.TYPE_FLASHLIGHT)
            it.add(ToolsType.TYPE_FONT_SIZE)
            it.add(ToolsType.TYPE_ALBUM)

            //普通菜单(清理与wifi不同)
            it.add(ScenesType.TYPE_PHONE_SPEED)
            it.add(ScenesType.TYPE_COOL_DOWN)
            it.add(ScenesType.TYPE_WX_CLEAN)
            it.add(ScenesType.TYPE_POWER_SAVE)
            it.add(ScenesType.TYPE_CAMERA_CHECK)
            it.add(ScenesType.TYPE_QQ_CLEAN)
            it.add(ScenesType.TYPE_CATON_SPEED)
            it.add(ScenesType.TYPE_TOOLS)
            it.add(ScenesType.TYPE_ALBUM_CLEAN)

            it.add(ScenesType.TYPE_DUST)
            it.add(ScenesType.TYPE_NOTIFY)
            it.add(ScenesType.TYPE_MAGNIFIER)
            it.add(ScenesType.TYPE_AUDIO)
            it.add(ScenesType.TYPE_CHECK_NET_WORK)
            it.add(ScenesType.TYPE_ACCOUNT)
            it.add(ScenesType.TYPE_DAYS)
            it.add(ScenesType.TYPE_FONT_SCALE)
        }

        return !noHideMenuList.contains(type)
    }

    //工具菜单列表
    protected fun getToolsType(): MutableList<Int> {
        if (toolsMenuList.isNotEmpty()) {
            return toolsMenuList
        }
        toolsMenuList.let {
            it.add(ToolsType.TYPE_FLASHLIGHT)
            it.add(ToolsType.TYPE_WEATHER)
            it.add(ToolsType.TYPE_FLOW_USING)
            it.add(ToolsType.TYPE_CALENDAR)
            it.add(ToolsType.TYPE_ALBUM)
            it.add(ToolsType.TYPE_ALARM_CLOCK)
            it.add(ToolsType.TYPE_FONT_SIZE)
            it.add(ToolsType.TYPE_PHONE_PARAMETERS)
        }
        return toolsMenuList
    }

    override suspend fun getHomeMenuList(): List<MenuItem> {
        return mutableListOf()
    }

    override suspend fun saveHomeMenuList(list: List<MenuItem>): Boolean {
        return true
    }

    override suspend fun getBusinessMenuList(): List<MenuBusinessItem> {
        return mutableListOf()
    }

    override suspend fun saveBusinessMenuList(list: List<MenuItem>): Boolean {
        return true
    }

    override suspend fun getJumpMenuList(): List<MenuJumpItem> {
        return mutableListOf()
    }

    override suspend fun saveJumpMenuList(list: List<MenuJumpItem>): Boolean {
        return true
    }

    override fun getMenuResId(type: Int): Int {
        return 0
    }
}
