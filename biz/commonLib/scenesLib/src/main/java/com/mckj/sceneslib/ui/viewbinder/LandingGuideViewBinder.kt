package com.mckj.sceneslib.ui.viewbinder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemLandingGuideBinding
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.SizeUtil
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.util.ScenesUtils
import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingGuideViewBinder : DataBindingViewBinder<AbstractScenes>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_landing_guide, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<AbstractScenes, ScenesItemLandingGuideBinding>(
            itemView
        ) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as AbstractScenes
                    onItemClick(it, position, item)
                    ScenesUtils.isClickGuide = true
                }
            }
            mBinding.itemBtn.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as AbstractScenes
                    onItemClick(it, position, item)
                    ScenesUtils.isClickGuide = true
                }
            }
        }

        override fun bindData(t: AbstractScenes) {
            mBinding.itemIv.imageResource = t.getGuideIconResId()
            mBinding.itemBtn.text = t.getData().btnText
            mBinding.itemNameTv.text = t.getData().name
            mBinding.itemDescTv.text = t.getData().recommendDesc
        }
    }

}