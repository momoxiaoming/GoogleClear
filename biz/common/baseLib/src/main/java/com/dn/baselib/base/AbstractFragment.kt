package com.dn.baselib.base

import com.dn.vi.app.base.app.ViFragment
import com.dn.baselib.ui.view.loading.LoadingDialog

/**
 * Describe:BaseFragment
 *
 * Created By yangb on 2020/9/25
 */
abstract class AbstractFragment : ViFragment() {

    /**
     * LoadingDialog
     */
    private val mLoadingDialog by lazy { LoadingDialog(requireContext()) }

    override fun onFirstVisible() {
        initData()
        initView()
    }

    override fun initLayout() {
    }

    protected abstract fun initData()

    protected abstract fun initView()

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        onFirstVisibleRunner.run()
    }

    /**
     * 显示loading
     */
    fun showLoading(msg: String = "") {
        mLoadingDialog.setMessage(msg)
        if (!mLoadingDialog.isShowing) {
            mLoadingDialog.show()
        }
    }

    /**
     * 隐藏loading
     */
    fun hideLoading() {
        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }

}