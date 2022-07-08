package com.mckj.gallery.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import com.mckj.baselib.helper.getApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.ceil

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/23 16:28
 * @desc
 */
object GalleryToolsUtil {
    const val TAG = "GalleryToolsUtil"
    private const val EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE"
    private const val INDIVIDUAL_DIR_NAME = "cleanUpxGallery"
    private const val UNIX_SEPARATOR = '/'
    private const val WINDOWS_SEPARATOR = '\\'
    private const val ONE_DAY = 60 * 60 * 24 * 1000f

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
            Log.d(TAG, "copyFile error  ${e.message}")
            return false
        }
    }

    /**
     * 获取1个回收的地址
     * @return
     */
    fun getRecycledDirectory(): File? {
        return getCacheDirectory(getApplicationContext())
    }

    fun getName(filename: String?): String? {
        if (filename == null) {
            return null
        }
        val index: Int = indexOfLastSeparator(filename)
        return filename.substring(index + 1)
    }

    fun indexOfLastSeparator(filename: String?): Int {
        if (filename == null) {
            return -1
        }
        val lastUnixPos: Int =
            filename.lastIndexOf(UNIX_SEPARATOR)
        val lastWindowsPos: Int =
            filename.lastIndexOf(WINDOWS_SEPARATOR)
        return Math.max(lastUnixPos, lastWindowsPos)
    }

    private fun getCacheDirectory(context: Context): File? {
        return getCacheDirectory(context, true)
    }

    private fun getCacheDirectory(context: Context, preferExternal: Boolean): File? {
        var appCacheDir: File? = null
        if (preferExternal && existSDCard() && hasExternalStoragePermission(context)
        ) {
            appCacheDir =
                getExternalCacheDir(context)
        }
        if (appCacheDir == null) {
            appCacheDir = context.cacheDir
        }
        if (appCacheDir == null) {
            @SuppressLint("SdCardPath") val cacheDirPath =
                "/data/data/" + context.packageName + "/cache/"
            Log.d(
                TAG,
                String.format(
                    "Can't define system cache directory! '%s' will be used.",
                    cacheDirPath
                )
            )
            appCacheDir = File(cacheDirPath)
        }
        return appCacheDir
    }

    private fun existSDCard(): Boolean {
        val externalStorageState: String = try {
            Environment.getExternalStorageState()
        } catch (e: NullPointerException) { // (sh)it happens (Issue #660)
            ""
        }
        return Environment.MEDIA_MOUNTED == externalStorageState
    }

    private fun hasExternalStoragePermission(context: Context): Boolean {
        val perm =
            context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION)
        return perm == PackageManager.PERMISSION_GRANTED
    }

    private fun getExternalCacheDir(context: Context): File? {
        val dataDir = File(File(Environment.getExternalStorageDirectory(), "Android"), "data")
        val appCacheDir = File(File(dataDir, context.packageName), "cache")
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                Log.d(TAG, "Unable to create external cache directory")
                return null
            }
            try {
                File(appCacheDir, ".nomedia").createNewFile()
            } catch (e: IOException) {
                Log.d(TAG, "Can't create \".nomedia\" file in application external cache directory")
            }
        }
        val recycledDir = File(appCacheDir, "recycled")
        if (!recycledDir.exists()) {
            if (!recycledDir.mkdirs()) {
                Log.d(TAG, "Unable to create external recycled directory")
                return null
            }
        }
        return recycledDir
    }

    fun checkRecycledTime(createTime: Long?): Int {
        if (createTime == null) return 0
        val passTime = System.currentTimeMillis() - createTime
        val days = passTime.toDouble() / ONE_DAY.toDouble()
        return ceil(days).toInt()
    }

    fun getDCIMPath(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
    }
}
