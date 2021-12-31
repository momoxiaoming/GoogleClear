package com.mckj.module.wifi.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import com.dn.vi.app.base.app.kt.getAs
import com.dn.vi.app.base.app.kt.transportData
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.sceneslib.manager.network.NetworkState
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.baselib.helper.showToast
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiDialogInputPasswordBinding
import com.mckj.module.wifi.gen.St
import com.mckj.module.wifi.ui.dialog.connecting.ConnectingDialogFragment
import com.mckj.module.wifi.utils.KeyboardUtil
import com.mckj.module.wifi.utils.Log
import com.mckj.openlib.helper.onceClick
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.jetbrains.anko.imageResource

/**
 * Describe:输入密码连接
 *
 * Created By yangb on 2020/10/15
 */
class InputPasswordDialogFragment() : LightDialogBindingFragment() {

    companion object {
        const val TAG = "InputPasswordDialog"

        @JvmStatic
        fun newInstance(wifiInfo: WifiInfo): InputPasswordDialogFragment {
            transportData {
                put("wifi_info", wifiInfo)
            }
            return InputPasswordDialogFragment()
        }

    }

    private val mHandler = Handler()
    private lateinit var mBinding: WifiDialogInputPasswordBinding

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        mBinding = WifiDialogInputPasswordBinding.inflate(inflater, container, false)
        return mBinding
    }

    var mWifiInfo: ObservableField<WifiInfo> = ObservableField<WifiInfo>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        transportData {
            val wifiInfo = getAs<WifiInfo>("wifi_info")
            mWifiInfo.set(wifiInfo)
        }
        if (mWifiInfo.get() == null) {
            dismissAllowingStateLoss()
            return
        }
        initView()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.let {
            it.setCanceledOnTouchOutside(false)
        }
        return dialog
    }

    private fun initView() {
        St.stWifilistNopopShow()
        mBinding.fragment = this
        mBinding.inputPasswordConfirmTv.isEnabled = false

        mBinding.inputPasswordTipTv.visibility = View.GONE
        mBinding.inputPasswordShowIv.onceClick {
            if (mBinding.inputPasswordNameEt.transformationMethod == PasswordTransformationMethod.getInstance()) {
                mBinding.inputPasswordNameEt.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                mBinding.inputPasswordShowIv.imageResource = R.drawable.wifi_icon_input_visible

            } else {
                mBinding.inputPasswordNameEt.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                mBinding.inputPasswordShowIv.imageResource = R.drawable.wifi_icon_input_invisible
            }
            mBinding.inputPasswordNameEt.setSelection(mBinding.inputPasswordNameEt.text.length)//将光标移至文字末尾
        }

        mBinding.inputPasswordNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mBinding.inputPasswordConfirmTv.isEnabled = s?.length ?: 0 >= 8
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        mBinding.inputPasswordCancelTv.onceClick {
            St.stWifilistNopopCloseClick()
            dismiss()
        }
        mBinding.inputPasswordConfirmTv.onceClick {
            St.stWifilistNopopGetClick()
            val wifiInfo = mWifiInfo.get()
            val password = mBinding.inputPasswordNameEt.text.toString()
            if (wifiInfo == null) {
                showToast("密码连接失败:WiFi信息异常")
                dismissAllowingStateLoss()
                return@onceClick
            }
            ConnectingDialogFragment.newInstance(wifiInfo, password)
                .rxShow(parentFragmentManager, ConnectingDialogFragment.TAG)
                .last(NetworkState.UNKNOWN.ordinal)
                .subscribeBy {
                    Log.i(TAG, "bindView: it:${it}")
                    when (it) {
                        NetworkState.CONNECTED.ordinal -> {
                            dispatchButtonClickObserver(it)
                            dismiss()
                        }
                        NetworkState.ERROR_AUTHENTICATING.ordinal -> {
                            mBinding.inputPasswordTipTv.visibility = View.VISIBLE
                            mBinding.inputPasswordTipTv.text = "连接失败"
                        }
                        NetworkState.ERROR_TIMEOUT.ordinal -> {
                            mBinding.inputPasswordTipTv.visibility = View.VISIBLE
                            mBinding.inputPasswordTipTv.text = "连接超时"
                        }
                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(Runnable {
            KeyboardUtil.showSoftInput(mBinding.inputPasswordNameEt)
        }, 500)
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacksAndMessages(null)
    }

}