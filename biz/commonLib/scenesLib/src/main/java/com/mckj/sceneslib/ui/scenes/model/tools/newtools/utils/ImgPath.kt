package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import java.io.FileNotFoundException

import android.graphics.BitmapFactory

import java.io.FileInputStream

import android.graphics.Bitmap




/**
 * @Description
 * @CreateTime 2022/3/24 17:40
 * @Author
 */
object ImgPath {
    fun getLocalBitmap(url: String?): Bitmap? {
        return try {
            val fis = FileInputStream(url)
            BitmapFactory.decodeStream(fis)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}