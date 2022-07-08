package com.mckj.api.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import okhttp3.internal.and
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/16 9:44
 * @desc
 */
object FileUtils {

    fun isDocument(suffix: String): Boolean {
        return isExcel(suffix) || isWord(suffix) || isPdf(suffix) || isTxt(suffix)
    }

    fun isExcel(suffix: String): Boolean {
        return suffix.equals("xlsx", true)
                || suffix.equals("xls", true)
                || suffix.equals("xlt", true)
                || suffix.equals("xlsm", true)
    }

    fun isPPT(suffix: String): Boolean {
        return suffix.equals("ppt", true)
                || suffix.equals("pptx", true)
                || suffix.equals("pot", true)
                || suffix.equals("pps", true)
                || suffix.equals("pptm", true)
                || suffix.equals("potx", true)
                || suffix.equals("potm", true)
                || suffix.equals("ppsx", true)
                || suffix.equals("ppsm", true)
    }

    fun isWord(suffix: String): Boolean {
        return suffix.equals("docx", true)
                || suffix.equals("dotx", true)
                || suffix.equals("docm", true)
                || suffix.equals("dotm", true)
    }

    fun isPdf(suffix: String): Boolean {
        return (suffix.equals("pdf", true))
    }

    fun isTxt(suffix: String): Boolean {
        return suffix.equals("txt", true)
    }


    fun delete(filePath: String?): Boolean {
        return delete(getFileByPath(filePath))
    }

    fun delete(file: File?): Boolean {
        if (file == null) return false
        return if (file.isDirectory) {
            deleteDir(file)
        } else deleteFile(file)
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir == null) return false
        // dir doesn't exist then return true
        if (!dir.exists()) return true
        // dir isn't a directory then return false
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.isFile) {
                    if (!file.delete()) return false
                } else if (file.isDirectory) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return dir.delete()
    }

    private fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    private fun getFileByPath(filePath: String?): File? {
        return if (filePath?.isNotEmpty() == true) {
            File(filePath)
        } else {
            null
        }
    }


    /**
     * SD path
     */
    fun getSDPath(): String {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) { // sd卡存在
            Environment.getExternalStorageDirectory().absolutePath
        } else {
            ""
        }
    }

    fun getLength(file: File?): Long {
        if (file == null) return 0
        return if (file.isDirectory) {
            getDirLength(file)
        } else getFileLength(file)
    }

    private fun getDirLength(dir: File): Long {
        if (!isDir(dir)) return 0
        var len: Long = 0
        val files = dir.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                len += if (file.isDirectory) {
                    getDirLength(file)
                } else {
                    file.length()
                }
            }
        }
        return len
    }

    fun getLength(filePath: String?): Long {
        return getLength(getFileByPath(filePath))
    }

    private fun getFileLength(file: File?): Long {
        return if (file != null && isFile(file)) {
            file.length()
        } else {
            0
        }
    }

    fun isDir(file: File?): Boolean {
        return file != null && file.exists() && file.isDirectory
    }

    fun isFile(filePath: String?): Boolean {
        return isFile(getFileByPath(filePath))
    }

    fun isFile(file: File?): Boolean {
        return file != null && file.exists() && file.isFile
    }

    /** 获取App缓存路径
     *
     * @param packageName 包名
     */
    fun getAppCache(packageName: String): String {
        return "Android${File.separatorChar}data${File.separatorChar}${packageName}${File.separatorChar}cache"
    }

    fun isFileExists(context: Context, file: File): Boolean {
        return if (file.exists()) {
            true
        } else isFileExists(context, file.absolutePath)
    }

    /**
     * Return whether the file exists.
     *
     * @param filePath The path of file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isFileExists(context: Context, filePath: String): Boolean {
        val file: File = getFileByPath(filePath) ?: return false
        return if (file.exists()) {
            true
        } else isFileExistsApi29(context, filePath)
    }

    private fun isFileExistsApi29(context: Context, filePath: String): Boolean {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                val uri: Uri = Uri.parse(filePath)
                val cr: ContentResolver = context.contentResolver
                val afd = cr.openAssetFileDescriptor(uri, "r") ?: return false
                try {
                    afd.close()
                } catch (ignore: IOException) {
                }
            } catch (e: FileNotFoundException) {
                return false
            }
            return true
        }
        return false
    }


    /**
     * 获取sdk/Android/data 目录
     */
    fun getDataPath(): String {
        val sdPath = getSDPath()
        return if (sdPath.isNotEmpty()) {
            "$sdPath${File.separatorChar}Android${File.separatorChar}data"
        } else {
            ""
        }
    }

    fun getAppPath(packageName: String): String {
        val dataPath = getDataPath()
        return if (dataPath.isNotEmpty()) {
            "$dataPath${File.separatorChar}$packageName"
        } else {
            ""
        }
    }
    fun getAppCachePath(packageName: String): String {
        val dataPath = getDataPath()
        return if (dataPath.isNotEmpty()) {
            "$dataPath${File.separatorChar}$packageName${File.separatorChar}cache"
        } else {
            ""
        }
    }
    /**
     * 获取文件md5值
     */
    fun getFileMD5(path: String?): String? {
        val sb = StringBuilder()
        try {
            val fis = FileInputStream(File(path))
            val md: MessageDigest = MessageDigest.getInstance("md5")
            val bytes = ByteArray(10240)
            var len: Int
            while (fis.read(bytes).also { len = it } != -1) {
                md.update(bytes, 0, len)
            }
            val digest: ByteArray = md.digest()
            for (b in digest) {
                val d: Int = b and 0xff
                var herString = Integer.toHexString(d)
                if (herString.length == 1) {
                    herString = "0$herString"
                }
                sb.append(herString)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sb.toString()
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
            return false
        }
    }
}