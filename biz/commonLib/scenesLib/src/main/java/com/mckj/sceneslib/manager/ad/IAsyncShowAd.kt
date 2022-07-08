package com.mckj.sceneslib.manager.ad

import com.org.openlib.help.Consumer


/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.manager.ad
 * @data  2022/5/24 15:51
 */
interface IAsyncShowAd {

    fun getScenesType(): Int=-1

    fun getAnimaState(): Boolean

    fun getStepConsumer(): Consumer<Boolean>?

}