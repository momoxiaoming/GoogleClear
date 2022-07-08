package com.mckj.sceneslib.ui.overseas.scan

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.org.openlib.help.Consumer
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentOvsScanBinding
import com.mckj.sceneslib.entity.LocalAdStatus
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.ad.InsertAdHelper
import com.mckj.sceneslib.manager.strategy.helper.WeightsScenesProvider
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.task.ScenesTaskFragment
import com.org.openlib.utils.FragmentUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.backgroundResource

/**
 * Describe:场景动画类
 *
 * Created By yangb on 2021/5/31
 */
open class OvsScenesScanFragment : LottieFragment<ScenesFragmentOvsScanBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "ScenesAnimFragment"

        fun newInstance(consumer: Consumer<Boolean>): OvsScenesScanFragment {
            return OvsScenesScanFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null
    private var isAnimaState = false
    private var stepConsumer: Consumer<Boolean>? = null

    override fun initData() {
        super.initData()
        showInsertAd.observe(this) {
            if (!it){
                return@observe
            }
            showInsertAd()
        }
    }

    override fun getLayoutId() = R.layout.scenes_fragment_ovs_scan

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initView() {
        mBinding.headerLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.headerLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            navigationIcon = null
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        mModel.getLottieAdapter().setNewLottieBgColor(mBinding.lottieBg, mBinding.lottieMaskBg)
    }


    override fun startAnim(consumer: Consumer<Boolean>) {
        val startTime = System.currentTimeMillis()
        val check = WeightsScenesProvider.checkIsFirstEchelon(mModel.getScenesData().type)
        if (check) {
//            mModel.loadInsertAd {
//                when (it.adStatus) {
//                    AdStatus.LOAD_END -> {
//                        val timeDiff = System.currentTimeMillis() - startTime
//                        checkAdLoadedTimeDiff(timeDiff)
//                    }
//                    else -> {
//
//                    }
//                }
//            }
        }
        consumer.accept(true)
    }

    private val showInsertAd = MutableLiveData(false)
    private fun checkAdLoadedTimeDiff(timeDiff: Long) {
        if (timeDiff > 1000) {
            showInsertAd.postValue(true)
        } else {
            viewScope.launch {
                delay(1000)
                showInsertAd.postValue(true)
            }
        }
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        St.stLevelFlashShow(mModel.enterFlag, mModel.getScenesData().name)
        val lottieData = mModel.getScenes().getLottieData()
        //适配新动画
        val lottieView =
            mModel.getLottieAdapter().filterLottieView(mBinding.animLottie, mBinding.animLottieNew)
        val fileRaw = lottieData?.fileRaw ?: ScenesLottieData.NO_RESOURCE_CODE
        mModel.getLottieAdapter().doNewLottieAnimationStep(lottieView, lottieData, true) {
            mModel.startRepeatLottie(
                lottieView,
                fileRaw,
                lottieData?.startFrame,
                consumer
            )
        }

        val taskData = mModel.getTaskData()
        if (taskData != null) {
            mBinding.taskLayout.show()
            FragmentUtil.show(childFragmentManager, ScenesTaskFragment(), R.id.task_layout)
        } else {
            mBinding.taskLayout.gone()
//            mBinding.taskNameTv.text = String.format(ResourceUtil.getString(R.string.scenes_ing),mModel.getScenesData().name)
        }
        mModel.runTask { size, index, result ->
            if (index >= size - 1) {
                mModel.endTaskLottie(lottieView, consumer)
            }
        }

    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        showInsertAd()
        isAnimaState = true
        if (mModel.mLocalAdStatus == LocalAdStatus.SHOW) {
            stepConsumer = consumer
        } else {
            consumer.accept(true)
        }
    }

    private fun showInsertAd() {
        if (isAnimaState) {
            Log.i("ScenesViewModel", "showInsertAd: 界面流程已结束")
            return
        }
        if (mModel.mLocalAdStatus >= LocalAdStatus.SHOW) {
            Log.i("ScenesViewModel", "showInsertAd: ad本地状态>=show")
            return
        }
        val adName = "scan_plaque"
//        val cacheAd = NewsAdHelper.isCacheAd(adName)
//        St.stIfAdIn(mModel.getScenesData().key, adName, cacheAd.toString())
//        if (!cacheAd){
//            Log.i("ScenesViewModel", "cacheAd: false")
//            return
//        }
//        mModel.showInsertAd(context) {
//            stepConsumer?.accept(true)
//        }
    }


    override fun preFinish(result: Boolean) {
        mConsumer?.accept(result)
    }
}