package com.org.openlib.ui.web

import android.content.Context
import android.util.Log
import com.dn.vi.app.base.app.ViFragment
import com.dn.vi.app.base.app.kt.arouter
import com.dn.vi.app.base.app.kt.arouterCtx
import com.dn.vi.app.base.app.kt.transportData
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.org.openlib.path.ARouterPath

/**
 * 兼容web触发
 *
 * ```
 * val req = DataTransport.getInstance().getAs<WebHelper.Request>(WebHelper.EXTRA_WEB_REQ)
 * if (req == null) {
 * finish()
 * return
 * }
 * req.inject(this)
 * ```
 *
 * Created by holmes on 2021/5/19.
 **/
object WebHelper {

    const val EXTRA_WEB_REQ = "web.req"
    internal const val EXTRA_WEB_REQ2 = "web.req2"

    /*
    .withString("webUrl", uri.toString())
    .withString("title", "")
    .withString("contentKey", "apiAd")
    .withBoolean("acceptDeeplink", true)
     */

    data class Request(
        var webUrl: String,
        var title: String = "",
        var contentKey: String = "",
        var extJs: String = "",
        var acceptDeeplink: Boolean = false,
    ) {

        fun inject(activity: WebActivity) {
            activity.webUrl = this.webUrl
            activity.title = this.title
            activity.extJs = this.extJs
            activity.contentKey = this.contentKey
            activity.acceptDp = this.acceptDeeplink
        }

        fun inject(f: WebToolBarFragment) {
            f.title = this.title
        }

        fun inject(f: WebFragment) {
            f.webUrl = this.webUrl
            f.extJs = this.extJs
            f.contentKey = this.contentKey
            f.acceptDp = this.acceptDeeplink
        }

    }


    @JvmStatic
    fun openWeb(context: Context?, req: Request) {
        transportData {
            put(EXTRA_WEB_REQ, req)
        }
        VLog.d("webHelper","open ${req.webUrl}")
        if (context != null) {
            context.arouterCtx()
                .navigation(ARouterPath.PAGE_WEB) {}
        } else {
            arouter().build(ARouterPath.PAGE_WEB)
                .navigation()
        }
    }

    /**
     * 生成一个打开web页的dialog
     * 8s 内必须使用，否者被销毁
     */
    @JvmStatic
    fun buildWebDialog(context: Context?, req: Request): LightDialogBindingFragment? {
        val dialogFragment = arouter().build("/open/dialog/web")
            .withString("webUrl", req.webUrl)
            .withString("title", req.title)
            .also {
                req.extJs.also { js ->
                    if (js.isNotEmpty()) {
                        it.withString("extJs", req.extJs)
                    }
                }
            }
            .navigation() as? LightDialogBindingFragment
        return dialogFragment
    }

    /**
     * 生成一个可以嵌入的web页
     * 8s 内必须使用，否者被销毁
     */
    @JvmStatic
    fun buildWebFragment(context: Context?, req: Request): ViFragment {
        transportData {
            put(EXTRA_WEB_REQ2, req)
        }
        val fragmentWeb = arouter().build("/pipe/sense/web")
            .navigation() as ViFragment
        return fragmentWeb
    }

}