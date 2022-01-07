package com.mckj.sceneslib.ui.scenes

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.core.util.Consumer
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.util.Log
import com.dn.baselib.base.databinding.AbstractViewModel
import com.dn.baselib.ext.launch
import com.dn.openlib.callback.Consumer3

import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/2/4
 */
class ScenesViewModel : AbstractViewModel() {

    companion object {
        const val TAG = "ScenesViewModel"
    }

    /**
     * 页面跳转参数
     */
    private lateinit var mJumpData: ScenesJumpData

    /**
     * 当前场景对象
     */
    private lateinit var mScenes: AbstractScenes

    /**
     * 任务列表数据
     */
    val taskDataLiveData = MutableLiveData<ScenesTaskData>()

    fun init(entity: ScenesJumpData?) {
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
    }

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
                for (index in list.indices) {
                    val task = list[index]
                    task.state = ScenesTask.STATE_LOADING
                    taskDataLiveData.value = taskData
                    task.block.invoke()
                    task.state = ScenesTask.STATE_COMPLETE
                    taskDataLiveData.value = taskData
                    consumer.accept(list.size, index, true)
                }
            }
        }
    }

    /**
     * 开始lottie动画
     */
    fun startLottie(lottieView: LottieAnimationView, consumer: Consumer<Boolean>) {
        //获取lottie动画数据
        val lottieData = getLottieData()
        if (lottieData == null) {
            Log.i(TAG, "startLottie error: lottieData is null")
            consumer.accept(false)
            return
        }
        //lottie 动画数据填充
        lottieView.imageAssetsFolder = lottieData.dir
        lottieView.setAnimation(lottieData.filePath)
        //执行开始帧动画
        val frame = lottieData.startFrame
        if (frame == null) {
            consumer.accept(true)
        } else {
            lottieView.apply {
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
    }

    /**
     * 运行lottie动画
     */
    fun runningLottie(
        lottieView: LottieAnimationView,
        consumer: Consumer<Boolean>,
    ) {
        val frame = getLottieData()?.runFrame
        if (frame == null) {
            consumer.accept(true)
        } else {
            //执行过程中，动画循环播放
            lottieView.apply {
                setMinAndMaxFrame(frame.start, frame.end)
                speed = 1f
                repeatCount = LottieDrawable.INFINITE
                repeatMode = LottieDrawable.RESTART
                playAnimation()
            }
        }
    }

    /**
     * 结束运行任务动画
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
        val frame = getLottieData()?.endFrame
        if (frame == null) {
            consumer.accept(true)
        } else {
            //结束动画只播放一次
            lottieView.apply {
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
    }

    /**
     * 加载落地页前广告
     */
    fun loadLandingBeforeAd() {

    }

    /**
     * 显示落地页前广告
     */
    fun showLandingBeforeAd(fm: FragmentManager, consumer: Consumer<Boolean>) {
        val adName = mJumpData.landingBeforeAdName
        if (!adName.isNullOrEmpty()) {
            showAd(adName, fm, consumer)
        } else {
            Log.i(TAG, "showLandingAfterAd error: adName is null")
            consumer.accept(true)
        }
    }

    /**
     * 加载落地页后广告
     */
    fun loadLandingAfterAd() {

    }

    /**
     * 显示落地页后广告
     */
    fun showLandingAfterAd(fm: FragmentManager, consumer: Consumer<Boolean>) {
        val adName = mJumpData.landingAfterAdName
        if (!adName.isNullOrEmpty()) {
            showAd(adName, fm, consumer)
        } else {
            Log.i(TAG, "showLandingAfterAd error: adName is null")
            consumer.accept(true)
        }
    }

    private fun showAd(adName: String, fm: FragmentManager, consumer: Consumer<Boolean>) {
        consumer.accept(true)
    }

}