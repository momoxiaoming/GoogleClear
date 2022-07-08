package com.org.admodule.data

/**
 * AdStatus
 *
 * @author mmxm
 * @date 2022/7/4 23:09
 */
enum class AdStatus {

    /**
     * 默认状态
     */
    NORMAL,

    /**
     * 加载成功
     */
    LOAD_SUCCESS,

    /**
     * 加载结束
     */
    LOAD_END,

    /**
     * 显示成功
     */
    SHOW_SUCCESS,

    /**
     * 点击
     */
    CLICK,

    /**
     * 广告奖励
     */
    REWARD,

    /**
     * 关闭
     */
    CLOSE,

    /**
     * 错误状态
     */
    ERROR;

    /**
     * 是否大于或等于此状态
     */
    fun isAtLeast(adStatus: AdStatus): Boolean {
        return compareTo(adStatus) >= 0
    }

}