package com.org.openlib.ui.web

import android.app.DownloadManager
import android.os.Environment
import com.alibaba.android.arouter.facade.template.IProvider
import io.reactivex.rxjava3.core.Single

/**
 * 下载管理模块.
 * 使用[Routers.SRV_DOWNLOADER]获取
 *
 * Created by holmes on 2020/12/11.
 **/
interface IDownloader : IProvider {


    /**
     * 下载状态
     * @param code 0 则不存在
     */
    data class Status(
        val code: Int,
        val localFile: String
    ) {
        val isSuccess: Boolean
            get() = code == DownloadManager.STATUS_SUCCESSFUL
    }

    /**
     * 下载[url]的内容。
     * 放在[Environment.DIRECTORY_DOWNLOADS]目录下
     */
    fun download(url: String)

    /**
     * 获取url的下载状态
     */
    fun getStatusByUrl(url: String): Single<Status>

}