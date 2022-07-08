package com.mckj.sceneslib.manager.network.ap

/**
 * 不同系统 的ap操作适配
 * Created by holmes on 2021/4/23.
 **/
abstract class ApController {

    /**
     * [ApManager.STATE_AP_OFF], [ApManager.STATE_AP_ON]
     */
    abstract fun getApState(): Int

    abstract fun openAp(apInfo: ApManager.ApInfo): ApManager.ApInfo

    abstract fun closeAp()

}