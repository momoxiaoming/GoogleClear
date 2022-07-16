package com.mckj.gallery.job.recycled

import android.util.Log
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.gallery.utils.GalleryToolsUtil
import com.mckj.gallery.utils.InjectUtils
import com.mckj.gallery.job.Job
import com.mckj.gen.St
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/25 17:18
 * @desc 清理回收站中超过7天的 资源
 */
class CleanRecycledDataJob : Job {

    companion object {
        const val Tag = "CleanRecycledDataJob"
    }

    override fun doJob(): Boolean {
        val recycledRepository = InjectUtils.getRecycledRepository()
        GlobalScope.launch(Dispatchers.IO) {
            val allData = recycledRepository.queryRecycledData()
            allData.let {
                Log.d(Tag, "所有被回收对象：${it.size}")
                var count = 0
                for (bean in it) {
                    if (GalleryToolsUtil.checkRecycledTime(bean.recycledTime) > 7) {
                        Log.d(Tag, "回收对象：${bean.originalPath}")
                        count++
                        FileUtil.delete(bean.originalPath)
                        recycledRepository.deleteById(bean)
                    }
                }
                if (count > 0) {
                    St.stRecycleAutoDelete(count.toString())
                }
            }
        }
        return true
    }
}