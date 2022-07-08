package com.org.openlib.ui.web

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * desc   : WebDialogFragment提供的代理
 * author : yibao
 * date   : 2021/5/25
 * version: 1.0
 */
interface WebDialogFragmentProvider : IProvider {
    fun getWebDialogFragment(webURL: String, title: String, extJs: String): DialogFragment?
}