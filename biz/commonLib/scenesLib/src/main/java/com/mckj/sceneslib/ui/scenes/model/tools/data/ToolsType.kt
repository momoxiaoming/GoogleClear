package com.mckj.sceneslib.ui.scenes.model.tools.data

import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.R

/**
 * Describe:
 *
 * Created By yangb on 2021/5/31
 */
object ToolsType {

    //天气
    const val TYPE_WEATHER = 111

    //日历
    const val TYPE_CALENDAR = 211

    //流量使用
    const val TYPE_FLOW_USING = 311

    //手机参数
    const val TYPE_PHONE_PARAMETERS = 411

    //手电筒
    const val TYPE_FLASHLIGHT = 511

    //字体大小
    const val TYPE_FONT_SIZE = 611

    //闹钟
    const val TYPE_ALARM_CLOCK = 711

    //相册
    const val TYPE_ALBUM = 811

    //放大镜
    const val TYPE_MAGNIFIER = 101

    //声波除尘
    const val TYPE_DUST = 102

    //通知清理
    const val TYPE_NOTIFY_CLEAR = 103

    //智能音量
    const val TYPE_INCREASE_VOICE = 104

    //记账
    const val TYPE_ACCOUNT = 105

    //网络监测
    const val TYPE_CHECK_NET_WORK = 106

    //倒数日
    const val TYPE_DAYS = 107

    //字体放大器
    const val TYPE_FONT_SCALE = 108


    fun getNameByType(type: Int): String {
        return when (type) {
            TYPE_WEATHER -> ResourceUtil.getString(R.string.scenes_tools_weather)
            TYPE_CALENDAR -> ResourceUtil.getString(R.string.scenes_tools_calendar)
            TYPE_FLOW_USING -> ResourceUtil.getString(R.string.scenes_tools_gprs_use)
            TYPE_PHONE_PARAMETERS -> ResourceUtil.getString(R.string.scenes_tools_phone_parameters)
            TYPE_FLASHLIGHT -> ResourceUtil.getString(R.string.scenes_tools_flashlight)
            TYPE_FONT_SIZE -> ResourceUtil.getString(R.string.scenes_tools_font)
            TYPE_ALARM_CLOCK -> ResourceUtil.getString(R.string.scenes_tools_alarm_clock)
            TYPE_ALBUM -> ResourceUtil.getString(R.string.scenes_tools_photo)
            TYPE_MAGNIFIER -> "放大镜"
            TYPE_DUST -> "声波除尘"
            TYPE_NOTIFY_CLEAR -> "通知清理"
            TYPE_INCREASE_VOICE -> "智能音量"
            TYPE_ACCOUNT -> "记账"
            TYPE_CHECK_NET_WORK -> "网络监测"
            TYPE_DAYS -> "倒数日"
            TYPE_FONT_SCALE -> "字体放大器"
            else -> {
                ResourceUtil.getString(R.string.scenes_tools_unknown)
            }
        }
    }
}