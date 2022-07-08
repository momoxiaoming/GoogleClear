package com.org.openlib.ui.web;

import android.content.Context
import android.webkit.WebView
import androidx.lifecycle.LifecycleOwner

/**
 * 注入配置webview
 */
interface WebViewInterrupt {

    /**
     * 配置webview
     * @param context
     * @param owner
     * @param webView 设置相关的属
     */
    fun onConfigureWebView(context: Context, owner: LifecycleOwner, webView: WebView)

    // === 代理 webView 的处理期间 Delegate ===

    /**
     * 代理 [WebViewClient]的[onPageFinished]接口
     * @param view
     * @param url
     */
    fun webViewClientOnPageFinished(view: WebView, url: String )

    // === $ ===

}