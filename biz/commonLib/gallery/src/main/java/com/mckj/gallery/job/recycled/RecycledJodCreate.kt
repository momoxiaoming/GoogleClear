package com.mckj.gallery.job.recycled

import android.content.Context
import com.mckj.gallery.bean.GalleryConstants
import com.mckj.gallery.bean.MediaBean
import com.mckj.gallery.job.Job
import com.mckj.gallery.job.JobCreator
import java.security.AccessControlContext

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/23 17:20
 * @desc
 * mediaBean 或 mediaList 只需要传一个
 */
class RecycledJodCreate(
    var context: Context,
    var block: ((status: GalleryConstants.RemoveStatus) -> Unit)? = null
) : JobCreator {
    private lateinit var mData: Any

    constructor(mediaList: List<MediaBean>,
                context: Context,
                block: ((status: GalleryConstants.RemoveStatus) -> Unit)? = null) :this(context,block) {
        mData = mediaList
    }

    constructor(mediaBean: MediaBean,
                context: Context,
                block: ((status: GalleryConstants.RemoveStatus) -> Unit)? = null) :this(context,block){
        mData = mediaBean
    }

    override fun create(): Job? {
        val params = Job.Params(mData)
        return RecycledJob(params, context, block)
    }
}