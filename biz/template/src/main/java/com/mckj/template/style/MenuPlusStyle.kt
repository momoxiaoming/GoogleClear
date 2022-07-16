package com.mckj.template.style

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.mckj.baselib.util.SizeUtil
import com.mckj.template.R
import com.mckj.template.abs.AbsMenuStyle
import com.mckj.template.databinding.CleanupMenuItemPlusBinding

/**
 *  author : xx
 *  date : 2022/3/5 10:20
 *  description :菜单长模板
 */
open class MenuPlusStyle : AbsMenuStyle() {
    private lateinit var binding: CleanupMenuItemPlusBinding

    override fun getRootView(inflater: LayoutInflater, parent: ViewGroup): View {
        binding =
            CleanupMenuItemPlusBinding.inflate(inflater, parent, false)
        getItemBg()?.let {
            binding.itemContainer.setBackgroundResource(it)
        }
        return binding.root
    }


    override fun getItemName(): TextView? {
        return binding.itemNameTv
    }

    override fun getItemDesc(): TextView? {
        return binding.itemDescTv
    }

    override fun getItemIcon(): ImageView? {
        getIconSize()?.let {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.itemContainer)
            constraintSet.constrainWidth(R.id.item_iv,SizeUtil.dp2px(it))
            constraintSet.constrainHeight(R.id.item_iv,SizeUtil.dp2px(it))
            constraintSet.applyTo(binding.itemContainer)
        }
        return binding.itemIv
    }

    override fun getItemTip(): TextView? {
        return null
    }

    override fun getIconSize(): Float? {
        return null
    }

    override fun getItemBg(): Int? {
        return null
    }

}