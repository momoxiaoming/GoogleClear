package com.mckj.sceneslib.manager.scenes.model

import android.content.Context
import com.dn.baselib.ext.idToString
import com.dn.datalib.ARouterPath
import com.dn.openlib.ui.startTitleFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:
 *
 * Created By yangb on 2021/3/24
 */
class FileManagerScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_FILE_MANAGER,
            "file_manager",
            idToString(R.string.scenes_file_manager),
            idToString(R.string.scenes_clean_file_more),
            "",
            idToString(R.string.scenes_clean_now),
            idToString(R.string.scenes_clean_finish),
            idToString(R.string.scenes_yh_ok),
            "home_file_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_file_manager

    override fun jumpPage(context: Context): Boolean {
        context.startTitleFragment(ARouterPath.Scenes.FRAGMENT_FILE_MENU)
        handleClickEvent()
        return true
    }

}