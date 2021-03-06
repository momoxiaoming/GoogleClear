package com.mckj.sceneslib.ui.scenes.anim

import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.org.openlib.help.Consumer
import com.mckj.baselib.util.ResourceUtil
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentScenesAnimBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.scenes.task.ScenesTaskFragment
import com.org.openlib.utils.FragmentUtil
import org.jetbrains.anko.backgroundResource

/**
 * Describe:场景动画类
 *
 * Created By yangb on 2021/5/31
 */
class ScenesAnimFragment : LottieFragment<ScenesFragmentScenesAnimBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "ScenesAnimFragment"

        fun newInstance(consumer: Consumer<Boolean>): ScenesAnimFragment {
            return ScenesAnimFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    override fun getLayoutId() = R.layout.scenes_fragment_scenes_anim

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
            navigationIcon = null
        }
    }


    override fun startAnim(consumer: Consumer<Boolean>) {
        val taskData = mModel.getTaskData()
        if (taskData != null) {
            mBinding.taskNameTv.gone()
            mBinding.taskLayout.show()
            FragmentUtil.show(childFragmentManager, ScenesTaskFragment(), R.id.task_layout)
        } else {
            mBinding.taskNameTv.show()
            mBinding.taskLayout.gone()
            mBinding.taskNameTv.text = String.format(ResourceUtil.getString(R.string.scenes_ing),mModel.getScenesData().name)
        }
        St.stLevelFlashShow(mModel.enterFlag,mModel.getScenesData().name)
        mModel.startLottie(mBinding.animLottie, consumer)
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        mModel.runningLottie(mBinding.animLottie, consumer)
        mModel.runTask { size, index, result ->
            if (index >= size - 1) {
                mModel.endTaskLottie(mBinding.animLottie, consumer)
            }
        }
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        mModel.endLottie(mBinding.animLottie, consumer)
    }

    override fun preFinish(result: Boolean) {
        mConsumer?.accept(result)
    }

}