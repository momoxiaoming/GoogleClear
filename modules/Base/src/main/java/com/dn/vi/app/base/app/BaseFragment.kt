package com.dn.vi.app.base.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ExposeXApp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dn.vi.app.base.helper.RunOnce
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.atomic.AtomicReference

/**
 * Base Fragment。
 * 简化 Fragment的view管理和生命周期
 *
 * 常用路径 :
 *
 * [getLayoutRes] -- [initLayout] -- [onFirstCreated] -- [onDestroyView]
 *
 * Created by holmes on 2020/5/19.
 **/
abstract class BaseFragment : Fragment(), NamedPage {

    /**
     * 扩展属性中使用.
     * [Fragments]
     */
    internal var internalLifeQueue = AtomicReference<Any>()

    /**
     * 绑定Fragment的lifecycle 的 Coroutine Scope
     */
    val scope: CoroutineScope
        get() = lifecycleScope

    /**
     * 绑定Fragment 当前的view的lifecycle 的 Coroutine Scope
     */
    val viewScope: CoroutineScope
        get() = if (view != null) {
            viewLifecycleOwner.lifecycleScope
        } else {
            scope
        }

    /**
     * 显示的view.<br/>
     *
     * 如果要在viewpager中每次出现的话
     * 都初始化view, 请在 #onDestroyView的时候
     * 设置root为空
     */
    var root: View? = null
        protected set

    /**
     * 是否是第一次启动
     */
    protected var firstCreated = false
    protected val onFirstVisibleRunner = FirstVisibleRunner()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 禁用掉fragment的恢复
        if (savedInstanceState != null) {
            savedInstanceState.remove(ExposeXApp.FRAGMENTS_TAG)
        }

        super.onCreate(savedInstanceState)
    }

    override fun getPageName(): String {
        return ""
    }

    /**
     * 设置布局资源
     *
     * @return 如果反回 0, 则会执行 [onCreateRootView]
     */
    protected abstract fun getLayoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root?.also { root ->
            firstCreated = false
            val rootParent = root.parent as? ViewGroup
            rootParent?.removeView(root)
            return root
        }

        val layoutRes = getLayoutRes()
        if (layoutRes > 0) {
            root = inflater.inflate(layoutRes, container, false)
        } else {
            root = onCreateRootView(inflater, container)
        }
        // 第一次生成 rootView
        firstCreated = true
        return root
    }

    /**
     * 在 #getLayoutRes <=0 的时候 ，这个方法会执行
     */
    protected open fun onCreateRootView(inflater: LayoutInflater, container: ViewGroup?): View? {
        return null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beforeInitLayout(firstCreated)
        if (firstCreated) {
            // 是第一次生成view,
            // 所以要初始化view
            initLayout()
        }
    }

    /**
     * 在initLayout之前执行
     */
    protected open fun beforeInitLayout(firstCreated: Boolean) {

    }

    /**
     * 初始化布局
     */
    protected abstract fun initLayout()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onInternalActivityCreated(savedInstanceState)
        // 因为 #firstCreated 这个是在 #onCreateView
        // 里面控制的，所以在这里，是没问题的
        if (firstCreated) {
            // 避免fragment复用，时老是create
            onFirstCreated(savedInstanceState)
        }
    }

    /**
     * 内部activityCreated方法.
     * 比起其它的依赖onActivityCreated的方法，如 onFirstCreated
     * 这个优先级最高, 并且每次都会执行
     * @param savedInstanceState
     */
    protected open fun onInternalActivityCreated(savedInstanceState: Bundle?) {
    }

    /**
     * 第一次生成view后，的生命周期启动。
     * 自己实现的一种机制, #onActivityCreated 的生命周期同级。
     * 主要在viewpager中，不会来会滑动多次执行。
     * 建议将一般性的 #onActivityCreated 的入口代码，移到这个里面.
     * 注, 依赖生成布局时rootView的非null状态.
     * @see .root
     */
    open fun onFirstCreated(savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        internalLifeQueue.get()?.also { wrap ->
            if (wrap is LifecycleCallbackWrap) {
                wrap.onResumed(this)
            }
        }

        onFirstVisibleRunner.run()
    }

    override fun onPause() {
        super.onPause()
        internalLifeQueue.get()?.also { wrap ->
            if (wrap is LifecycleCallbackWrap) {
                wrap.onPaused(this)
            }
        }
    }

    /**
     * 第一次用户可见的时候才运行。
     * 可以配合 viewpager使用，实现可见时加载逻辑。
     */
    open fun onFirstVisible() {

    }

    protected inner class FirstVisibleRunner : RunOnce() {
        override fun running() {
            this@BaseFragment.onFirstVisible()
        }
    }

}