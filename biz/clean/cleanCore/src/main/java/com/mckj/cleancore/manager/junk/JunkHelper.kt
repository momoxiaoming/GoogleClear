package com.mckj.cleancore.manager.junk

import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.cleancore.db.entity.JunkDbEntity
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
        val sdPath = FileUtil.getSDPath()
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
                val isExists = FileUtil.isFileExists(AppMod.app, path)
                if (!isExists) {
                    continue
                }
                length = FileUtil.getLength(path)
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
            val size = FileUtil.getLength(path)
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

    /**
     * 获取sd卡一级路径所有名称
     */
    fun getSdDirs(): List<String>? {
        val sdPath = FileUtil.getSDPath()
        val file = File(sdPath)
        return file.listFiles(FileFilter {
            FileUtil.isDir(it)
        })?.map { it.name }
    }

    /**
     * 获取/Android/data/下所有列表
     */
    fun getAndroidDataDir(): List<String>? {
        val sdPath = FileUtil.getDataPath()
        val file = File(sdPath)
        return file.listFiles(FileFilter {
            FileUtil.isDir(it)
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