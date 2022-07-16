package com.mckj.sceneslib.manager.scenes.model

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.permission.DPermissionUtils

/**
 * Describe:
 *
 * Created By xx on 2021/07/19
 */
open class AlbumCleanScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_ALBUM_CLEAN,
            "album_clean",
            ResourceUtil.getString(R.string.scenes_photo_cleaner),
            if (ScenesManager.getInstance().isRegisterCleanerBody()
                || ScenesManager.getInstance().isRegisterPowerBody()
            ){
                ResourceUtil.getString(R.string.scenes_organize_albums)
            } else {
                ResourceUtil.getString(R.string.scenes_organize_albums_1)
            },
            "",
            ResourceUtil.getString(R.string.scenes_album_clean),
            ResourceUtil.getString(R.string.scenes_album_clean_finish),
            "",
            "home_photoclean_click",
            recommendDesc = ResourceUtil.getString(R.string.scenes_album_clean_recommend_desc),
            followAudit = false
        )
    }

    override fun getGuideIconResId(): Int {
        return R.drawable.ic_land_album
    }

    override fun jumpPage(context: Context, invoke: ((accept: Boolean) -> Unit)?): Boolean {
        if (context is FragmentActivity) {
            DPermissionUtils.checkStoragePermission(context,) { accept ->
                if (accept) {
                    ARouter.getInstance().build("/gallery/main").navigation()
                    handleClickEvent()
                }
                invoke?.invoke(accept)
            }
        }
        return true
    }
}