package com.mckj.sceneslib.manager.scenes.model

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.entity.ScenesData

/**
 * Describe:红包赚钱
 *
 * Created By yangb on 2021/3/24
 */
class LotteryScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_LOTTERY,
            "lottery",
            "幸运抽手机",
            "iPhone12等你来拿",
            "",
            "立即参与",
            "",
            ""
        )
    }

    override fun jumpPage(context: Context): Boolean {
//        val intent = Intent(MessageAction.ACTION_LOTTERY_H5)
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
//        handleClickEvent()
        return true
    }

}