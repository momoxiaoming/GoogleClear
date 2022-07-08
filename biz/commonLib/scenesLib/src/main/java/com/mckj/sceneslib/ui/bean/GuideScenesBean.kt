package com.mckj.sceneslib.ui.bean

import com.mckj.sceneslib.manager.scenes.AbstractScenes

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.bean
 * @data  2022/4/19 09:51
 */
data class GuideScenesBean(val type:Int,val scenes: AbstractScenes){
    companion object{
        const val SPECIAL_TYPE = 0
        const val NORMAL_TYPE = 1
    }
}
