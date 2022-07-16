package com.mckj.template.template

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.template.abs.AbsMenuView
import com.mckj.template.style.MenuPlusStyle
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.template.R
import com.mckj.template.databinding.CleanupMenuViewBinding

/**
 *  author :xx
 *  date : 2022/3/5 11:46
 *  description : 默认的菜单模板
 *  宫格形式排列
 */
class MenuTemplate(
    context: Context,
    val spanSize: Int = 2
) :
    AbsMenuView(context) {


    private lateinit var mBinding: CleanupMenuViewBinding

    override fun getRoot(): View {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.cleanup_menu_view,
            null,
            false
        )
        return mBinding.root
    }

    override fun getMenuList(): RecyclerView? {
        val menuList = mBinding.menuList
        menuList.apply {
            mDecoration?.let {
                this.addItemDecoration(it)
            }
            layoutManager = GridLayoutManager(context, spanSize)
        }
        return menuList
    }

    override fun getContainerView(): View {
        return mBinding.rootView
    }

    override fun getCategory(): TextView? {
        return mBinding.category
    }

    override fun getMenuAdapter(): MultiTypeAdapter? {
        val adapter = MultiTypeAdapter()
        if (mStyle == null) {
            mStyle = MenuPlusStyle()
        }
        adapter.register(
            MenuItem::class,
            mStyle!!.also {
                it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MenuItem> {
                    override fun onItemClick(view: View, position: Int, t: MenuItem) {
                        ScenesManager.getInstance().getScenes(t.type)?.jumpPage(context)
                        if (t.isRecommend) {
                            t.isRecommend = false
                            onItemClick(t.type)
                            adapter.notifyItemChanged(position)
                        }
                    }
                }
            })
        return adapter
    }
}