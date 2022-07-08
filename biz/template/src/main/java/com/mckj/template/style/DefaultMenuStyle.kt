package com.mckj.template.style

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mckj.template.abs.AbsMenuStyle
import com.mckj.template.databinding.CleanupMenuItemBinding

/**
 *  author : leix
 *  date : 2022/3/5 10:09
 *  description :默认的首页菜单模板，默认tip提示
 */
class DefaultMenuStyle : AbsMenuStyle() {
    private lateinit var binding: CleanupMenuItemBinding

    override fun getRootView(inflater: LayoutInflater, parent: ViewGroup): View {
        binding =
            CleanupMenuItemBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun getItemName(): TextView? {
        return binding.itemNameTv
    }

    override fun getItemDesc(): TextView? {
        return null
    }

    override fun getItemIcon(): ImageView? {
        return binding.itemIv
    }

    override fun getRecommendView(): View? {
        return binding.itemTip
    }
}