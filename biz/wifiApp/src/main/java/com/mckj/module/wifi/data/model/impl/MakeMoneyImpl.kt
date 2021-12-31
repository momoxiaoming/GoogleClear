package com.mckj.module.wifi.data.model.impl

import com.mckj.module.wifi.data.model.IMakeMoney
import com.mckj.sceneslib.util.NetUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Describe:
 *
 * Created By yangb on 2021/5/19
 */
class MakeMoneyImpl : IMakeMoney {

    override suspend fun requestEnvelope(): Boolean {
        return withContext(Dispatchers.IO) {
            val random = (1..3).random()
            NetUtil.pingEnable(random)
        }
    }

}