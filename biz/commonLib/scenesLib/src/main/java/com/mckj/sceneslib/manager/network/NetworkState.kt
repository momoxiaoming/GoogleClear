package com.mckj.sceneslib.manager.network

/**
 * Describe:
 *
 * Created By yangb on 2020/10/15
 */
enum class NetworkState {
    /**
     * 连接中
     */
    CONNECTING,

    /**
     * 关闭中
     */
    DISCONNECTING,

    /**
     * 已连接
     */
    CONNECTED,

    /**
     * 已关闭
     */
    DISCONNECTED,

    /**
     * 验证错误
     */
    ERROR_AUTHENTICATING,

    /**
     * 连接超时
     */
    ERROR_TIMEOUT,

    /**
     * 未知
     */
    UNKNOWN,

}