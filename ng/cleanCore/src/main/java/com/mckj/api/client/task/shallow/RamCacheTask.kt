package com.mckj.api.client.task.shallow

import android.content.pm.PackageInfo
import android.util.Log
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.mckj.api.client.JunkConstants
import com.mckj.api.client.task.base.BaseTask
import com.mckj.api.util.FileUtils
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkDescription
import com.mckj.api.entity.JunkInfo
import com.org.openlib.utils.ProcessUtil
import io.reactivex.rxjava3.functions.Consumer
import java.lang.Exception

/**
 * @author xx
 * @version 1
 * @createTime 2021/10/28 16:38
 * @desc 清理内存缓存任务
 */
class RamCacheTask : BaseTask() {

    companion object {
        const val TAG = "RamCacheTask"
    }


    override fun getName(): String {
        return "内存缓存"
    }

    override fun scan(consumer: Consumer<AppJunk>): Boolean {
        val list = ProcessUtil.getRunningApps()
        if (list.isNullOrEmpty()) {
            Log.e(TAG, "scan: list is null")
            return false
        }
        list.forEach { info ->
            consumer.accept(getRamCacheEntity(info))
        }
        return true
    }

    override fun clean(appJunk: AppJunk): Boolean {
        return try {
            appJunk.junks?.forEach {
                FileUtils.delete(it.path)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun stop() {

    }


    private fun getRamCacheEntity(info: PackageInfo): AppJunk {
        val appName: String = AppUtil.getAppName(AppMod.app, info.packageName).toString() ?: ""
        val icon = AppUtil.getAppIcon(AppMod.app, info.packageName)
        val detailList = mutableListOf<JunkInfo>()
        detailList.add(
            JunkInfo(
                junkType = JunkConstants.JunkType.RAM,
                fileType = JunkConstants.JunkFileType.OTHER,
                name = info.packageName,
                description = "内存缓存"
            )
        )
        return AppJunk(
            type = JunkConstants.AppCacheType.RAM,
            appName = appName,
            packageName = info.packageName,
            icon = icon,
            junkSize = 1,
            junkDescription = JunkDescription(title = "内存缓存"),
            junks = detailList
        )
    }
}