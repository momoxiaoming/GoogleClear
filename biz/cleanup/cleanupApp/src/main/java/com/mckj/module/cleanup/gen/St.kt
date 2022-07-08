package com.mckj.module.cleanup.gen

import android.content.Context
import androidx.collection.ArrayMap
import com.org.proxy.EvAgent
import com.dn.vi.app.base.app.AppMod


/**
 * Analyse
 * Created by [als-gen] at 11:08:11 2021-07-30.
 */
object St {

    private val context: Context
        get() = AppMod.app



    // ================================
    // Events count: 57
    // ================================

    // arrayMap.clear() 会让arrayMap使用缓存池


    /**
     * 首页_展示 
     */
    fun stCleanShow(){
        EvAgent.sendEvent("clean_show")
    }

    /**
     * 主页_一键清理按钮_点击 
     */
    fun stHomeCleanClick(){
        EvAgent.sendEvent("home_clean_click")
    }

    /**
     * 主页_一键清理图标_点击 
     */
    fun stHomeIconcleanClick(){
        EvAgent.sendEvent("home_iconclean_click")
    }

    /**
     * 主页_一键清理自动扫描次数 
     */
    fun stHomeCleanAutoScan(){
        EvAgent.sendEvent("home_clean_auto_scan")
    }

    /**
     * 主页_立即清理_点击 
     */
    fun stHomeCleanCleanClick(){
        EvAgent.sendEvent("home_clean_clean_click")
    }

    /**
     * 主页_立即清理图标_点击 
     */
    fun stHomeCleanIconcleanClick(){
        EvAgent.sendEvent("home_clean_iconclean_click")
    }

    /**
     * 主页_清理详细页_点击 
     */
    fun stHomeDetailClick(){
        EvAgent.sendEvent("home_detail_click")
    }

    /**
     * 主页_手机加速_点击 
     */
    fun stHomeSpeedupClick(){
        EvAgent.sendEvent("home_speedup_click")
    }

    /**
     * 主页_手机降温_点击 
     */
    fun stHomeCooldownClick(){
        EvAgent.sendEvent("home_cooldown_click")
    }

    /**
     * 主页_微信清理_点击 
     */
    fun stHomeWechatcleanClick(){
        EvAgent.sendEvent("home_wechatclean_click")
    }

    /**
     * 主页_超级省电_点击 
     */
    fun stHomeSavepowerClick(){
        EvAgent.sendEvent("home_savepower_click")
    }

    /**
     * 主页_网络加速_点击 
     */
    fun stHomeNetworkClick(){
        EvAgent.sendEvent("home_network_click")
    }

    /**
     * 主页_手机杀毒_点击 
     */
    fun stHomeAntivirusClick(){
        EvAgent.sendEvent("home_antivirus_click")
    }

    /**
     * 主页_短视频清理_点击 
     */
    fun stHomeSmallvideoClick(){
        EvAgent.sendEvent("home_smallvideo_click")
    }

    /**
     * 主页_摄像头检测_点击 
     */
    fun stHomeCameraClick(){
        EvAgent.sendEvent("home_camera_click")
    }

    /**
     * 主页_QQ清理_点击 
     */
    fun stHomeQqcleanClick(){
        EvAgent.sendEvent("home_qqclean_click")
    }

    /**
     * 主页_残留清理_点击 
     */
    fun stHomeUninstallClick(){
        EvAgent.sendEvent("home_uninstall_click")
    }

    /**
     * 主页_信号优化_点击 
     */
    fun stHomeSignalClick(){
        EvAgent.sendEvent("home_signal_click")
    }

    /**
     * 主页_文件管理_点击 
     */
    fun stHomeFileClick(){
        EvAgent.sendEvent("home_file_click")
    }

    /**
     * 主页_右上角设置_点击  
     * @param type 来源  
     */
    fun stHomeSetupClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("home_setup_click", map)
        map.clear()
    }

    /**
     * 主页_右上角权限_点击 
     */
    fun stHomeAuthorityClick(){
        EvAgent.sendEvent("home_authority_click")
    }

    /**
     * 主页_相册清理_点击 
     */
    fun stHomePhotoClick(){
        EvAgent.sendEvent("home_photo_click")
    }

    /**
     * 主页_视频清理_点击 
     */
    fun stHomeVideoClick(){
        EvAgent.sendEvent("home_video_click")
    }

    /**
     * 主页_大文件管理_点击 
     */
    fun stHomeBigfileClick(){
        EvAgent.sendEvent("home_bigfile_click")
    }

    /**
     * 主页_安装包清理_点击 
     */
    fun stHomeInstalleClick(){
        EvAgent.sendEvent("home_installe_click")
    }

    /**
     * 主页_音乐清理_点击 
     */
    fun stHomeMusicClick(){
        EvAgent.sendEvent("home_music_click")
    }

    /**
     * 主页_压缩包清理_点击 
     */
    fun stHomeZipClick(){
        EvAgent.sendEvent("home_zip_click")
    }

    /**
     * 首页_扫描_开始 
     */
    fun stHomeScanStart(){
        EvAgent.sendEvent("home_scan_start")
    }

    /**
     * 首页_扫描_停止 
     */
    fun stHomeScanStop(){
        EvAgent.sendEvent("home_scan_stop")
    }

    /**
     * 首页_一键清理_垃圾详情_点击 
     */
    fun stHomeCleanDetailClick(){
        EvAgent.sendEvent("home_clean_detail_click")
    }

    /**
     * 首页_工具箱_点击 
     */
    fun stHomeKitClick(){
        EvAgent.sendEvent("home_kit_click")
    }

    /**
     * 首页_相册清理_点击 
     */
    fun stHomePhotoCleanClick(){
        EvAgent.sendEvent("home_photo_clean_click")
    }

    /**
     * 工具箱_天气_点击 
     */
    fun stKitWeatherClick(){
        EvAgent.sendEvent("kit_weather_click")
    }

    /**
     * 工具箱_页面展示 
     */
    fun stKitPageShow(){
        EvAgent.sendEvent("kit_page_show")
    }

    /**
     * 工具箱_日历_点击 
     */
    fun stKitCalendarClick(){
        EvAgent.sendEvent("kit_calendar_click")
    }

    /**
     * 工具箱_流量使用_点击 
     */
    fun stKitTrafficClick(){
        EvAgent.sendEvent("kit_traffic_click")
    }

    /**
     * 工具箱_手机参数_点击 
     */
    fun stKitParameterClick(){
        EvAgent.sendEvent("kit_parameter_click")
    }

    /**
     * 工具箱_字体大小_点击 
     */
    fun stKitSizeClick(){
        EvAgent.sendEvent("kit_size_click")
    }

    /**
     * 工具箱_手电筒_点击  
     * @param type 开关  
     */
    fun stKitFlashlightClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("kit_flashlight_click", map)
        map.clear()
    }

    /**
     * 工具箱_闹钟_点击 
     */
    fun stKitClockClick(){
        EvAgent.sendEvent("kit_clock_click")
    }

    /**
     * 工具箱_相册_点击 
     */
    fun stKitPhotoClick(){
        EvAgent.sendEvent("kit_photo_click")
    }

    /**
     * 相册清理_上滑引导_展示 
     */
    fun stPhotoCleanUpGuidanceShow(){
        EvAgent.sendEvent("photo_clean_up_guidance_show")
    }

    /**
     * 相册清理_左右划引导_展示 
     */
    fun stPhotoCleanBothSidesGuidanceShow(){
        EvAgent.sendEvent("photo_clean_both_sides_guidance_show")
    }

    /**
     * 相册清理_回收站引导_展示 
     */
    fun stPhotoCleanRecycleShow(){
        EvAgent.sendEvent("photo_clean_recycle_show")
    }

    /**
     * 相册清理_清理页面_上滑 
     */
    fun stPhotoCleanPageUpglide(){
        EvAgent.sendEvent("photo_clean_page_upglide")
    }

    /**
     * 相册清理_清理页面_左右划 
     */
    fun stPhotoCleanPageBothSides(){
        EvAgent.sendEvent("photo_clean_page_both_sides")
    }

    /**
     * 相册清理_回收站_点击 
     */
    fun stPhotoCleanRecycleClick(){
        EvAgent.sendEvent("photo_clean_recycle_click")
    }

    /**
     * 相册清理_相册分类_点击 
     */
    fun stPhotoCleanClassifyClick(){
        EvAgent.sendEvent("photo_clean_classify_click")
    }

    /**
     * 相册清理_缩略图_点击 
     */
    fun stPhotoCleanThumbnailClick(){
        EvAgent.sendEvent("photo_clean_thumbnail_click")
    }

    /**
     * 相册清理_缩略图_点击 
     */
    fun stPhotoCleanThumbnailSlide(){
        EvAgent.sendEvent("photo_clean_thumbnail_slide")
    }

    /**
     * 相册分类弹窗_展示 
     */
    fun stClassifyPopShow(){
        EvAgent.sendEvent("classify_pop_show")
    }

    /**
     * 相册分类弹窗_关闭_点击 
     */
    fun stClassifyPopCloseClick(){
        EvAgent.sendEvent("classify_pop_close_click")
    }

    /**
     * 相册分类弹窗_分类_点击  
     * @param type 分类名称  
     */
    fun stClassifyPopAlbumClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("classify_pop_album_click", map)
        map.clear()
    }

    /**
     * 回收站_恢复_点击 
     */
    fun stRecycleRecoverClick(){
        EvAgent.sendEvent("recycle_recover_click")
    }

    /**
     * 回收站_彻底删除_点击 
     */
    fun stRecycleDeleteClick(){
        EvAgent.sendEvent("recycle_delete_click")
    }

    /**
     * 回收站_图片_大图预览 
     */
    fun stRecyclePhotoPreview(){
        EvAgent.sendEvent("recycle_photo_preview")
    }

    /**
     * 回收站_7天自动删除_删除  
     * @param type 删除张数  
     */
    fun stRecycleAutoDelete(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("recycle_auto_delete", map)
        map.clear()
    }

    /**
     * 主页_推荐_展示
     * @param type 来源
     * @param from 是否首次
     */
    fun stHomeRecommendShow(type: String, from: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        map["from"] = from

        EvAgent.sendEventMap("recommend_card_show", map)
        map.clear()
    }

    /**
     * 主页_推荐_点击
     * @param type 来源
     * @param from 是否首次
     */
    fun stHomeRecommendClick(type: String, from: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        map["from"] = from

        EvAgent.sendEventMap("recommend_card_click", map)
        map.clear()
    }

}