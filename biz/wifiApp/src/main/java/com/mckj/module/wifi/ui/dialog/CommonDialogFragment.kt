package com.mckj.module.wifi.ui.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.dn.vi.app.base.app.UI
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.baselib.util.SizeUtil
import com.mckj.module.wifi.databinding.WifiDialogCommonBinding

/**
 * Describe:共用Dialog
 *
 * Created By yangb on 2020/10/28
 */
class CommonDialogFragment : LightDialogBindingFragment() {

    companion object {
        const val TAG = "CommonDialogFragment"

        @JvmStatic
        fun newInstance(): CommonDialogFragment {
            return CommonDialogFragment()
        }
    }

    override val dialogWindowWidth: Int
        get() = UI.screenWidth - SizeUtil.dp2px(32f)

    private lateinit var mBinding: WifiDialogCommonBinding

    private var mTitle: String = ""
    private var mContent: String = ""
    private var mCancelText: String = ""
    private var mConfirmText: String = ""
    private var mCancelListener: View.OnClickListener? = null
    private var mConfirmListener: View.OnClickListener? = null

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        mBinding = WifiDialogCommonBinding.inflate(inflater, container, false)
        initView()
        return mBinding
    }

    /**
     * 加载公共控件
     */
    private fun initView() {
        mBinding.commonTitleTv.text = mTitle
        mBinding.commonContentTv.text = mContent
        mBinding.commonBtnInclude.cancelBtn.apply {
            setOnClickListener {
                mCancelListener?.onClick(it)
                dismiss()
            }
            text = mCancelText
        }
        mBinding.commonBtnInclude.confirmBtn.apply {
            setOnClickListener {
                mConfirmListener?.onClick(it)
                dismiss()
            }
            text = mConfirmText
        }
        mBinding.commonBtnInclude.dividerBtn.visibility =
            if (mCancelListener == null || mConfirmListener == null) {
                View.GONE
            } else {
                View.VISIBLE
            }
    }

    fun setTitle(text: String): CommonDialogFragment {
        mTitle = text
        return this
    }

    fun setContent(text: String): CommonDialogFragment {
        mContent = text
        return this
    }

    fun setOnCancelListener(listener: View.OnClickListener): CommonDialogFragment {
        return setOnCancelListener("取消", listener)
    }

    fun setOnCancelListener(
        text: String,
        listener: View.OnClickListener
    ): CommonDialogFragment {
        mCancelText = text
        mCancelListener = listener
        return this
    }

    fun setOnConfirmListener(listener: View.OnClickListener): CommonDialogFragment {
        return setOnConfirmListener("确定", listener)
    }

    fun setOnConfirmListener(
        text: String,
        listener: View.OnClickListener
    ): CommonDialogFragment {
        mConfirmText = text
        mConfirmListener = listener
        return this
    }

}