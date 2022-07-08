package com.mckj.sceneslib.manager.scenes

/**
 * Describe:
 *
 * Created By yangb on 2021/5/31
 * wifi场景：
1.手机加速
2.手机降温
3.微信加速
4.网络加速
5.短视频加速
6.摄像头检测
7.QQ加速
8.残留清理
9.信号增强
10.垃圾清理
11.安全检测
12.网络测速（真实功能）
13.省电加速
14.防止蹭网
15.热点分享
16.卡顿优化
17.工具箱
18.相册清理（真实功能）
19.红包测速
20.应用管理


清理场景：
1.手机加速
2.手机降温
3.微信清理（真实功能）
4.超级省电
5.网络加速
6.手机杀毒
7.短视频清理
8.摄像头检测
9.QQ清理（真实功能）
10.残留清理
11.信号优化
12.垃圾清理
13.清理加速
14.网络测速（真实功能）
15.卡顿优化
16.工具箱
17.相册清理（真实功能）
18.垃圾清理大字版
19.红包测速
20.应用管理
 */
object ScenesType {




    /**
     *  wifi场景
     */

    //手机加速
    const val TYPE_PHONE_SPEED = 1

    //手机降温
    const val TYPE_COOL_DOWN = 2

    //微信加速
    const val TYPE_WECHAT_SPEED = 3

    //网络加速
    const val TYPE_NETWORK_SPEED = 4

    //短视频加速
    const val TYPE_SHORT_VIDEO_SPEED = 5

    //摄像头检测
    const val TYPE_CAMERA_CHECK = 6

    //QQ加速
    const val TYPE_QQ_SPEED = 7

    //残留清理
    const val TYPE_UNINSTALL_CLEAN = 8

    //信号增强
    const val TYPE_SIGNAL_BOOST = 9

    //垃圾清理
    const val TYPE_JUNK_CLEAN = 10

    //安全检测
    const val TYPE_SECURITY_CHECK = 11

    //网络测速
    const val TYPE_NETWORK_TEST = 12

    //省电加速
    const val TYPE_POWER_SPEED = 13

    //防止蹭网
    const val TYPE_NETWORK_CHECK = 14

    //热点分享
    const val TYPE_HOT_SHARING = 15

    //卡顿优化
    const val TYPE_CATON_SPEED = 16

    //工具箱
    const val TYPE_TOOLS = 17

    //相册清理
    const val TYPE_ALBUM_CLEAN = 18

    //红包测速
    const val TYPE_ENVELOPE_TEST = 19

    //应用管理
    const val TYPE_APP_MANAGER = 20

    //日历
    const val TYPE_CALENDAR = 28

    //流量使用
    const val TYPE_FLOW_USAGE = 29

    //声波除尘
    const val TYPE_DUST = 30

    //通知栏清理
    const val TYPE_NOTIFY = 31

    //放大镜
    const val TYPE_MAGNIFIER = 32

    //智能音量
    const val TYPE_AUDIO = 34

    //网络检测
    const val TYPE_CHECK_NET_WORK = 35

    //记账
    const val TYPE_ACCOUNT = 36

    //倒数日
    const val TYPE_DAYS = 37

    //字体放大器
    const val TYPE_FONT_SCALE = 38


    /**
     * 清理场景
     */

    //1.手机加速

    //2.手机降温

    const val TYPE_WX_CLEAN = 21

    //超级省电
    const val TYPE_POWER_SAVE = 22

    //手机杀毒
    const val TYPE_ANTIVIRUS = 23

    //短视频清理
    const val TYPE_SHORT_VIDEO_CLEAN = 24

    //摄像头检测

    //QQ清理
    const val TYPE_QQ_CLEAN = 25


    //信号优化
    const val TYPE_SIGNAL_SPEED = 26

    //垃圾清理

    //一键加速
    const val TYPE_ONE_KEY_SPEED = 27

    //应用锁
    const val TYPE_APP_LOCK = 30

    //海外壁纸(不属于场景)
    const val TYPE_OVS_WALLPAPER = 31

    //网络测速
    //卡顿优化
    //工具箱
    //相册清理
    //红包测速
    //应用管理

    //微信清理
    const val TYPE_WX_CACHE_CLEAN = 100
    const val TYPE_QQ_CACHE_CLEAN = 101

    object LandType {
        const val TYPE_RECOMMEND = 201
        const val TYPE_NEWS = 202
    }

}