package com.dn.baselib.http

/**
 * 进度监听
 */
interface ProgressListener {

    /**
     * @param updateSize 更新大小
     * @param downloadSize 下载大小
     * @param totalSize 总共大小
     */
    fun update(updateSize: Long, downloadSize: Long, totalSize: Long)

    /**
     * 下载完成
     */
    fun onSuccess(boolean: Boolean)

    /**
     * 下载异常
     */
    fun onFailed(throwable: Throwable)

}