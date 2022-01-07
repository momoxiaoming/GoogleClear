package com.mckj.sceneslib.data.model.impl

import com.dn.baselib.util.ProcessUtil
import com.mckj.sceneslib.data.model.IRamData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Describe:
 *
 * Created By yangb on 2020/10/22
 */
class RamDataImpl : IRamData {

    override suspend fun killAllProcessesToPercent(): Float {
        return withContext(Dispatchers.IO) {
            delay(2000)
            ProcessUtil.killAllProcessesToPercent()
        }
    }

}