package com.mckj.gallery.job.recycled

import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.gallery.utils.InjectUtils
import com.mckj.gallery.datebase.entity.RecycledBean
import com.mckj.gallery.job.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/25 15:25
 * @desc 图库彻底删除任务
 */
class CompletelyDeleteJob(params: Job.Params) : Job {

    companion object {
        const val Tag = "CompletelyDeleteJob"
    }

    private var mParams: Job.Params = params
    override fun doJob(): Boolean {
        val data = mParams.mData
        if (data is RecycledBean) {
            GlobalScope.launch(Dispatchers.IO) {
                val deleteSuccess = FileUtil.delete(data.originalPath)
                if (deleteSuccess) {
                    InjectUtils.getRecycledRepository().deleteById(data)
                }
            }
        }
        return true
    }
}