package com.mckj.datalib.entity

/**
 * Describe:ARouterPath
 *
 * Created By yangb on 2020/10/12
 */
object ARouterPath {

    // === activity ===

    //app
    //启动页
    const val APP_ACTIVITY_SPLASH = "/app/activity/splash"

    /**
     * Wifi路径
     */
    object Wifi {

        /**
         * wifi主页
         */
        const val FRAGMENT_HOME = "/d_wi/fragment/home"

        /**
         * 红包wifi
         */
        const val FRAGMENT_HOME_RED_ENVELOPE = "/c_wi/fragment/home_red_envelope"

        /**
         * wifi详情
         */
        const val FRAGMENT_DETAIL = "/wifi/fragment/detail"

        /**
         * wifi列表
         */
        const val FRAGMENT_WIFI_LIST = "/wifi/fragment/wifi_list"

        /**
         * 红包测速
         */
        const val FRAGMENT_ENVELOPE_TEST = "/redPack/fragment/envelope_test"

    }

    /**
     * 广播界面的空白转跳
     */
    object Receiver{
        const val FRAGMENT_EMPTY = "/receiver/fragment/empty"
    }

    /**
     * 清理 路径
     */
    object Cleanup {

        /**
         * 主页
         */
//        const val FRAGMENT_HOME = "/v_cl/fragment/home"

        const val FRAGMENT_HOME = "/v_cl/fragment/home"

        /**
         * 主页推荐
         */
        const val FRAGMENT_HOME_RECOMMEND = "/v_cl_plus/fragment/home_recommend"

        /**
         * 垃圾详情
         */
        const val FRAGMENT_JUNK_DETAIL = "/scene/fragment/junk_detail"

        /**
         * 提醒页,包含设置壁纸,设置小工具等功能
         */
        const val FRAGMENT_POINT = "/cl/fragment/point"

        /**
         * 应用管理页
         */
        const val ACTIVITY_APP_MANAGER = "/cl/fragment/app_manager"


        /**
         * 微信/QQ清理页面
         */
        const val FRAGMENT_CLEAN = "/mc/fragment/TX_CLEAN"

        const val SCENE_CONTAINER = "/scene/activity/sceneContainer"
    }

    object ModuleClean {
        const val MODULE_CLEAN = "/mc/activity/mediaClean"
    }

    /**
     * 清理大字版 路径
     */
    object Cleanupx {

        /**
         * 主页
         */
        const val FRAGMENT_HOME = "/cleanupx/fragment/home"

        /**
         * 主页 TAB
         */
        const val FRAGMENT_TAB_HOME = "/cleanupx/fragment/tab_home"

        /**
         * 工具集合
         */
        const val FRAGMENT_TOOLS = "/cleanupx/fragment/tools"

        /**
         * 清理场景content
         */
        const val FRAGMENT_SCENES_COMMON = "/cleanupx/fragment/scenes"
        /**
         * 工具-放大镜
         */
        const val FRAGMENT_TOOLS_MAGNIFIER = "/cleanupx/fragment/magnifier"

        /**
         * 工具-记账
         */
        const val FRAGMENT_TAB_ACCOUNT = "/cleanupx/fragment/tab_account"

        /**
         * 工具-字体放大
         */
        const val FRAGMENT_TAB_FONT_SCALE = "/cleanupx/fragment/tab_font_scale"

    }


    /**
     * 我的 路径
     */
    object Mine {

        /**
         * 我的主界面
         */
        const val FRAGMENT_HOME = "/mine/fragment/home"

        const val FRAGMENT_HOME_DETAIL = "/mine/fragment/home_detail"

    }

    /**
     * 赚钱
     */
    object Money {

        //主页
        const val FRAGMENT_HOME = "/vest/cornucopia/fm/home"

        //成语
        const val FRAGMENT_PHRASE = "/phrase/new"

        //转盘
        const val MONEY_LOTTERY = "/lottery/dial"

        //我的
        const val MONEY_ME = "/mine/fm/main"
    }

    /**
     * 拼图
     */
    object Puzzle {

        //主页
        const val FRAGMENT_HOME = "/fmPlay/gamefm"
    }

    /**
     * 日历
     */
    object Calendar {

        //主页
        const val FRAGMENT_HOME = "/calendar/home/fragment"

        //运势
        const val FRAGMENT_LUCK = "/calendar/constellation/fragment"

    }

    /**
     * 美化大师
     */
    object CAMERA {

        //主页
        const val FRAGMENT_HOME = "/magic/camera/home"

        //壁纸
        const val WALLPAPER_MAIN = "/wallpaper/main"

        //我的
        const val LIB_CAMERA_MINE = "/lib/camera/mine"

        const val BACKGROUND = "/change/background/bg"

    }

    /**
     * 红包群
     */
    object RPG {


        const val FRAGMENT_LOGIN = "/rpg/fragment/login"  //登录
        const val FRAGMENT_LOTTERY = "/rpg_lottery/fragment/lottery" //转盘
        const val FRAGMENT_PHRASE = "/rpg/fragment/phrase" //成语
        const val FRAGMENT_MINE = "/rpg/fragment/mine" //我的页面
        const val FRAGMENT_WALLET = "/rpg/fragment/wallet" //钱包
        const val FRAGMENT_WITHDRAW = "/rpg/fragment/withdraw" //提现记录
        const val FRAGMENT_PLAY_AWARD = "/rpg_lottery/fragment/play/award"
        const val FRAGMENT_CHAT_CONVERSATION = "/chat/fragment/conversationList" //主页
        const val FRAGMENT_CHAT_RED_DIALOG = "/chat/fragment/chat/award"
        const val FRAGMENT_CHAT_LIST = "/chat/fragment/chat/list"
        const val RPG_ACTIVITY_CHAT_LIST = "/chat/activity/chat/list"
        const val RPG_ACTIVITY_SYSTEM_LIST = "/chat/activity/system/list"
        const val PARAM_SESSION_ID = "param_sessionId"


    }

    /**
     * 大字报
     */
    object Dazibao {
        /**
         * 大字新闻
         */
        const val FRAGMENT_NEWS = "/dazibao/fragment/news"

        /**
         * 大字视频
         */
        const val FRAGMENT_VIDEO = "/dazibao/fragment/video"

        /**
         * 抠图
         */
        const val FRAGMENT_CUTOUT = "/dazibao/fragment/cutout"

        /**
         * 搜索页
         */
        const val PAGE_SEARCHING = "/dazibao/page/searching"

        /**
         * 搜索页
         */
        const val FRAGMENT_SEARCH_RESULT = "/dazibao/search/result"

    }


    /**
     * 应用场景
     */
    object Scenes {

        /**
         * wifi设备详情
         */
        const val FRAGMENT_WIFI_DEVICE_DETAIL = "/scenes/fragment/wifi_device_detail"


    }

    object ComposeWeather {
        /**
         * 主页
         */
        const val FRAGMENT_HOME = "/composeWeather/home/fragment"

        /**
         * 城市
         */
        const val FRAGMENT_WEATHER_CITY = "/city/fragment"

        /**
         * 城市
         */
        const val FRAGMENT_WEATHER_SPLASH = "/splash/fragment"

        /**
         * 天气马甲2
         */
        const val FRAGMENT_VEST_WEATHER2 = "/composeWeather2/fragment/weather2"


        /**
         * 城市选择
         */
        const val FRAGMENT_WEATHER_SELECT_CITY = "/selectCity/fragment"

        /**
         * 城市详情
         */
        const val FRAGMENT_WEATHER_CITY_INFO = "/cityInfo/fragment"
    }


    /**
     * 答题
     */
    object Answer {

        /**
         * 主页
         */
        const val FRAGMENT_HOME = "/answer/fragment/home"

        /**
         * 主页
         */
        const val FRAGMENT_AUDIT_HOME = "/answer/fragment/audit/home"

        /**
         * 诗歌
         */
        const val FRAGMENT_POETRY_HOME = "/poetry/home"

        /**
         *诗人
         */
        const val FRAGMENT_POETRY_POET_DETAIL = "/poetry/poet/detail"

        /**
         * 我的
         */
        const val FRAGMENT_POETRY_MINE = "/poetry/mine"


        /**
         * 任务页
         */
        const val FRAGMENT_TASK = "/answer/fragment/task"

        /**
         * 个人页
         */
        const val FRAGMENT_MINE = "/answer/fragment/mine"
    }

    /**
     * 外卖福利宝
     */
    object TakeoutBool {

        /**
         * 主页
         */
        const val FRAGMENT_HOME = "/tkb/fragment/home"

        /**
         * 返现教程
         */
        const val FRAGMENT_COURSE = "/tkb/fragment/course"

        /**
         * 个人页
         */
        const val FRAGMENT_MINE = "/tkb/fragment/mine"

        /**
         * 提现页
         */
        const val FRAGMENT_WITH_DRAW = "/tkb/fragment/withdraw"

        /**
         * 提现记录页
         */
        const val FRAGMENT_SET_NEW = "/tkb/fragment/record"

        /**
         * 提现规则页
         */
        const val FRAGMENT_RULE = "/tkb/fragment/rule"

        /**
         * 定时页
         */
        const val FRAGMENT_ALARM = "/tkb/fragment/alarm"


        /**
         * 登录页
         */
        const val FRAGMENT_LOGIN = "/tkb/fragment/login"

        /**
         * 下单跳转页
         */
        const val FRAGMENT_ORDER_JUMP_PAGE = "/tkb/fragment/order_jump"

        /**
         * 下单跳转页
         */
        const val FRAGMENT_SETTING = "/tkb/fragment/setting"

        /**
         * 订单查询跳转页
         */
        const val ORDER_CHECK_PAGE = "/tkb/fragment/order_check"
    }

    /**
     * 视频天天看
     */
    object DayDayLook {

        //主页
        const val VIDEO_MONEY_HOME = "/video/money/home"

        //打卡
        const val VIDEO_MONEY_CLOCK = "/video/money/clock"

        //我的
        const val VIDEO_MONEY_MINE = "/video/money/mine"


    }

    object NewTools {
        //落地
        const val NEW_TOOLS_LANDING = "/new_tools/landing"

        //声波除尘
        const val NEW_TOOLS_DUST = "/new_tools/dust"

        //放大镜
        const val NEW_TOOLS_MAGNIFIER = "/new_tools/magnifier"

        //放大镜里的放大图片
        const val NEW_TOOLS_PICTURE_MAGNIFIER = "/picture/new_tools/picture_magnifier"

        //智能音量
        const val NEW_TOOLS_AUDIO = "/new_tools/audio"

        //通知清理
        const val NEW_TOOLS_NOTIFY_COVER = "/new_tools/notify/cover"
        const val NEW_TOOLS_NOTIFY_CONTENT = "/new_tools/notify/content"
        const val NEW_TOOLS_NOTIFY_APP = "/new_tools/notify/app"

        //记账
        const val NEW_TOOLS_ACCOUNT = "/new_tools/account"
        const val NEW_TOOLS_ACCOUNT_RECORD = "/new_tools/account_record"


        //网络检测
        const val NEW_TOOLS_CHECK_NET_WORK = "/new_tools/check_netWork"

        //倒数日
        const val NEW_TOOLS_DAYS = "/new_tools/days"

        //字体放大器
        const val NEW_TOOLS_FONT_SCALE = "/new_tools/font_scale"

    }

}