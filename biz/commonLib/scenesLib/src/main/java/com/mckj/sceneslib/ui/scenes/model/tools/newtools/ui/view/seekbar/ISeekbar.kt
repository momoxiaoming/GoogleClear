package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.view.seekbar

/**
 * ISeekbar
 *
 * @author mmxm
 * @date 2022/3/29 19:54
 */
interface ISeekbar {
    /**
     * 设置进度
     * @param int Int
     */
    fun setProgress(int: Int)

    /**
     * 设置进度回调
     */
    fun setOnProgressCallback(callback:ISeekbarCallback)
}