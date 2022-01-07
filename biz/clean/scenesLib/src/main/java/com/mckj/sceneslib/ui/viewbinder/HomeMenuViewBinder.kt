package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dn.baselib.base.databinding.DataBindingViewBinder
import com.dn.baselib.ext.onceClick
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemMenuHomeBinding
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.scenes.ScenesManager

import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2020/10/15
 */
class HomeMenuViewBinder : DataBindingViewBinder<MenuItem>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_menu_home, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MenuItem, ScenesItemMenuHomeBinding>(itemView) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as MenuItem
                    onItemClick(it, position, item)
                }
            }
        }

        override fun bindData(t: MenuItem) {
            var tip = ""
            val scenes = ScenesManager.getInstance().getScenes(t.type)
            if (scenes == null) {
                mBinding.menuNameTv.text = "未知菜单"
                return
            }
            mBinding.menuIv.imageResource = t.resId
            scenes.let {
                mBinding.menuNameTv.text = it.getData().name
                tip = it.getData().tip
            }
            if (t.isRecommend && tip.isNotEmpty()) {
                mBinding.menuTipTv.show()
                mBinding.menuTipTv.text = tip
            } else {
                mBinding.menuTipTv.gone()
            }
        }
    }
}