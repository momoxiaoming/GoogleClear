package com.mckj.baselib.base.databinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mckj.baselib.base.AbstractFragment

/**
 * Describe:BaseFragment
 *
 * Created By yangb on 2020/9/25
 */
abstract class DataBindingFragment<T : ViewDataBinding, VM : AbstractViewModel> :
    AbstractFragment() {

    companion object {
        const val TAG = "DataBindingFragment"
    }

    /**
     * DataBinding
     */
    protected lateinit var mBinding: T

    /**
     * ViewModel
     */
    protected val mModel: VM by lazy { getViewModel() }

    override fun onCreateRootView(inflater: LayoutInflater, container: ViewGroup?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mBinding.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mBinding.lifecycleOwner = viewLifecycleOwner
        initObserver()
        return view
    }

    //弃用屏蔽BaseFragment获取资源方法
    override fun getLayoutRes(): Int {
        return 0
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModel(): VM

    protected open fun initObserver() {
        mModel.isFinish.observe(viewLifecycleOwner, {
            if (it) {
                activity?.finish()
            }
        })
        mModel.mLoadingDialog.observe(viewLifecycleOwner) {
            if (it.isShow) {
                showLoading(it.msg)
            } else {
                hideLoading()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::mBinding.isInitialized) {
            mBinding.unbind()
            mBinding.lifecycleOwner = null
        }
    }

}