package com.mckj.module.cleanup.manager.jump

import android.content.Context

/**
 * Describe:
 *
 * Created By yangb on 2021/7/22
 */
interface IJumpPage {

    /**
     * 跳转天气使用
     */
    fun jumpWeather(context: Context): Boolean

    /**
     * 跳转日历使用
     */
    fun jumpCalender(context: Context): Boolean

    /**
     * 跳转流量使用
     */
    fun jumpNetData(context: Context): Boolean

    /**
     * 跳转手机参数使用
     */
    fun jumpParam(context: Context): Boolean


    /**
     * 跳转字体大小使用
     */
    fun jumpFontSize(context: Context): Boolean

    /**
     * 跳转相册
     */
    fun jumpAlbum(context: Context): Boolean

    /**
     * 跳转闹钟
     */
    fun jumpClock(context: Context): Boolean
}