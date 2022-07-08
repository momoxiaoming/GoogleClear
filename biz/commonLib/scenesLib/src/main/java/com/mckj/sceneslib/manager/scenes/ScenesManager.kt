package com.mckj.sceneslib.manager.scenes

import android.content.Context
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.AppBroadcastReceiver
import com.mckj.sceneslib.manager.SceneTaskProducer
import com.mckj.sceneslib.manager.scenes.model.*
import com.mckj.sceneslib.util.Log
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.scenes.*

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

    var mVestMainBody = Vest.CLEAN

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
     * @param form 转跳来源
     */
    fun jumpPage(context: Context, type: Int, form: String): Boolean {
        val scenes: AbstractScenes? = getScenes(type)
        scenes?.enterFlag = form
        return scenes?.jumpPage(context) ?: false
    }

    /**
     * 跳转
     */
    fun jumpPage(context: Context, type: Int): Boolean {
        val scenes: AbstractScenes? = getScenes(type)
        return scenes?.jumpPage(context) ?: false
    }

    fun jumpNow(context: Context, type: Int) {
        val scenes: AbstractScenes? = getScenes(type)
        scenes?.jump(context)
    }

    fun jumpPage(
        context: Context,
        type: Int,
        invoke: ((accept: Boolean) -> Unit)? = null
    ): Boolean {
        val scenes: AbstractScenes? = getScenes(type)
        return scenes?.jumpPage(context) ?: false
    }

    fun getScenesMap(): MutableMap<Int, AbstractScenes> {
        return mScenesMap
    }

    fun isRegisterCleanerBody(): Boolean {
        return mVestMainBody == Vest.CLEAN
    }

    fun isRegisterWifiBody(): Boolean {
        return mVestMainBody == Vest.WIFI
    }

    fun isRegisterPowerBody(): Boolean {
        return mVestMainBody == Vest.POWER
    }

    /**
     * 预初始化
     * @param isWifi  ture  wifi马甲初始化  false  清理马甲初始化
     */
    fun preInit(isWifi: Boolean) {
        if (isWifi) {
            mVestMainBody = Vest.WIFI
            register(NetworkTestScenes())
        } else {
            mVestMainBody = Vest.CLEAN
            register(JunkCleanScenes())
        }
    }

    fun initCleaner() {
        register(PhoneSpeedScenes())//手机加速
        register(CoolDownScenes())//手机降温
        register(PowerSaveScenes())//超级省电
//        register(NetworkSpeedScenes())//网络加速
//        register(AntivirusScenes())//手机杀毒
//        register(CameraCheckScenes())//短视频清理
        register(UninstallCleanScenes())//残留清理
//        register(SingleSpeedScenes())//信号优化
        register(JunkCleanScenes())//垃圾清理
//        register(NetworkTestScenes())//网络测速
        register(CatonSpeedScenes())//卡顿优化

        register(AlbumCleanScenes())//相册清理
//        register(EnvelopeScenes())//红包测速
        register(AppManagerScenes())//应用管理
        register(OneKeySpeedScenes())//一键加速
//        register(AppLockScenes()) //应用锁


//        register(NetworkCheckScenes())//防止蹭网

//        register(DustScenes())//声波除尘
//        register(NotifyScenes())//通知栏清理
//        register(AudioScenes())//智能音量
//        register(MagnifierScenes())//放大镜
//        register(FontScaleScenes())//字体放大器
//        register(DaysScenes())//倒数日
//        register(AccountScenes())//记账
        //register(NetworkScenes())//网络监测

//        SceneTaskProducer.getInstance().executeAutoTask(true)
    }


    fun registerByType(type: Int) {
        initCleaner()
    }

    enum class Vest {
        CLEAN, WIFI, POWER
    }
}
