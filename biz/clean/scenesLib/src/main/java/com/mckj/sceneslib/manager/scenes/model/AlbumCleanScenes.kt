package com.mckj.sceneslib.manager.scenes.model

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:
 *
 * Created By leix on 2021/07/19
 */
open class AlbumCleanScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_ALBUM_CLEAN,
            "album_clean",
            idToString(R.string.scenes_photo_clean),
            idToString(R.string.scenes_clean_many_ram),
            "",
            idToString(R.string.scenes_clean_now),
            idToString(R.string.scenes_speed_finish),
            idToString(R.string.scenes_yh_ok),
            "home_photoclean_click"
        )
    }

    override fun jumpPage(context: Context): Boolean {
        ARouter.getInstance().build("/gallery/main").navigation()
        handleClickEvent()
        return true
    }
}