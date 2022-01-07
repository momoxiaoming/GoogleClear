package com.mckj.sceneslib.manager.scenes

import android.content.Context
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.model.*

/**
 * Describe:
 *
 * Created By yangb on 2021/5/31
 */
class ScenesManager {

    companion object {
        const val TAG = "ScenesManager"

        private val INSTANCE by lazy { ScenesManager() }

        fun getInstance(): ScenesManager = INSTANCE
    }

    /**
     * 清理类集合
     *
     * key-类型 AbstractScenes
     */
    private val mScenesMap: MutableMap<Int, AbstractScenes> by lazy { mutableMapOf() }

    fun register(type: Int, func: AbstractScenes) {
        mScenesMap[type] = func
    }

    fun register(scenes: AbstractScenes) {
        register(scenes.getData().type, scenes)
    }

    fun unregister(type: Int) {
        mScenesMap.remove(type)
    }

    /**
     * 获取清理对象
     */
    fun getScenes(type: Int): AbstractScenes? {
        return mScenesMap[type]
    }

    /**
     * 获取清理数据
     */
    fun getData(type: Int): ScenesData? {
        return getScenes(type)?.getData()
    }

    /**
     * 跳转
     */
    fun jumpPage(context: Context, type: Int): Boolean {
        val scenes: AbstractScenes? = getScenes(type)
        return scenes?.jumpPage(context) ?: false
    }

    init {
        register(AntivirusScenes())
        register(ApkCleanScenes())
        register(ApkManagerScenes())
        register(AudioCleanScenes())
        register(AudioManagerScenes())

        register(BigFileManagerScenes())
        register(CameraCheckScenes())
        register(CatonSpeedScenes())
        register(CoolDownScenes())
        register(FileCleanScenes())

        register(FileManagerScenes())
        register(GameSpeedScenes())
        register(HotSharingScenes())
        register(JunkCleanScenes())
        register(LotteryScenes())

        register(MemorySpeedScenes())
        register(NetworkCheckScenes())
        register(NetworkSpeedScenes())
        register(NetworkTestScenes())
        register(OneKeySpeedScenes())

        register(PhoneSpeedScenes())
        register(PhotoManagerScenes())
        register(PowerSaveScenes())
        register(PowerSpeedScenes())
//        register(QQCleanScenes())

//        register(QQSpeedScenes())
//        register(RedEnvelopeScenes())
        register(SecurityCheckScenes())
        register(ShortVideoCleanScenes())
        register(ShortVideoSpeedScenes())

        register(SignalBoostScenes())
        register(SingleSpeedScenes())
//        register(UninstallCleanScenes())
        register(VideoManagerScenes())
        register(VideoSpeedScenes())

//        register(WechatCleanScenes())
//        register(WechatSpeedScenes())
        register(ZipCleanScenes())
        register(ZipManagerScenes())
//        register(ToolsScenes())
        register(AlbumCleanScenes())
        register(XJunkCleanScenes())
    }

}