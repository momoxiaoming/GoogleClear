package com.mckj.template.header.clean

import android.content.Context
import android.view.View
import android.widget.TextView
import com.mckj.template.abs.AbsHeader
import com.mckj.template.impl.ICleanStatus

/**
 * 该层处理
 */
abstract class AbsCleanHeader(context: Context) : AbsHeader(context), ICleanStatus {


    /**
     * 根布局
     */
    abstract fun getRoot(): View

    /**
     * 动画、图片区域
     */
    abstract fun getAnimView(): View?

    /**
     * 扫描大小
     */
    abstract fun getScanSize(): TextView?

    /**
     * 扫描单位
     */
    abstract fun getScanUnit(): TextView?

    /**
     * 扫描垃圾的描述
     */
    abstract fun getJunkDesc(): TextView?

    /**
     * 扫描
     */
    abstract fun getScanDesc(): TextView?

    /**
     * 扫描状态:扫描中...  查看垃圾详情 等描述
     */
    abstract fun getScanStatus(): TextView?

    /**
     * 一键清理、停止扫描，按钮区域
     */
    abstract fun getScanAction(): TextView?

    /**
     * 扫描空文件的提示区域
     */
    abstract fun getScanEmptyView(): View?

    override fun initView(context: Context): View {
        return getRoot()
    }

}