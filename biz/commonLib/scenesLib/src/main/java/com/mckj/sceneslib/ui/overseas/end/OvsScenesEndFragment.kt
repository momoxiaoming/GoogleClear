package com.mckj.sceneslib.ui.overseas.end

import androidx.lifecycle.ViewModelProvider
import com.org.openlib.help.Consumer
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentOvsEndBinding
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import org.jetbrains.anko.backgroundResource

/**
 * Describe:场景动画类
 *
 * Created By yangb on 2021/5/31
 */
open class OvsScenesEndFragment : LottieFragment<ScenesFragmentOvsEndBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "ScenesAnimFragment"

        fun newInstance(consumer: Consumer<Boolean>): OvsScenesEndFragment {
            return OvsScenesEndFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    open var mConsumer: Consumer<Boolean>? = null

    override fun getLayoutId() = R.layout.scenes_fragment_ovs_end

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
        }
        mModel.getLottieAdapter().setNewLottieBgColor(mBinding.lottieBg,mBinding.lottieMaskBg)
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        val endLottie = mModel.getScenes().getEndLottie()
        //适配新动画
        val lottieView = mModel.getLottieAdapter().filterLottieView(mBinding.animLottie,mBinding.animLottieNew)
        mModel.startOnceLottie(
            lottieView,
            endLottie?.fileRaw?:ScenesLottieData.NO_RESOURCE_CODE,
            endLottie?.endFrame,
            consumer
        )
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        consumer.accept(true)
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        consumer.accept(true)
    }

    override fun preFinish(result: Boolean) {
        St.stLevelFlashEnd(mModel.enterFlag,mModel.getScenesData().name)
        mConsumer?.accept(result)
    }

}