package com.mckj.module.utils

import com.mckj.api.client.JunkConstants
import com.mckj.module.gen.St
import okhttp3.internal.wait

/**
 * @author xx
 * @version 1
 * @createTime 2021/10/9 14:27
 * @desc
 */
object EventTrack {

    /**
     * 通过type返回出运维能看懂的名称
     * @param type [JunkConstants.Session.WECHAT_CACHE]或者[JunkConstants.Session.QQ_CACHE]
     * @return QQ或者微信。不是以上两种类型会返回未知
     */
    private fun getTypeNameByType(type: Int) = when (type) {
        JunkConstants.Session.WECHAT_CACHE -> "QQ"
        JunkConstants.Session.QQ_CACHE -> "微信"
        else -> "未知"
    }

    /**
     * 清理_扫描_开始
     */
    fun stScanStart(type: Int) {
        val t = getTypeNameByType(type)
        St.stAppcleanScanStart(t)
    }



    /**
     * 微信清理_扫描结果_展示
     * @param type 垃圾大小
     */
    fun stScanResultShow(type: Int, junkSize: String) {
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanScanResultShow(junkSize,typeNameByType)
    }

    /**
     * 微信清理_一键清理_点击
     */
    fun stCleanClick(type: Int) {
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanCleanClick(typeNameByType)
    }

    /**
     * 微信清理_一键清理_勾选框_点击
     */
    fun stCleanCheckBoxClick(type: Int) {
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanCleanCheckBoxClick(typeNameByType)
    }

    /**
     * 微信清理_手动清理_图片_点击
     */
    fun stManualCleanPictureClick(type: Int) {
        val what = "图片"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanManualCleanPictureClick(what,typeNameByType)
    }

    /**
     * 微信清理_手动清理_视频_点击
     */
    fun stManualCleanVideoClick(type: Int) {
        val what = "视频"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanManualCleanPictureClick(what,typeNameByType)
    }

    /**
     * 微信清理_手动清理_文件_点击
     */
    fun stManualCleanFileClick(type: Int) {
        val what = "文件"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanManualCleanPictureClick(what,typeNameByType)
    }

    /**
     * 微信清理_橙色背景_展示
     */
    fun stOrangeShow(type: Int) {
        val typeNameByType = getTypeNameByType(type)
    }

    /**
     * 微信清理_主色背景_展示
     */
    fun stThemeColorShow(type: Int) {
        val typeNameByType = getTypeNameByType(type)
    }

    /**
     * 微信清理_一键清理_空状态_展示
     */
    fun stCleanEmptyShow(type: Int) {
        val what = "一键清理"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanEmptyShow(what,typeNameByType)
    }

    /**
     * 微信清理_手动清理_图片_空状态_展示
     */
    fun stManualCleanPictureEmptyShow(type: Int) {
        val what = "手动清理_图片"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanEmptyShow(what,typeNameByType)
    }

    /**
     * 微微信清理_手动清理_视频_空状态_展示
     */
    fun stManualCleanVideoEmptyShow(type: Int) {
        val what = "手动清理_视频"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanEmptyShow(what,typeNameByType)
    }

    /**
     * 微信清理_手动清理_文件_空状态_展示
     */
    fun stManualCleanFileEmptyShow(type: Int) {
        val what = "手动清理_文件"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanEmptyShow(what,typeNameByType)
    }

    /**
     * 微信清理_无垃圾空状态_展示
     */
    fun stNoGarbageEmptyShow(type: Int) {
        val what = "无垃圾"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanEmptyShow(what,typeNameByType)
    }

    /**
     * 微信清理_无微信空状态_展示
     */
    fun stUnInstallView(type: Int) {
        val what = "未安装"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanEmptyShow(what,typeNameByType)
    }

    /**
     * 微信清理_图片_保存_点击
     * @param type 保存张数
     */
    fun stPictureSaveCilck(type: Int, count: String) {
        val what = "保存"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanPictureButtonCilck(what,typeNameByType)
    }

    /**
     * 微信清理_图片_删除_点击
     * @param type 删除张数
     */
    fun stPictureDeleteClick(type: Int, count: String) {
        val what = "删除"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanPictureButtonCilck(what,typeNameByType)
    }

    /**
     * 图片_删除弹窗_删除_点击
     */
    fun stPicturePopDeleteClick(type: Int) {
        val what = "删除弹窗_删除"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanPictureButtonCilck(what,typeNameByType)

    }

    /**
     * 图片_删除弹窗_取消_点击
     */
    fun stPicturePopCancelClick(type: Int) {
        val what = "删除弹窗_取消"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanPictureButtonCilck(what,typeNameByType)
    }

    /**
     * 图片_大图详情_上滑删除
     */
    fun stPictureDetailUpGlide(type: Int) {
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanPictureDetailUpGlide(typeNameByType)
    }

    /**
     * 微信清理_视频_保存_点击
     * @param type 保存张数
     */
    fun stVideoSaveClick(type: Int, count: String) {
        val what = "保存"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanVideoButtonCilck(what,typeNameByType)
    }

    /**
     * 微信清理_视频_删除_点击
     * @param type 删除张数
     */
    fun stVideoDeleteClick(type: Int, count: String) {
        val what = "删除"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanVideoButtonCilck(what,typeNameByType)
    }

    /**
     * 视频_删除弹窗_删除_点击
     */
    fun stVideoPopDeleteClick(type: Int) {
        val what = "删除弹窗_删除"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanVideoButtonCilck(what,typeNameByType)
    }

    /**
     * 视频_删除弹窗_取消_点击
     */
    fun stVideoPopCancelClick(type: Int) {
        val what = "删除弹窗_取消"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanVideoButtonCilck(what,typeNameByType)
    }

    /**
     * 视频_大图详情_上滑删除
     */
    fun stVideoDetailUpGlide(type: Int) {
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanVideoDetailUpGlide(typeNameByType)
    }

    /**
     * 视频_视频日期_最新_点击
     */
    fun stVideoDateLatelyClick(type: Int) {
        val what = "日期_最新"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanVideoButtonCilck(what,typeNameByType)
    }

    /**
     * 视频_视频日期_最久_点击
     */
    fun stVideoDateOldestClick(type: Int) {
        val what = "日期_最久"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanVideoButtonCilck(what,typeNameByType)
    }

    /**
     * 视频_视频大小_最大_点击
     */
    fun stVideoSizeLargestClick(type: Int) {
        val what = "大小_最大"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanVideoButtonCilck(what,typeNameByType)
    }

    /**
     * 视频_视频大小_最小_点击
     */
    fun stVideoSizeSmallestClick(type: Int) {
        val what = "大小_最小"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanVideoButtonCilck(what,typeNameByType)
    }

    /**
     * 微信清理_文件_分享
     */
    fun stFileShare(type: Int) {
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanFileShare(typeNameByType)
    }

    /**
     * 文件_查找弹窗_分享_点击
     */
    fun stFileFindPopShareClick(type: Int) {
        val typeNameByType = getTypeNameByType(type)

    }

    /**
     * 文件_查找弹窗_取消_点击
     */
    fun stFileFindPopCancleClick(type: Int) {
        val typeNameByType = getTypeNameByType(type)

    }

    /**
     * 微信清理_文件_删除
     */
    fun stFileDelete(type: Int) {
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanFileDelete(typeNameByType)
    }

    /**
     * 文件_删除弹窗_删除_点击
     */
    fun stFileDeletePopDeleteClick(type: Int) {
        val what = "删除弹窗_删除"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanFileButtonCilck(what,typeNameByType)
    }

    /**
     * 文件_删除弹窗_取消_点击
     */
    fun stFileDeletePopCancleClick(type: Int) {
        val what = "删除弹窗_取消"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanFileButtonCilck(what,typeNameByType)
    }

    /**
     * 文件_打开应用弹窗_某一应用_点击
     */
    fun stFileOpenPopAppClick(type: Int) {
        val what = "打开应用弹窗_某一应用"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanFileButtonCilck(what,typeNameByType)
    }

    /**
     * 文件_文件日期_最新
     */
    fun stFileDateLatelyClick(type: Int) {
        val what = "日期_最新"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanFileButtonCilck(what,typeNameByType)
    }

    /**
     * 文件_文件日期_最久
     */
    fun stFileDateOldestClick(type: Int) {
        val what = "日期_最久"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanFileButtonCilck(what,typeNameByType)
    }

    /**
     * 文件_文件大小_最大
     */
    fun stFileSizeLargestClick(type: Int) {
        val what = "大小_最大"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanFileButtonCilck(what,typeNameByType)
    }

    /**
     * 文件_文件大小_最小
     */
    fun stFileSizeSmallestClick(type: Int) {
        val what = "大小_最小"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanFileButtonCilck(what,typeNameByType)
    }

    /**
     * 文件_文件类型_某一选项_点击
     */
    fun stFileTypeClick(type: Int) {
        val what = "类型_某一选项"
        val typeNameByType = getTypeNameByType(type)
        St.stAppcleanFileButtonCilck(what,typeNameByType)
    }

    /**
     * 应用管理_卸载_点击
     */
    fun stManagementUnloadClick(){
        val type = "卸载"
        St.stManagementButtonClick(type)
    }

    /**
     * 应用管理_卸载弹窗_卸载_点击
     */
    fun stManagementPopUnloadClick(){
        val type = "卸载弹窗_卸载"
        St.stManagementButtonClick(type)
    }

    /**
     * 应用管理_卸载弹窗_取消_点击
     */
    fun stManagementPopCancelClick(){
        val type = "卸载弹窗_取消"
        St.stManagementButtonClick(type)
    }

    /**
     * 应用管理_使用频率_很久未用_点击
     */
    fun stManagementFrequencyOldestClick(){
        val type = "使用频率_很久未用"
        St.stManagementButtonClick(type)
    }

    /**
     * 应用管理_使用频率_最近使用_点击
     */
    fun stManagementFrequencyLatelyClick(){
        val type = "使用频率_最近使用"
        St.stManagementButtonClick(type)
    }

    /**
     * 应用管理_安装日期_最新_点击
     */
    fun stManagementDateLatelyClick(){
        val type = "安装日期_最新"
        St.stManagementButtonClick(type)
    }

    /**
     * 应用管理_安装日期_最久_点击
     */
    fun stManagementDateOldestClick(){
        val type = "安装日期_最久"
        St.stManagementButtonClick(type)
    }

    /**
     * 应用管理_应用大小_最大_点击
     */
    fun stManagementSizeLargestClick(){
        val type = "应用大小_最大"
        St.stManagementButtonClick(type)
    }

    /**
     * 应用管理_应用大小_最小_点击
     */
    fun stManagementSizeSmallestClick(){
        val type = "应用大小_最小"
        St.stManagementButtonClick(type)
    }

    /**
     * 应用管理_搜索框_点击
     */
    fun stManagementSearchClick(){
        val type = "搜索框"
        St.stManagementButtonClick(type)
    }
}