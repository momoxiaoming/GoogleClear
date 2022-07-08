package com.org.openlib.ui.web

import android.webkit.DownloadListener
import com.dn.vi.app.base.app.kt.arouter
import com.dn.vi.app.cm.log.VLog
import com.org.openlib.path.ARouterPath

/**
 * webview 的下载支持
 * Created by holmes on 2020/12/11.
 **/
class WebDownloader : DownloadListener {

    private val downloader: IDownloader? by lazy {
        arouter().build(ARouterPath.SRV_DOWNLOADER).navigation() as? IDownloader
    }

    override fun onDownloadStart(
        url: String?,
        userAgent: String?,
        contentDisposition: String?,
        mimetype: String?,
        contentLength: Long
    ) {
        if (url.isNullOrEmpty()) {
            VLog.w("web downloading with a null url")
            return
        }
        if (contentLength <= 0L) {
            VLog.w("web downloading with zero length content")
            return
        }

        if (downloader != null) {
            VLog.i("web downloading enqueued, [$url]")
            downloader?.download(url)
        } else {
            VLog.w("web downloading, but downloader not found")
        }

    }

}