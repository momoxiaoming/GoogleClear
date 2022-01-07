package com.mckj.sceneslib.ui.scenes.model.junk

import androidx.core.util.Consumer
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dn.openlib.utils.FragmentUtil
import com.dn.vi.app.base.app.kt.getAs
import com.dn.vi.app.base.helper.DataTransport
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.sceneslib.R

import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.scenes.task.ScenesTaskFragment
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.cleancore.entity.IJunkEntity
import com.mckj.sceneslib.databinding.ScenesFragmentJunkBinding
import org.jetbrains.anko.backgroundResource

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class JunkFragment : LottieFragment<ScenesFragmentJunkBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "JunkFragment"

        fun newInstance(consumer: Consumer<Boolean>): JunkFragment {
            return JunkFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    private val mJunkModel by lazy {
        ViewModelProvider(this, JunkViewModelFactory()).get(
            JunkViewModel::class.java
        )
    }

    override fun getLayoutId() = R.layout.scenes_fragment_junk

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
        val list = DataTransport.getInstance().getAs<List<IJunkEntity>>("junk_list")
        mJunkModel.initJunkData(list)
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
        mJunkModel.mJunkSizeLiveData.observe(viewLifecycleOwner, Observer {
            mBinding.junkSizeTv.text = FileUtil.getFileSizeNumberText(it)
            mBinding.junkUnitTv.text = FileUtil.getFileSizeUnitText(it)
        })
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        mBinding.junkSizeTv.gone()
        mBinding.junkUnitTv.gone()
        val taskData = mJunkModel.getTaskData()
        if (taskData != null) {
            FragmentUtil.show(childFragmentManager, ScenesTaskFragment(), R.id.task_layout)
        }
        mModel.startLottie(mBinding.junkLottie, consumer)
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        mBinding.junkSizeTv.show()
        mBinding.junkUnitTv.show()
        mModel.runningLottie(mBinding.junkLottie, consumer)
        mModel.runTask(mJunkModel.getTaskData()) { size, index, result ->
            if (index >= size - 1) {
                mModel.endTaskLottie(mBinding.junkLottie, consumer)
            }
        }
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        mBinding.junkSizeTv.gone()
        mBinding.junkUnitTv.gone()
        mModel.endLottie(mBinding.junkLottie, consumer)
    }

    override fun preFinish(result : Boolean) {
        mConsumer?.accept(result)
    }

}