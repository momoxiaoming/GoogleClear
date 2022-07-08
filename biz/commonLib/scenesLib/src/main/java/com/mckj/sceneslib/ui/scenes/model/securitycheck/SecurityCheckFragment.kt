package com.mckj.sceneslib.ui.scenes.model.securitycheck

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.org.openlib.help.Consumer
import com.mckj.sceneslib.R
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.scenes.task.ScenesTaskFragment
import com.mckj.baselib.util.TextUtils
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.databinding.ScenesFragmentSecurityCheckBinding
import com.mckj.sceneslib.gen.St
import com.org.openlib.utils.FragmentUtil
import org.jetbrains.anko.backgroundResource
import java.util.*

/**
 * Describe:安全检测
 *
 * Created By yangb on 2020/10/21
 */
class SecurityCheckFragment :
    LottieFragment<ScenesFragmentSecurityCheckBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "SecurityCheckFragment"

        fun newInstance(consumer: Consumer<Boolean>): SecurityCheckFragment {
            return SecurityCheckFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    private val mSecurityModel by lazy {
        ViewModelProvider(this, SecurityCheckViewModelFactory()).get(
            SecurityCheckViewModel::class.java
        )
    }

    override fun getLayoutId() = R.layout.scenes_fragment_security_check

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
    }

    override fun initObserver() {
        super.initObserver()
        mSecurityModel.mPercentLiveData.observe(viewLifecycleOwner, Observer {
            val percentText = String.format(Locale.getDefault(), "%1.0f", it)
            mBinding.securityCheckPercentTv.text = TextUtils.string2SpannableStringForSize(
                "${percentText}%",
                percentText,
                sizeDip = 36
            )
        })
        mSecurityModel.mExceptionCountLiveData.observe(viewLifecycleOwner) { count ->
            mBinding.securityCheckCountTv.text = "发现${count}处风险"
        }
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        val taskData = mSecurityModel.getTaskData()
        if (taskData != null) {
            FragmentUtil.show(childFragmentManager, ScenesTaskFragment(), R.id.task_layout)
        }
        St.stLevelFlashShow(mModel.enterFlag,mModel.getScenesData().name)
        mModel.startLottie(mBinding.securityCheckLottie, consumer)
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        mModel.runningLottie(mBinding.securityCheckLottie, consumer)
        mModel.runTask(mSecurityModel.getTaskData()) { size, index, result ->
            if (index >= size - 1) {
                mModel.endTaskLottie(mBinding.securityCheckLottie, consumer)
            }
        }
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        val exceptionCount = mSecurityModel.mExceptionCountLiveData.value ?: 0
        mModel.getScenesData().landingDesc = "发现${exceptionCount}处风险"
        St.stLevelFlashEnd(mModel.enterFlag,mModel.getScenesData().name)
        mModel.endLottie(mBinding.securityCheckLottie, consumer)
    }

    override fun preFinish(result : Boolean) {
        mConsumer?.accept(result)
    }

}