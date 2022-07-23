package com.org.gradle.vest1.style

import com.mckj.template.style.MenuStyle01
import com.org.gradle.vest1.R

class VestRecommendMenuStyle : MenuStyle01() {

    override fun getIconSize(): Float? {
        return 30f
    }

    override fun getItemNameColor(): Int {
        return R.color.color_000000
    }

    override fun getItemNameSize(): Float {
        return 11f
    }

    override fun getNameTopMargin(): Float {
        return 2f
    }

    override fun forceHideDesc(): Boolean {
        return true
    }

}