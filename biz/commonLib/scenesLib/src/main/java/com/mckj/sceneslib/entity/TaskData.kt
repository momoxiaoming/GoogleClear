package com.mckj.sceneslib.entity

/**
 * 任务
 *
 * @author mmxm
 * @date 2021/5/11 11:33
 */
data class TaskData(
    var title: String = "清理中...",
    var tasks: MutableList<TaskEntity> = mutableListOf<TaskEntity>() //任务标识
){

}