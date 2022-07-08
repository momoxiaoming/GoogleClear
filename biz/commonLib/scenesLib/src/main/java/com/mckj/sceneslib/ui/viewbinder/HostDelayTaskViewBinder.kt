package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemHostDelayBinding
import com.mckj.sceneslib.entity.DelayTestTaskData
import com.mckj.sceneslib.manager.network.WifiInfo
import org.jetbrains.anko.image
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColor

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class HostDelayTaskViewBinder : DataBindingViewBinder<DelayTestTaskData>() {
    var finish: Boolean = false

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(
            inflater.inflate(
                R.layout.scenes_item_host_delay,
                parent,
                false
            )
        )
    }

    inner class ViewHolder(itemView: View) :
        DataBindingViewHolder<DelayTestTaskData, ScenesItemHostDelayBinding>(itemView) {
/*        init {
            mBinding.tvStatus.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val delayTask = adapter.items[position] as DelayTestTaskData
                    onItemClick(it, position, delayTask)
                }
            }
        }*/

        override fun bindData(t: DelayTestTaskData) {
            mBinding.ivIcon.imageResource = t.icon
            mBinding.tvTitle.text = t.title

            if (!finish){
                loadingStatusView()
                return
            }

            when (t.status) {

                DelayTestTaskData.STATE_LOADING -> {
                    loadingStatusView()
                }

                DelayTestTaskData.STATE_FINISH -> {
                    if (t.delay<=-1){
                        timeOutStatusView(t)
                    }else{
                        successStatusView(t)
                    }
                }

                else -> {
                    timeOutStatusView(t)
                }
            }
        }

        private fun loadingStatusView() {
            mBinding.tvStatus.visibility = View.INVISIBLE
            mBinding.taskStatePb.visibility = View.VISIBLE
            mBinding.tvDelayTime.apply {
                text = "--"
                textColor =
                    ResourcesCompat.getColor(resources, R.color.primaryTextColor, null)
            }

        }

        private fun successStatusView(t: DelayTestTaskData) {
            mBinding.tvStatus.apply {
                text = getTextByDelay(t.delay)
                visibility = View.VISIBLE
                textColor = getColorByDelay(t.delay, this)
                background = null
            }
            mBinding.taskStatePb.visibility = View.GONE
            mBinding.tvDelayTime.apply {
                text = t.delay.toString()
                textColor =
                    ResourcesCompat.getColor(resources, R.color.primaryTextColor, null)
                visibility = View.VISIBLE
            }
        }

        private fun timeOutStatusView(t: DelayTestTaskData) {
            if (finish) {
                mBinding.tvStatus.apply {
                    text = context.getString(R.string.scenes_test_timeout)
                    visibility = View.VISIBLE
                    textColor = getColorByDelay(t.delay, this)
//                    background = ResourcesCompat.getDrawable(
//                        resources,
//                        R.drawable.scenes_refresh_rounded_rectangle,
//                        null
//                    )
                }
            } else {
                mBinding.tvStatus.visibility = View.GONE
            }

            mBinding.taskStatePb.visibility = View.GONE
            mBinding.tvDelayTime.apply {
                text = context.getString(R.string.scenes_test_request_timeout)
                textColor = ResourcesCompat.getColor(resources, R.color.textRed, null)
                visibility = View.VISIBLE
            }
        }
    }

    private fun getTextByDelay(delay: Int): CharSequence {
        return when  {
            delay < 0 -> "超时"
            delay in 0..30 -> "极好"
            delay in 31..50 -> "较好"
            delay in 51..100 -> "一般"
            delay in 101..200 -> "较差"
            delay in 201..500 -> "非常差"
            else -> "极差"
        }
    }

    private fun getColorByDelay(delay: Int, v: View): Int {
        val resources = v.context.resources
        return when {
//            delay < 0 -> ResourcesCompat.getColor(resources, R.color.white, null)
            delay in 0..30 -> ResourcesCompat.getColor(resources, R.color.green_15b464, null)
            delay in 31..50 -> ResourcesCompat.getColor(resources, R.color.green_60c48b, null)
            delay in 51..100 -> ResourcesCompat.getColor(resources, R.color.green_7acc9c, null)
            delay in 101..200 -> ResourcesCompat.getColor(resources, R.color.red_f59285, null)
            delay in 201..500 -> ResourcesCompat.getColor(resources, R.color.red_f47f6f, null)
            else -> ResourcesCompat.getColor(resources, R.color.textRed, null)
        }
    }


}