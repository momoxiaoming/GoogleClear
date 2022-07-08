package com.mckj.module.cleanup.entity

import android.content.Context
import com.mckj.sceneslib.R
import com.mckj.sceneslib.manager.scenes.ScenesManager

abstract class AbsHomeRecommendScenes {

    /**
     * 场景数据
     */
    private var mData: RecommendData? = null

    /**
     * 初始化数据
     */
    abstract fun initScenesData():RecommendData

    open fun jumpPage(context: Context){
        val type = getData().type
        ScenesManager.getInstance().jumpPage(context,type,"推荐引导")
    }

    /**
     * 获取场景数据
     */
    fun getData(): RecommendData {
        var data = mData
        if (data == null) {
            data = initScenesData()
            mData = data
        }
        return data
    }

    /**
     * 获取引导icon
     */
    open fun getIconResId() = R.drawable.ic_land_default
}