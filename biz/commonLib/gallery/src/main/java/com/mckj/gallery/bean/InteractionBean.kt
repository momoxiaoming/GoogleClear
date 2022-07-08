package com.mckj.gallery.bean

import com.mckj.gallery.datebase.entity.RecycledBean

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/17 15:29
 * @desc
 */
data class InteractionBean(
    var mediaList: List<MediaBean>? = null,
    var position: Int = 0,
    var removeBlock: ((bean: MediaBean) -> Unit)?=null,
    var regainBlock: ((recycleBean: RecycledBean) -> Unit)?=null
)