package com.mckj.gallery.job

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/23 16:50
 * @desc
 */
interface Job {
    fun doJob():Boolean

    class Params(jobData: Any) {
        var mData: Any? = jobData
    }
}