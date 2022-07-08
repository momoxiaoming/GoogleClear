package com.mckj.module.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupRecommendItemBinding
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.scenes.ScenesManager
import org.jetbrains.anko.backgroundResource

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/2 11:38
 * @desc
 */
class RecommendMenuViewBinder : DataBindingViewBinder<MenuItem>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_recommend_item, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MenuItem, CleanupRecommendItemBinding>(itemView) {
        override fun bindData(t: MenuItem) {
            mBinding.menuForward.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as MenuItem
                    onItemClick(it, position, item)
                }
            }
            val scenes = ScenesManager.getInstance().getScenes(t.type)
            if (scenes == null) {
                mBinding.menuName.text = "未知菜单"
                return
            }
            mBinding.menuIv.backgroundResource = t.resId
            scenes.let {
                mBinding.menuName.text = it.getData().name
                if (it.getData().tip.isEmpty()) {
                    mBinding.menuDesc.gone()
                } else {
                    mBinding.menuDesc.show()
                    mBinding.menuDesc.text = it.getData().tip
                }
            }
        }
    }
}