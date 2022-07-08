package com.mckj.gallery.job.recycled

import com.mckj.gallery.job.Job
import com.mckj.gallery.job.JobCreator

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/25 17:17
 * @desc
 */
class CleanRecycledDataJodCreate() : JobCreator {

    override fun create(): Job? {
        return CleanRecycledDataJob()
    }
}