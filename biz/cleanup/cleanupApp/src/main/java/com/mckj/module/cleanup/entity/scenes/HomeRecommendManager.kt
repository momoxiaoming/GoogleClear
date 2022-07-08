package com.mckj.module.cleanup.entity.scenes

import android.content.Context
import com.mckj.module.cleanup.entity.AbsHomeRecommendScenes
import com.mckj.module.cleanup.entity.RecommendData


/**
 * Describe:
 *
 * Created By yangb on 2021/5/31
 */
class HomeRecommendManager() {

    companion object {
        const val TAG = "ScenesManager"

        private val INSTANCE by lazy { HomeRecommendManager() }

        fun getInstance(): HomeRecommendManager = INSTANCE
    }

    /**
     * 首页推荐类集合
     *
     * key-类型 AbsHomeRecommendScenes
     */
    private val mScenesMap: MutableMap<Int, AbsHomeRecommendScenes> by lazy { mutableMapOf() }

    fun register(type: Int, func: AbsHomeRecommendScenes) {
        mScenesMap[type] = func
    }

    fun register(scenes: AbsHomeRecommendScenes) {
        register(scenes.getData().type, scenes)
    }

    fun unregister(type: Int) {
        mScenesMap.remove(type)
    }

    /**
     * 获取清理对象
     */
    fun getScenes(type: Int): AbsHomeRecommendScenes? {
        return mScenesMap[type]
    }

    /**
     * 获取推荐数据
     */
    fun getData(type: Int): RecommendData? {
        return getScenes(type)?.getData()
    }

    /**
     * 跳转
     */
    fun jumpPage(context: Context, type: Int){
        val scenes: AbsHomeRecommendScenes? = getScenes(type)
        scenes?.jumpPage(context)
    }

    fun getScenesMap(): MutableMap<Int, AbsHomeRecommendScenes> {
        return mScenesMap
    }

    fun init() {
        register(PowerSaveScenes())//超级省电
        register(PhoneSpeedScenes())//手机加速
        register(JunkCleanScenes())//垃圾清理
        register(CoolDownScenes())//手机降温
        register(AntivirusScenes())//手机杀毒
        register(UninstallCleanScenes())//残留清理
        register(ShortVideoCleanScenes())//短视频清理
        register(NetWorkSpeedScene())//网络加速
    }

    init {
        init()
    }


}