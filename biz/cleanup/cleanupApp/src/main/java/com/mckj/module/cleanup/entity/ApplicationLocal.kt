package com.mckj.module.cleanup.entity

import android.app.usage.StorageStatsManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.text.format.Formatter
import androidx.annotation.RequiresApi
import com.mckj.baselib.helper.getApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * author：zpwang
 */
data class ApplicationLocal(
    val packageName: String,
    val name: String,
    val icon: Drawable,
    val noUseTime: String,
    val appSize: Long,
    val firstInstallTime: Long,
    val lastUpdateTime: Long,
    val sourceDir: String,
    val isSystemApp: Boolean,
    val dataDir: String?,
) {
    override fun toString(): String {
        return "$name\n$icon\n$noUseTime\n" + "$appSize\n$firstInstallTime" +
                "\n$lastUpdateTime\n$sourceDir" + "$isSystemApp\n$dataDir"
    }

    var uid = 0
    var storageUuid :UUID = UUID.fromString("41217664-9172-527a-b3d5-edabb50a7d69")

    val lastUseTime: Long?
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
        get(){
            return AppInfoHolder.getNonSystemApplicationTime(getApplicationContext(),packageName)
        }

    val firstInstallTimeFormat: String
        get() {
            val time = if (firstInstallTime == 0L) {
                ""
            } else {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                if(simpleDateFormat.format(Date(firstInstallTime)).startsWith("1970")){
                    "未知"
                }else{
                    simpleDateFormat.format(Date(firstInstallTime))
                }
            }
            return time
        }

    val apkSize: Long
        get(){
            val size = if(TextUtils.isEmpty(sourceDir)){
                0L
            } else {
                File(sourceDir).length()
            }
            return size
        }

    val apkSizeFormat: String
        get() {

//            val appTotalSize = getAppTotalSize()
            val size = if (TextUtils.isEmpty(sourceDir)) {
                ""
            } else {
                Formatter.formatShortFileSize(getApplicationContext(), File(sourceDir).length())
            }
//            val formatShortFileSize =
//                Formatter.formatShortFileSize(getApplicationContext(), size)
            return size
        }



    /**
     * android 8.0通过存储状态服务获取APP总计大小
     * 8.0以下通过APP路径，获取APP大小（只有APP自身，没有缓存，数据）
     */
    fun getAppTotalSize(): Long {
        val appContext = getApplicationContext()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val storageManager= appContext.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
            val result = storageManager.queryStatsForUid(storageUuid, uid)
            return result.appBytes+result.cacheBytes+result.dataBytes
        } else {
            if (TextUtils.isEmpty(sourceDir)) {
                return 0L
            } else {
                return File(sourceDir).length()
            }
        }
    }

}