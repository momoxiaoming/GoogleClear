package com.dn.vi.app.base.app

import android.os.Bundle
import android.view.Choreographer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

/**
 * 使用 dataBinding 的fragment
 * Created by holmes on 2020/5/20.
 **/
abstract class DatabindingFragment<T : ViewDataBinding> : ViFragment() {

    lateinit var binding: T

    /**
     * [binding]是否已经赋值初始化过。
     * （这里不是好的方式，只能临时处理一下一些问题）
     *
     * 因为外部分可能有些生命周期函数里面会调用binding，
     * 但binding又依赖生命周期流程。如果有可能跨生命周期里的调用，可能就因为
     * binding未初始化，而挂了。
     * 如： 没走 [onCreateView] 而直接走了 [onDetach], 然后在onDetach里面
     * 又有binding的调用，就会挂了
     *
     */
    val bindingInited: Boolean
        get() = this::binding.isInitialized

    override fun getLayoutRes(): Int = 0

    override fun initLayout() {
    }

    override fun onCreateRootView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = onCreateDatabinding(inflater, container)
        return binding.root
    }

    abstract fun onCreateDatabinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onResume() {
        super.onResume()
        binding.executePendingBindings()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindDataLifecycle()
    }

    private fun bindDataLifecycle() {
        Choreographer.getInstance().postFrameCallback {
            binding.lifecycleOwner = this
            onDataLifecycleBound()
        }
    }

    protected open fun onDataLifecycleBound() {}

    override fun onDestroyView() {
        super.onDestroyView()
        binding.apply {
            unbind()
            // 如果在设置Databinding的数据之前设置 Lifecycle
            // 可能会卡死在 ViewDataBinding.requestRebind
            // 因为里面的 mPendingRebind为true, 但Lifecycle可能还没有start
            // 会造成 mRebindRunnable 无法执行
            setLifecycleOwner(null)
        }
        // currentBinding = null
    }

}