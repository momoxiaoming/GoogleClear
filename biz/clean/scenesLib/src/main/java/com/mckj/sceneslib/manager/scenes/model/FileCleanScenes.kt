package com.mckj.sceneslib.manager.scenes.model

import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:
 *
 * Created By yangb on 2021/3/24
 */
class FileCleanScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_FILE_CLEAN,
            "file_clean",
            idToString(R.string.scenes_file_clean),
            idToString(R.string.scenes_clean_file_more),
            "",
            idToString(R.string.scenes_clean_now),
            idToString(R.string.scenes_clean_finish),
            idToString(R.string.scenes_yh_ok),
            "home_file_click"
        )
    }

    override fun isRequestPermission(): Boolean {
        return true
    }
}