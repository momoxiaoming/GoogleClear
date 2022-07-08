package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemMenuTitleBinding
import com.mckj.sceneslib.entity.MenuTitle

/**
 * Describe:
 *
 * Created By yangb on 2021/5/8
 */
class MenuTitleViewBinder(
    private val lifecycleOwner: LifecycleOwner,

) :
    DataBindingViewBinder<MenuTitle>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_menu_title, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MenuTitle, ScenesItemMenuTitleBinding>(
            itemView
        ) {

//        private val iAdContainer = mBinding.itemAdLayout.toAdContainer()

        override fun bindData(t: MenuTitle) {
            mBinding.itemTv.text = t.title
//            var adSession = t.adSession
//            if (adSession == null) {
//                adSession = AdManager.getInstance()
//                    .showAd(t.adName, iAdContainer, lifecycleOwner, t.render) {
//                        consumer?.accept(it)
//                    }
//                t.adSession = adSession
//            } else {
//                adSession.show(iAdContainer)
//            }
        }

    }

}