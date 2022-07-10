package com.org.openlib.path

/**
 * Describe:
 *
 * Created By yangb on 2020/12/22
 */
object ARouterPath {

    const val EXTRA_APP_MAIN = "/app/main"

    /**
     * fragment路径
     */
    const val EXTRA_FRAGMENT_PATH = "fragment:path"

    /**
     * 空Activity容器
     */
    const val ACTIVITY_CONTAINER = "/open/activity/container"

    /**
     * Activity容器-全屏
     */
    const val ACTIVITY_CONTAINER_FULL = "/open/activity/container_full"

    /**
     * Activity容器-有标题
     */
    const val ACTIVITY_CONTAINER_TITLE = "/open/activity/container_title"

    /**
     * 欢迎页
     */
    const val FRAGMENT_WELCOME = "/open/fragment/welcome"

    /**
     * 壁纸
     */
    const val FRAGMENT_WALLPAPER = "/open/fragment/wallpaper"

    /**
     * 壁纸
     */
    const val FRAGMENT_OVS_WALLPAPER = "/open/fragment/ovs_wallpaper"


    /**
     * 设置页
     */
    const val FRAGMENT_SETTING = "/open/fragment/setting"
    const val FRAGMENT_SETTING_MORE_LCS = "/open/fragment/setting/more_lcs"
    const val FRAGMENT_SETTING_MORE_CARD = "/open/fragment/setting/more_card"

    /**
     * 关于页
     */
    const val FRAGMENT_ABOUT = "/open/fragment/about"

    /**
     * web Dialog
     */
    const val DIALOG_WEB = "/open/dialog/web"

    /**
     * web Dialog Proxy
     */
    const val DIALOG_WEB_PROXY = "/open/dialog/web/proxy"

    /**
     * 二级欢迎页
     */
    const val SECOND_SPLASH = "/open/fragment/splash"

    /**
     * 落地页展示
     */
    const val FRAGMENT_LANDING_PAGE = "/open/fragment/landing_page"

    /**
     * tab
     */
    object Tab {

        /**
         * 新闻tab
         */
        const val NEWS = "/open/tab/news"

        /**
         * 视频tab
         */
        const val VIDEO = "/open/tab/video"

        /**
         * 电商
         */
        const val E_COMMERCE = "/open/tab/e_commerce"

    }

    const val PAGE_WEB = "/openlib/page/web"
    /**
     * 内置下载服务。
     * 下载apk会自动安装。
     */
    const val SRV_DOWNLOADER = "/openlib/extsrv/downloader"

    /**
     * 实现[WebViewInteruptServer] Component
     */
    const val SRV_WEBVIEW_INTERRUPT = "/openlib/web/interrupt"
}