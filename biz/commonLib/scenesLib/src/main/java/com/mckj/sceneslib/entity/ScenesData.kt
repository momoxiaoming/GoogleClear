package com.mckj.sceneslib.entity

/**
 * Describe:
 *
 * Created By yangb on 2021/3/25
 */
data class ScenesData(
    /**
     * 功能类型
     */
    val type: Int,

    /**
     * key值
     */
    val key: String,

    /**
     * 名称
     */

    val name: String,

    /**
     * 获取描述
     */

    var desc: String,

    /**
     * 高亮提示
     */

    var tip: String,

    /**
     * 获取按钮文本
     */

    var btnText: String,

    /**
     * 获取落地页名称
     */

    var landingName: CharSequence,

    /**
     * 获取落地页描述
     */

    var landingDesc: CharSequence,

    /**
     * 点击事件
     */
    var clickEvent: String = "",
    var block: (() -> Unit)? = null,
    var exposeTimestamp: Long = 0,//场景曝光时间记录
    var mSceneCategory: SceneCategory? = SceneCategory.ALL,//场景归属：默认属于wifi&清理

    /**
     * 落地页推荐功能描述
     */
    var recommendDesc: CharSequence? = null,

    /**
     * 当前场景是否在落地页推荐里被排除
     */
    var isRepu:Boolean = false,

    /**
     * 当前场景扫描到的垃圾大小
     */
    var scanSize: Long? = 0L,

    /**
     * 当前场景扫描到的垃圾大小描述(含单位)
     */
    var scanSizeDesc: String? = "",
    /**
     * 是否走审核模式
     */
    val followAudit:Boolean = true,

    /**
     *   安全状态头部落地状态描述
     */
    var landingSafetyHeaderName:String="",

    /**
     *  安全状态头部落地提示描述
     */
    var landingSafetyHeaderDes:String="",


    ) {
    fun isWifi(): Boolean {
        return mSceneCategory == SceneCategory.WIFI
    }

    fun isCleaner(): Boolean {
        return mSceneCategory == SceneCategory.CLEANER
    }

    //场景曝光时间是否>5分钟
    fun isUsedInFiveMin(): Boolean {
        return (System.currentTimeMillis() - exposeTimestamp) < 1000 * 60 * 5
    }
}

enum class SceneCategory {
    ALL, WIFI, CLEANER
}