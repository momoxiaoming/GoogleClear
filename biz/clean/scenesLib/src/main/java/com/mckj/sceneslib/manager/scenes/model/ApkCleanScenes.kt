package com.mckj.sceneslib.manager.scenes.model

import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:
 *
 * Created By yangb on 2021/6/2
 */
class ApkCleanScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_APK_CLEAN,
            "apk_clean",
            idToString(R.string.scenes_app_zip_clean),
            idToString(R.string.scenes_delete_zip_app),
            "",
            idToString(R.string.scenes_clean_now),
            idToString(R.string.scenes_clean_finish),
            idToString(R.string.scenes_yh_ok),
            "home_installe_click"
        )
    }

    override fun isRequestPermission(): Boolean {
        return true
    }
}