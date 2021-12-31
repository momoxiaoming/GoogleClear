package com.dn.vi.app.base.app


/**
 * 生命周期状态回调
 */
interface LifecycleCallbackWrap {

    /**
     * on [tag] resumed
     */
    fun onResumed(tag: Any?)

    /**
     * on [tag] paused
     */
    fun onPaused(tag: Any?)

}

/**
 * 带生命周期状态，特殊用途数据包装
 * Created by holmes on 2021/6/24.
 **/
abstract class LifecycleWrapData<T> : LifecycleCallbackWrap {

    abstract val data : T

}