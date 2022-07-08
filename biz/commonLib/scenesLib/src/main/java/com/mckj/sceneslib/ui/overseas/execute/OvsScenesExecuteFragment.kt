package com.mckj.sceneslib.ui.overseas.execute

import android.content.pm.PackageManager
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.org.openlib.help.Consumer
import com.mckj.baselib.util.SizeUtil
import com.mckj.baselib.util.launch
import com.org.openlib.utils.ProcessUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentOvsExecuteBinding
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jetbrains.anko.backgroundResource

/**
 * Describe:任务执行场景
 *
 * Created By kim
 */
open class OvsScenesExecuteFragment :
    LottieFragment<ScenesFragmentOvsExecuteBinding, ScenesViewModel>() {

    private val executeVM by lazy {
        ViewModelProvider(this).get(
            ExecuteViewModel::class.java
        )
    }

    companion object {
        const val TAG = "ScenesAnimFragment"
        const val DELAY_TIME = 3000

        fun newInstance(consumer: Consumer<Boolean>): OvsScenesExecuteFragment {
            return OvsScenesExecuteFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    override fun getLayoutId() = R.layout.scenes_fragment_ovs_execute

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initView() {
        mBinding.headerLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.headerLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            navigationIcon =null
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        mModel.getLottieAdapter().setNewLottieBgColor(mBinding.lottieBg,mBinding.lottieMaskBg)
    }

    var startMemory = 0f

    private fun getStartMemory(){
        startMemory = ProcessUtil.getAvailMemory()
    }

    private fun getOptimizeMemory(){
        if (startMemory==0f){
            Log.e(TAG, "getOptimizeMemory: 未统计优化前内存")
            return
        }
        val diffRam = startMemory - ProcessUtil.getAvailMemory()
        St.stOptimizeLevelShow("Phone Boost",diffRam.toString())
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        getStartMemory()
        consumer.accept(true)
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        val lottie = mModel.getScenes().getExecuteLottie()
        //适配新动画
        val lottieView =
            mModel.getLottieAdapter().filterLottieView(mBinding.animLottie, mBinding.animLottieNew)
        val fileRaw = lottie?.fileRaw ?: ScenesLottieData.NO_RESOURCE_CODE
        mBinding.ivApp.isVisible = mBinding.animLottie.isVisible
        mModel.getLottieAdapter().doNewLottieAnimationStep(lottieView,lottie,false){
            mModel.startRepeatLottie(lottieView,fileRaw,lottie?.runFrame,consumer)
        }

        executeVM.launch {
            val runningApps = executeVM.getRunningApps()
            if (runningApps.isNullOrEmpty()) {
                consumer.accept(true)
                return@launch
            }
            val delay = DELAY_TIME / runningApps.size
            withContext(Dispatchers.Main) {
                val ivApp = mBinding.ivApp
                val pm: PackageManager = ivApp.context.packageManager
                for (app in runningApps) {
                    val applicationInfo = app.applicationInfo
                    val loadIcon = applicationInfo.loadIcon(pm)
                    Glide.with(ivApp).load(loadIcon).transform(RoundedCorners(SizeUtil.dp2px(5f))).into(ivApp)
                    ProcessUtil.killBackgroundProcesses(app.packageName)
                    delay(delay.toLong())
                }
                mModel.endTaskLottie(lottieView,consumer)
            }
        }
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        getOptimizeMemory()
        consumer.accept(true)
    }

    override fun preFinish(result: Boolean) {
        mConsumer?.accept(result)
    }

}