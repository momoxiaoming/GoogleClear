package com.mckj.module.queue

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.org.openlib.help.Consumer
import com.mckj.api.entity.JunkInfo
import com.mckj.gallery.bean.GalleryConstants
import com.mckj.gallery.bean.MediaBean
import com.mckj.gallery.job.JobChain
import com.mckj.gallery.job.recycled.RecycledJodCreate
import com.org.openlib.utils.Log
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.ArrayList

/**
 * @author xx
 * @version 1
 * @createTime 2021/12/20 17:51
 * @desc
 */
class MdRemover {

    companion object {
        const val TAG = "MdRemover"
    }

    private val mdQueue = LinkedBlockingQueue<QueueBean>()

    private var mOptQueueBean: QueueBean? = null

    fun removeJunks(
        context: Context,
        list: MutableList<JunkInfo>,
        cons: Consumer<MediaBean?>
    ) {
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            next.mediaBean?.let {
                val queueBean = QueueBean(it)
                mdQueue.add(queueBean)
            }
        }
        loop(context, cons)
    }

    private fun loop(context: Context, cons: Consumer<MediaBean?>) {
        Log.d("xx", "Loop：${mdQueue.size}")
        if (!mdQueue.isEmpty()) {
            poll()?.let {
                Log.d("xx", "取出对象：${it.mediaBean.id}")
                mOptQueueBean = it
                mdQueue.remove(it)
                preRemove(context, it) { remove ->
                    Log.d("xx", "预删除结果：${it.mediaBean.id}----remove:$remove")
                    if (remove) {
                        cons.accept(it.mediaBean)
                    } else {
                        cons.accept(null)
                    }
                    loop(context, cons)
                }
            }
        }
    }


    private fun startRemove() {
        Log.d("xx", "startRemove:-->${mOptQueueBean?.mediaBean?.id}")
        mOptQueueBean?.let {
            it.block?.invoke(true)
        } ?: let {
            Log.d(TAG, "start end  queue is empty")
        }
    }

    private fun reject() {
        Log.d("xx", "reject:-->${mOptQueueBean?.mediaBean?.id}")
        mOptQueueBean?.let {
            it.block?.invoke(false)
        } ?: let {
            Log.d(TAG, "start end  queue is empty")
        }
    }

    private fun poll(): QueueBean? {
        return mdQueue.poll()
    }


    private fun preRemove(context: Context, queueBean: QueueBean, cons: Consumer<Boolean>) {
        Log.d("xx", "开始预删除:${queueBean.mediaBean.id}")
        queueBean.mediaBean.let { bean ->
            JobChain.newInstance()
                .addJob(RecycledJodCreate(bean, context, block = {
                    when (it) {
                        GalleryConstants.RemoveStatus.REJECT_BY_PERMISSION -> {
                            Log.d("xx", "需要授权")
                            queueBean.block = { removed ->
                                Log.d("xx", "授权结果:${queueBean.mediaBean.id}：-->$removed")
                                if (removed) {
                                    preRemove(context, queueBean, cons)
                                } else {
                                    cons.accept(false)
                                }
                            }
                        }
                        GalleryConstants.RemoveStatus.REMOVED -> {
                            Log.d("xx", "不需要授权")
                            cons.accept(true)
                        }
                    }
                }).create())
        }
    }


    data class QueueBean(val mediaBean: MediaBean, var block: ((remove: Boolean) -> Unit)? = null) {
        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val (bean, _) = o as QueueBean
            return mediaBean.id == bean.id
        }

        override fun hashCode(): Int {
            return Objects.hash(mediaBean.id)
        }
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GalleryConstants.REQUEST_CODE_SECURITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    startRemove()
                } else {
                    reject()
                }
            }
        }
    }
}