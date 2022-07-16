package com.mckj.gallery.job.recycled

import android.content.Context
import android.util.Log
import com.dn.vi.app.cm.utils.FileUtils
import com.mckj.gallery.bean.MediaBean
import com.mckj.gallery.impl.PictureImpl
import com.mckj.gallery.impl.VideoImpl
import com.mckj.gallery.utils.GalleryToolsUtil
import com.mckj.gallery.utils.InjectUtils
import com.mckj.baselib.helper.getApplicationContext
import com.mckj.gallery.bean.GalleryConstants
import com.mckj.gallery.bean.MediaConstant
import com.mckj.gallery.datebase.entity.RecycledBean
import com.mckj.gallery.job.Job

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/23 17:07
 * @desc job任务：
 *  1：copy文件到cache 目录
 *  2：数据库插入1条回收记录
 *  3：移除媒体库资源
 */
class RecycledJob(
    params: Job.Params,
    var context: Context,
    var block: ((status: GalleryConstants.RemoveStatus) -> Unit)? = null
) : Job {
    companion object {
        const val Tag = "RecycledJob"
    }

    private var mParams: Job.Params = params

    override fun doJob(): Boolean {
        Log.d(Tag, "开启回收资源任务")
        val data = mParams.mData
        if (data is MediaBean) {
            data.originalPath?.apply {
                getTempRecycledPath(this)?.apply {
                    Log.d(Tag, "获取临时路径：$this")
                    val isSuccess = GalleryToolsUtil.copyFile(data.originalPath!!, this)
                    Log.d(Tag, "copy文件：isSuccess:-->$isSuccess")
                    if (isSuccess) {
                        removeMedia(data) {
                            when (it) {
                                GalleryConstants.RemoveStatus.REMOVED -> {
                                    var mimeType: String? = null
                                    if (data.isImage()) {
                                        mimeType = MediaConstant.MEDIA_TYPE_IMG
                                    } else if (data.isVideo()) {
                                        mimeType = MediaConstant.MEDIA_TYPE_VIDEO
                                    }
                                    InjectUtils.getRecycledRepository().insert(
                                        RecycledBean(
                                            id = data.id!!,
                                            originalPath = this@apply,
                                            size = data.length,
                                            mimeType = mimeType,
                                            recycledTime = System.currentTimeMillis()
                                        )
                                    )

                                    block?.invoke(it)
                                }
                                GalleryConstants.RemoveStatus.REJECT_BY_PERMISSION -> {
                                    block?.invoke(it)
                                    FileUtils.deleteFile(data.originalPath)
                                }
                            }
                        }
                    }
                }
            }

        }else if (data is List<*> && data as? List<MediaBean> != null ){
            Log.d(Tag, "获取临时路径：${data}")
            //批量删除图片
            removeButchMedia(data){
                when(it){
                    GalleryConstants.RemoveStatus.REMOVED -> {
                        block?.invoke(it)
                    }
                    GalleryConstants.RemoveStatus.REJECT_BY_PERMISSION -> {
                        block?.invoke(it)
                    }
                }
            }
        }
        return true
    }

    private fun removeMedia(
        data: MediaBean,
        block: ((status: GalleryConstants.RemoveStatus) -> Unit)?
    ) {
        if (data.isImage()) {
            PictureImpl().deleteMedia(context, data, block)
        } else {
            VideoImpl().deleteMedia(context, data, block)
        }
    }

    private fun removeButchMedia(
        data: List<MediaBean>,
        block: ((status: GalleryConstants.RemoveStatus) -> Unit)?
    ){
        PictureImpl().deleteBatchMedia(context,data,block)
    }

    private fun getTempRecycledPath(originPath: String): String? {
        var path: String? = null
        GalleryToolsUtil.getRecycledDirectory()?.let {
            path = it.absolutePath + "/" + GalleryToolsUtil.getName(originPath)
        }
        return path
    }
}