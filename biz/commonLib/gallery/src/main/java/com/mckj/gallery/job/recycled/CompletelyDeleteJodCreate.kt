package com.mckj.gallery.job.recycled

import com.mckj.gallery.datebase.entity.RecycledBean
import com.mckj.gallery.job.Job
import com.mckj.gallery.job.JobCreator

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/25 11:20
 * @desc
 */
class CompletelyDeleteJodCreate(recycledBean: RecycledBean) : JobCreator {
    private var mData: Any = recycledBean

    override fun create(): Job? {
        val params = Job.Params(mData)
        return CompletelyDeleteJob(params)
    }
}