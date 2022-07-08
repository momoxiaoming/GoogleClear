package com.mckj.sceneslib.ui.scenes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.app.kt.resumedContStep
import com.dn.vi.app.base.helper.DataTransport
import com.dn.vi.app.base.helper.StepRunner
import com.mckj.baselib.base.databinding.DataBindingActivity
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesActivityScenesContainerBinding
import com.mckj.sceneslib.entity.ScenesJumpData
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.permission.DPermissionUtils
import com.mckj.sceneslib.ui.scenes.landing.ScenesLandingScrollFragment
import com.mckj.sceneslib.util.Log
import com.org.openlib.utils.FragmentUtil
import kotlin.coroutines.resume

/**
 * Describe:场景容器
 *
 * Created By yangb on 2021/5/31
 */
//@Route(path = ARouterPath.Cleanup.SCENE_CONTAINER)
class ScenesContainerActivity :
    DataBindingActivity<ScenesActivityScenesContainerBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "ScenesContainerActivity"

        private const val BUNDLE_JUMP_ENTITY = "jump_entity"

        /**
         * 启动场景类
         *
         * @param context 上下文
         * @param jumpData ScenesJumpEntity
         */
        fun startActivity(context: Context, jumpData: ScenesJumpData) {
            val intent = Intent(context, ScenesContainerActivity::class.java)
            intent.putExtra(BUNDLE_JUMP_ENTITY, jumpData)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.scenes_activity_scenes_container

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(this, ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    //初始化流程
    override fun initData(savedInstanceState: Bundle?) {
        //获取传递数据
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
                entity = ScenesJumpData(
                    scenes.getData().type,
                    scenes.getGuideTypes(),
                    "level_win",
                    "clean_landing",
                    "home_mfzs"
                )
            }
            val init = mModel.init(entity)
            if (!init) {
                finish()
                return
            }
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
                            this@ScenesContainerActivity,
                            mModel.getScenes().getData().name
                        ) {
                            cont.resume(it)
                        }
                    }
                }

//                resumedContStep(this) { cont ->
//                    val scenes = mModel.getScenes()
//                    if (scenes is QQCleanScenes || (scenes is WechatCleanScenes) || (scenes is WechatSpeedScenes) || (scenes is QQSpeedScenes)) {
//                        scenes.jumpPage(this@ScenesContainerActivity)
//                        finishedAction?.run()
//                    } else {
//                        cont.resume(true)
//                    }
//                }

                resumedContStep(this) { cont ->
                    Log.i(TAG, "initData: 显示动画场景")
                    //显示动画场景
                    mModel.getScenes().addScenesView(
                        this@ScenesContainerActivity,
                        mBinding.containerLayout
                    ) { result ->
                        Log.i(TAG, "initData: 显示动画场景 result:$result")
                        cont.resume(result)
                    }
                    mModel.loadLandingBeforeAd()
                    St.stLevelFlashShow(mModel.enterFlag, mModel.getScenes().getData().name)
                }

                resumedContStep(this) { cont ->
                    Log.i(TAG, "initData: 显示落地页前广告")
                    //显示落地页前广告
                    mModel.showLandingBeforeAd(this@ScenesContainerActivity) { result ->
                        Log.i(TAG, "initData: 显示落地页前广告 result:$result")
                        cont.resume(result)
                    }
                }
                resumedContStep(this) { cont ->
                    Log.i(TAG, "initData: 显示落地页")
                    //显示落地页
                    val name = mModel.getScenes().getData().name
                    St.stLevelShow(mModel.enterFlag, name)
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

    override fun initView() {
        SystemUiUtil.immersiveSystemUi(window)

        this.onBackPressedDispatcher.addCallback(MyBackPressCallback())
    }

    private inner class MyBackPressCallback : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
            //场景动画不允许返回
        }
    }

}