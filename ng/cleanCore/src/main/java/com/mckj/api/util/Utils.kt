package com.mckj.api.util

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * @author xx
 * @version 1
 * @createTime 2021/11/3 14:43
 * @desc
 */
object Utils {
    private const val UNIX_SEPARATOR = '/'
    private const val WINDOWS_SEPARATOR = '\\'

    fun getDCIMPath(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
    }

    fun getName(filename: String?): String? {
        if (filename == null) {
            return null
        }
        val index: Int = indexOfLastSeparator(filename)
        return filename.substring(index + 1)
    }

    fun copyFile(originPath: String, targetPath: String): Boolean {
        try {
            val oldFile = File(originPath)
            if (!oldFile.exists() || !oldFile.isFile || !oldFile.canRead()) return false
            val fileInputStream = FileInputStream(originPath)
            val fileOutputStream = FileOutputStream(targetPath)
            val buffer = ByteArray(1024)
            var byteRead = 0
            while (-1 != (fileInputStream.read(buffer).also { byteRead = it })) {
                fileOutputStream.write(buffer, 0, byteRead)
            }
            fileInputStream.close()
            fileOutputStream.flush()
            fileOutputStream.close()
            return true
        } catch (e: Exception) {
            Log.d("gallery", "copyFile error  ${e.message}")
            return false
        }
    }

    private fun indexOfLastSeparator(filename: String?): Int {
        if (filename == null) {
            return -1
        }
        val lastUnixPos: Int =
            filename.lastIndexOf(UNIX_SEPARATOR)
        val lastWindowsPos: Int =
            filename.lastIndexOf(WINDOWS_SEPARATOR)
        return Math.max(lastUnixPos, lastWindowsPos)
    }
}