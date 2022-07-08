package com.mckj.sceneslib.ui.scenes.model.signalboost

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.org.openlib.help.Consumer
import com.mckj.sceneslib.R
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.baselib.util.TextUtils
import com.mckj.sceneslib.databinding.ScenesFragmentSignalBoostBinding
import com.mckj.sceneslib.gen.St
import org.jetbrains.anko.backgroundResource
import java.util.*

/**
 * Describe:信号增强
 *
 * Created By yangb on 2020/10/21
 */
class SignalBoostFragment : LottieFragment<ScenesFragmentSignalBoostBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "SignalBoostFragment"

        fun newInstance(consumer: Consumer<Boolean>): SignalBoostFragment {
            return SignalBoostFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    private val mSignalModel by lazy {
        ViewModelProvider(this, SignalBoostViewModelFactory()).get(
            SignalBoostViewModel::class.java
        )
    }

    override fun getLayoutId() = R.layout.scenes_fragment_signal_boost

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initView() {
        mBinding.headerLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.headerLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            navigationIcon = null
        }
    }

    override fun initData() {
        super.initData()
        mSignalModel.mPercentLiveData.observe(this, Observer {
            val keyText = it.toInt().toString()
            mBinding.signalBoostPercentTv.text = TextUtils.string2SpannableStringForSize(
                "$keyText%",
                keyText,
                sizeDip = 67
            )
        })
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        St.stLevelFlashShow(mModel.enterFlag,mModel.getScenesData().name)
        mModel.startLottie(mBinding.signalBoostLottie, consumer)
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        mModel.runningLottie(mBinding.signalBoostLottie, consumer)
        mSignalModel.startCheck {
            mModel.endTaskLottie(mBinding.signalBoostLottie, consumer)
        }
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        St.stLevelFlashEnd(mModel.enterFlag,mModel.getScenesData().name)
        mModel.endLottie(mBinding.signalBoostLottie, consumer)
    }

    override fun preFinish(result: Boolean) {
        val percentText =
            String.format(Locale.getDefault(), "%1.0f", (10..20).random().toFloat())
        mModel.getScenesData().landingDesc = "信号增强${percentText}%以上"
        mConsumer?.accept(result)
    }

}