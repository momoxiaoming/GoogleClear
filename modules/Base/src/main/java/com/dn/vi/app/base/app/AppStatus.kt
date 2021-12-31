package com.dn.vi.app.base.app

/**
 * 当前app的 交互运行状态
 * Created by holmes on 2020/5/19.
 **/
interface AppStatus {

    /**
     * 当前是否有UI页打开了（未退出）。
     */
    fun isAlive(): Boolean

    /**
     * 当前是否有UI在前台显示
     */
    fun isForeground(): Boolean

}