package com.mckj.sceneslib.ui.overseas.scan.uninstall

import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.org.openlib.help.Consumer
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentOvsScanBinding
import com.mckj.sceneslib.databinding.ScenesFragmentOvsUninstallScanBinding
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.model.UninstallCleanScenes
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.overseas.scan.OvsScenesScanFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.task.ScenesTaskFragment
import com.org.openlib.utils.FragmentUtil
import org.jetbrains.anko.backgroundResource

/**
 * Describe:场景动画类
 *
 * Created By yangb on 2021/5/31
 */
class OvsResidualFragment : LottieFragment<ScenesFragmentOvsUninstallScanBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "ScenesAnimFragment"

        fun newInstance(consumer: Consumer<Boolean>): OvsResidualFragment {
            return OvsResidualFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    override fun getLayoutId() = R.layout.scenes_fragment_ovs_uninstall_scan

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
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        consumer.accept(true)
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        St.stLevelFlashShow(mModel.enterFlag,mModel.getScenesData().name)
        val lottieData = mModel.getScenes().getLottieData()
        mModel.startRepeatLottie(
            mBinding.animLottie,
            lottieData?.fileRaw?:ScenesLottieData.NO_RESOURCE_CODE,
            lottieData?.startFrame,
            consumer
        )
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
                mModel.endTaskLottie(mBinding.animLottie, consumer)
            }
        }
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        consumer.accept(true)
    }

    override fun preFinish(result: Boolean) {
        mConsumer?.accept(result)
    }

}