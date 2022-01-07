package com.mckj.sceneslib.ui.scenes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.app.kt.resumedContStep
import com.dn.vi.app.base.helper.StepRunner
import com.dn.baselib.base.databinding.DataBindingActivity
import com.dn.openlib.utils.FragmentUtil
import com.dn.openlib.utils.SystemUiUtil

import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesActivityScenesContainerBinding
import com.mckj.sceneslib.entity.ScenesJumpData
import com.mckj.sceneslib.ui.scenes.landing.ScenesLandingFragment
import com.mckj.sceneslib.util.Log
import kotlin.coroutines.resume

/**
 * Describe:场景容器
 *
 * Created By yangb on 2021/5/31
 */
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
            if(context !is Activity){
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

    override fun initData(savedInstanceState: Bundle?) {
        //获取传递数据
        val entity: ScenesJumpData? = intent.getParcelableExtra(BUNDLE_JUMP_ENTITY)
        mModel.init(entity)

        StepRunner.runner(scope) {
            finishedAction = Runnable {
                Log.i(TAG, "initData: finishedAction")
                mModel.isFinish.value = true
            }
            breakAction = finishedAction

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
            }

            resumedContStep(this) { cont ->
                Log.i(TAG, "initData: 显示落地页前广告")
                //显示落地页前广告
                mModel.showLandingBeforeAd(supportFragmentManager) { result ->
                    Log.i(TAG, "initData: 显示落地页前广告 result:$result")
                    cont.resume(result)
                }
            }

            resumedContStep(this) { cont ->
                Log.i(TAG, "initData: 显示落地页")
                //显示落地页
                val fragment = ScenesLandingFragment.newInstance { result ->
                    Log.i(TAG, "initData: 显示落地页 result:$result")
                    cont.resume(result)
                }
                FragmentUtil.show(supportFragmentManager, fragment, R.id.container_layout)
                mModel.loadLandingAfterAd()
            }

            resumedContStep(this) { cont ->
                Log.i(TAG, "initData: 显示落地页后广告")
                //显示落地页后广告
                mModel.showLandingAfterAd(supportFragmentManager) { result ->
                    Log.i(TAG, "initData: 显示落地页后广告 result:$result")
                    cont.resume(result)
                }
            }
        }
    }

    override fun initView() {
        SystemUiUtil.immersiveSystemUi(window)
    }

}