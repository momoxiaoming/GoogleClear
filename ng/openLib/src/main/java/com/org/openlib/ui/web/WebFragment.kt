package com.org.openlib.ui.web

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.DatabindingFragment
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.base.app.kt.arouter
import com.dn.vi.app.base.app.kt.peek
import com.dn.vi.app.base.helper.DataTransport
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.repo.kv.KvSp
import com.org.openlib.databinding.PiLayoutWebBinding
import com.org.openlib.path.ARouterPath
import kotlinx.coroutines.launch
import me.jessyan.autosize.utils.ScreenUtils
import java.util.concurrent.atomic.AtomicBoolean

/**
 * WebFragment
 * 处理网页打开
 * Created by Vito on 2020/7/8.
 **/
@Route(path = "/pipe/sense/web")
class WebFragment : DatabindingFragment<PiLayoutWebBinding>() {

    @JvmField
    var webUrl: String = ""

    @JvmField
    var extJs: String = ""

    /**
     * 如果为 "lock",则应用web嵌新闻
     */
    @JvmField
    var contentKey: String = "lock"

    /**
     * 是否开启deeplink功能
     */
    @JvmField
    var acceptDp: Boolean = false

    /**
     * 滚动标志
     */
    private var scrollTag=AtomicBoolean(false)

    /**
     * webview
     */
    var webViewInterruptServer: WebViewInterruptServer? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
            if (!vm.webViewGoBackIfCan(binding.webView)) {
                this.isEnabled = false
                // back activity
                VLog.d("web is root url back stack. so back activity")
                activityAs<ViActivity> {
                    // 释放这次main thread
                    this.scope.launch {
                        goBack()
                    }
                }
            }
        }
    }

    private val vm: WebViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        ).get(WebViewModel::class.java)
    }

    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PiLayoutWebBinding {
        return PiLayoutWebBinding.inflate(inflater, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val req = DataTransport.getInstance().peek<WebHelper.Request>(WebHelper.EXTRA_WEB_REQ2)
        if (req == null) {
            return
        }
        req.inject(this)

        (arouter().build(ARouterPath.SRV_WEBVIEW_INTERRUPT)
            .navigation() as? WebViewInterruptServer)?.also {
            webViewInterruptServer = it
        }

        binding.vm = vm

        if (contentKey.isNullOrEmpty()) {
            contentKey = "lock"
        }
        val interrupt = webViewInterruptServer?.createInterrupt(contentKey)
        vm.initWebViewSettings(binding.webView, extJs, interrupt, acceptDp)
        interrupt?.onConfigureWebView(requireActivity(), requireActivity(), binding.webView)
        vm.setRetryWebUrl(webUrl)

        binding.retryBtn.setOnClickListener {
            vm.retryLoadUrl(binding.webView)
        }
        binding.webView.loadUrl(webUrl)
        listenerWebViewScroll()
    }

    override fun onFirstCreated(savedInstanceState: Bundle?) {
        super.onFirstCreated(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            binding.webView.stopLoading()
            binding.webView.destroy()
        } catch (e: Exception) {
        }
    }

    /**
     * 监听webview滚动
     */
    private fun listenerWebViewScroll(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.webView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if(!scrollTag.get()){
                    val height = ScreenUtils.getScreenSize(activity)[1]*2/3
                    if (scrollY>=height){
                        scrollTag.set(true)
                    }
                }
            }
        }
    }

}