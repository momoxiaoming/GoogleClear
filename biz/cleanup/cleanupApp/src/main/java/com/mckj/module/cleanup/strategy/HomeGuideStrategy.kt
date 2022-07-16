package com.mckj.module.cleanup.strategy

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mckj.module.cleanup.gen.CleanupSp
import com.mckj.module.cleanup.ui.OsHomeGuideFragment
import com.mckj.module.cleanup.util.Log
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import io.reactivex.rxjava3.disposables.Disposable

/**
 *  author : xx
 *  date : 2022/3/6 22:17
 *  description :首页强弹推荐的逻辑
 */
class HomeGuideStrategy(fm: FragmentManager) : LifecycleEventObserver {
    private var TAG = "HomeGuideStrategy"
    private var mDispose: Disposable? = null
    private val mFm = fm
    private var popTime = 1

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.d(TAG, "onCreate")
            }
            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "onResume")
                showHomeGuide()
            }
            Lifecycle.Event.ON_STOP,
            Lifecycle.Event.ON_PAUSE,
            -> {
                Log.d(TAG, "on page dissmiss")
                mDispose?.dispose()
            }
            Lifecycle.Event.ON_DESTROY -> {
                Log.d(TAG, "on page dissmiss")
                if (mDispose != null && mDispose?.isDisposed == false) {
                    mDispose?.dispose()
                    mDispose = null
                }
            }
        }
    }


//    private fun startTimer() {
//        Observable.timer(0, TimeUnit.SECONDS)
//            .doOnSubscribe {
//                mDispose = it
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                Log.d(TAG, "判断是否展示推荐页面")
//                showHomeGuide()
//            }
//        showHomeGuide()
//    }


    //判断是否跳到首页首次引导弹框界面
    private fun showHomeGuide() {
        val lastTime = CleanupSp.instance.cleanupHomeGuidePopTime
        val isCleanFirstPop = CleanupSp.instance.isCleanupHomeGuideFirstPop

        if(isCleanFirstPop){
            CleanupSp.instance.isCleanupHomeGuideFirstPop = false
            return
        }

        if(lastTime != 0L){
            return
        }

        val speedInTime = ScenesManager.getInstance().getScenes(ScenesType.TYPE_PHONE_SPEED)
            ?.checkSafeTimeState() == true
        val coolDownInTime =  ScenesManager.getInstance().getScenes(ScenesType.TYPE_COOL_DOWN)
            ?.checkSafeTimeState() == true

        if (speedInTime && coolDownInTime) {
            //赋予展示时间后以后不再展示，因为首次引导已经走完
            CleanupSp.instance.cleanupHomeGuidePopTime = System.currentTimeMillis()
            return
        }

        if(speedInTime){
            showTipsDialog(ScenesType.TYPE_COOL_DOWN)
        }else{
            showTipsDialog(ScenesType.TYPE_PHONE_SPEED)
        }
    }

    private fun showTipsDialog(sceneType: Int) {
        St.stMaskShow(sceneType.toString())
        val dialog = OsHomeGuideFragment.newInstance(sceneType)
        dialog.isCancelable = false
        dialog.showNow(mFm, "showHomeGuideDialog")
    }

}