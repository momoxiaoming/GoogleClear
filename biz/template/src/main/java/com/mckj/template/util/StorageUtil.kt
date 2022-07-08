package com.mckj.template.util

import android.annotation.TargetApi
import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.*
import android.os.storage.StorageManager
import com.mckj.template.entity.StorageBean
import java.io.File
import java.io.IOException
import java.lang.reflect.Field
import java.util.*

/**
 *  author :leo
 *  date : 2022/3/14 11:26
 *  description :存储空间占用
 */
object StorageUtil {

    fun query(context: Context): StorageBean? {
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE)
        val version = Build.VERSION.SDK_INT
        if (version > Build.VERSION_CODES.M) {
            val getVolumes = storageManager.javaClass.getDeclaredMethod("getVolumes")
            val getVolumeInfo = getVolumes.invoke(storageManager) as List<Object>
            var getType: Field? = null
            var used = 0L
            var total = 0L
            var systemSize = 0L
            for (obj in getVolumeInfo) {
                if (getType == null) {
                    getType = obj.`class`.getField("type")
                }
                val type = getType?.getInt(obj)
                when (type) {
                    1 -> {//TYPE_PRIVATE
                        var totalSize = 0L
                        if (version >= Build.VERSION_CODES.O) {//8.0
                            val getFsUuid = obj.javaClass.getDeclaredMethod("getFsUuid")
                            val fsUuid = getFsUuid.invoke(obj)
                            totalSize = getTotalSize(context, fsUuid)//8.0 以后使用
                        } else if (version >= Build.VERSION_CODES.N_MR1) {//7.1.1
                            val getPrimaryStorageSize =
                                StorageManager::class.java.getMethod("getPrimaryStorageSize")//5.0 6.0 7.0没有
                            totalSize = getPrimaryStorageSize.invoke(storageManager) as Long
                        }

                        val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                        val readable = isMountedReadable.invoke(obj) as Boolean
                        if (readable) {
                            val file = obj.javaClass.getDeclaredMethod("getPath")
                            val f = file.invoke(obj) as File

                            if (totalSize == 0L) {
                                totalSize = f.totalSpace
                            }
                            systemSize = totalSize - f.totalSpace
                            used += totalSize - f.freeSpace
                            total += totalSize
                        }
                    }
                    0 -> {//TYPE_PUBLIC
                        val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                        val readable = isMountedReadable.invoke(obj) as Boolean
                        if (readable) {
                            val file = obj.javaClass.getDeclaredMethod("getPath")
                            val f = file.invoke(obj) as File
                            used += f.totalSpace - f.freeSpace
                            total += f.totalSpace
                        }
                    }
                }
            }
            return StorageBean(
                total = total,
                available = total - used,
                used = used,
                system = systemSize
            )

        } else {
            val statFs = StatFs(Environment.getExternalStorageDirectory().path)
            val size = statFs.blockSizeLong
            return StorageBean(
                total = size * statFs.blockCountLong,
                available = size * statFs.availableBlocksLong,
                used = size * (statFs.blockCountLong - statFs.availableBlocksLong),
                system = -1
            )
        }

        return null
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun getTotalSize(context: Context, fsUuid: Any?): Long {
        try {
            val id: UUID = fsUuid?.run { UUID.fromString(fsUuid as String) }
                ?: StorageManager.UUID_DEFAULT
            val stats = context.getSystemService(StorageStatsManager::class.java)
            return stats?.getTotalBytes(id) ?: -1
        } catch (e: NoSuchFieldError) {
            e.printStackTrace()
            return -1
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
            return -1
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return -1
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
    }

    private val units = arrayOf("B", "KB", "MB", "GB", "TB")

    /**
     * 进制转换
     */
    fun getUnit(_size: Long?): String {
        var base = 1024
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            base = 1000
        }
        var size = _size?.toFloat() ?: 0F
        var index = 0
        while (size > base && index < 4) {
            size = size.div(base)
            index++
        }
        return String.format(Locale.getDefault(), " %.2f %s ", size, units[index])
    }

    /**
     * 进制转换(不带小数点)
     */
    fun getUnitInteger(_size: Long?): String {
        var base = 1024
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            base = 1000
        }
        var size = _size?.toFloat() ?: 0F
        var index = 0
        while (size > base && index < 4) {
            size = size.div(base)
            index++
        }
        return String.format(Locale.getDefault(), " %.0f %s ", size, units[index])
    }
}