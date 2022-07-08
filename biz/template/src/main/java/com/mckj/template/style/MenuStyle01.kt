package com.mckj.template.style

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import com.mckj.baselib.util.SizeUtil
import com.mckj.template.R
import com.mckj.template.abs.AbsMenuStyle
import com.mckj.template.databinding.CleanupMenuItemStyle01Binding


/**
 */
open class MenuStyle01 : AbsMenuStyle() {
    private lateinit var binding: CleanupMenuItemStyle01Binding

    override fun getRootView(inflater: LayoutInflater, parent: ViewGroup): View {
        binding =
            CleanupMenuItemStyle01Binding.inflate(inflater, parent, false)
        getItemBg()?.let {
            binding.root.setBackgroundResource(it)
        }
        return binding.root
    }

    override fun getItemName(): TextView? {
        getNameTopMargin().let {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.consRoot)
            constraintSet.setMargin(R.id.menu_name_tv, ConstraintSet.TOP, SizeUtil.dp2px(it))
            constraintSet.applyTo(binding.consRoot)
        }
        return binding.menuNameTv
    }

    override fun getItemDesc(): TextView? {
        getDescTopMargin().let {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.consRoot)
            constraintSet.setMargin(R.id.menu_desc_tv, ConstraintSet.TOP, SizeUtil.dp2px(it))
            constraintSet.applyTo(binding.consRoot)
        }
        return binding.menuDescTv
    }

    override fun getItemIcon(): ImageView? {
        getIconSize()?.let {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.consRoot)
            constraintSet.constrainWidth(R.id.menu_iv, SizeUtil.dp2px(it))
            constraintSet.constrainHeight(R.id.menu_iv, SizeUtil.dp2px(it))
            constraintSet.applyTo(binding.consRoot)
        }
        return binding.menuIv
    }

    override fun getRecommendView(): View? {
        return null
    }

    /**
     * item背景色
     */
    override fun getItemBg(): Int? {
        return null
    }

    /**
     * 图片大小:直接传dp
     */
    override fun getIconSize(): Float? {
        return null
    }

    /**
     * item name 配置
     */
    override fun getItemNameColor(): Int {
        return R.color.color_333841
    }

    override fun getItemNameSize(): Float {
        return 14f
    }

    /**
     * item Desc 配置
     */
    override fun getItemDescColor(): Int {
        return R.color.color_999999
    }

    override fun getItemDescSize(): Float {
        return 10f
    }

    /**
     * margin
     */
    override fun getNameTopMargin(): Float {
        return 4f
    }

    override fun getDescTopMargin(): Float {
        return 0f
    }

    protected open fun showDesc(): Boolean {
        return true
    }

}