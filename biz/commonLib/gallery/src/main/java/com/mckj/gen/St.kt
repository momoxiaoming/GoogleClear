package com.mckj.gen

import android.content.Context
import androidx.collection.ArrayMap
import com.dn.vi.app.base.app.AppMod
import com.org.proxy.EvAgent


/**
 * Analyse
 * Created by [als-gen] at 16:09:59 2022-04-28.
 */
object St {

    private val context: Context
        get() = AppMod.app



    // ================================
    // Events count: 16
    // ================================

    // arrayMap.clear() 会让arrayMap使用缓存池


    /**
     * 相册清理_清理页面_上滑 
     */
    fun stPhotoCleanPageUpGlide(){
        EvAgent.sendEvent("photo_clean_page_up_glide")
    }

    /**
     * 相册清理_清理页面_左右划 
     */
    fun stPhotoCleanPageBothSides(){
        EvAgent.sendEvent("photo_clean_page_both_sides")
    }

    /**
     * 相册清理_缩略图_点击 
     */
    fun stPhotoCleanThumbnailClick(){
        EvAgent.sendEvent("photo_clean_thumbnail_click")
    }

    /**
     * 相册清理_缩略图_滑动 
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
     * 相册清理_引导_展示  
     * @param type 步骤展示名称  
     */
    fun stPhotoCleanGuidanceShow(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("photo_clean_guidance_show", map)
        map.clear()
    }

    /**
     * 相册清理_功能_点击  
     * @param type 步骤展示名称  
     */
    fun stPhotoCleanFunctionClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("photo_clean_function_click", map)
        map.clear()
    }

    /**
     * 图片清理介绍弹窗_展示 
     */
    fun stPhotoCleanPopupShow(){
        EvAgent.sendEvent("photo_clean_popup_show")
    }

    /**
     * 图片清理介绍弹窗_点击  
     * @param type 点击功能名  
     */
    fun stPhotoCleanPopupClick(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("photo_clean_popup_click", map)
        map.clear()
    }

    /**
     * 图片清理页_无照片_展示 
     */
    fun stPhotoCleanNoPhoto(){
        EvAgent.sendEvent("photo_clean_no_photo")
    }

    /**
     * 图片清理页_展示 
     */
    fun stPhotoCleanShow(){
        EvAgent.sendEvent("photo_clean_show")
    }

    /**
     * 图片选择页_展示  
     * @param from 上报来源  
     */
    fun stPhotoSelectShow(from: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        
        EvAgent.sendEventMap("photo_select_show", map)
        map.clear()
    }

    /**
     * 图片选择页_点击Clean Up  
     * @param from 上报来源  
     * @param type 上报删除图片数量  
     */
    fun stPhotoSelectClick(from: String, type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        map["type"] = type
        
        EvAgent.sendEventMap("photo_select_click", map)
        map.clear()
    }

    /**
     * 相册清理 操作记录  
     * @param type 操作类型  
     */
    fun stPhotoCleanAction(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("photo_clean_action", map)
        map.clear()
    }


}