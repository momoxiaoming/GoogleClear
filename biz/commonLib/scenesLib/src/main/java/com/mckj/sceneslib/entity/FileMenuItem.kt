package com.mckj.sceneslib.entity

/**
 * FileMenuItem
 *
 * @author mmxm
 * @date 2021/3/5 10:45
 */
data class FileMenuItem(var iconRes:Int,var title: String, var desc: String,var type:String) {
    companion object {
        /**
         * 相册
         */
        const val PHOTO="photo"

        /**
         * 视频
         */
        const val VIDEO="video"

        /**
         * apk
         */
        const val APK="apk"

        /**
         * audio,音乐
         */
        const val AUDIO="audio"

        /**
         * zip
         */
        const val ZIP="zip"

        /**
         * 大文件
         */
        const val BIG_FILE="big_file"
    }
}