package com.mckj.baselib.base

import android.os.Bundle
import android.view.KeyEvent
import com.dn.vi.app.base.app.ViActivity
import com.mckj.baselib.view.loading.LoadingDialog

/**
 * Describe:BaseActivity
 *
 * Created By yangb on 2020/10/15
 */
abstract class AbstractActivity : ViActivity() {

    /**
     * LoadingDialog
     */
    private val mLoadingDialog by lazy { LoadingDialog(this) }

    override fun onInternalCreate(savedInstanceState: Bundle?) {
        super.onInternalCreate(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun initLayout() {
        loadView()
        initView()
    }

    protected open fun loadView() {
        setContentView(getLayoutId())
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initData(savedInstanceState: Bundle?)

    protected abstract fun initView()

    /**
     * 显示loading
     */
    fun showLoading(msg: String? = "") {
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

    fun setCanceledOnTouchOutside(enable: Boolean) {
        mLoadingDialog.setOnKeyListener { _, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount === 0 }
        mLoadingDialog.setCanceledOnTouchOutside(enable)
    }
}