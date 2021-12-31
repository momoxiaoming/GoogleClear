package com.dn.vi.app.base.app

/**
 * 一个代命名代号的页,
 * 代号一般用于统计
 * Created by holmes on 2020/10/14.
 **/
interface NamedPage {

    /**
     * 获取页面的命名代号
     * 如果为空字符，则没有指定代号
     */
    fun getPageName(): String

}