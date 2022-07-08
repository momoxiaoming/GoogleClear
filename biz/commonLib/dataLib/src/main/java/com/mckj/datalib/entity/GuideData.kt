package com.mckj.datalib.entity

/**
 * Describe:引导页数据
 *
 * Created By yangb on 2021/3/31
 */
data class GuideData(val type: Int, val block: () -> Boolean) {

    companion object {
        //自动加速
        const val TYPE_AUTO_PHONE_SPEED = 1

        //引导页 一键加速
        const val TYPE_GUIDE_PHONE_SPEED = 2

        //引导页 赚钱
        const val TYPE_GUIDE_MONEY = 3

        //大字报 第一次引导
        const val TYPE_GUIDE_DAZIBAO_FIRST = 4

        //大字报 字体引导
        const val TYPE_GUIDE_DAZIBAO_FONT = 5
    }

}