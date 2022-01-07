package com.dn.baselib.base.databinding

import androidx.databinding.ViewDataBinding
import com.dn.baselib.ext.IE
import com.dn.baselib.ui.view.loading.LoadingItem
import com.dn.baselib.ui.view.loading.DefaultLoadDialog
import com.dn.baselib.ui.view.loading.ILoadDialog


/**
 * 带加载状态的fragment
 * @author mmxm
 * @date 2021/6/18 12:13
 */
abstract class LoadDataBindingFragment<T : ViewDataBinding, VM : AbsViewModel> :
    DataBindingFragment<T, VM>() {

    private val loadDialog: ILoadDialog by lazy {
        createDialog()
    }

    /**
     * 创建加载dialog 可重写此方法实现自己别样的加载dialog
     * @return Dialog
     */
    open fun createDialog(): ILoadDialog {
        return DefaultLoadDialog(requireActivity())
    }

    private fun updateDialog(loadingItem: LoadingItem) {
        loadingItem.isShow.IE({
            //设置图形
            loadDialog.setMessage(loadingItem.msg)
            loadDialog.iShow()
        }, { loadDialog.iDismiss() })

    }

    override fun initObserver() {
        mModel.loadingData.observe(this, {
            updateDialog(it)
        })
        mModel.pageFinishData.observe(this, {
            if (it) {
                requireActivity().finish()
            }
        })
    }

}