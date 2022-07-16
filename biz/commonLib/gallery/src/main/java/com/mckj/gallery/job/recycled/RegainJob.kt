package com.mckj.gallery.job.recycled

import com.mckj.gallery.impl.PictureImpl
import com.mckj.gallery.impl.VideoImpl
import com.mckj.gallery.utils.InjectUtils
import com.mckj.baselib.helper.getApplicationContext
import com.mckj.gallery.datebase.entity.RecycledBean
import com.mckj.gallery.job.Job

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/25 11:25
 * @desc 图库恢复任务
 */
class RegainJob(params: Job.Params) : Job {

    companion object {
        const val Tag = "RegainJob"
    }

    private var mParams: Job.Params = params
    override fun doJob(): Boolean {
        val data = mParams.mData
        if (data is RecycledBean) {
            if (data.isImage()) {
                val insertSuccess =
                    PictureImpl().insertMedia(getApplicationContext(), data.originalPath!!)
                if (insertSuccess) {
                    InjectUtils.getRecycledRepository().deleteById(data)
                }
            } else if (data.isVideo()) {
                    val insertSuccess =
                        VideoImpl().insertMedia(getApplicationContext(), data.originalPath!!)
                    if (insertSuccess) {
                        InjectUtils.getRecycledRepository().deleteById(data)
                    }
            }
        }
        return true
    }
}