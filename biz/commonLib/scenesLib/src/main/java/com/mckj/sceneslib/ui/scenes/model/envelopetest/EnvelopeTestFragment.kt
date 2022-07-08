package com.mckj.sceneslib.ui.scenes.model.envelopetest

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.cm.utils.TextUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.org.openlib.help.Consumer
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.util.ResourceUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentEnvelopeTestBinding
import com.mckj.sceneslib.entity.DelayTestTaskData
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.sceneslib.manager.scenes.model.EnvelopeScenes
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.viewbinder.HostDelayTaskViewBinder
import com.mckj.sceneslib.util.Log
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor


/**
 * 红包测速
 */
@Route(path = ARouterPath.Wifi.FRAGMENT_ENVELOPE_TEST)
class EnvelopeTestFragment : LottieFragment<ScenesFragmentEnvelopeTestBinding, ScenesViewModel>() {

    private var taskFinished: Boolean = false
    private var adCached: Boolean = false

    private val mEnvelopeModel by lazy {
        ViewModelProvider(this).get(
            EnvelopeTestViewModel::class.java
        )
    }


    /**
     * 页面关闭回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    companion object {

        const val TAG = "NetworkTestFragment"

        fun newInstance(consumer: Consumer<Boolean>): EnvelopeTestFragment {
            return EnvelopeTestFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    private var binder: HostDelayTaskViewBinder? = null

    //列表适配器
    private val mTaskAdapter: MultiTypeAdapter by lazy {
        binder = HostDelayTaskViewBinder().also {
            it.itemClickListener = onRetryClickListener
        }
        val adapter = MultiTypeAdapter()
        adapter.register(DelayTestTaskData::class, binder!!)
        adapter
    }

    //列表测速点击事件
    private val onRetryClickListener =
        object : AbstractViewBinder.OnItemClickListener<DelayTestTaskData> {
            override fun onItemClick(view: View, position: Int, t: DelayTestTaskData) {
                when (view.id) {
                    R.id.tv_status -> {
                        if (t.delay >= 0) {
                            return
                        }
                        if (!mEnvelopeModel.isConnect) {
                            Toast.makeText(requireContext(), "网络未连接", Toast.LENGTH_SHORT).show()
                            return
                        }

                        val value = mEnvelopeModel.taskDataLiveData.value
                        if (value != null) {
                            startEnvelopeTestOfIndex(value!!.toMutableList(), position)
                        }
                    }
                }
            }
        }

    override fun initView() {
        //初始化Toolbar
        mBinding.headerLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.headerLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            navigationIcon = null
        }

        //重新测速点击事件
//        mBinding.tvRestart.setOnClickListener {
//            if (mEnvelopeModel.isConnect) {
//                binder?.finish = false
//
//                //重新测速埋点
//                St.stRedPacketRetestClick()
//                //开始测速
//                startEnvelopeTest()
//                showHeadProgressView()
//            } else {
//                Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show()
//            }
//        }
//
        mBinding.rvList.layoutManager = LinearLayoutManager(mBinding.rvList.context)
    }

    override fun initObserver() {
        super.initObserver()
        //网络监听
        NetworkData.getInstance().connectInfoLiveData.observe(viewLifecycleOwner, {
            Log.i(TAG, "initObserver: it:$it")
            mEnvelopeModel.isConnect = (it.networkType != ConnectInfo.NetworkType.NOT_CONNECTED)
        })

        //任务监听
        mEnvelopeModel.taskDataLiveData.observe(viewLifecycleOwner) {
            val scenes = mModel.getScenes()
            if (scenes is EnvelopeScenes) {
                scenes.taskList = it
            }
            setAdapter(it)
        }

        //进度监听
        mEnvelopeModel.progressNum.observe(viewLifecycleOwner) {
            mBinding.tvProgress.text = "$it%"
            if (it >= 100) {
                taskFinished = true
                //告知适配器，测速完成
//                binder?.finish = true

                //测速完成埋点
                St.stRedPacketResultShow()

                finishFragment()
            } else {
//                mBinding.tvRestart.visibility = View.INVISIBLE
                mBinding.tvProgressStatus.text = "正在测速中..."
            }
        }
    }

    private fun finishFragment() {
        //取消动画
        val animation = mBinding.loadingProgress.animation
        animation.cancel()
        mConsumer?.accept(true)
    }

    private fun showAD(context : Context) {
        mModel.showLandingBeforeAd(context) {
            hideHeadProgressView()
        }
    }

    /**
     * 隐藏头部进度
     */
    private fun hideHeadProgressView() {
//        mBinding.tvRestart.visibility = View.VISIBLE
        mBinding.tvProgressStatus.text = "测速完成"
        mBinding.llHeader.visibility = View.GONE
    }

    /**
     * 显示头部进度
     */
    private fun showHeadProgressView() {
        val visibility = mBinding.llHeader.visibility
        if (visibility != View.VISIBLE) {
            mBinding.llHeader.visibility = View.VISIBLE
        }
    }

    /**
     * 获取旋转动画
     */
    private fun getRotateAnimation(): RotateAnimation {
        return RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = 2000
            repeatCount = -1
            fillAfter = true
            startOffset = 0
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.scenes_fragment_envelope_test
    }

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }


    override fun startAnim(consumer: Consumer<Boolean>) {
        val rotateAnimation = getRotateAnimation()
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                mEnvelopeModel.isRotate = true
            }

            override fun onAnimationEnd(animation: Animation?) {
                mEnvelopeModel.isRotate = false
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        mBinding.loadingProgress.animation = rotateAnimation
        St.stLevelFlashShow(mModel.enterFlag,mModel.getScenesData().name)
        consumer.accept(true)
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        mEnvelopeModel.runTask(mEnvelopeModel.initTestTask())
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        St.stLevelFlashEnd(mModel.enterFlag,mModel.getScenesData().name)
    }

    override fun preFinish(result: Boolean) {
        //测速完成后不需要关闭页面
    }

    private fun startEnvelopeTest() {
        //测速动画
        val rotateAnimation = getRotateAnimation()
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                mEnvelopeModel.isRotate = true
            }

            override fun onAnimationEnd(animation: Animation?) {
                mEnvelopeModel.isRotate = false
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        mBinding.loadingProgress.animation = rotateAnimation
        //红包测速开始埋点
        St.stRedPacketScanStart()

        //初始化测速数据
        val testTaskList = mEnvelopeModel.initTestTask()
        //开始测速
        mEnvelopeModel.runTask(testTaskList)
    }

    private fun startEnvelopeTestOfIndex(task: MutableList<DelayTestTaskData>, index: Int) {
        val rotateAnimation = getRotateAnimation()
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                mEnvelopeModel.isRotate = true
            }

            override fun onAnimationEnd(animation: Animation?) {
                mEnvelopeModel.isRotate = false
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        mBinding.loadingProgress.animation = rotateAnimation

        mEnvelopeModel.runTaskOfIndex(task, index)
    }


    private fun setAdapter(list: List<DelayTestTaskData>) {
        if (mBinding.rvList.adapter == null) {
            mBinding.rvList.adapter = mTaskAdapter
        }
        mTaskAdapter.items = list ?: emptyList()

        mTaskAdapter.notifyDataSetChanged()
    }

}