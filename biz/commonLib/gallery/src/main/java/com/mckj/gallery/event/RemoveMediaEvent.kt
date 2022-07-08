package com.mckj.gallery.event

import com.mckj.gallery.bean.MediaBean

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/26 12:30
 * @desc
 */
data class RemoveMediaEvent(val mediaBean: MediaBean)