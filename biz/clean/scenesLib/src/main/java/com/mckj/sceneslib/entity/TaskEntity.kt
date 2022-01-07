package com.mckj.sceneslib.entity

import com.google.gson.annotations.SerializedName

/**
 * 任务
 *
 * @author mmxm
 * @date 2021/5/11 11:33
 */
data class TaskEntity(
    @SerializedName("desc")
    var desc: String,
    @SerializedName("state")
    var state: Int = 0,  //0未完成,1已完成
    @SerializedName("taskTag")
    var taskTag: String, //任务标识
) {


}