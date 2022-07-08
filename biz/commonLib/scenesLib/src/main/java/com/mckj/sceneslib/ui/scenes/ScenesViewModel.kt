package com.mckj.sceneslib.ui.scenes

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import androidx.collection.arrayMapOf
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.org.openlib.help.Consumer
import com.org.openlib.help.Consumer3
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.util.Log
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.launch
import com.mckj.sceneslib.entity.ScenesLottieData.Companion.NO_RESOURCE_CODE
import com.mckj.sceneslib.manager.lottie.NewLottieAdapter
import com.mckj.sceneslib.manager.strategy.helper.StrategyManager
import com.mckj.sceneslib.manager.strategy.helper.StrategyType
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/2/4
 */
class ScenesViewModel : AbstractViewModel() {

    companion object {
        const val TAG = "ScenesViewModel"

        //最小执行时间
        const val MIN_TOTAL_TIME = 3000L
    }

    var enterFlag = "in"

    /**
     * 页面跳转参数
     */
    lateinit var mJumpData: ScenesJumpData

    /**
     * 当前场景对象
     */
    private lateinit var mScenes: AbstractScenes

    /**
     * 任务列表数据
     */
    val taskDataLiveData = MutableLiveData<ScenesTaskData>()

    /**
     * 是否点击落地页返回键
     */
    private val isClickBack = MutableLiveData<Boolean>()
    var isClickLandBack = isClickBack

    /**
     * 任务页广告缓存状态
     */
//    val beforeAdLiveData: MutableLiveData<Boolean> = MutableLiveData()

   private val  mNewLottieAdapter by lazy { NewLottieAdapter(getScenesData().type) }

   fun getLottieAdapter() = mNewLottieAdapter

    fun init(entity: ScenesJumpData?): Boolean {
        var result = false
        do {
            if (entity == null) {
                Log.i(TAG, "init error: entity is null")
                break
            }
            mJumpData = entity
            val scenes = ScenesManager.getInstance().getScenes(entity.type)
            if (scenes == null) {
                Log.i(TAG, "init error: scenes is null, type[${entity.type}]")
                break
            }
            mScenes = scenes
            val taskData = scenes.getTaskData()
            if (taskData != null) {
                taskDataLiveData.value = taskData
            }
            result = true
        } while (false)
        if (!result) {
            isFinish.value = true
        }
        return result
    }

    fun getStrategy() = StrategyManager.getInstance().getStrategy(StrategyType.WEIGHTS_STRATEGY)

    fun getJumpData(): ScenesJumpData {
        return mJumpData
    }

    fun getScenes(): AbstractScenes {
        return mScenes
    }

    fun getScenesData(): ScenesData {
        return getScenes().getData()
    }

    /**
     * 获取动画数据
     */
    fun getLottieData(): ScenesLottieData? {
        return mScenes.getLottieData()
    }

    /**
     * 获取引导类型列表
     */
    fun getGuideTypeList(): List<Int>? {
        return getJumpData().guideTypes
    }

    fun getTaskData(): ScenesTaskData? {
        return getScenes().getTaskData()
    }

    /**
     * 获取引导对象列表
     */
    fun getGuideScenesList(): List<AbstractScenes>? {
        val types = getGuideTypeList()
        if (types.isNullOrEmpty()) {
            return null
        }
        val list = mutableListOf<AbstractScenes>()
        types.forEach { type ->
            val scenes = ScenesManager.getInstance().getScenes(type)
            if (scenes != null) {
                list.add(scenes)
            }
        }
        return list
    }

    /**
     * 开始执行任务
     *
     * @param consumer(Size : Int, Index : Int, Result: Boolean)
     */
    fun runTask(consumer: Consumer3<Int, Int, Boolean>) {
        runTask(getTaskData(), consumer)
    }

    fun runTask(taskData: ScenesTaskData?, consumer: Consumer3<Int, Int, Boolean>) {
        launch {
            val list = taskData?.taskList
            if (list.isNullOrEmpty()) {
                delay(2000L)
                consumer.accept(0, 0, true)
            } else {
                val size = list.size
                val startTime = System.currentTimeMillis()
                for (index in list.indices) {
                    val task = list[index]
                    task.state = ScenesTask.STATE_LOADING
                    taskDataLiveData.value = taskData
                    task.block.invoke()
                    if (index == size - 1) {
                        val totalTime = System.currentTimeMillis() - startTime
                        if (totalTime < MIN_TOTAL_TIME) {
                            delay(MIN_TOTAL_TIME - totalTime)
                        }
                    }
                    task.state = ScenesTask.STATE_COMPLETE
                    taskDataLiveData.value = taskData
                    consumer.accept(size, index, true)
                }
            }
        }
    }

    fun startOnceLottie(
        lottieView: LottieAnimationView,
        lottieRaw: Int,
        frame: LottieFrame?,
        consumer: Consumer<Boolean>,
    ) {
        if (lottieRaw== NO_RESOURCE_CODE) {
            consumer.accept(true)
            Log.i(TAG, "startLottie error: lottiePath is null")
            return
        }
        if (frame == null) {
            consumer.accept(true)
            Log.i(TAG, "startLottie error: frame is null")
            return
        }
        lottieView.apply {
            setAnimation(lottieRaw)
            //动画播放一次结束
            setMinAndMaxFrame(frame.start, frame.end)
            speed = 1f
            repeatCount = 0
            repeatMode = LottieDrawable.RESTART
            addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator?) {
                    super.onAnimationCancel(animation)
                    removeAllAnimatorListeners()
                    consumer.accept(true)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    removeAllAnimatorListeners()
                    consumer.accept(true)
                }
            })
            playAnimation()
        }
    }


    fun startRepeatLottie(
        lottieView: LottieAnimationView,
        lottieRaw: Int,
        frame: LottieFrame?,
        consumer: Consumer<Boolean>
    ) {
        if (lottieRaw== NO_RESOURCE_CODE) {
            consumer.accept(true)
            Log.i(TAG, "startLottie error: lottiePath is null")
            return
        }
        if (frame == null) {
            consumer.accept(true)
            Log.i(TAG, "startLottie error: frame is null")
            return
        }
        //执行过程中，动画循环播放
        lottieView.apply {
            setAnimation(lottieRaw)
            setMinAndMaxFrame(frame.start, frame.end)
            speed = 1f
            repeatCount = LottieDrawable.INFINITE
            repeatMode = LottieDrawable.RESTART
            playAnimation()
        }
    }

    /**
     * 开始lottie动画
     */
    fun startLottie(lottieView: LottieAnimationView, consumer: Consumer<Boolean>) {
        //获取lottie动画数据
        val lottieData = getLottieData()
        startOnceLottie(lottieView, lottieData?.fileRaw?:NO_RESOURCE_CODE, lottieData?.startFrame, consumer)
    }

    /**
     * 运行lottie动画
     */
    fun runningLottie(
        lottieView: LottieAnimationView,
        consumer: Consumer<Boolean>,
    ) {
        val filePath = getLottieData()?.fileRaw?: NO_RESOURCE_CODE
        val frame = getLottieData()?.runFrame
        startRepeatLottie(lottieView, filePath, frame, consumer)
    }

    /**
     * 结束运行任务动画
     *
     * 移除动画监听
     */
    fun endTaskLottie(lottieView: LottieAnimationView, consumer: Consumer<Boolean>) {
        lottieView.apply {
            repeatCount = 0
            addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator?) {
                    super.onAnimationCancel(animation)
                    removeAllAnimatorListeners()
                    consumer.accept(true)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    removeAllAnimatorListeners()
                    consumer.accept(true)
                }
            })
        }
    }

    /**
     * 完成lottie动画
     */
    fun endLottie(lottieView: LottieAnimationView, consumer: Consumer<Boolean>) {
        val lottieData = getLottieData()
        //结束动画只播放一次
        mScenes.getData().block?.invoke()
        startOnceLottie(lottieView, lottieData?.fileRaw?: NO_RESOURCE_CODE, lottieData?.endFrame, consumer)
    }


    /**
     * 加载落地页前广告
     */
    fun loadLandingBeforeAd() {
//        preLoadAd()
//        val adName = mJumpData.landingBeforeAdName
//        if (adName.isNullOrEmpty()) {
//            return
//        }
//        NewsAdHelper.loadAd(adName)
    }

    private fun preLoadAd() {
        getJumpData().landingAdName?.let {
//            NewsAdHelper.loadAd(it)
        }
    }

    /**
     * 显示落地页前广告
     */
    fun showLandingBeforeAd(context: Context, consumer: Consumer<Boolean>) {
//        val adName = mJumpData.landingBeforeAdName?:""
//        stIfAdIn(adName)
//        if (adName.isNotEmpty()) {
//            showAd(context, adName, consumer)
//        } else {
//            Log.i(TAG, "showLandingAfterAd error: adName is null")
//            consumer.accept(true)
//        }
        consumer.accept(true)
    }

    private fun stIfAdIn(adName: String) {
//        val cacheAd = NewsAdHelper.isCacheAd(adName)
//        St.stIfAdIn(getScenesData().key,adName,cacheAd.toString())
    }

    /**
     * 加载落地页后广告
     */
    fun loadLandingAfterAd() {
//        val adName = mJumpData.landingAfterAdName
//        if (!adName.isNullOrEmpty()) {
//            AdHelper.loadAd(adName)
//        }
    }

    /**
     * 显示落地页后广告
     */
    fun showLandingAfterAd(context: Context, consumer: Consumer<Boolean>) {
        val adName = mJumpData.landingAfterAdName
        if (!adName.isNullOrEmpty()) {
            showAd(context, adName, consumer)
        } else {
            Log.i(TAG, "showLandingAfterAd error: adName is null")
            consumer.accept(true)
        }
    }

    private fun showAd(context: Context, adName: String, consumer: Consumer<Boolean>) {
//        Log.i(TAG, "${StringHexConfig.showad}: name:$adName")
//        NewsAdHelper.showAd(context, adName, false) { adStatus ->
//            val response = LiveDataResponse.success(adStatus)
//            Log.i(TAG, "${StringHexConfig.showad} name:$adName status:$adStatus")
//            when (adStatus) {
//                AdStatus.SHOW_SUCCESS -> {
//                    St.stLevelAdShow(enterFlag, mScenes.getData().key)
//                }
//                AdStatus.CLICK -> {
////                    St.stLevelBackAdClick(mScenes.getData().key)
//                }
//                AdStatus.CLOSE -> {
//                    St.stLevelAdClose(mScenes.getData().key)
//                    consumer.accept(true)
//                }
//                AdStatus.ERROR -> {
//                    consumer.accept(true)
//                }
//                else -> {
//                }
//            }
//        }
    }

    var mLocalAdStatus = LocalAdStatus.NORMAL

//    /**
//     * 加载插屏广告
//     */
//    fun loadInsertAd(consumer: Consumer<AdResult<AdItem>>? = null) {
//        mLocalAdStatus = LocalAdStatus.LOAD
//        val adName = "scan_plaque"
//        NewsAdHelper.loadAd(adName, consumer)
//    }

    /**
     * 显示插屏广告
     */
    fun showInsertAd(context: Context?, consumer: Consumer<Boolean>? = null) {
        if (context==null){
            Log.i(TAG, "showInsertAd but context==null")
            consumer?.accept(false)
            return
        }
        mLocalAdStatus = LocalAdStatus.SHOW
        val adName = "scan_plaque"
        stIfAdIn(adName)
//        NewsAdHelper.showAdx(context, adName, false) { adResult ->
//            Log.i(TAG, "showInsertAd showAdx:${adResult}")
//            when (adResult.adStatus) {
//                AdStatus.SHOW_SUCCESS -> {
//                    St.stScanInsertAdShow()
//                }
//
//                AdStatus.CLOSE->{
//                    mLocalAdStatus = LocalAdStatus.STOP
//                    St.stScanInsertAdColse()
//                    consumer?.accept(true)
//                }
//                AdStatus.ERROR -> {
//                    mLocalAdStatus = LocalAdStatus.STOP
//                    consumer?.accept(true)
//                }
//
//                else -> {
//                }
//            }
//        }
        mLocalAdStatus = LocalAdStatus.STOP
        consumer?.accept(true)
    }

    /**
     * 确保场景被注册
     */
    fun ensureSceneRegister(sceneType: Int):Int {
        ScenesManager.getInstance().getScenes(sceneType) ?: let {
            ScenesManager.getInstance().initCleaner()
        }
        return sceneType
    }
}