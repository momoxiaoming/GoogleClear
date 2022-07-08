package com.mckj.api.client


/**
 * @author leix
 * @version 1
 * @createTime 2021/9/28 14:41
 * @desc
 */
object JunkConstants {

    object Session {
        //应用缓存
        const val APP_CACHE: Int = 1 shl 0

        //微信缓存
        const val WECHAT_CACHE: Int = 1 shl 1

        //QQ缓存
        const val QQ_CACHE: Int = 1 shl 2

        //apk清理
        const val APK = 1 shl 3

        //残留清理
        const val UNINSTALL_RESIDUE = 1 shl 4

        //微信静默清理
        const val WECHAT_SILENT_CACHE: Int = 1 shl 5

        //QQ静默清理
        const val QQ_SILENT_CACHE: Int = 1 shl 6
    }


    // 垃圾分类：  0其它, 1缓存，2广告，3下载.4 log
    object JunkType {
        const val OTHER = 0
        const val CACHE = 1
        const val AD_CACHE = 2
        const val DOWNLOAD = 3
        const val LOG = 4
        const val APK = 5
        const val RAM = 6
        const val EMPTY_DIR = 7//空文件
    }

    //文件类型(0其它, 1图片，2音频，3视频，4日志，5压缩包, 6安装包)
    object JunkFileType {
        const val OTHER = 0
        const val IMG = 1
        const val AUDIO = 2
        const val VIDEO = 3
        const val LOG = 4
        const val ZIP = 5
        const val APK = 6
        const val EMPTY_DIR = 7//空文件
        const val DOCUMENT = 100//文档类型，服务端未定义
    }

    object AppCacheType {

        const val APP_CACHE = 1

        const val IMG_CACHE = 2

        const val VIDEO_CACHE = 3

        const val DOCUMENT_CACHE = 4

        const val APK = 5

        const val RAM = 6

        const val UNINSTALL_RESIDUE = 7
    }

    object JunkCode {
        const val Complete = 200
        const val Doing = 300
        const val Failure = 400
    }

    object ScanStatus {
        const val START = 100
        const val SCAN_IDLE = 101
        const val COMPLETE = 102
        const val ERROR = 103
        const val COMPLETELY_CLEAN = 104//清理完成
        const val CLEAN = 105
        const val SILEN = 106//靜默處理
        const val CACHE = 107//缓存
    }

    //标记垃圾，垃圾传递过程中可以朔源
    object JunkTag {
        const val HOME = 100
        const val WX = 101
        const val QQ = 102
    }
}