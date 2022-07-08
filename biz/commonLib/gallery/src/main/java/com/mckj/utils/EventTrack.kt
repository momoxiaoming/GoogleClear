package com.mckj.utils

import com.mckj.gen.St
import com.org.proxy.EvAgent

object EventTrack {

    /**
     * 相册清理_上滑引导_展示
     */
    fun stPhotoCleanUpGuidanceShow(){
        val type = "上滑"
        St.stPhotoCleanGuidanceShow(type)
    }

    /**
     * 相册清理_左右划引导_展示
     */
    fun stPhotoCleanBothSidesGuidanceShow(){
        val type = "左右划"
        St.stPhotoCleanGuidanceShow(type)
    }

    /**
     * 相册清理_回收站引导_展示
     */
    fun stPhotoCleanRecycleShow(){
        val type = "回收站"
        St.stPhotoCleanGuidanceShow(type)
    }

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
     * 相册清理_回收站_点击
     */
    fun stPhotoCleanRecycleClick(){
        val type = "回收站"
        St.stPhotoCleanFunctionClick(type)
    }

    /**
     * 相册清理_相册分类_点击
     */
    fun stPhotoCleanClassifyClick(){
        val type = "相册分类"
        St.stPhotoCleanFunctionClick(type)
    }

    /**
     * 相册清理_缩略图_点击
     */
    fun stPhotoCleanThumbnailClick(){
        val type = "缩略图"
        St.stPhotoCleanFunctionClick(type)
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
        val newType = "相册分类弹窗-$type"
        St.stPhotoCleanFunctionClick(newType)
    }

    /**
     * 回收站_恢复_点击
     */
    fun stRecycleRecoverClick(){
        val type = "回收站_恢复"
        St.stPhotoCleanFunctionClick(type)
    }

    /**
     * 回收站_彻底删除_点击
     */
    fun stRecycleDeleteClick(){
        val type = "回收站_彻底删除"
        St.stPhotoCleanFunctionClick(type)
    }

    /**
     * 回收站_图片_大图预览
     */
    fun stRecyclePhotoPreview(){
        EvAgent.sendEvent("recycle_photo_preview")
    }

}