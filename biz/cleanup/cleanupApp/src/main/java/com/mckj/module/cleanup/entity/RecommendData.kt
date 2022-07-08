package com.mckj.module.cleanup.entity

class RecommendData(
    val type: Int,

    /**
     * 名称
     */

    val name: String,

    /**
     * 获取描述
     */

    var desc: CharSequence?,

    /**
     * 获取按钮文本
     */

    var btnText: String,

    /**
     * 广告名称
     */

    var adName: String? = "recommend_msg",

    /**
     * 标题
     */
    var title: CharSequence?="",
) {

}
