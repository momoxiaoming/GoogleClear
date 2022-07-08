package com.mckj.sceneslib.ui.viewbinder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.TextUtils
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemHomeFooterBinding

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class BusinessFooterViewBinder : DataBindingViewBinder<Integer>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_home_footer, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<Integer, ScenesItemHomeFooterBinding>(
            itemView
        ) {

        override fun bindData(t: Integer) {
            val key: String = t.toString()
            val content = String.format(ResourceUtil.getString(R.string.scenes_x_day_partner_with_you),ResourceUtil.getText(R.string.app_name),key)
            mBinding.itemBusinessFooterTv.text = TextUtils.string2SpannableStringForColor(
                content,
                key,
                color = Color.parseColor("#333333")
            )
        }
    }
}