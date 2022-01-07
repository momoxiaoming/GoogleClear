package com.dn.datalib

/**
 * ARouterPath
 *
 * @author mmxm
 * @date 2022/1/4 16:27
 */
object ARouterPath {
    object Cleanup{

        /**
         * 主页
         */
        const val FRAGMENT_HOME = "/v_cl/fragment/home"

        /**
         * 垃圾详情
         */
        const val FRAGMENT_JUNK_DETAIL = "/cl/fragment/junk_detail"

        /**
         * 提醒页,包含设置壁纸,设置小工具等功能
         */
        const val FRAGMENT_POINT = "/cl/fragment/point"
    }

    /**
     * 应用场景
     */
    object Scenes {

        /**
         * wifi设备详情
         */
        const val FRAGMENT_WIFI_DEVICE_DETAIL = "/scenes/fragment/wifi_device_detail"

        /**
         * 文件管理
         */
        const val FRAGMENT_FILE_MENU = "/scenes/fragment/file_menu"

    }

    /**
     * 清理大字版 路径
     */
    object Cleanupx {

        /**
         * 主页
         */
        const val FRAGMENT_HOME = "/vest_cleanupx/fragment/home"

        /**
         * 工具集合
         */
        const val FRAGMENT_TOOLS = "/cleanupx/fragment/tools"

        /**
         * 相册首页
         */
        const val GALLERY_PATH = "/gallery/main"

        /**
         * 相册回收站
         */
        const val GALLERY_RECYCLED_PATH = "/gallery/recycled"

        /**
         * 相册预览
         */
        const val GALLERY_PREVIEW_PATH = "/gallery/preview"
    }
}