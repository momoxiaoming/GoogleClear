package com.mckj.module.wifi.gen

import android.content.Context
import com.dn.vi.app.base.app.AppMod
import com.tz.gg.appproxy.EvAgent


/**
 * Analyse
 * Created by [als-gen] at 14:34:15 2021-04-25.
 */
object St {

    private val context: Context
        get() = AppMod.app



    // ================================
    // Events count: 52
    // ================================

    // arrayMap.clear() 会让arrayMap使用缓存池


    /**
     * 主页_设置按钮_点击 
     */
    fun stHomeSetClick(){
        EvAgent.sendEvent("home_set_click")
    }

    /**
     * 设置_功能设置按钮_点击 
     */
    fun stSetFunctionsetClick(){
        EvAgent.sendEvent("set_functionset_click")
    }

    /**
     * 设置_关于我们按钮_点击 
     */
    fun stSetAboutusClick(){
        EvAgent.sendEvent("set_aboutus_click")
    }

    /**
     * 主页_一键加速_点击 
     */
    fun stHomeWifispeedupClick(){
        EvAgent.sendEvent("home_wifispeedup_click")
    }

    /**
     * 主页_网络测试_点击 
     */
    fun stHomeSpeedtestClick(){
        EvAgent.sendEvent("home_speedtest_click")
    }

    /**
     * 主页_安全检测_点击 
     */
    fun stHomeSafetycheckClick(){
        EvAgent.sendEvent("home_safetycheck_click")
    }

    /**
     * 主页_信号增强_点击 
     */
    fun stHomeSignalboostClick(){
        EvAgent.sendEvent("home_signalboost_click")
    }

    /**
     * 网络测试_页面_展示 
     */
    fun stSpeedtestShow(){
        EvAgent.sendEvent("speedtest_show")
    }

    /**
     * 安全检测_页面_展示 
     */
    fun stSafetycheckShow(){
        EvAgent.sendEvent("safetycheck_show")
    }

    /**
     * 信号增强_页面_展示 
     */
    fun stSignalboostShow(){
        EvAgent.sendEvent("signalboost_show")
    }

    /**
     * 网络测试_返回按钮_点击 
     */
    fun stSpeedtestBackClick(){
        EvAgent.sendEvent("speedtest_back_click")
    }

    /**
     * 网络测试_停止测速_点击 
     */
    fun stSpeedtestStoptestClick(){
        EvAgent.sendEvent("speedtest_stoptest_click")
    }

    /**
     * 安全检测_返回按钮_点击 
     */
    fun stSafetycheckBackClick(){
        EvAgent.sendEvent("safetycheck_back_click")
    }

    /**
     * 信号增强_返回按钮_点击 
     */
    fun stSignalboostBackClick(){
        EvAgent.sendEvent("signalboost_back_click")
    }

    /**
     * 幸运抽手机_点击 
     */
    fun stHomeWifiLotteryPhoneClick(){
        EvAgent.sendEvent("home_wifi_lottery_phone_click")
    }

    /**
     * 网络加速_点击 
     */
    fun stHomeWifiNetworkSpeedupClick(){
        EvAgent.sendEvent("home_wifi_network_speedup_click")
    }

    /**
     * 清理加速_点击 
     */
    fun stHomeWifiCleanSpeedupClick(){
        EvAgent.sendEvent("home_wifi_clean_speedup_click")
    }

    /**
     * 微信加速_点击 
     */
    fun stHomeWifiWechatSpeedupClick(){
        EvAgent.sendEvent("home_wifi_wechat_speedup_click")
    }

    /**
     * 视频加速_点击 
     */
    fun stHomeWifiVideoSpeedupClick(){
        EvAgent.sendEvent("home_wifi_video_speedup_click")
    }

    /**
     * 切换wifi_点击 
     */
    fun stHomeWifiSwitchClick(){
        EvAgent.sendEvent("home_wifi_switch_click")
    }

    /**
     * 已连接wifi_下方弹框_展示 
     */
    fun stWifilistGetpopShow(){
        EvAgent.sendEvent("wifilist_getpop_show")
    }

    /**
     * 已连接wifi_点击 
     */
    fun stWifilistGetClick(){
        EvAgent.sendEvent("wifilist_get_click")
    }

    /**
     * 未连接wifi_点击 
     */
    fun stWifilistNoClick(){
        EvAgent.sendEvent("wifilist_no_click")
    }

    /**
     * 已连接wifi_下方弹框_取消按钮_点击 
     */
    fun stWifilistGetpopCloseClick(){
        EvAgent.sendEvent("wifilist_getpop_close_click")
    }

    /**
     * 已连接wifi_一键加速_点击 
     */
    fun stWifilistGetpopWifispeedupClick(){
        EvAgent.sendEvent("wifilist_getpop_wifispeedup_click")
    }

    /**
     * 已连接wifi_网络测试_点击 
     */
    fun stWifilistGetpopSpeedtestClick(){
        EvAgent.sendEvent("wifilist_getpop_speedtest_click")
    }

    /**
     * 已连接wifi_安全检测_点击 
     */
    fun stWifilistGetpopSafetycheckClick(){
        EvAgent.sendEvent("wifilist_getpop_safetycheck_click")
    }

    /**
     * 已连接wifi_信号增强_点击 
     */
    fun stWifilistGetpopSignalboostClick(){
        EvAgent.sendEvent("wifilist_getpop_signalboost_click")
    }

    /**
     * 已连接wifi_热点信息_点击 
     */
    fun stWifilistGetpopHotspotinfoClick(){
        EvAgent.sendEvent("wifilist_getpop_hotspotinfo_click")
    }

    /**
     * 已连接wifi_举报钓鱼_点击 
     */
    fun stWifilistGetpopAccusalClick(){
        EvAgent.sendEvent("wifilist_getpop_accusal_click")
    }

    /**
     * 已连接wifi_断开网络_点击 
     */
    fun stWifilistGetpopDisconnectClick(){
        EvAgent.sendEvent("wifilist_getpop_disconnect_click")
    }

    /**
     * 未连接wifi_输入密码弹框_确认按钮_点击 
     */
    fun stWifilistNopopGetClick(){
        EvAgent.sendEvent("wifilist_nopop_get_click")
    }

    /**
     * 未连接wifi_输入密码弹框_取消按钮_点击 
     */
    fun stWifilistNopopCloseClick(){
        EvAgent.sendEvent("wifilist_nopop_close_click")
    }

    /**
     * 未连接wifi_输入密码弹框_展示 
     */
    fun stWifilistNopopShow(){
        EvAgent.sendEvent("wifilist_nopop_show")
    }

    /**
     * 小工具设置成功 
     */
    fun stWidgetLongCreate(){
        EvAgent.sendEvent("widget_long_create")
    }

    /**
     * 小工具三格-桌面图标-点击使用 
     */
    fun stWidgetLongIconClick(){
        EvAgent.sendEvent("widget_long_icon_click")
    }

    /**
     * 弹出小工具设置_展示 
     */
    fun stWidgetLongSystemShow(){
        EvAgent.sendEvent("widget_long_system_show")
    }

    /**
     * 常驻通知栏-展示 
     */
    fun stNotifyShow(){
        EvAgent.sendEvent("notify_show")
    }

    /**
     * 常驻通知栏-公共区域-点击（进入app首页） 
     */
    fun stNotifyHomeClick(){
        EvAgent.sendEvent("notify_home_click")
    }

    /**
     * 常驻通知栏-一键加速-点击 
     */
    fun stNotifySpeedupClick(){
        EvAgent.sendEvent("notify_speedup_click")
    }

    /**
     * 主页_定位获取_点击 
     */
    fun stHomeLocationClick(){
        EvAgent.sendEvent("home_location_click")
    }

    /**
     * 主页_定位获取_成功 
     */
    fun stHomeLocationSuccess(){
        EvAgent.sendEvent("home_location_success")
    }

    /**
     * 弹出权限请求 
     */
    fun stAPopReqpermissionShow(){
        EvAgent.sendEvent("A_pop_reqpermission_show")
    }

    /**
     * 权限全通过 
     */
    fun stAPopReqpermissionAllpass(){
        EvAgent.sendEvent("A_pop_reqpermission_allpass")
    }

    /**
     * wifi_视频广告_展示_总和 
     */
    fun stWifiVideoadShowAll(){
        EvAgent.sendEvent("wifi_videoad_show_all")
    }

    /**
     * wifi_插屏广告_展示_总和 
     */
    fun stWifiMsgadShowAll(){
        EvAgent.sendEvent("wifi_msgad_show_all")
    }

    /**
     * 主页_防蹭网_点击 
     */
    fun stHomeRubnetClick(){
        EvAgent.sendEvent("home_rubnet_click")
    }

    /**
     * 主页_热点分享_点击 
     */
    fun stHomeSharingClick(){
        EvAgent.sendEvent("home_sharing_click")
    }


}