package com.dn.vi.app.base.app

import com.dn.vi.app.base.arch.gmvp.LifecycleView
import com.dn.vi.app.base.lifecycle.RxLifecycleDelegate

/**
 * 对base的进一步扩展
 * Created by holmes on 2020/5/20.
 **/
abstract class ViFragment : BaseFragment(), LifecycleView {

    private val rxLifecycle: RxLifecycleDelegate by lazy { RxLifecycleDelegate(this) }

    /**
     * 直接只关联 Destroy
     *
     * [RxLifecycleDelegate]
     */
    override fun <T> bindUntilDestroy(): RxLifecycleDelegate.LifecycleTransformer<T> {
        return rxLifecycle.bindUntilDestroy()
    }

    /**
     * 获取当前activity，并尝试转换为[T]
     * @param withActivity 如果转换成功，则会在 [T]的作用域下执行[withActivity]
     */
    inline fun <reified T> activityAs(withActivity: T.() -> Unit): T? {
        return (activity as? T).also {
            it?.withActivity()
        }
    }

}