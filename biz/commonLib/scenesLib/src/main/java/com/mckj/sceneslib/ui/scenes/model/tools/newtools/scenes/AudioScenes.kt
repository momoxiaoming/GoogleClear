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
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.audio.AudioFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.dust.DustFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.landhead.DustHeadFragment

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.scenes
 * @data  2022/3/8 16:03
 */
class AudioScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_AUDIO,
            "",
            "智能音量",
            "智能调节音量",
            "",
            "立即调整",
            "",
            "",
            "",
            mSceneCategory = SceneCategory.CLEANER,
            recommendDesc = TextUtils.string2SpannableString(
                "手机音量可能不是最佳状态",
                "最佳状态",
                30,
                color = Color.parseColor("#FA3C32"),
                true
            )
        )
    }


    override fun getGuideIconResId() = R.drawable.cleanup_icon_znyl

    override fun jumpPage(context: Context): Boolean {
        context.startFragment(ARouterPath.NewTools.NEW_TOOLS_AUDIO)
        return true
    }




}