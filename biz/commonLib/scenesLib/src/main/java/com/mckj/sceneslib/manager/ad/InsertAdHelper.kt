package com.mckj.sceneslib.manager.ad

import android.app.Activity
import android.content.Context

import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.strategy.helper.WeightsScenesProvider.checkIsFirstEchelon

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.manager.ad
 * @data  2022/5/24 09:37
 */
class InsertAdHelper(val context: Context, private val iAsyncShowAd: IAsyncShowAd) {
    /**
     * 1.广告什么时候加载完就什么时候显示会出现两种情况
     *  1.1.广告显示但是动画流程还没走完
     *  1.2.动画流程走完了但是广告还没回调
     */
    companion object {
        private const val AD_NAME = "scan_plaque"
    }

    /**
     * 广告结束标志位
     */
    private var isAdClose = false
    private val scenesType = iAsyncShowAd.getScenesType()


    /**
     * 广告处于结束状态才能结束流程
     */
    private fun doAnimFinishStep() {
        if (isAdClose) finishStep()
    }


    /**
     * 动画处于结束状态才能结束流程
     */
    private fun doAdFinishStep() {
        if (iAsyncShowAd.getAnimaState()) finishStep()
    }

    /**
     * 结束流程
     */
    private fun finishStep() {
        iAsyncShowAd.getStepConsumer()?.accept(true)
    }


    fun doInsertAdFinishStep() {
        if (checkIsFirstEchelon(scenesType)) {
            //动画可能比广告结束前，保护层
            doAnimFinishStep()
        } else {
            finishStep()
        }
    }


    /**
     * 加载插屏广告
     */
    fun loadInsertAd(scenesKey: String,onAdLoadEnd:()->Unit) {
        if (!checkIsFirstEchelon(scenesType)) return
//        NewsAdHelper.loadAd(AD_NAME) {
//            if (it.adStatus == AdStatus.LOAD_END) {
//                onAdLoadEnd()
//                val cacheAd = NewsAdHelper.isCacheAd(AD_NAME)
//                St.stIfAdIn(scenesKey, AD_NAME,cacheAd.toString())
//                showInsertAd {
//                    doAdFinishStep()
//                    isAdClose = true
//                }
//            }else if(it.adStatus == AdStatus.ERROR){
//                doAdFinishStep()
//                isAdClose = true
//            }
//        }
    }

    /**
     * 显示插屏广告
     */
    private fun showInsertAd(finishAction: () -> Unit) {
//        NewsAdHelper.showAd(context, AD_NAME, false) { adStatus ->
//            when (adStatus) {
//                AdStatus.SHOW_SUCCESS -> {
//                    St.stScanInsertAdShow()
//                }
//                AdStatus.CLOSE -> {
//                    St.stScanInsertAdColse()
//                    finishAction()
//                }
//                AdStatus.ERROR -> {
//                    finishAction()
//                }
//                else -> {
//                }
//            }
//        }
    }


}