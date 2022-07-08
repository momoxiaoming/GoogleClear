package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemLandingGuideCardBinding
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.org.openlib.utils.onceClick

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingGuideCardViewBinder : DataBindingViewBinder<AbstractScenes>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(
            inflater.inflate(
                R.layout.scenes_item_landing_guide_card,
                parent,
                false
            )
        )
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<AbstractScenes, ScenesItemLandingGuideCardBinding>(
            itemView
        ) {

        val warningAdapter: MultiTypeAdapter by lazy {
            val adapter = MultiTypeAdapter()
            adapter.register(String::class, LandingWarningViewBinder())
            adapter
        }

        init {
            mBinding.itemWarningRecycler.layoutManager = LinearLayoutManager(itemView.context)
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as AbstractScenes
                    onItemClick(it, position, item)
                }
            }
            mBinding.itemBtn.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as AbstractScenes
                    onItemClick(it, position, item)
                }
            }

        }

        override fun bindData(t: AbstractScenes) {
            mBinding.itemNameTv.text = t.getData().name
            mBinding.itemBtn.text = t.getData().btnText
            setWarningAdapter(t.getWarningDescList())
        }

        private fun setWarningAdapter(list: List<String>?) {
            if (list.isNullOrEmpty()) {
                mBinding.itemWarningRecycler.gone()
                return
            }
            mBinding.itemWarningRecycler.show()
            if (mBinding.itemWarningRecycler.adapter == null) {
                mBinding.itemWarningRecycler.adapter = warningAdapter
            }
            warningAdapter.items = list
            warningAdapter.notifyDataSetChanged()
        }
    }

}