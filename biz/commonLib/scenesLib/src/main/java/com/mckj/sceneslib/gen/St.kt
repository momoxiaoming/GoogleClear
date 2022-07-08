package com.mckj.sceneslib.gen

import android.content.Context
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import com.org.proxy.EvAgent
import com.dn.vi.app.base.app.AppMod
import com.org.openlib.utils.SpUtil


/**
 * Analyse
 * Created by [als-gen] at 16:19:39 2022-04-28.
 */
object St {

    private val context: Context
        get() = AppMod.app



    // ================================
    // Events count: 63
    // ================================

    // arrayMap.clear() 会让arrayMap使用缓存池


    /**
     * 落地页前_动画_展示  
     * @param from 站内外  
     * @param type 场景名称   
     */
    fun stLevelFlashShow(from: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        map["type"] = type
        
        EvAgent.sendEventMap("level_flash_show", map)
        map.clear()
    }

    /**
     * 落地页前_动画_结束  
     * @param from 站内外  
     * @param type 场景名称  
     */
    fun stLevelFlashEnd(from: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        map["type"] = type
        
        EvAgent.sendEventMap("level_flash_end", map)
        map.clear()
    }

    /**
     * 落地页_页面_展示  
     * @param from 站内外  
     * @param type 场景名称  
     */
    fun stLevelShow(from: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        map["type"] = type
        
        EvAgent.sendEventMap("level_show", map)
        map.clear()
    }

    /**
     * 落地页_返回按钮_左上角_点击  
     * @param type 来源  
     */
    fun stLevelReturnClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("level_return_click", map)
        map.clear()
    }

    /**
     * 落地页_顶部返回按钮_点击_左边  
     * @param type 来源  
     */
    fun stWifiLevelBackButtonTopClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("wifi_level_backButtonTop_click", map)
        map.clear()
    }

    /**
     * 落地页_居中返回按钮_点击  
     * @param type 来源  
     */
    fun stWifiLevelBackButtonMiddleClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("wifi_level_backButtonMiddle_click", map)
        map.clear()
    }

    /**
     * 落地页_右上角返回按钮_点击  
     * @param type 来源  
     */
    fun stWifiLevelBackButtonrightClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("wifi_level_backButtonright_click", map)
        map.clear()
    }

    /**
     * wifi_落地页_功能引导_点击  
     * @param type 来源  
     */
    fun stLevelGuideClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("level_guide_click", map)
        map.clear()
    }

    /**
     * 落地页_广告_展现  
     * @param from 站内外  
     * @param type 场景名称  
     */
    fun stLevelAdShow(from: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        map["type"] = type
        
        EvAgent.sendEventMap("level_ad_show", map)
        map.clear()
    }

    /**
     * 落地页_广告_展现  
     * @param type 来源  
     */
    fun stLevelAdClose(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("level_ad_close", map)
        map.clear()
    }

    /**
     * 落地页_广告_点击  
     * @param type 来源  
     */
    fun stLevelAdClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("level_ad_click", map)
        map.clear()
    }

    /**
     * 落地页前_广告_展示  
     * @param type 来源  
     */
    fun stLevelBeforeAdShow(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("level_before_ad_show", map)
        map.clear()
    }

    /**
     * 落地页前_广告_点击  
     * @param type 来源  
     */
    fun stLevelBeforeAdClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("level_before_ad_click", map)
        map.clear()
    }

    /**
     * 落地页前_广告关闭按钮_点击  
     * @param type 来源  
     */
    fun stLevelBeforeAdClose(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("level_before_ad_close", map)
        map.clear()
    }

    /**
     * 落地页后_视频广告_展示  
     * @param type 来源  
     */
    fun stLevelBackAdShow(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("level_back_ad_show", map)
        map.clear()
    }

    /**
     * 落地页后_视频广告_点击  
     * @param type 来源  
     */
    fun stLevelBackAdClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("level_back_ad_click", map)
        map.clear()
    }

    /**
     * 落地页后_广告_关闭  
     * @param type 来源  
     */
    fun stLevelBackAdClose(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("level_back_ad_close", map)
        map.clear()
    }

    /**
     * 主页_卡顿优化_点击 
     */
    fun stHomeCatonClick(){
        EvAgent.sendEvent("home_caton_click")
    }

    /**
     * 网络测速_扫描_开始 
     */
    fun stSpeedScanStart(){
        EvAgent.sendEvent("speed_scan_start")
    }

    /**
     * 网络测速_扫描_返回键_点击 
     */
    fun stSpeedScanReturnClick(){
        EvAgent.sendEvent("speed_scan_return_click")
    }

    /**
     * 网络测速_测速报告_展示 
     */
    fun stSpeedReportShow(){
        EvAgent.sendEvent("speed_report_show")
    }

    /**
     * 网络测速_抢红包测速_点击 
     */
    fun stSpeedRedPacketClick(){
        EvAgent.sendEvent("speed_red_packet_click")
    }

    /**
     * 抢红包测速_扫描_开始 
     */
    fun stRedPacketScanStart(){
        EvAgent.sendEvent("red_packet_scan_start")
    }

    /**
     * 抢红包测速_扫描结果_展示 
     */
    fun stRedPacketResultShow(){
        EvAgent.sendEvent("red_packet_result_show")
    }

    /**
     * 抢红包测速_扫描_返回键_点击 
     */
    fun stRedPacketReturnClick(){
        EvAgent.sendEvent("red_packet_return_click")
    }

    /**
     * 抢红包测速_重新测速_点击 
     */
    fun stRedPacketRetestClick(){
        EvAgent.sendEvent("red_packet_retest_click")
    }

    /**
     * 应用内权限弹窗展示  
     * @param what 权限名称  
     * @param from 触发功能来源  
     */
    fun stPermissionSysDialogShow(what: String, from: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["from"] = from
        
        EvAgent.sendEventMap("permission_sys_dialog_show", map)
        map.clear()
    }

    /**
     * 应用内权限弹窗关闭  
     * @param what 权限名称  
     * @param type 权限结果  
     */
    fun stPermissionSysDialogDismiss(what: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["type"] = type
        
        EvAgent.sendEventMap("permission_sys_dialog_dismiss", map)
        map.clear()
    }

    /**
     * 应用内权限引导弹窗展示  
     * @param what 权限名称  
     * @param from 触发功能来源  
     */
    fun stPermissionAppDialogShow(what: String, from: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["from"] = from
        
        EvAgent.sendEventMap("permission_app_dialog_show", map)
        map.clear()
    }

    /**
     * 应用内权限引导弹窗结束  
     * @param what 权限名称  
     * @param type 点击结果  
     */
    fun stPermissionAppDialogDismiss(what: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["type"] = type
        
        EvAgent.sendEventMap("permission_app_dialog_dismiss", map)
        map.clear()
    }

    /**
     * 主页_功能按钮_点击  
     * @param type 功能按钮的名称  
     */
    fun stHomeFunctionClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("home_function_click", map)
        map.clear()
    }

    /**
     * 工具箱_功能按钮_点击  
     * @param type 功能按钮的名称  
     */
    fun stKitFunctionClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("kit_function_click", map)
        map.clear()
    }

    /**
     * 小组件弹窗准备弹出 
     */
    fun stDesktopWidgetPrePop(){
        EvAgent.sendEvent("desktop_widget_pre_pop")
    }

    /**
     * 小组件弹窗展示 
     */
    fun stDesktopWidgetPop(){
        EvAgent.sendEvent("desktop_widget_pop")
    }

    /**
     * 小组件设置结果  
     * @param type 组件设置结果  
     */
    fun stDesktopWidgetSetSuccess(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("desktop_widget_set_success", map)
        map.clear()
    }

    /**
     * 桌面小组件按钮点击  
     * @param type 调起结果  
     */
    fun stODesktopWidgetClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("O_desktop_widget_click", map)
        map.clear()
    }

    /**
     * 退出优化_二次确认弹窗_展示  
     * @param from 对应场景  
     */
    fun stExitConfirmPopupShow(from: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        
        EvAgent.sendEventMap("exit_confirm_popup_show", map)
        map.clear()
    }

    /**
     * 退出优化_二次确认弹窗_点击  
     * @param from 对应场景  
     * @param type Keep或者Stop  
     */
    fun stExitConfirmPopupClick(from: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        map["type"] = type
        
        EvAgent.sendEventMap("exit_confirm_popup_click", map)
        map.clear()
    }

    /**
     * 优化结果  
     * @param from 对应场景  
     * @param type 清理结果差  
     */
    fun stOptimizeLevelShow(from: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        map["type"] = type
        
        EvAgent.sendEventMap("optimize_level_show", map)
        map.clear()
    }

    /**
     * 垃圾文件_扫描动画_展示  
     * @param what 垃圾大小  
     * @param where 场景来源，站外站内  
     * @param from 对应任务  
     * @param type 耗时  
     */
    fun stScanJunkFlashShow(what: String, where: String, from: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["where"] = where
        map["from"] = from
        map["type"] = type
        
        EvAgent.sendEventMap("scan_junk_flash_show", map)
        map.clear()
    }

    /**
     * 扫描结果页_点击清理  
     * @param type 是否自动点击  
     */
    fun stOptimizeLevelClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("optimize_level_click", map)
        map.clear()
    }

    /**
     * 落地页信息流_广告_展示 
     */
    fun stLevelMsgShow(){
        EvAgent.sendEvent("level_msg_show")
    }

    /**
     * 通知清理_清理_展示 
     */
    fun stNoticeCleanShow(){
        EvAgent.sendEvent("notice_clean_show")
    }

    /**
     * 通知清理_清理_点击 
     */
    fun stNoticeCleanAllClick(){
        EvAgent.sendEvent("notice_clean_all_click")
    }

    /**
     * 智能音量_点击  
     * @param type 类型  
     */
    fun stSetVolumeClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("set_volume_click", map)
        map.clear()
    }

    /**
     * 智能音量_展示 
     */
    fun stSetVolumeShow(){
        EvAgent.sendEvent("set_volume_show")
    }

    /**
     * 声波除尘_扬声器_开始除尘_点击 
     */
    fun stDustSpeakerStartClick(){
        EvAgent.sendEvent("dust_speaker_start_click")
    }

    /**
     * 声波除尘_听筒除尘_点击 
     */
    fun stDustReceiverClick(){
        EvAgent.sendEvent("dust_receiver_click")
    }

    /**
     * 声波除尘_听筒除尘_开始除尘_点击 
     */
    fun stDustReceiverStartClick(){
        EvAgent.sendEvent("dust_receiver_start_click")
    }

    /**
     * 插屏广告展示 
     */
    fun stScanInsertAdShow(){
        EvAgent.sendEvent("scan_insert_ad_show")
    }

    /**
     * 插屏广告关闭 
     */
    fun stScanInsertAdColse(){
        EvAgent.sendEvent("scan_insert_ad_colse")
    }

    /**
     * 首页遮罩_展示  
     * @param from 场景类型  
     */
    fun stMaskShow(from: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        
        EvAgent.sendEventMap("mask_show", map)
        map.clear()
    }

    /**
     * 首页遮罩_点击  
     * @param from 场景类型  
     * @param type 点击类型  
     */
    fun stMaskClick(from: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        map["type"] = type
        
        EvAgent.sendEventMap("mask_click", map)
        map.clear()
    }

    /**
     * 清理落地页_点击返回 
     */
    fun stLevelReturnClose(){
        EvAgent.sendEvent("level_return_close")
    }

    /**
     * 清理落地页_点击功能引导  
     * @param type 功能名  
     */
    fun stLevelRecommendClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        whenFirstRecommendClick(map)
        EvAgent.sendEventMap("level_recommend_click", map)
        map.clear()
    }

    /**
     * 清理落地页_自动功能跳转  
     * @param type 功能名  
     */
    fun stLevelRecommendClickAuto(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        whenFirstRecommendClick(map)
        EvAgent.sendEventMap("level_recommend_click_auto", map)
        map.clear()
    }

    private fun whenFirstRecommendClick(map: ArrayMap<String, String>) {
        val boolean = SpUtil.getBoolean("level_recommend_click", true)
        SpUtil.put("level_recommend_click", false)
        if (boolean) {
            map["first"] = "true"
        }
    }

    /**
     * 退出优化_威胁页_点击  
     * @param type 功能名  
     */
    fun stExitConfirmThreatenClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("exit_confirm_threaten_click", map)
        map.clear()
    }

    /**
     * 威胁结果页_信息流展示  
     * @param from 功能名  
     * @param type 应用内或者应用外  
     */
    fun stAdMsgThreatenShow(from: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        map["type"] = type
        
        EvAgent.sendEventMap("ad_msg_threaten_show", map)
        map.clear()
    }

    /**
     * 退出优化_威胁页_展示  
     * @param from 功能名  
     */
    fun stExitConfirmThreatenShow(from: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        
        EvAgent.sendEventMap("exit_confirm_threaten_show", map)
        map.clear()
    }

    /**
     * 功能推荐池_保护  
     * @param type 类型  
     */
    fun stRecommendPoolProtect(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("recommend_pool_protect", map)
        map.clear()
    }

    /**
     * 功能_保护  
     * @param type 类型  
     */
    fun stRecommendFunctionProtect(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("recommend_function_protect", map)
        map.clear()
    }

    /**
     * 功能推荐卡片_展示  
     * @param type 功能名称  
     */
    fun stRecommendCardShow(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("recommend_card_show", map)
        map.clear()
    }

    /**
     * 功能推荐卡片_点击  
     * @param type 类型  
     */
    fun stRecommendCardClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("recommend_card_click", map)
        map.clear()
    }

    /**
     * 是否有广告
     * @param scenesName 广告场景名称
     * @param adName 广告名称
     * @param adCache 广告缓存结果
     */
    fun stIfAdIn(scenesName: String, adName: String, adCache: String) {
        val arrayMap = arrayMapOf<String, String>()
        arrayMap[scenesName] = adCache
        arrayMap["ad_name"] = adName
        EvAgent.sendEventMap("if_ad_in", arrayMap)
        arrayMap.clear()
    }

    /**
     * 记账_点击
     * @param type 类型
     */
    fun stSetAccountingClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("set_accounting_click", map)
        map.clear()
    }

    /**
     * 记账_展示
     */
    fun stSetAccountingShow(){
        EvAgent.sendEvent("set_accounting_show")
    }

    /**
     * 网络监控_点击
     */
    fun stSetMonitorClick(){
        EvAgent.sendEvent("set_monitor_click")
    }

    /**
     * 网络监控_展示
     */
    fun stSetMonitorShow(){
        EvAgent.sendEvent("set_monitor_show")
    }

    /**
     * 倒数日_点击
     * @param type 类型
     */
    fun stSetCountdownClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("set_countdown_click", map)
        map.clear()
    }

    /**
     * 倒数日_展示
     */
    fun stSetCountdownShow(){
        EvAgent.sendEvent("set_countdown_show")
    }

    /**
     * 字体放大_点击
     * @param type 类型
     */
    fun stSetFontClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("set_font_click", map)
        map.clear()
    }

    /**
     * 字体放大_展示
     */
    fun stSetFontShow(){
        EvAgent.sendEvent("set_font_show")
    }


}