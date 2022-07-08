package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemLandingWarningBinding
import com.mckj.baselib.base.databinding.DataBindingViewBinder

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingWarningViewBinder : DataBindingViewBinder<String>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_landing_warning, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<String, ScenesItemLandingWarningBinding>(
            itemView
        ) {

        override fun bindData(t: String) {
            mBinding.itemTv.text = t
        }
    }

}