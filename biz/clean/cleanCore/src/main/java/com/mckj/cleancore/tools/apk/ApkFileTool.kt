package com.mckj.cleancore.tools.apk

import android.database.Cursor
import android.provider.MediaStore
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.cleancore.entity.ApkFileEntity
import com.mckj.cleancore.entity.IJunkEntity
import com.mckj.cleancore.entity.JunkDetailEntity
import com.mckj.cleancore.tools.AbsJunkTool
import com.mckj.cleancore.util.Log
import io.reactivex.rxjava3.functions.Consumer

/**
 * Describe:安装包文件管理
 *
 * 主要从系统数据库查询apk文件
 *
 * Created By yangb on 2021/3/8
 */
class ApkFileTool : AbsJunkTool() {

    companion object {
        const val TAG = "ApkFileTool"
    }

    override fun getName(): String {
        return "安装包文件"
    }

    override fun scan(consumer: Consumer<IJunkEntity>): Boolean {
        if (!isOptEnable()) {
            Log.i(TAG, "scan error: isOptEnable is false")
            return false
        }
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
                val entity: ApkFileEntity = getApkFileEntity(name, size, data) ?: continue
                consumer.accept(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return true
    }

    override fun clean(entity: IJunkEntity): Boolean {
        val details = entity.getJunkDetails()
        Log.i(TAG, "clean: details.size:${details?.size ?: 0}")
        details?.forEach {
            FileUtil.delete(it.path)
        }
        return true
    }

    private fun getApkFileEntity(name: String, size: Long, path: String): ApkFileEntity? {
        var result: ApkFileEntity? = null
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
            val details = mutableListOf<JunkDetailEntity>()
            details.add(
                JunkDetailEntity(
                    JunkDetailEntity.CATEGORY_NORMAL_CACHE,
                    JunkDetailEntity.TYPE_NORMAL,
                    "安装包文件",
                    path,
                    size
                )
            )
            result = ApkFileEntity(
                name,
                desc,
                info.packageName,
                AppUtil.getAppIcon(AppMod.app, info),
                details,
                size
            )
        } while (false)
        return result
    }

}