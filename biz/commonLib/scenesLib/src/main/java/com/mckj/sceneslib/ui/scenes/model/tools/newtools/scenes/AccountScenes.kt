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

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.scenes
 * @data  2022/3/8 16:03
 */
class AccountScenes : AbstractScenes() {


    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_ACCOUNT,
            "",
            "记账",
            "统计每日支出",
            "",
            "立即统计",
            "",
            "",
            "",
            mSceneCategory = SceneCategory.CLEANER,
            recommendDesc = TextUtils.string2SpannableString(
                "统计每日支出",
                "",
                30,
                color = Color.parseColor("#FA3C32"),
                true
            )
        )
    }



    override fun getGuideIconResId() = R.drawable.ic_toolkit_bill

    override fun jumpPage(context: Context): Boolean {
        context.startFragment(ARouterPath.NewTools.NEW_TOOLS_ACCOUNT)
        return true
    }




}