package com.mckj.sceneslib.ui.scenes.model.envelopetest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.app.kt.resumedContStep
import com.dn.vi.app.base.helper.StepRunner
import com.mckj.baselib.base.databinding.DataBindingActivity
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesActivityEnvelopeTestBinding
import com.mckj.sceneslib.entity.ScenesJumpData
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.util.Log
import kotlin.coroutines.resume

class EnvelopeTestActivity :
    DataBindingActivity<ScenesActivityEnvelopeTestBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "EnvelopeTestActivity"

        private const val BUNDLE_JUMP_ENTITY = "jump_entity"

        /**
         * 启动场景类
         *
         * @param context 上下文
         * @param jumpData ScenesJumpEntity
         */
        fun startActivity(context: Context, entity: ScenesJumpData) {
            val intent = Intent(context, EnvelopeTestActivity::class.java)
            intent.putExtra(BUNDLE_JUMP_ENTITY, entity)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(this, ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.scenes_activity_envelope_test
    }

    override fun initView() {
        SystemUiUtil.immersiveSystemUi(window)
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
                    this@EnvelopeTestActivity,
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
                mModel.showLandingBeforeAd(this@EnvelopeTestActivity) { result ->
                    Log.i(TAG, "initData: 显示落地页前广告 result:$result")
                    cont.resume(result)
                }
            }
        }
    }


}