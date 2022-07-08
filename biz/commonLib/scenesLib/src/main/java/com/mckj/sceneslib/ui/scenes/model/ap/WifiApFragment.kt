package com.mckj.sceneslib.ui.scenes.model.ap

import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.adapter.MultiTypeDiffAdapter
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.base.app.kt.resumedContStep
import com.dn.vi.app.base.app.kt.tip
import com.dn.vi.app.base.helper.StepRunner
import com.dn.vi.app.base.lifecycle.withCond
import com.dn.vi.app.base.view.DefaultItemAnimatorCompat
import com.org.openlib.help.Consumer
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesLayoutApShareBinding
import com.mckj.sceneslib.entity.SecurityCheckEntity
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.viewbinder.SecurityCheckViewBinder
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.launch
import kotlin.coroutines.resume

/**
 * wifi AP 交互页
 * 关闭当前WIFI, 打开AP
 * Created by holmes on 2021/4/21.
 **/
class WifiApFragment : LottieFragment<ScenesLayoutApShareBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "CoolDownFragment"

        fun newInstance(consumer: Consumer<Boolean>): WifiApFragment {
            return WifiApFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    private val mApModel by lazy {
        ViewModelProvider(requireActivity()).get(WifiApViewModel::class.java)
    }

    private val adapter: MultiTypeDiffAdapter by lazy {
        MultiTypeDiffAdapter().apply {
            register(SecurityCheckEntity::class, SecurityCheckViewBinder())
        }
    }

    private var settingCheckingTag: Any? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settingCheckingTag = mApModel.preCheckPermission(this@WifiApFragment)
    }

    override fun getLayoutId() = R.layout.scenes_layout_ap_share

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
        super.initData()

    }

    override fun initView() {
        mBinding.titleBar.title = mModel.getScenesData().name
        mBinding.titleBar.setNavigationOnClickListener {
            activityAs<ViActivity> { goBack() }
        }
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        St.stLevelFlashShow(mModel.enterFlag,mModel.getScenesData().name)
        mModel.startLottie(mBinding.anim, consumer)
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        mModel.runningLottie(mBinding.anim, consumer)
        StepRunner.runner(scope) {
            breakAction = Runnable {
                tip("没有权限")
                consumer.accept(false)
            }

            finishedAction = Runnable {
                mModel.endTaskLottie(mBinding.anim, consumer)
            }
            resumedContStep(this) { cont ->
                if (settingCheckingTag == null) {
                    cont.resume(true)
                    return@resumedContStep
                }
                WifiPermissionRequireFragment.newInstance()
                    .rxShow(childFragmentManager, "settingRequire")
                    .map { it == DialogInterface.BUTTON_POSITIVE }
                    .subscribeBy { ok ->
                        cont.resume(ok)
                    }
            }
            resumedContStep(this) { cont ->
                mApModel.checkPermission()
                mApModel.settingPermission.observe(this@WifiApFragment) { resource ->
                    resource.withCond(
                        loading = {
                            mApModel.log.d("checking setting permission")
                        },
                        error = {}
                    ) {
                        var has = it.data!!
                        if (!has) {
                            has = mApModel.hasPermission()
                        }
                        cont.resume(has)
                    }
                }
            }

            resumedContStep(this) { cont ->
                mModel.getScenes().loadAdAfterReachConditionConsumer?.invoke(true)
                mApModel.updateStatusItems()
                // 构建状态列表
                mBinding.statusList.apply {
                    val lm = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                    layoutManager = lm
                    DefaultItemAnimatorCompat().attach(this).also { animator ->
                        animator.supportsChangeAnimations = false
                    }
                    itemAnimator = null
                    adapter = this@WifiApFragment.adapter
                }

                mApModel.statusItem.observe(this@WifiApFragment) { resource ->
                    resource.withCond(loading = {}, error = {}) {
                        mApModel.log.i("update list")
                        adapter.submitList(it.data)
                    }
                }

                cont.resume(true)
            }

            resumedContStep(this) { cont ->
                scope.launch {
                    try {
                        mApModel.startOp()
                    } catch (e: Exception) {
                    }
                    cont.resume(true)
                }
            }
        }
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        St.stLevelFlashEnd(mModel.enterFlag,mModel.getScenesData().name)
        mModel.endLottie(mBinding.anim, consumer)
    }

    override fun preFinish(result : Boolean) {
        mConsumer?.accept(result)
    }

}