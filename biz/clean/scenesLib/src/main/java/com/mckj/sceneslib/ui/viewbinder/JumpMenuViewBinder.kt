package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dn.baselib.base.databinding.DataBindingViewBinder
import com.dn.baselib.ext.onceClick
import com.dn.baselib.util.ListUtil
import com.dn.baselib.util.SizeUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemMenuJumpBinding
import com.mckj.sceneslib.entity.MenuJumpItem
import com.mckj.sceneslib.manager.scenes.ScenesManager

import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class JumpMenuViewBinder : DataBindingViewBinder<MenuJumpItem>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_menu_jump, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MenuJumpItem, ScenesItemMenuJumpBinding>(
            itemView
        ) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as MenuJumpItem
                    onItemClick(it, position, item)
                }
            }
        }

        override fun bindData(t: MenuJumpItem) {
            mBinding.menu = t
            val scenes = ScenesManager.getInstance().getScenes(t.type)
            if (scenes == null) {
                mBinding.itemBusinessNameTv.text = "未知菜单"
                return
            }
            mBinding.itemJumpIv.imageResource = t.resId
            scenes.let {
                mBinding.itemBusinessNameTv.text = it.getData().name
                mBinding.itemBusinessDescTv.text = it.getData().desc
            }
            val layoutParams =
                mBinding.itemJumpLayout.layoutParams as RecyclerView.LayoutParams
            layoutParams.topMargin = if (ListUtil.isFirst(adapterItems, t)) {
                SizeUtil.dp2px(8f)
            } else {
                0
            }
        }
    }

}