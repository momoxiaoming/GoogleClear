package com.mckj.sceneslib.ui.scenes.model.tools.data.entity

import android.annotation.SuppressLint
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType

@SuppressLint("ParcelCreator")
class MenuToolsItem(
    type: Int,
    update: Long = 0L,
    recommendAble: Boolean = false,
    isAuditConfig: Boolean = false
) : MenuItem(type, update, recommendAble, isAuditConfig){

    var isOpenFlash: Boolean = false

    val name: CharSequence
        get() {
            if(type == ToolsType.TYPE_FONT_SIZE){
                return ResourceUtil.getString(R.string.scenes_tools_font)
            }
            if(type == ToolsType.TYPE_PHONE_PARAMETERS){
                return ResourceUtil.getString(R.string.scenes_tools_phone_parameters)
            }
            if(type == ToolsType.TYPE_ALARM_CLOCK){
                return ResourceUtil.getString(R.string.scenes_tools_alarm_clock)
            }
            if(type == ToolsType.TYPE_ALBUM){
                return ResourceUtil.getString(R.string.scenes_tools_photo)
            }
            if(type == ToolsType.TYPE_CALENDAR){
                return ResourceUtil.getString(R.string.scenes_tools_calendar)
            }
            if(type == ToolsType.TYPE_FLASHLIGHT){
                return ResourceUtil.getString(R.string.scenes_tools_flashlight)
            }
            if(type == ToolsType.TYPE_FLOW_USING){
                return ResourceUtil.getString(R.string.scenes_tools_gprs_use)
            }
            if(type == ToolsType.TYPE_WEATHER){
                return ResourceUtil.getString(R.string.scenes_tools_weather)
            }
            if(type == ToolsType.TYPE_MAGNIFIER){
                return "放大镜"
            }
            if(type == ToolsType.TYPE_DUST){
                return "声波除尘"
            }
            if(type == ToolsType.TYPE_NOTIFY_CLEAR){
                return "通知清理"
            }
            if(type == ToolsType.TYPE_INCREASE_VOICE){
                return "智能音量"
            }
            if(type == ToolsType.TYPE_ACCOUNT){
                return "记账"
            }
            if(type == ToolsType.TYPE_CHECK_NET_WORK){
                return "网络检测"
            }
            if(type == ToolsType.TYPE_DAYS){
                return "倒数日"
            }
            if(type == ToolsType.TYPE_FONT_SCALE){
                return "字体放大器"
            }
            return ""
        }

    val desc: CharSequence
        get() {
            if(type == ToolsType.TYPE_WEATHER){
                return "预测两周后天气"
            }
            if(type == ToolsType.TYPE_PHONE_PARAMETERS){
                return "快速了解各参数"
            }
            if(type == ToolsType.TYPE_FLASHLIGHT){
                return "自由控制开和关"
            }
            if(type == ToolsType.TYPE_ALARM_CLOCK){
                return "起床不延误"
            }
            if(type == ToolsType.TYPE_CALENDAR){
                return "节日不错过"
            }
            if(type == ToolsType.TYPE_FONT_SIZE){
                return "字体大小随意调"
            }
            if(type == ToolsType.TYPE_FLOW_USING){
                return "查看流量消耗"
            }
            return ""
        }

}