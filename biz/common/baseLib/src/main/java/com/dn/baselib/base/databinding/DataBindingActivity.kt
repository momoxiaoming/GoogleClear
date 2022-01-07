package com.dn.baselib.base.databinding

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dn.baselib.base.AbstractActivity

/**
 * Describe:DataBindingActivity
 *
 * Created By yangb on 2020/9/25
 */

abstract class DataBindingActivity<T : ViewDataBinding, VM : AbstractViewModel> : AbstractActivity() {

    companion object {
        const val TAG = "BaseActivity"
    }

    /**
     * DataBinding
     */
    protected lateinit var mBinding: T

    /**
     * ViewModel
     */
    protected val mModel: VM by lazy { getViewModel() }

    override fun loadView() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this
        initObserver()
    }

    protected abstract fun getViewModel(): VM

    open fun initObserver() {
        mModel.isFinish.observe(this, {
            if (it) {
                goBack()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

}