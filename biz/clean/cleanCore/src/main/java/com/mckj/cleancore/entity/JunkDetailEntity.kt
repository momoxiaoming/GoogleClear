package com.mckj.cleancore.entity

/**
 * Describe:
 *
 * Created By yangb on 2021/5/14
 */
data class JunkDetailEntity(
    /**
     * 垃圾类别
     */
    val category: Int,
    /**
     * 类型
     */
    val type: Int,
    /**
     * 描述
     */
    val desc: String,

    /**
     * 路径
     */
    val path: String,

    /**
     * 大小
     */
    val size : Long,
) {

    companion object{

        /**
         * 类别-普通缓存
         */
        const val CATEGORY_NORMAL_CACHE = 1

        /**
         * 类别-广告缓存
         */
        const val CATEGORY_AD_CACHE = 2

        /**
         * 类型-默认
         */
        const val TYPE_NORMAL = 0

        /**
         * 类型-图片
         */
        const val TYPE_IMAGE = 1

        /**
         * 类型-音频
         */
        const val TYPE_AUDIO = 2

        /**
         * 类型-频
         */
        const val TYPE_VIDEO = 3

    }

}