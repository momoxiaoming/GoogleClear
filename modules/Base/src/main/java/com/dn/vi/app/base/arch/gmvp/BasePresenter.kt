package com.dn.vi.app.base.arch.gmvp

/**
 * Presenter. in google mvp
 * Created by holmes on 16-12-11.
 */
interface BasePresenter {

    /**
     * 类生命(行为)周期.life circle (create, start, resume)
     *
     *
     * or like rx subscibe
     *
     */
    fun subscribe()

    /**
     * 类生命(行为)周期.life circle (destroy, stop, pause)
     *
     *
     * or like rx unsubscibe of subscription
     *
     */
    fun unsubscribe()

}
