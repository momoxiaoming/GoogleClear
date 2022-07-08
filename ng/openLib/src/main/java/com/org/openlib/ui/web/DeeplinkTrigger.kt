package com.org.openlib.ui.web

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.log.VLog
import io.reactivex.rxjava3.functions.Consumer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

/**
 *
 * 处理deep link的跳转
 * @param failback 在没有跳转成功的时候，会执行failback(uri)
 *
 * Created by holmes on 2020/11/2.
 **/
class DeeplinkTrigger(private val failback: Consumer<Uri>) {

    companion object {
        /**
         * 成功
         */
        const val RESULT_OK = 0

        /**
         * 加载失败
         */
        const val RESULT_ERROR = 1

        /**
         * 没有找到对应的app
         */
        const val RESULT_NOT_FOUND = 2
    }

    /**
     * failback里面什么也不做
     */
    class NoneFailback : Consumer<Uri> {
        override fun accept(t: Uri?) { // do nothing
        }
    }

    /**
     * 默认的用web页打开failback
     */
    class DefaultWebFailback : Consumer<Uri> {
        override fun accept(url: Uri?) {
            url ?: return // open webview

            WebHelper.openWeb(
                null, WebHelper.Request(
                    webUrl = url.toString(),
                    title = "",
                )
            )

            VLog.scoped("dp").w("open url with web")
        }

    }

    var context: Context? = AppMod.app

    val log: VLog.Logger
        get() = VLog.scoped("dp")

    /**
     * 打开deeplink。
     * 如果无法打开跳转，会走[failback]。
     *
     * @return 跳转了deeplink则为true, 否则为false
     */
    fun open(url: String,context: Context? = null): Int {
        if (url.isNullOrEmpty()) {
            return RESULT_ERROR
        }
        val uri: Uri = try {
            Uri.parse(url)
        } catch (e: Exception) {
            log.e("parse url error. ${url}")
            return RESULT_ERROR
        }
        log.i("open deeplink url: ${url}")

        var result = RESULT_ERROR
        if (!uri.scheme.equals("http", ignoreCase = true) && !uri.scheme.equals(
                "https",
                ignoreCase = true
            )
        ) { // open schema
            if (context!=null){
                this.context = WeakReference(context).get()
            }
            this.context?.apply {
                val viewIntent = Intent(Intent.ACTION_VIEW)
                if (this !is Activity) {
                    viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                viewIntent.addCategory(Intent.CATEGORY_DEFAULT)
                viewIntent.addCategory(Intent.CATEGORY_BROWSABLE)
                viewIntent.setData(uri)

                try {
                    startActivity(viewIntent)
                    result = RESULT_OK
                } catch (e: Exception) {
                    result = RESULT_NOT_FOUND
                    log.w("no deepLink found. ${url}")
                }
            }
        }

        if (result != RESULT_OK) {
            failback.accept(uri)
        }

        return result
    }

    fun isHttp(uri: Uri?): Boolean {
        uri ?: return false
        return uri.scheme.equals("http", ignoreCase = true) || uri.scheme.equals(
            "https",
            ignoreCase = true
        )
    }

    /**
     * 打开市场链接
     * @return true: 打开成功
     */
    suspend fun openMarket(marketPkg: String, jumpId: String): Boolean {

        val hasPkg = withContext(Dispatchers.IO) {
            isPackageInstalled(marketPkg)
        }
        if (!hasPkg) {
            return false
        }

        var handled = false

        val marketUri = "market://details?id=${jumpId}"
        val marketIntent = Intent("android.intent.action.VIEW")
        marketIntent.setPackage(marketPkg)
        marketIntent.data = Uri.parse(marketUri)

        withContext(Dispatchers.Main) {
            context?.apply {
                if (this !is Activity) {
                    marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                try {
                    startActivity(marketIntent)
                    handled = true
                } catch (e: Exception) {
                }
            }
        }
        return handled
    }

    suspend fun isPackageInstalled(pkg: String): Boolean {
        val pm = context?.packageManager
        try {
            val pkgInfo = pm?.getPackageInfo(pkg, 0)
            return pkgInfo != null
        } catch (e: Exception) {
            return false
        }
    }

}


