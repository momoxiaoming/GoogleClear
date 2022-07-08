package com.mckj.sceneslib.ui.scenes.model.tools.data

import com.mckj.sceneslib.ui.scenes.model.tools.data.entity.MenuToolsItem
import com.mckj.sceneslib.ui.scenes.model.tools.data.model.IToolsData
import com.mckj.sceneslib.R

/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
class ToolsDataImpl : IToolsData {

    companion object {
        const val TAG = "CleanupImpl"
    }

    override suspend fun getToolsMenuList(): List<MenuToolsItem> {
        val list = mutableListOf<MenuToolsItem>()
        list.add(MenuToolsItem(ToolsType.TYPE_CALENDAR))
        list.add(MenuToolsItem(ToolsType.TYPE_FLOW_USING))
        list.add(MenuToolsItem(ToolsType.TYPE_PHONE_PARAMETERS))

        list.add(MenuToolsItem(ToolsType.TYPE_FLASHLIGHT))
        list.add(MenuToolsItem(ToolsType.TYPE_FONT_SIZE))
        list.add(MenuToolsItem(ToolsType.TYPE_ALARM_CLOCK))
        list.add(MenuToolsItem(ToolsType.TYPE_ALBUM))

        list.add(MenuToolsItem(ToolsType.TYPE_MAGNIFIER))
        list.add(MenuToolsItem(ToolsType.TYPE_DUST))
        list.add(MenuToolsItem(ToolsType.TYPE_NOTIFY_CLEAR))
        list.add(MenuToolsItem(ToolsType.TYPE_INCREASE_VOICE))
        list.add(MenuToolsItem(ToolsType.TYPE_ACCOUNT))
        //list.add(MenuToolsItem(ToolsType.TYPE_CHECK_NET_WORK))
        list.add(MenuToolsItem(ToolsType.TYPE_DAYS))
        list.add(MenuToolsItem(ToolsType.TYPE_FONT_SCALE))
        return list
    }

    override suspend fun saveToolsMenuList(list: List<MenuToolsItem>): Boolean {
        return false
    }

    override fun getMenuResId(type: Int): Int {
        return when (type) {
            ToolsType.TYPE_WEATHER -> R.drawable.cleanup_icon_tools_weather
            ToolsType.TYPE_CALENDAR -> R.drawable.cleanup_icon_tools_calendar
            ToolsType.TYPE_FLOW_USING -> R.drawable.cleanup_icon_tools_flow
            ToolsType.TYPE_PHONE_PARAMETERS -> R.drawable.cleanup_icon_tools_parameter

            ToolsType.TYPE_FLASHLIGHT -> R.drawable.cleanup_icon_tools_flashlight_open
            ToolsType.TYPE_FONT_SIZE -> R.drawable.cleanup_icon_tools_font
            ToolsType.TYPE_ALARM_CLOCK -> R.drawable.cleanup_icon_tools_alarm
            ToolsType.TYPE_ALBUM -> R.drawable.cleanup_icon_tools_album

            ToolsType.TYPE_MAGNIFIER->R.drawable.cleanup_icon_fdj
            ToolsType.TYPE_DUST->R.drawable.cleanup_icon_sbcc
            ToolsType.TYPE_NOTIFY_CLEAR->R.drawable.cleanup_icon_tzql
            ToolsType.TYPE_INCREASE_VOICE->R.drawable.cleanup_icon_znyl

            ToolsType.TYPE_ACCOUNT->R.drawable.ic_toolkit_bill
            ToolsType.TYPE_CHECK_NET_WORK->R.drawable.ic_toolkit_network_detection
            ToolsType.TYPE_DAYS->R.drawable.ic_toolkit_reciprocal
            ToolsType.TYPE_FONT_SCALE->R.drawable.ic_toolkit_font_amplification
            else -> R.drawable.cleanup_icon_tools_weather
        }

    }

}
