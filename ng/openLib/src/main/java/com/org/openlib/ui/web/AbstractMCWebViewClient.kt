package com.org.openlib.ui.web

import android.annotation.TargetApi
import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.org.openlib.ui.web.WebViewInterrupt

/**
 * webview client一些基本处理
 * @param interrupt 全局web处理拦截
 */
abstract class AbstractMCWebViewClient(protected var interrupt: WebViewInterrupt?) :
    WebViewClient() {

    private var isOnWebError = 0

    /**
     * 设置error状态
     */
    fun setErrorMark(err: Boolean) {
        isOnWebError = if (err) {
            1
        } else {
            0
        }
    }

    /**
     * web是否加载出错了
     */
    fun isError(): Boolean {
        return isOnWebError != 0
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        url: String?
    ): Boolean {
        setErrorMark(false)

        view ?: return true
        url ?: return true

        return super.shouldOverrideUrlLoading(view, url)
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        setErrorMark(false)
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        setErrorMark(true)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        view: WebView,
        req: WebResourceRequest,
        rerr: WebResourceError
    ) {
        if (req.isForMainFrame) {
            super.onReceivedError(
                view,
                rerr.errorCode,
                rerr.description.toString(),
                req.url.toString()
            )
            setErrorMark(true)
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view ?: return
        url ?: return

        onPerformPageFinishedState(view, url, isError())

        interrupt?.webViewClientOnPageFinished(view, url)
    }

    /**
     * 处理页面加载完成后的状态
     * @param error 是否加载出错了
     */
    protected open fun onPerformPageFinishedState(view: WebView?, url: String?, error: Boolean) {

    }

}