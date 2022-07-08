 package com.dn.vi.app.cm.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import okhttp3.internal.and
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


/**
 * Describe:
 *
 * Created By yangb on 2021/3/2
 */
object FileUtil {

    const val TAG = "FileUtil"
    fun getFileSizeNumber(size: Long): Float {
        return when {
            size < 1000 -> {
                //999B
                size.toFloat()
            }

            size < 1000 * 1024L -> {
                //999KB
                size / 1024f
            }
            size < 1000 * 1024 * 1024L -> {
                size / 1024f / 1024
            }

            size < 1000 * 1024 * 1024 * 1024L -> {
                size / 1024f / 1024 / 1024
            }
            else -> {
                size / 1024f / 1024 / 1024 / 1024
            }
        }
    }
    /**
     * Return the file by path.
     *
     * @param filePath The path of file.
     * @return the file
     */
    fun getFileByPath(filePath: String?): File? {
        return if (filePath?.isNotEmpty() == true) {
            File(filePath)
        } else {
            null
        }
    }

    /**
     * Return whether it is a directory.
     *
     * @param dirPath The path of directory.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isDir(dirPath: String?): Boolean {
        return isDir(getFileByPath(dirPath))
    }

    /**
     * Return whether it is a directory.
     *
     * @param file The file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isDir(file: File?): Boolean {
        return file != null && file.exists() && file.isDirectory
    }

    /**
     * Return whether it is a file.
     *
     * @param filePath The path of file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isFile(filePath: String?): Boolean {
        return isFile(getFileByPath(filePath))
    }

    /**
     * Return whether it is a file.
     *
     * @param file The file.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isFile(file: File?): Boolean {
        return file != null && file.exists() && file.isFile
    }

    /**
     * Return whether the file exists.
     *
     * @param file The file.
     * @return `true`: yes<br></br>`false`: no
     */
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
     * Delete the directory.
     *
     * @param filePath The path of file.
     * @return `true`: success<br></br>`false`: fail
     */
    fun delete(filePath: String?): Boolean {
        return delete(getFileByPath(filePath))
    }

    /**
     * Delete the directory.
     *
     * @param file The file.
     * @return `true`: success<br></br>`false`: fail
     */
    fun delete(file: File?): Boolean {
        if (file == null) return false
        return if (file.isDirectory) {
            deleteDir(file)
        } else deleteFile(file)
    }

    /**
     * Delete the directory.
     *
     * @param dir The directory.
     * @return `true`: success<br></br>`false`: fail
     */
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

    /**
     * Delete the file.
     *
     * @param file The file.
     * @return `true`: success<br></br>`false`: fail
     */
    private fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    /**
     * Delete the all in directory.
     *
     * @param dirPath The path of directory.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteAllInDir(dirPath: String?): Boolean {
        return deleteAllInDir(getFileByPath(dirPath))
    }

    /**
     * Delete the all in directory.
     *
     * @param dir The directory.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteAllInDir(dir: File?): Boolean {
        return deleteFilesInDirWithFilter(dir, object : FileFilter {
            override fun accept(pathname: File?): Boolean {
                return true
            }
        })
    }

    /**
     * Delete all files in directory.
     *
     * @param dirPath The path of directory.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteFilesInDir(dirPath: String?): Boolean {
        return deleteFilesInDir(getFileByPath(dirPath))
    }

    /**
     * Delete all files in directory.
     *
     * @param dir The directory.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteFilesInDir(dir: File?): Boolean {
        return deleteFilesInDirWithFilter(dir, object : FileFilter {
            override fun accept(pathname: File): Boolean {
                return pathname.isFile
            }
        })
    }

    /**
     * Delete all files that satisfy the filter in directory.
     *
     * @param dirPath The path of directory.
     * @param filter  The filter.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteFilesInDirWithFilter(
        dirPath: String?,
        filter: FileFilter?,
    ): Boolean {
        return deleteFilesInDirWithFilter(getFileByPath(dirPath), filter)
    }

    /**
     * Delete all files that satisfy the filter in directory.
     *
     * @param dir    The directory.
     * @param filter The filter.
     * @return `true`: success<br></br>`false`: fail
     */
    fun deleteFilesInDirWithFilter(dir: File?, filter: FileFilter?): Boolean {
        if (dir == null || filter == null) return false
        // dir doesn't exist then return true
        if (!dir.exists()) return true
        // dir isn't a directory then return false
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (filter.accept(file)) {
                    if (file.isFile) {
                        if (!file.delete()) return false
                    } else if (file.isDirectory) {
                        if (!deleteDir(file)) return false
                    }
                }
            }
        }
        return true
    }

    /**
     * Return the length.
     *
     * @param filePath The path of file.
     * @return the length
     */
    fun getLength(filePath: String?): Long {
        return getLength(getFileByPath(filePath))
    }

    /**
     * Return the length.
     *
     * @param file The file.
     * @return the length
     */
    fun getLength(file: File?): Long {
        if (file == null) return 0
        return if (file.isDirectory) {
            getDirLength(file)
        } else getFileLength(file)
    }

    /**
     * Return the length of directory.
     *
     * @param dir The directory.
     * @return the length of directory
     */
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

    /**
     * Return the length of file.
     *
     * @param file The file.
     * @return the length of file
     */
    private fun getFileLength(file: File?): Long {
        return if (file != null && isFile(file)) {
            file.length()
        } else {
            0
        }
    }

    fun getFileSizeText(size: Long): String {
        return when {
            size < 1000 -> {
                //999B
                String.format(Locale.getDefault(), "%dB", size)
            }
            size < 10 * 1024L -> {
                //9.99KB
                String.format(Locale.getDefault(), "%1.2fKB", size / 1024f)
            }
            size < 100 * 1024L -> {
                //99.9KB
                String.format(Locale.getDefault(), "%1.1fKB", size / 1024f)
            }
            size < 1000 * 1024L -> {
                //999KB
                String.format(Locale.getDefault(), "%dKB", (size / 1024f).toInt())
            }
            size < 10 * 1024 * 1024L -> {
                //9.99MB
                String.format(Locale.getDefault(), "%1.2fMB", size / 1024f / 1024)
            }
            size < 100 * 1024 * 1024L -> {
                //99.9MB
                String.format(Locale.getDefault(), "%1.1fMB", size / 1024f / 1024)
            }
            size < 1000 * 1024 * 1024L -> {
                //999MB
                String.format(Locale.getDefault(), "%dMB", (size / 1024f / 1024).toInt())
            }
            size < 10 * 1024 * 1024 * 1024L -> {
                //9.99GB
                String.format(Locale.getDefault(), "%1.2fGB", size / 1024f / 1024 / 1024)
            }
            size < 100 * 1024 * 1024 * 1024L -> {
                //99.9GB
                String.format(Locale.getDefault(), "%1.1fGB", size / 1024f / 1024 / 1024)
            }
            size < 1000 * 1024 * 1024 * 1024L -> {
                //999GB
                String.format(Locale.getDefault(), "%dGB", (size / 1024f / 1024 / 1024).toInt())
            }
            else -> {
                String.format(
                    Locale.getDefault(),
                    "%1.2fTB",
                    size / 1024f / 1024 / 1024 / 1024
                )
            }
        }
    }

    /**
     * 获取文件大小数字
     *
     * 数字显示尽量控制在3位
     */
    fun getFileSizeNumberText(size: Long): String {
        return when {
            size < 1000 -> {
                //999B
                String.format(Locale.getDefault(), "%d", size)
            }
            size < 10 * 1024L -> {
                //9.99KB
                String.format(Locale.getDefault(), "%1.2f", size / 1024f)
            }
            size < 100 * 1024L -> {
                //99.9KB
                String.format(Locale.getDefault(), "%1.1f", size / 1024f)
            }
            size < 1000 * 1024L -> {
                //999KB
                String.format(Locale.getDefault(), "%d", (size / 1024f).toInt())
            }
            size < 10 * 1024 * 1024L -> {
                //9.99MB
                String.format(Locale.getDefault(), "%1.2f", size / 1024f / 1024)
            }
            size < 100 * 1024 * 1024L -> {
                //99.9MB
                String.format(Locale.getDefault(), "%1.1f", size / 1024f / 1024)
            }
            size < 1000 * 1024 * 1024L -> {
                //999MB
                String.format(Locale.getDefault(), "%d", (size / 1024f / 1024).toInt())
            }
            size < 10 * 1024 * 1024 * 1024L -> {
                //9.99GB
                String.format(Locale.getDefault(), "%1.2f", size / 1024f / 1024 / 1024)
            }
            size < 100 * 1024 * 1024 * 1024L -> {
                //99.9GB
                String.format(Locale.getDefault(), "%1.1f", size / 1024f / 1024 / 1024)
            }
            size < 1000 * 1024 * 1024 * 1024L -> {
                //999GB
                String.format(Locale.getDefault(), "%d", (size / 1024f / 1024 / 1024).toInt())
            }
            else -> {
                String.format(
                    Locale.getDefault(),
                    "%1.2f",
                    size / 1024f / 1024 / 1024 / 1024
                )
            }
        }
    }

    /**
     * 获取文件大小单位
     */
    fun getFileSizeUnitText(size: Long): String {
        return when {
            size < 1000 -> {
                "B"
            }
            size < 1000 * 1024 -> {
                "KB"
            }
            size < 1000 * 1024 * 1024 -> {
                "MB"
            }
            size < 1000 * 1024 * 1024 * 1024L -> {
                "GB"
            }
            else -> {
                "TB"
            }
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

    /**
     * 获取App缓存路径
     *
     * @param packageName 包名
     */
    fun getAppCachePath(packageName: String): String {
        val dataPath = getDataPath()
        return if (dataPath.isNotEmpty()) {
            "$dataPath${File.separatorChar}$packageName${File.separatorChar}cache"
        } else {
            ""
        }
    }

    /**
     * 获取App缓存路径
     *
     * @param packageName 包名
     */
    fun getAppCache(packageName: String): String {
        return "Android${File.separatorChar}data${File.separatorChar}${packageName}${File.separatorChar}cache"
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
            Log.d(TAG, "copyFile error  ${e.message}")
            return false
        }
    }
}