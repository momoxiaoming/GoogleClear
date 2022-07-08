package com.mckj.template.entity

/**
 *  author : leix
 *  date : 2022/3/5 18:23
 *  description : 清理wifi常量池
 */
object TepConstants {

    const val CLEAN = 0
    const val HEALTHY = 1024 * 1024 * 20
    const val WARN = 1024 * 1024 * 100

    object Menu {
        const val HOME_MENU_MAIN = 10//主菜单
        const val HOME_MENU_BUS = 20//特色菜单
        const val HOME_MENU_BUS_PLUS = 30//特色plus菜单
        const val HOME_MENU_RECOMMEND = 40//推荐菜单
    }

    object Sp {
        const val CACHE = "CACHE"
        const val RECOMMEND_INFO = "recommend_info"
        const val LAST_SCAN_TIME = "last_scan_time"
    }

    object CleanHeaderStatus {
        const val DENY = 1
        const val CACHE = 2
        const val SCAN = 3
    }

    /**
     * 清理的页面状态
     */
    object ScanUIStatus {
        const val DENY = 1//权限拒绝状态
        const val SCAN = 2//扫描状态
        const val COMPLETE_CLEAN = 3//干净状态
        const val HEALTHY = 4//扫描健康状态
        const val NORMAL = 5//扫描普通状态
        const val WARN = 6//扫描警告状态
    }

}