package com.mckj.sceneslib.ui.scenes.model.cooldown

import androidx.core.util.Consumer
import androidx.lifecycle.ViewModelProvider
import com.dn.openlib.utils.FragmentUtil
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.sceneslib.R
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.scenes.task.ScenesTaskFragment
import com.mckj.sceneslib.databinding.ScenesFragmentCoolDownBinding
import org.jetbrains.anko.backgroundResource

/**
 * Describe:
 *
 * Created By yangb on 2021/3/4
 */
class CoolDownFragment :
    LottieFragment<ScenesFragmentCoolDownBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "CoolDownFragment"

        fun newInstance(consumer: Consumer<Boolean>): CoolDownFragment {
            return CoolDownFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    private val mCoolDownModel by lazy {
        ViewModelProvider(requireActivity(), CoolDownViewModelFactory()).get(
            CoolDownViewModel::class.java
        )
    }

    override fun getLayoutId() = R.layout.scenes_fragment_cool_down

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
        super.initData()
    }

    override fun initView() {
        mBinding.headerLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.headerLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        mCoolDownModel.mTemperatureLiveData.observe(viewLifecycleOwner) {
            mBinding.coolDownTemperatureTv.text = "$it℃"
        }
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        mBinding.coolDownTemperatureTv.show()
        val taskData = mCoolDownModel.getTaskData()
        if (taskData != null) {
            FragmentUtil.show(childFragmentManager, ScenesTaskFragment(), R.id.task_layout)
        }
        mModel.startLottie(mBinding.coolDownLottie, consumer)
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        mModel.runningLottie(mBinding.coolDownLottie, consumer)
        mModel.runTask(mCoolDownModel.getTaskData()) { size, index, result ->
            if (index >= size - 1) {
                mModel.endTaskLottie(mBinding.coolDownLottie, consumer)
            }
        }
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        mBinding.coolDownTemperatureTv.gone()
        mModel.endLottie(mBinding.coolDownLottie, consumer)
    }

    override fun preFinish(result : Boolean) {
        mConsumer?.accept(result)
    }

}
