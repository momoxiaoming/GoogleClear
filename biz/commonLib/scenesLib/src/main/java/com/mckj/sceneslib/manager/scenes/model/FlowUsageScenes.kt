package com.mckj.sceneslib.manager.scenes.model

import android.content.Context
import com.mckj.baselib.util.Log


import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.SystemJumpManager
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:
 *
 * Created By leix on 2021/07/19
 */
open class FlowUsageScenes : AbstractScenes() {
    companion object {
        const val TAG = "FlowUsageScenes"
    }
    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_FLOW_USAGE,
            "flow_usage",
            "流量使用",
            "避免流量不够用",
            "",
            "",
            "",
            "",
            "null"
        )
    }

    override fun jumpPage(context: Context, invoke: ((accept: Boolean) -> Unit)?): Boolean {

        openFlow(context)
        handleClickEvent()
        return true
    }

    private fun openFlow(context: Context) {
        val result = SystemJumpManager.getInstance().getJumpPage().jumpNetData(context)
        Log.i(TAG, "openFlow: result:$result")
    }
}