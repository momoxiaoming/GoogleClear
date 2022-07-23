package com.org.gradle.vest1.style

import com.mckj.template.style.MenuStyle01
import com.org.gradle.vest1.R

class VestMainMenuStyle : MenuStyle01() {

    override fun getIconSize(): Float? {
        return 32f
    }

    override fun getItemNameColor(): Int {
        return R.color.color_28292d
    }

    override fun getItemDescColor(): Int {
        return R.color.color_999999
    }


    override fun getItemNameSize(): Float {
        return 14f
    }

    override fun getItemDescSize(): Float {
        return 11f
    }

    override fun getNameTopMargin(): Float {
        return 9f
    }

    override fun getDescTopMargin(): Float {
        return 1f
    }

    override fun forceHideDesc(): Boolean {
        return true
    }

}