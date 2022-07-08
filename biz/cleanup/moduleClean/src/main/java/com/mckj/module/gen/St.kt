package com.mckj.module.gen

import android.content.Context
import androidx.collection.ArrayMap
import com.org.proxy.EvAgent
import com.dn.vi.app.base.app.AppMod


/**
 * Analyse
 * Created by [als-gen] at 14:48:31 2021-12-30.
 */
object St {

    private val context: Context
        get() = AppMod.app



    // ================================
    // Events count: 19
    // ================================

    // arrayMap.clear() 会让arrayMap使用缓存池


    /**
     * 软件专清_扫描_开始
     * @param type 专清软件type
     */
    fun stAppcleanScanStart(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("appclean_scan_start", map)
        map.clear()
    }

    /**
     * 软件专清_扫描结果_展示
     * @param what 垃圾大小
     * @param type 专清软件type
     */
    fun stAppcleanScanResultShow(what: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["type"] = type

        EvAgent.sendEventMap("appclean_scan_result_show", map)
        map.clear()
    }

    /**
     * 软件专清_一键清理_点击
     * @param type 专清软件type
     */
    fun stAppcleanCleanClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("appclean_clean_click", map)
        map.clear()
    }

    /**
     * 软件专清_一键清理_勾选框_点击
     * @param type 专清软件type
     */
    fun stAppcleanCleanCheckBoxClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("appclean_clean_check_box_click", map)
        map.clear()
    }

    /**
     * 软件专清_手动清理_类型_点击
     * @param what 类型（图片、视频、文件）
     * @param type 软件type
     */
    fun stAppcleanManualCleanPictureClick(what: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["type"] = type

        EvAgent.sendEventMap("appclean_manual_clean_picture_click", map)
        map.clear()
    }

    /**
     * 软件专清_空状态_展示
     * @param what 空状态来源(一键清理、手动清理_图片、无垃圾等等)
     * @param type 软件type
     */
    fun stAppcleanEmptyShow(what: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["type"] = type

        EvAgent.sendEventMap("appclean_empty_show", map)
        map.clear()
    }

    /**
     * 软件专清_图片_按钮点击
     * @param what 操作按钮名称(保存、删除、删除弹窗_删除等)
     * @param type 软件type
     */
    fun stAppcleanPictureButtonCilck(what: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["type"] = type

        EvAgent.sendEventMap("appclean_picture_button_cilck", map)
        map.clear()
    }

    /**
     * 软件专清_视频_按钮点击
     * @param what 操作按钮名称（保存、删除、删除弹窗_删除等）
     * @param type 软件type
     */
    fun stAppcleanVideoButtonCilck(what: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["type"] = type

        EvAgent.sendEventMap("appclean_video_button_cilck", map)
        map.clear()
    }

    /**
     * 软件专清_文件_按钮点击
     * @param what 操作按钮名称（保存、删除、删除弹窗_删除等）
     * @param type 软件type
     */
    fun stAppcleanFileButtonCilck(what: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["what"] = what
        map["type"] = type

        EvAgent.sendEventMap("appclean_file_button_cilck", map)
        map.clear()
    }

    /**
     * 软件专清_图片_大图详情_上滑删除
     * @param type 软件type
     */
    fun stAppcleanPictureDetailUpGlide(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("appclean_picture_detail_up_glide", map)
        map.clear()
    }

    /**
     * 软件专清_视频_大图详情_上滑删除
     * @param type 软件type
     */
    fun stAppcleanVideoDetailUpGlide(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("appclean_video_detail_up_glide", map)
        map.clear()
    }

    /**
     * 软件专清_文件_分享
     * @param type 软件type
     */
    fun stAppcleanFileShare(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("appclean_file_share", map)
        map.clear()
    }

    /**
     * 软件专清_文件_删除
     * @param type 软件type
     */
    fun stAppcleanFileDelete(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("appclean_file_delete", map)
        map.clear()
    }

    /**
     * 应用管理_功能按钮_点击
     * @param type 按钮名称
     */
    fun stManagementButtonClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("management_button_click", map)
        map.clear()
    }

    /**
     * 网络测速_功能按钮_点击
     * @param type 按钮名称
     */
    fun stSpeedButtonClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type

        EvAgent.sendEventMap("speed_button_click", map)
        map.clear()
    }

    /**
     * 网络测速_扫描_开始
     */
    fun stSpeedScanStart(){
        EvAgent.sendEvent("speed_scan_start")
    }

    /**
     * 网络测速_测速报告_展示
     */
    fun stSpeedReportShow(){
        EvAgent.sendEvent("speed_report_show")
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





}