package com.mckj.sceneslib.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Describe:
 *
 * Created By yangb on 2021/5/31
 */

/**
 * 场景跳转类数据
 */
@Parcelize
data class ScenesJumpData(

    /**
     * 场景类型
     */
    val type: Int,

    /**
     * 引导类型
     */
    val guideTypes: List<Int>? = null,

    /**
     * 落地页前广告名
     */
    val landingBeforeAdName: String? = null,

    /**
     * 落地页广告名
     */
    val landingAdName: String? = null,

    /**
     * 落地页后广告名
     */
    val landingAfterAdName: String? = null,

    ) : Parcelable {

}

/**
 * Describe: 应用场景落地页样式
 *
 * Created By yangb on 2021/5/24
 */
enum class ScenesLandingStyle {

    /**
     * 默认
     */
    DEFAULT,

    /**
     * 加速样式(目前是手机加速独有)
     */
    SPEED,

    /**
     * 清理样式
     */
    CLEAN,

}

data class ScenesLottieData(
    /**
     * 动画资源目录
     */
    val dir: String,
    /**
     * 动画json文件路径
     */
    val filePath: String,

    val startFrame: LottieFrame?,
    val runFrame: LottieFrame?,
    val endFrame: LottieFrame?
) {

}

/**
 * Describe:场景任务数据
 *
 * Created By yangb on 2021/5/31
 */
data class ScenesTaskData(

    /**
     * 标题
     */
    val title: String,

    /**
     * 描述
     */
    val desc: String,

    /**
     * 任务列表
     */
    val taskList: List<ScenesTask>? = null
)

data class ScenesTask(
    /**
     * 任务名称
     */
    val name: String,

    /**
     * 任务接口
     */
    val block: suspend () -> Boolean
) {

    companion object {
        const val STATE_NORMAL = 1
        const val STATE_LOADING = 2
        const val STATE_COMPLETE = 3
    }

    /**
     * 状态
     */
    var state = STATE_NORMAL

}

data class LottieFrame(
    val start: Int,
    val end: Int
)
