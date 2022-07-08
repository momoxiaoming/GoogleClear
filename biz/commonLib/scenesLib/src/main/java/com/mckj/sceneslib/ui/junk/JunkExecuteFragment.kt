package com.mckj.sceneslib.ui.junk

import android.animation.Animator
import android.animation.ValueAnimator
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieDrawable
import com.dn.vi.app.cm.utils.FileUtil
import com.org.openlib.help.Consumer
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesOvsJunkExecuteBinding
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import org.jetbrains.anko.backgroundResource

/**
 * Describe:任务执行场景
 *
 * Created By kim
 */
class JunkExecuteFragment : LottieFragment<ScenesOvsJunkExecuteBinding, ScenesViewModel>() {

    companion object {
        const val TAG = "ScenesAnimFragment"
        const val DELAY_TIME = 3000

        fun newInstance(consumer: Consumer<Boolean>): JunkExecuteFragment {
            return JunkExecuteFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    private val mJunkModel by lazy {
        ViewModelProvider(requireActivity(), JunkViewModelFactory()).get(
            JunkViewModel::class.java
        )
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    override fun getLayoutId(): Int {
        return R.layout.scenes_ovs_junk_execute
    }

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initView() {
        mJunkModel.isOut = mModel.enterFlag
        mBinding.headerLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.headerLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            navigationIcon = null
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        mModel.getLottieAdapter().setNewLottieBgColor(mBinding.lottieBg,mBinding.lottieMaskBg)
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        val mSelectSize = mJunkModel.mSelectSizeLiveData.value ?: 0
        if (mSelectSize <= 0L) {
            preFinish(true)
            return
        }
        //适配新动画
        val lottieView = mModel.getLottieAdapter().filterLottieView(mBinding.animLottie,mBinding.animLottieNew)
        mBinding.ivApp.isVisible = mBinding.animLottie.isVisible
        val lottie = mModel.getScenes().getExecuteLottie()
        val fileRaw = lottie?.fileRaw ?: ScenesLottieData.NO_RESOURCE_CODE
        mModel.getLottieAdapter().doNewLottieAnimationStep(lottieView,lottie,false){
            mModel.startRepeatLottie(lottieView, fileRaw, lottie?.runFrame, consumer)
        }
        mBinding.ivApp.apply {
            setAnimation(R.raw.junk_clean_icon)
            speed = 1f
            repeatCount = LottieDrawable.INFINITE
            repeatMode = LottieDrawable.RESTART
            playAnimation()
        }

        val fileSizeNumber = FileUtil.getFileSizeNumber(mSelectSize)
        val fileSizeUnitText = FileUtil.getFileSizeUnitText(mSelectSize)
        val animator = ValueAnimator.ofFloat()
        animator.duration = 3000
        animator.setFloatValues(fileSizeNumber, 0f)
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            mBinding.tvOptimize.text = String.format("%.1f%s", value, fileSizeUnitText)
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                consumer.accept(true)
            }

            override fun onAnimationCancel(animation: Animator?) {
                consumer.accept(true)
            }

            override fun onAnimationRepeat(animation: Animator?) {

            }
        })
        animator.start()
    }


    override fun runningAnim(consumer: Consumer<Boolean>) {
        mBinding.ivApp.removeAllAnimatorListeners()
        mBinding.animLottie.removeAllAnimatorListeners()
        consumer.accept(true)
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        consumer.accept(true)
    }

    override fun preFinish(result: Boolean) {
        mConsumer?.accept(result)
    }


}