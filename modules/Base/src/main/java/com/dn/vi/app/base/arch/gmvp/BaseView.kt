package com.dn.vi.app.base.arch.gmvp

/**
 * The View layer, Google mvp
 * Created by holmes on 16-12-11.
 */
interface BaseView<T> {

    /**
     * 设置所绑定的presenter.
     * 一般在Presenter的 构造里面完成。
     * @param presenter
     */
    fun setPresenter(presenter: T)

}
