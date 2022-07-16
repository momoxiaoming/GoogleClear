package com.mckj.module.cleanup.strategy

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.dn.vi.app.cm.log.VLog
import com.mckj.module.cleanup.entity.scenes.HomeRecommendManager
import com.mckj.module.cleanup.gen.CleanupSp
import com.mckj.module.cleanup.ui.HomeRecommendFragment
import com.mckj.sceneslib.manager.strategy.GuideStrategy
import com.mckj.sceneslib.manager.strategy.helper.StrategyHelper
import com.mckj.sceneslib.manager.strategy.helper.StrategyManager
import com.mckj.sceneslib.manager.strategy.helper.StrategyType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *  author : xx
 *  date : 2022/3/6 22:17
 *  description :首页强弹推荐的逻辑
 */
class HomeRecommendStrategy(fm: FragmentManager) : LifecycleEventObserver {
    private var TAG = "HomeRecommendStrategy"
    private var mDispose: Disposable? = null
    private val mFm = fm
    private val log by lazy {
        VLog.scoped("HomeRecommendStrategy")
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
            }
            Lifecycle.Event.ON_RESUME -> {
                val checkShow = checkShow()
                if (!checkShow) {
                    return
                }

                StrategyHelper.getInstance().doGuideStrategy { type ->
                    if (type > 0) {
                        val scenes = HomeRecommendManager.getInstance().getScenes(type)
                        if (scenes != null) {
                            startTimer(type)
                        } else {
                            log.e("场景${type}不存在")
                        }
                    }
                }
            }
            Lifecycle.Event.ON_STOP,
            Lifecycle.Event.ON_PAUSE,
            -> {
                mDispose?.dispose()
            }
            Lifecycle.Event.ON_DESTROY -> {
                if (mDispose != null && mDispose?.isDisposed == false) {
                    mDispose?.dispose()
                    mDispose = null
                }
            }
            else -> {}
        }
    }

    private var firstCleanGuide = true
    private var lastMask = 0L
    private fun checkShow(): Boolean {
        //更新条件默认值
        if (firstCleanGuide) {
            firstCleanGuide = CleanupSp.instance.firstCleanGuide
        }
        if (lastMask== 0L){
            lastMask = CleanupSp.instance.cleanupHomeGuidePopTime
        }
        //判断是否过推荐CD期
        val skipTimestamp = CleanupSp.instance.skipTimestamp
        val inCD = (System.currentTimeMillis() - skipTimestamp) <= TimeUnit.MINUTES.toMillis(3)
        if (inCD) {
            log.d("小于上次跳过3分钟")
            return false
        }
        //判断是否已经第一次引导
        if (firstCleanGuide) {
            log.d("未进行第一次引导")
            return false
        }
        //判断是否已经首次蒙层
        if (lastMask == 0L) {
            log.d("未进行第一次蒙层")
            return false
        }
        return true
    }


    private fun startTimer(scenesType: Int) {
        Observable.timer(3, TimeUnit.SECONDS)
            .doOnSubscribe {
                mDispose = it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showHomeRecommend(scenesType)
            }
    }


    //判断是否跳到首页推荐弹框界面
    private fun showHomeRecommend(scenesType: Int) {
        showTipsDialog(scenesType)
    }

    private fun showTipsDialog(scenesType: Int) {
        val dialog = HomeRecommendFragment.newInstance(scenesType)
        dialog.showNow(mFm, HomeRecommendFragment.TAG)
    }

}