package com.mckj.sceneslib.ui.scenes.model.file

import androidx.lifecycle.MutableLiveData
import com.dn.baselib.base.databinding.AbstractViewModel
import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import com.mckj.sceneslib.data.ToolRepository
import com.mckj.sceneslib.entity.FileMenuItem


/**
 * FileViewModel
 *
 * @author mmxm
 * @date 2021/3/4 12:08
 */
class FileViewModel : AbstractViewModel() {

    companion object {
        const val TAG = "FileViewModel"
    }

    private val repository: ToolRepository by lazy { ToolRepository() }


    /**
     * 文件管理菜单
     */
    private val _fileMenu = MutableLiveData<MutableList<FileMenuItem>>()
    var fileMenu = _fileMenu


    init {
        if (_fileMenu.value == null) {
            getFileMenu()
        }

    }

    /**
     * 文件管理菜单
     */
    fun getFileMenu() {
        val list = mutableListOf<FileMenuItem>()
        list.add(
            FileMenuItem(
                R.drawable.scenes_icon_xiangc_list,
                idToString(R.string.scenes_photo_manager),
                idToString(R.string.scenes_photo_manager_desc),
                FileMenuItem.PHOTO
            )
        )
        list.add(
            FileMenuItem(
                R.drawable.scenes_icon_ship_list,
                idToString(R.string.scenes_video_manager),
                idToString(R.string.scenes_clean_video_kj),
                FileMenuItem.VIDEO
            )
        )
        //审核关闭时，才引用
        list.add(
            FileMenuItem(
                R.drawable.scenes_icon_azb_list,
                idToString(R.string.scenes_zip_app_manager),
                idToString(R.string.scenes_clean_app_zip),
                FileMenuItem.APK
            )
        )
//        list.add(
//            FileMenuItem(
//                R.drawable.scenes_icon_music_list,
//                "音乐管理",
//                "清理不喜欢的音乐",
//                FileMenuItem.AUDIO
//            )
//        )
        list.add(
            FileMenuItem(
                R.drawable.scenes_icon_ysb_list,
                idToString(R.string.scenes_zip_manager),
                idToString(R.string.scenes_zip_need_clean2),
                FileMenuItem.ZIP
            )
        )
        list.add(
            FileMenuItem(
                R.drawable.scenes_icon_dawj_list,
                idToString(R.string.scenes_big_file_manager),
                idToString(R.string.scenes_clean_big_file2),
                FileMenuItem.BIG_FILE
            )
        )
        _fileMenu.value = list
    }


}

