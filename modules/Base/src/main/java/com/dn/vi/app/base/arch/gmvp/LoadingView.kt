package com.dn.vi.app.base.arch.gmvp

/**
 * 带加载过程效果
 * @author holmes on 17-2-7.
 */
interface LoadingView {

    /**
     * Start loading
     */
    fun startLoading()

    /**
     * Stop loading
     * @param withError whether error happened
     * *
     * @param e 可能为null.
     */
    fun stopLoading(withError: Boolean, e: Throwable?)
}
