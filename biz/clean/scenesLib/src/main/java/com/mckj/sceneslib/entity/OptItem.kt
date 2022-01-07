package com.mckj.sceneslib.entity

/**
 * OptItem
 *
 * @author mmxm
 * @date 2021/3/3 17:50
 */
data class OptItem(var type:String) {

    companion object{
        /**
         * 自动清理
         */
        const val AUTO_CLEAN="auto_clean"

        /**
         * 桌面工具
         */
        const val DESK_TOOL="desk_tool"

        /**
         * 开启通知
         */
        const val OPEN_NOT="open_not"

        /**
         * 开启悬浮
         */
        const val OPEN_FLOAT="open_float"

        /**
         * 开启权限,主要是sd权限
         */
        const val OPEN_AUTHORITY="open_authority"

    }

}