package com.mckj.sceneslib.manager.network

/**
 * Describe:
 *
 * Created By yangb on 2020/10/20
 */
enum class WifiState {
    /**
     * wifi列表为空
     */
    TYPE_SCAN_EMPTY,

    /**
     * wifi已打开
     */
    TYPE_WIFI_ENABLED,

    /**
     * wifi打开中
     */
    TYPE_WIFI_ENABLING,

    /**
     * wifi已关闭
     */
    TYPE_WIFI_DISABLED,

    /**
     * wifi关闭中
     */
    TYPE_WIFI_DISABLING,

    /**
     * 定位未打开
     */
    TYPE_LOCATION_DISABLE,

    /**
     * 定位权限未获取
     */
    TYPE_LOCATION_PERMISSION,

    /**
     * wifi未知状态
     */
    TYPE_WIFI_UNKNOWN,

}