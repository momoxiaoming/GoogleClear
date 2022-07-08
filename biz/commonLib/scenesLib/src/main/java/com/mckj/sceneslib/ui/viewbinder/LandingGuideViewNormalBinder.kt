package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemLandingGuideNormalBinding
import com.mckj.sceneslib.ui.bean.GuideScenesBean
import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingGuideViewNormalBinder : DataBindingViewBinder<GuideScenesBean>() {

    private var isSafetyState = true

    fun setSafetyState(state: Boolean) {
        isSafetyState = state
    }

    fun getSafetyState() = isSafetyState

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_landing_guide_normal, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<GuideScenesBean, ScenesItemLandingGuideNormalBinding>(
            itemView
        ) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as GuideScenesBean
                    onItemClick(it, position,item)
                }
            }
            mBinding.scenesLandingBottomBt.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as GuideScenesBean
                    onItemClick(it, position, item)
                }
            }

        }

        override fun bindData(t: GuideScenesBean) {
            mBinding.apply {
                    t.scenes.apply {
                        scenesLandingBottomIcon.imageResource = getGuideIconResId()
                        scenesLandingBottomBt.text = getData().btnText
                        scenesLandingBottomTitle.text = getData().name
                        scenesLandingBottomHint.text = getData().recommendDesc
                    }
                }
            }
        }
}