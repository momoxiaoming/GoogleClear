package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemTaskBinding
import com.mckj.sceneslib.entity.ScenesTask
import com.dn.baselib.base.databinding.DataBindingViewBinder

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class ScenesTaskViewBinder : DataBindingViewBinder<ScenesTask>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(
            inflater.inflate(
                R.layout.scenes_item_task,
                parent,
                false
            )
        )
    }

    inner class ViewHolder(itemView: View) :
        DataBindingViewHolder<ScenesTask, ScenesItemTaskBinding>(itemView) {

        override fun bindData(t: ScenesTask) {
            mBinding.taskNameTv.text = t.name
            when (t.state) {
                ScenesTask.STATE_LOADING -> {
                    mBinding.taskStateIv.gone()
                    mBinding.taskStatePb.show()
                }
                ScenesTask.STATE_COMPLETE -> {
                    mBinding.taskStateIv.show()
                    mBinding.taskStatePb.gone()
                }
                else -> {
                    mBinding.taskStateIv.gone()
                    mBinding.taskStatePb.gone()
                }
            }
        }
    }

}