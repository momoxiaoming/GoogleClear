package com.org.openlib.ui.web

import android.content.res.Resources
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * webview 注入服务。
 * 由外部模块实现。
 *
 * 注入控制 webview的加载周期。
 * Created by holmes on 2020/10/21.
 **/
interface WebViewInterruptServer : IProvider {

    /**
     * 生成一个[key]对应interrupt。
     * 如果没相关的interrupt，则抛出NotFoundException
     */
    @Throws(Resources.NotFoundException::class)
    fun createInterrupt(key: String): WebViewInterrupt

}