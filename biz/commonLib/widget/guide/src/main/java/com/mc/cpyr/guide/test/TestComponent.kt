package com.mc.cpyr.guide.test

import android.view.LayoutInflater
import android.view.View
import com.mc.cpyr.guide.R
import com.mc.cpyr.guide.config.Component

/**
 * TestComponent
 *
 * @author mmxm
 * @date 2021/3/8 20:01
 */
class TestComponent() : Component {

    override fun getView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.view_test_component, null, false)
    }

    override fun getAnchor(): Int {
        return Component.ANCHOR_BOTTOM
    }

    override fun getFitPosition(): Int {
        return Component.FIT_CENTER
    }

    override fun getXOffset(): Int {
        return 0
    }

    override fun getYOffset(): Int {
        return 0
    }
}