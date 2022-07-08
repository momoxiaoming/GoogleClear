package com.mckj.sceneslib.ui.scenes.model.tools.newtools.scenes

import android.content.Context
import android.graphics.Color
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.helper.startFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.SceneCategory
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.CheckNetWorkHelper

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.scenes
 * @data  2022/3/8 16:03
 */
class NetworkScenes : AbstractScenes() {


    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_CHECK_NET_WORK,
            "",
            "网络检测",
            "实时监控APP网络使用情况",
            "",
            "立即检测",
            "",
            "",
            "",
            mSceneCategory = SceneCategory.CLEANER,
            recommendDesc = TextUtils.string2SpannableString(
                "实时监控APP网络使用情况",
                "",
                30,
                color = Color.parseColor("#FA3C32"),
                true
            )
        )
    }

    init {
        CheckNetWorkHelper.initAllAppData()
    }


    override fun getGuideIconResId() = R.drawable.ic_toolkit_network_detection

    override fun jumpPage(context: Context): Boolean {
        context.startFragment(ARouterPath.NewTools.NEW_TOOLS_CHECK_NET_WORK)
        return true
    }




}