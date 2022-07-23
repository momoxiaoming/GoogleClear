package com.org.gradle.vest1.style

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mckj.template.abs.AbsMenuStyle
import com.org.gradle.vest1.R
import com.org.gradle.vest1.databinding.CleanupMenuBusItemStyleBinding

class VestBusMenuStyle : AbsMenuStyle() {

    private lateinit var binding: CleanupMenuBusItemStyleBinding

    override fun getRootView(inflater: LayoutInflater, parent: ViewGroup): View {
        binding =
            CleanupMenuBusItemStyleBinding.inflate(inflater, parent, false)

        return binding.root
    }

    override fun getItemName(): TextView? {
        return binding.menuNameTv
    }

    override fun getItemIcon(): ImageView? {
        return binding.menuIv
    }

    override fun getIconSize(): Float? {
        return null
    }
    override fun getItemNameColor(): Int {
        return R.color.color_28292d
    }

    override fun getItemDescColor(): Int {
        return R.color.color_999999
    }

    override fun getItemNameSize(): Float {
        return 16f
    }

    override fun getItemDescSize(): Float {
        return 11f
    }

    override fun getNameTopMargin(): Float {
        return 11f
    }

    override fun getDescTopMargin(): Float {
        return 1f
    }

    override fun forceHideDesc(): Boolean {
        return true
    }

    override fun splitItemName(name: String): String {
        val spArr=name.split(" ")
        val newArr=spArr.filter {
            it.trim().isNotEmpty()
        }
        if(newArr.size==2){
            return "${newArr[0]}\n${newArr[1]}"
        }else{
            return name
        }
    }

}