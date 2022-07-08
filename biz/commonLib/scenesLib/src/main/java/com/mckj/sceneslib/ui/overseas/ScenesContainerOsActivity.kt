package com.mckj.sceneslib.ui.overseas

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.base.app.kt.resumedContStep
import com.dn.vi.app.base.helper.DataTransport
import com.dn.vi.app.base.helper.StepRunner
import com.mckj.baselib.base.databinding.DataBindingActivity
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ActivityScenesContainerOsBinding
import com.mckj.sceneslib.entity.ScenesJumpData
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesStep
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.permission.DPermissionUtils
import com.mckj.sceneslib.ui.overseas.dialog.ScenesStopDialogFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.landing.ScenesLandingScrollFragment
import com.mckj.sceneslib.util.Log
import com.org.openlib.utils.FragmentUtil
import com.org.openlib.utils.SystemUiUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlin.coroutines.resume

/**
 * 海外场景容器
 */
@Route(path = ARouterPath.Cleanup.SCENE_CONTAINER)
class ScenesContainerOsActivity :
    DataBindingActivity<ActivityScenesContainerOsBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "ScenesContainerOsActivity"

        private const val BUNDLE_JUMP_ENTITY = "jump_entity"

        /**
         * 启动场景类
         *
         * @param context 上下文
         * @param jumpData ScenesJumpEntity
         */
        fun startActivity(context: Context, jumpData: ScenesJumpData) {
            val intent = Intent(context, ScenesContainerOsActivity::class.java)
            intent.putExtra(BUNDLE_JUMP_ENTITY, jumpData)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            }
            context.startActivity(intent)
//            val isForeground = ProcessUtil.isRunningForeground()
//            if (isForeground) {
//                context.startActivity(intent)
//            } else {
//                DSdkHelper.jumpActivityForHome(context, intent)
//            }
        }
    }

    private var exitTipsDialog: ScenesStopDialogFragment? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_scenes_container_os
    }

    override fun initData(savedInstanceState: Bundle?) {
        var type: String? = null
        DataTransport.getInstance().get("type")?.let {
            type = it as String
        }
        try {
            val entity: ScenesJumpData?
            if (type.isNullOrEmpty()) {
                entity = intent.getParcelableExtra(BUNDLE_JUMP_ENTITY)
            } else {
                //外跳内
                val sceneType = mModel.ensureSceneRegister(type?.toInt() ?: 0)
                val scenes = ScenesManager.getInstance().getScenes(sceneType)
                if (scenes == null) {
                    finish()
                    return
                }
                entity = scenes.getScenesJumpData()
            }
            val init = mModel.init(entity)
            if (!init) {
                finish()
                return
            }
            initOnBackPress()
            initOutJumpStatus()
            StepRunner.runner(scope) {
                finishedAction = Runnable {
                    Log.i(TAG, "initData: finishedAction")
                    mModel.isFinish.value = true
                }
                breakAction = finishedAction

                resumedContStep(this) { cont ->
                    if (!mModel.getScenes().isRequestPermission()) {
                        cont.resume(true)
                    } else {
                        DPermissionUtils.requestFileScanPermission(
                            this@ScenesContainerOsActivity,
                            mModel.getScenes().getData().name
                        ) {
                            cont.resume(it)
                        }
                    }
                }

//                resumedContStep(this) { cont ->
//                    val scenes = mModel.getScenes()
//                    if (scenes is QQCleanScenes || (scenes is WechatCleanScenes) || (scenes is WechatSpeedScenes) || (scenes is QQSpeedScenes)) {
//                        scenes.jumpPage(this@ScenesContainerOsActivity)
//                        finishedAction?.run()
//                    } else {
//                        cont.resume(true)
//                    }
//                }

                resumedContStep(this) { cont ->
                    mModel.getScenes().scenesStep = ScenesStep.Scan
                    Log.i(TAG, "initData: 显示扫描场景")
                    mModel.loadLandingBeforeAd()
                    val name = mModel.getScenes().getData().name
                    //显示动画场景
                    mModel.getScenes().addScenesView(
                        this@ScenesContainerOsActivity,
                        mBinding.containerLayout
                    ) { result ->
                        Log.i(TAG, "initData: 扫描场景 result:$result")
                        cont.resume(result)
                    }
                }

                resumedContStep(this) { cont ->
                    mModel.getScenes().scenesStep = ScenesStep.Execute
                    Log.i(TAG, "initData: 显示优化场景")
                    //显示优化场景
                    mModel.getScenes().addExecuteScenesFragment(
                        this@ScenesContainerOsActivity,
                        mBinding.containerLayout
                    ) { result ->
                        Log.i(TAG, "initData: 优化场景 result:$result")
                        cont.resume(result)
                    }
                }

                resumedContStep(this) { cont ->
                    mModel.getScenes().scenesStep = ScenesStep.End
                    exitTipsDialog?.dismiss()
                    Log.i(TAG, "initData: 显示完成场景")
                    //显示完成场景
                    mModel.getScenes().addEndScenesFragment(
                        this@ScenesContainerOsActivity,
                        mBinding.containerLayout
                    ) { result ->
                        Log.i(TAG, "initData: 完成场景 result:$result")
                        cont.resume(result)
                    }
                }


                resumedContStep(this) { cont ->
                    mModel.getScenes().scenesStep = ScenesStep.AD

                    Log.i(TAG, "initData: 显示落地页前广告")
                    //显示落地页前广告
                    mModel.showLandingBeforeAd(this@ScenesContainerOsActivity) { result ->
                        Log.i(TAG, "initData: 显示落地页前广告 result:$result")
                        cont.resume(result)
                    }
                }

                resumedContStep(this) { cont ->
                    mModel.getScenes().scenesStep = ScenesStep.Landing
                    val name = mModel.getScenes().getData().name
                    St.stLevelShow(mModel.enterFlag, name)
                    Log.i(TAG, "initData: 显示落地页")
                    //显示落地页
                    val fragment = ScenesLandingScrollFragment.newInstance { result ->
                        Log.i(TAG, "initData: 显示落地页 result:$result")
                        cont.resume(result)
                    }
                    FragmentUtil.show(supportFragmentManager, fragment, R.id.container_layout)
                }
            }
        } catch (e: Exception) {

        }
    }

    private fun initOnBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            val scenesStep = mModel.getScenes().scenesStep
            val scenesType = mModel.getScenesData().type
            if (scenesStep == ScenesStep.Scan) {
                if (scenesType == ScenesType.TYPE_JUNK_CLEAN) {
                    //  showTipsDialog()
                }
            } else if (scenesStep >= ScenesStep.Landing) {
                //   navigationToHome()
                St.stLevelReturnClick(mModel.getScenesData().name)
            }
        }
    }

    private fun initOutJumpStatus() {
        mModel.enterFlag = mModel.getScenes().enterFlag
        mModel.getScenes().enterFlag = "in"
    }

    override fun initView() {
        SystemUiUtil.immersiveSystemUi(window)
    }

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(this).get(
            ScenesViewModel::class.java
        )
    }


    private fun showTipsDialog() {
        val name = mModel.getScenes().getData().name
        St.stExitConfirmPopupShow(name)
        exitTipsDialog = ScenesStopDialogFragment.newInstance()
        exitTipsDialog?.apply {
            isCancelable = false
            rxShow(supportFragmentManager, "showAppTips")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy { code ->
                    when (code) {
                        DialogInterface.BUTTON_NEGATIVE -> {
                            navigationToHome()
                            St.stExitConfirmPopupClick(name, "Stop")
                        }
                        DialogInterface.BUTTON_POSITIVE -> {
                            //继续
                            St.stExitConfirmPopupClick(name, "Keep")
                        }
                    }
                }
        }
    }

    private fun navigationToHome() {
        ARouter.getInstance().build("/app/main").navigation()
        finish()
    }

}