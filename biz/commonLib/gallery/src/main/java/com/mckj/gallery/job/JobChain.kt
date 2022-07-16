package com.mckj.gallery.job

import java.util.*
import java.util.concurrent.LinkedBlockingDeque

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/23 16:50
 * @desc
 */
class JobChain {
    private var jobQueue: Queue<Job>? = LinkedBlockingDeque()
    private var queueFree = true

    companion object {
        const val TAG = "GalleryJob"

        fun newInstance(): JobChain {
            return JobChain()
        }
    }

    fun addJob(job: Job?) {
        if (jobQueue?.isEmpty() == true && queueFree) {
            jobQueue?.offer(job)
            start()
        } else {
            jobQueue?.offer(job)
        }
    }

    private fun start() {
            queueFree = false
            var job: Job?
            while (jobQueue?.poll().also { job = it } != null) {
                job?.doJob()
            }
            queueFree = true
    }

    fun clearJob() {
        jobQueue?.clear()
    }
}