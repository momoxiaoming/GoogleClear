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
import com.mckj.sceneslib.databinding.ScenesItemMenuBusinessBinding
import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.manager.scenes.ScenesManager

import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class BusinessMenuViewBinder : DataBindingViewBinder<MenuBusinessItem>() {

    companion object {
        const val TAG = "BusinessMenuViewBinder"
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_menu_business, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MenuBusinessItem, ScenesItemMenuBusinessBinding>(
            itemView
        ), View.OnClickListener {

        init {
            mBinding.itemBusinessLayout.onceClick(this)
            mBinding.itemBusinessBtn.onceClick(this)
        }

        override fun bindData(t: MenuBusinessItem) {
            mBinding.menu = t
            val scenes = ScenesManager.getInstance().getScenes(t.type)
            if (scenes == null) {
                mBinding.itemBusinessNameTv.text = "未知菜单"
                return
            }
            mBinding.itemBusinessIv.imageResource = t.resId
            scenes.let {
                mBinding.itemBusinessNameTv.text = it.getData().name
                mBinding.itemBusinessDescTv.text = it.getData().desc
                mBinding.itemBusinessBtn.text = it.getData().btnText
            }
            val layoutParams =
                mBinding.itemBusinessLayout.layoutParams as RecyclerView.LayoutParams
            layoutParams.topMargin = if (ListUtil.isFirst(adapterItems, t)) {
                SizeUtil.dp2px(8f)
            } else {
                0
            }
        }

        override fun onClick(v: View) {
            itemClickListener?.apply {
                val position = layoutPosition
                val item = adapter.items[position] as MenuBusinessItem
                onItemClick(v, position, item)
            }
        }
    }

}