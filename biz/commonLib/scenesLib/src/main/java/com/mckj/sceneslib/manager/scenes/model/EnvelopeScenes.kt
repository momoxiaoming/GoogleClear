package com.mckj.sceneslib.manager.scenes.model

import android.graphics.Color
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.org.openlib.help.Consumer
import com.mckj.baselib.util.TextUtils
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.landing.header.LandingHeaderEnvelopeFragment
import com.mckj.sceneslib.ui.scenes.model.ap.WifiApLandingHeaderFragment
import com.mckj.sceneslib.ui.scenes.model.envelopetest.EnvelopeTestFragment
import com.org.openlib.utils.FragmentUtil

class EnvelopeScenes : AbstractScenes() {
    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_ENVELOPE_TEST,
            "envelope_test",
            "抢红包测速",
            "手速老是比别人慢？",
            "抢红包测速",
            "一键测速",
            "测速完成",
            "网络加速${(10..20).random()}%以上",
            "home_envelope_click",
            recommendDesc = TextUtils.string2SpannableString(
                "手速老是比别人慢？",
                "慢",
                30,
                color = Color.parseColor("#FA3C32"),
                true
            ),
            followAudit = true
        )
    }

    override fun getGuideIconResId(): Int {
        return R.drawable.ic_redpacket_function
    }

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = EnvelopeTestFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun addExecuteScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        //跳过执行
        consumer.accept(true)
    }

    override fun addEndScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        //跳过结束
        consumer.accept(true)
    }

    override fun addLandingHeaderView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        FragmentUtil.show(activity.supportFragmentManager,LandingHeaderEnvelopeFragment(), parent.id)
    }

    //结果页需要的的list，从动画页的任务结果传递过来的
    var taskList: List<DelayTestTaskData>? = null

    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_ENVELOPE_TEST
        )
        list?.addAll(arrayListOf)
        return list
    }

}