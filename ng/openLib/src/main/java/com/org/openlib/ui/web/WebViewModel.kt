package com.org.openlib.ui.web

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.webkit.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.kt.ofActivity
import com.dn.vi.app.base.lifecycle.Resource
import com.dn.vi.app.base.lifecycle.wrapError
import com.dn.vi.app.base.lifecycle.wrapLoading
import com.dn.vi.app.base.lifecycle.wrapSuccess
import com.dn.vi.app.cm.log.VLog
import io.reactivex.rxjava3.functions.Consumer
import org.jetbrains.anko.toast

/**
 * WebViewModel
 * Created by Vito on 2020/7/8.
 **/
class WebViewModel : ViewModel() {

    /**
     * 文件选择
     */
    private val CHOOSE_REQUEST_CODE = 0x9001

    val webState: MutableLiveData<Resource<Boolean>> = MutableLiveData(Resource.Success(true))
    val progressBarProgress: MutableLiveData<Resource<Int>> =
        MutableLiveData(Resource.Error("error"))
    val webUrl: MutableLiveData<Resource<String>> =
        MutableLiveData(Resource.Error("error"))

    val acceptScheme = mutableListOf<String>(
        "http", "https"
    )

    private var uploadFiles: ValueCallback<Array<Uri>>? = null


    fun initWebViewSettings(
        webView: WebView,
        extJs: String?,
        interrupt: WebViewInterrupt?,
        acceptDp: Boolean
    ) {
        val webSettings = webView.settings
        webView.settings.blockNetworkImage = false
        //设置WebView是否使用viewport，当该属性被设置为false时，加载页面的宽度总是适应WebView控件宽度；
        //当被设置为true，当前页面包含viewport属性标签，在标签中指定宽度值生效，如果页面不包含viewport标签，
        //无法提供一个宽度值，这个时候该方法将被使用。
        // webSettings.useWideViewPort = true   // 去掉viewport支持，因为协议页的内容没有加viewport，造成页面没有自动适应

        webSettings.javaScriptEnabled = true
        //设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        //5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        val client: WebViewClient = if (acceptDp) {
            DpWebViewClient(interrupt)
        } else {
            NormalWebViewClient(interrupt)
        }
        webView.webViewClient = client
        webView.setDownloadListener(WebDownloader())

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {

                if (newProgress == 100) {
                    progressBarProgress.postValue(100.wrapSuccess())

                    injectJs(extJs, webView)
                } else {
                    progressBarProgress.postValue(newProgress.wrapLoading())
                }
                super.onProgressChanged(view, newProgress)
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {

                uploadFiles = filePathCallback
                openFileChooseProcess(webView!!.context!!.ofActivity()!!)

                return true
            }
        }
    }

    /**
     * 打开文件选择
     */
    fun openFileChooseProcess(activity: Activity): Boolean {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"

        return try {
            activity.startActivityForResult(
                Intent.createChooser(i, "Choose"),
                CHOOSE_REQUEST_CODE
            )
            true
        } catch (e: Exception) {
            AppMod.app.toast("未找到国片选择器")
            false
        }
    }

    /**
     * 文件选择结果回调反馈
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CHOOSE_REQUEST_CODE -> {
                    uploadFiles?.also {
                        val result =
                            if (data == null || resultCode != Activity.RESULT_OK) null else data.data
                        it.onReceiveValue(arrayOf(result!!))
                    }
                    uploadFiles = null
                }
                else -> {
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            uploadFiles?.also {
                it.onReceiveValue(null)
            }
            uploadFiles = null
        }
    }

    private fun injectJs(extJs: String?, webView: WebView) {
        var extJs = extJs
        if (!extJs.isNullOrEmpty()) {
            if (!extJs.startsWith("javascript")) {
                extJs = "javascript:$extJs"
            }
            webView.evaluateJavascript(extJs,
                ValueCallback<String?> { VLog.d("inject extJs to web, $webUrl") })
        }
    }

    fun setRetryWebUrl(url: String) {
        webUrl.postValue(url.wrapSuccess())
    }

    fun retryLoadUrl(webView: WebView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (webView.webViewClient as? AbstractMCWebViewClient)?.also { ac ->
                ac.setErrorMark(false)
            }
        }
        webUrl.value?.data?.apply {
            webView.loadUrl(this)
        }
    }


    /**
     * 正常页
     */
    private inner class NormalWebViewClient(interrupt: WebViewInterrupt?) :
        AbstractMCWebViewClient(interrupt) {

        override fun onPerformPageFinishedState(view: WebView?, url: String?, error: Boolean) {
            super.onPerformPageFinishedState(view, url, error)
            if (!error) {
                this@WebViewModel.webState.postValue(true.wrapSuccess())
            } else {
                this@WebViewModel.webState.postValue(true.wrapError("error"))
            }
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view ?: return true
            url ?: return true
            val uri = Uri.parse(url)
            val scheme = uri.scheme?.toLowerCase() ?: return false

            if (acceptScheme.contains(scheme)) {
                return false
            }

            return true
        }

    }

    /**
     * 处理deeplink的webview
     */
    private inner class DpWebViewClient(interrupt: WebViewInterrupt?) :
        AbstractMCWebViewClient(interrupt) {

        private inner class DpFailback : Consumer<Uri> {

            /**
             * 处理http web链接
             */
            var handleHttp = false

            override fun accept(url: Uri?) {
                handleHttp = deeplinkTrigger.isHttp(url)
            }

            fun resetHandleState() {
                handleHttp = false
            }

        }

        private val failback = DpFailback()
        private val deeplinkTrigger = DeeplinkTrigger(failback)

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view ?: return true
            url ?: return true

            failback.resetHandleState()
            val acceptCode = deeplinkTrigger.open(url)

            if (
                failback.handleHttp
            ) {
                return super.shouldOverrideUrlLoading(view, url)
            }

            // finish
            // onDeepLinkConsumed.accept(acceptCode)

            return true
        }

    }

    fun webViewGoBackIfCan(webView: WebView?): Boolean {
        var isCanGoBack = false
        if (webView != null) {
            if (webView.canGoBack()) {
                isCanGoBack = true
                webView.goBack()
            }
        }
        return isCanGoBack
    }

}