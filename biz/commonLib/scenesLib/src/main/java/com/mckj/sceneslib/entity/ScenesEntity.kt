package com.mckj.sceneslib.entity

import android.os.Parcelable
import com.mckj.sceneslib.R
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

    /**
     * 一键清理样式
     */
    ONE_KEY_CLEAN,

    /**
     * 报告模式（WiFi测速报告）
     */
    REPORT,

    /**
     * 报告模式（清理测速报告）
     */
    REPORT_CLEAN,

    /**
     * 抢红包测速
     */
    ENVELOPE,

    /**
     * 新闻样式
     */
    NEWS,
}

data class DelayTestTaskData(
    val icon: Int,

    //ping的网址名称
    val title: String,
    //ping的网址
    val host: String,
    //延时结果
    var delay: Int,
) : Comparable<DelayTestTaskData> {
    companion object {
        const val STATE_LOADING = 1
        const val STATE_FINISH = 2
        const val STATE_ERROR = 3
    }

    var status: Int = STATE_LOADING

    override fun compareTo(other: DelayTestTaskData): Int {
        if (this.delay == other.delay) {
            return 0
        }

        if (this.delay <= -1) {
            return 1
        }

        if (other.delay <= -1) {
            return -1
        }

        return if (this.delay > other.delay) {
            1
        } else {
            -1
        }
    }
}


data class ScenesLottieData(
    /**
     * 动画资源目录
     */
    val dir: String,
    /**
     * 动画json文件路径
     */
//    val filePath: String,
    val idResRaw:Int,

    val startFrame: LottieFrame?,
    val runFrame: LottieFrame?,
    val endFrame: LottieFrame?,
    val fileRaw:Int=NO_RESOURCE_CODE,
    val startPreFrame: LottieFrame?=null,
    val runPreFrame: LottieFrame?=null
) {
    companion object{
        const val NO_RESOURCE_CODE=-1
    }
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
