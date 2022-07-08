package com.mckj.sceneslib.ui.scenes.model.tools.newtools.scenes

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.cm.utils.TextUtils
import com.org.openlib.help.Consumer
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.helper.startFragment
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.SceneCategory
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.entity.ScenesLandingStyle
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.junk.JunkFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.dust.DustFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.landhead.DustHeadFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.landhead.NotifyHeadFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.notify.NotifyContentFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.notify.NotifyCoverFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.notify.AppStateProvider
import com.org.openlib.utils.FragmentUtil

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.scenes
 * @data  2022/3/8 16:03
 */
class NotifyScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_NOTIFY,
            "tzqllanding_msg",
            "通知栏清理",
            "清理积累的无用通知",
            "",
            "立即清理",
            "",
            "",
            "",
            mSceneCategory = SceneCategory.CLEANER,
            recommendDesc = TextUtils.string2SpannableString(
                "手机通知栏可能有无用通知",
                "有无用通知",
                30,
                color = Color.parseColor("#FA3C32"),
                true
            ),
            followAudit = false
        )
    }

    override fun getGuideIconResId() = R.drawable.cleanup_icon_tzql

    init {
        AppStateProvider.initSourceData()
    }

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = NotifyCoverFragment.newInstance(consumer,parent.id)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }


    override fun addLandingHeaderView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        FragmentUtil.show(activity.supportFragmentManager, NotifyHeadFragment.getInstance(), parent.id)
    }


    override fun loadAdAfterCondition(): Boolean {
        return true
    }
}