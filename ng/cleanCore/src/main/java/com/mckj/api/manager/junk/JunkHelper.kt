package com.mckj.api.manager.junk

import com.dn.vi.app.base.app.AppMod
import com.mckj.api.util.FileUtils
import com.mckj.api.db.entity.JunkDbEntity
import java.io.File
import java.io.FileFilter

/**
 * Describe: 垃圾回收帮助类
 *
 * Created By yangb on 2021/3/9
 */
object JunkHelper {

    const val TAG = "JunkHelper"

    /**
     * 从数据库查找缓存
     */
    fun getCachePathListByDb(
        packageName: String,
        allAppJunkList: List<JunkDbEntity>?,
    ): List<String>? {
        if (allAppJunkList.isNullOrEmpty()) {
            return null
        }
        val appList = getListByPackageName(allAppJunkList, packageName)
        if (appList.isEmpty()) {
            return null
        }
        val sdPath = FileUtils.getSDPath()
        val result = mutableListOf<String>()
        for (item in appList) {
            val path = getUsePath(item) ?: continue
            result.add("$sdPath${File.separator}$path")
        }
        return result
    }

    /**
     * 校验路径列表
     */
    fun checkPathList(list: MutableList<String>) {
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            val path = iterator.next()
            var length = 0L
            do {
                val isExists = FileUtils.isFileExists(AppMod.app, path)
                if (!isExists) {
                    continue
                }
                length = FileUtils.getLength(path)
            } while (false)
            if (length <= 0) {
                iterator.remove()
            }
        }
    }

    /**
     * 获取文件大小
     */
    fun getSizeByPathList(list: List<String>): Long {
        var result = 0L
        for (path in list) {
            val size = FileUtils.getLength(path)
            result += size
        }
        return result
    }

    /**
     * 获取缓存列表-通过包名
     */
    private fun getListByPackageName(
        allAppJunkList: List<JunkDbEntity>,
        packageName: String,
    ): List<JunkDbEntity> {
        val result = mutableListOf<JunkDbEntity>()
        for (item in allAppJunkList) {
            if (item.packageName == packageName) {
                result.add(item)
            }
        }
        return result
    }

    fun getUsePath(entity: JunkDbEntity): String? {
        return if (entity.rootPath.isNullOrEmpty()) {
            entity.filePath
        } else {
            entity.rootPath
        }
    }

    fun getQDocument(path: String): String {
        return "primary:$path"
    }

    /**
     * 获取sd卡一级路径所有名称
     */
    fun getSdDirs(): List<String>? {
        val sdPath = FileUtils.getSDPath()
        val file = File(sdPath)
        return file.listFiles(FileFilter {
            FileUtils.isDir(it)
        })?.map { it.name }
    }

    /**
     * 获取/Android/data/下所有列表
     */
    fun getAndroidDataDir(): List<String>? {
        val sdPath = FileUtils.getDataPath()
        val file = File(sdPath)
        return file.listFiles(FileFilter {
            FileUtils.isDir(it)
        })?.map { it.name }
    }

    private fun getPathByReg(path: String): String {
        val splits = path.split(File.separator)
        val stringBuilder = StringBuilder()
        for (item in splits) {
            if (item.isEmpty()) {
                continue
            }
            //如果文件名包含[或{,判断为正则表达式
            if (item.contains("[") || item.contains("{")) {
                break
            }
            stringBuilder.append(File.separator).append(item)
        }
        return stringBuilder.toString()
    }

}