package com.mckj.template.impl


/**
 * @author leix
 * @version 1
 * @createTime 2022/2/28 10:58
 * @desc
 */
interface IAction {
    fun startScan()
    fun startClean()
    fun stop()
    fun lookDetail()
}