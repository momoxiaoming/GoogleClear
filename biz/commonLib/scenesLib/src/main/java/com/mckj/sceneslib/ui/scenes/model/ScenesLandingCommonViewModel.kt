package com.mckj.sceneslib.ui.scenes.model

import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType

class ScenesLandingCommonViewModel : AbstractViewModel() {
    /**
     * 当前场景对象
     */
    private lateinit var mScenes: AbstractScenes

    private var mScenesList = mutableListOf<AbstractScenes>()

    init {
        val iterator = ScenesManager.getInstance().getScenesMap().iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
//            if (ScenesType.TYPE_ALBUM_CLEAN == next.key){
//                continue
//            }
            mScenesList.add(next.value)
        }

    }

    fun getGuideScenesList(): List<AbstractScenes>? {
        return mScenesList.shuffled().take(4)
    }

}