package com.mckj.module.event

import com.mckj.api.entity.AppJunk


/**
 * @author xx
 * @version 1
 * @createTime 2021/9/16 19:47
 * @desc
 */
data class UpdateAppJunkEvent(var type:Int,var appJunk: AppJunk)