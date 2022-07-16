package com.mckj.api.data

/**
 * @author xx
 * @version 1
 * @createTime 2021/8/6 11:59
 * @desc
 */
object Constants {

    /**
     * 分类-其他
     */

    const val JUNK_CATE_OTHER = 0

    /**
     * 分类-缓存
     */
    const val JUNK_CATE_CACHE = 1

    /**
     * 分类-广告
     */
    const val JUNK_CATE_AD = 2

    /**
     * 分类-下载
     */
    const val JUNK_CATE_DOWN = 3

    /**
     * 分类-日志
     */
    const val JUNK_CATE_LOG = 4

    /**
     * ======================垃圾的文件类型========================
     */
    /**
     * 默认类型
     */
    const val FILE_TYPE_DEFAULT = 0

    /**
     * 图片类型
     */
    const val FILE_TYPE_IMAGE = 1

    /**
     * 音频类型
     */
    const val FILE_TYPE_AUDIO = 2

    /**
     * 视频类型
     */
    const val FILE_TYPE_VIDEO = 3

    /**
     * 日志
     */
    const val FILE_TYPE_LOG = 4

    /**
     * 压缩包
     */
    const val FILE_TYPE_ZIP = 5

    /**
     * 安装包
     */
    const val FILE_TYPE_APK = 6

    const val FILE_TYPE_EMPTY_DIR = 7


    const val SCAN_STATUS_START = 10
    const val SCAN_STATUS_ING = 100
    const val SCAN_STATUS_END = 101
    const val SCAN_STATUS_ERROR = 400
    const val CLEAN_FINISH = 500

    /**
     * 类别-普通缓存
     */
    const val CATEGORY_NORMAL_CACHE = 1

    /**
     * 类别-广告缓存
     */
    const val CATEGORY_AD_CACHE = 2

    /**
     * 类别-残留垃圾
     */
    const val CATEGORY_RESIDUAL = 3

    /**
     * 类别-无用文件
     */
    const val CATEGORY_UN_USE = 4


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

    /**
     * 文档类型
     */
    const val TYPE_DOCUMENTS = 4


}