package com.mckj.api.client.task.shallow

import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.mckj.api.client.JunkConstants
import com.mckj.api.client.task.base.BaseTask
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkInfo
import com.mckj.api.util.FileUtils
import io.reactivex.rxjava3.functions.Consumer

/**
 * @author leix
 * @version 1
 * @createTime 2021/10/28 16:19
 * @desc 无用安装包清理
 */
class ApkFileTask : BaseTask() {

    companion object {
        const val TAG = "ApkFileTask"
    }
    override fun getName(): String {
        return "安装包文件"
    }

    override fun scan(consumer: Consumer<AppJunk>): Boolean {
        var cursor: Cursor? = null
        try {
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
            )
            //相当于我们常用sql where 后面的写法
            val selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            val selectionArgs = arrayOf("application/vnd.android.package-archive")
            cursor = AppMod.app.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                null
            )
            if (cursor == null) {
                Log.i(TAG, "scan error: cursor is null")
                return false
            }
            val _name = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val _data = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val _size = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            while (cursor.moveToNext()) {
                val name = cursor.getString(_name)
                val data: String = cursor.getString(_data)
                val size = cursor.getLong(_size)
                Log.i(TAG, "scan: name:$name data:$data size:$size")
                val entity: AppJunk = getApkFileEntity(name, size, data) ?: continue
                consumer.accept(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return true
    }

    override fun clean(appJunk: AppJunk): Boolean {
        return try {
            appJunk.junks?.forEach {
                FileUtils.delete(it.path)
            }
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }

    override fun stop() {

    }


    private fun getApkFileEntity(name: String, size: Long, path: String): AppJunk? {
        var result: AppJunk? = null
        do {
            if (size <= 0) {
                Log.d(TAG, "getApkJunkEntity error: size <= 0")
                break
            }
            val info = AppUtil.parseApk(AppMod.app, path)
            if (info == null) {
                Log.d(TAG, "getApkJunkEntity error: packageInfo is null")
                break
            }
            val desc = "v${info.versionName} ${
                if (AppUtil.isInstall(AppMod.app, info.packageName)) {
                    "已安装"
                } else {
                    "未安装"
                }
            }"
            val details = mutableListOf<JunkInfo>()
            details.add(
                JunkInfo(
                    parent = info.packageName,
                    junkType = JunkConstants.JunkType.APK,
                    fileType = JunkConstants.JunkFileType.APK,
                    name = "安装包文件",
                    description = desc,
                    junkSize = size,
                    path = path
                )
            )
            result = AppJunk(
                type = JunkConstants.Session.APK,
                appName = name,
                packageName = info.packageName,
                icon = AppUtil.getAppIcon(AppMod.app, info),
                junkSize = size,
                junks = details,
            )
        } while (false)
        return result
    }
}