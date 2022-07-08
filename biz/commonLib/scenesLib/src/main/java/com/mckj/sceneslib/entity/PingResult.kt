package com.mckj.sceneslib.entity

data class PingResult(val host: String, val text: String) {
    /**
     * ping成功状态，成功为0，失败为-1
     */
    var status = -1

    /**
     * 掉包率
     */
    var loss = 100

    /**
     * 最小延时
     */
    var min = -1

    /**
     * 平均延时
     */
    var avg = -1

    /**
     * 最大延时
     */
    var max = -1

    /**
     * 波动
     */
    var mdev = -1
}