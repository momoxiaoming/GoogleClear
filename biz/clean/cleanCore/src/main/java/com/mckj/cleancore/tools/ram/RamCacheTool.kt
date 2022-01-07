package com.mckj.cleancore.tools.ram

import android.content.pm.PackageInfo
import com.dn.baselib.util.ProcessUtil
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.mckj.cleancore.entity.IJunkEntity
import com.mckj.cleancore.entity.JunkDetailEntity
import com.mckj.cleancore.entity.RamCacheEntity
import com.mckj.cleancore.tools.AbsJunkTool
import com.mckj.cleancore.util.Log
import io.reactivex.rxjava3.functions.Consumer

/**
 * Describe:
 *
 * Created By yangb on 2021/5/17
 */
class RamCacheTool : AbsJunkTool() {

    companion object {
        const val TAG = "RamCacheTool"
    }

    override fun getName(): String {
        return "内存缓存"
    }

    override fun scan(consumer: Consumer<IJunkEntity>): Boolean {
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

    override fun clean(entity: IJunkEntity): Boolean {
        val details = entity.getJunkDetails()
        Log.i(TAG, "clean: details.size:${details?.size ?: 0}")
        details?.forEach {
            ProcessUtil.killBackgroundProcesses()
        }
        return true
    }

    private fun getRamCacheEntity(info: PackageInfo): RamCacheEntity {
        val appName: String = AppUtil.getAppName(AppMod.app, info.packageName)?.toString() ?: ""
        val icon = AppUtil.getAppIcon(AppMod.app, info.packageName)
        val detailList = mutableListOf<JunkDetailEntity>()
        detailList.add(
            JunkDetailEntity(
                JunkDetailEntity.CATEGORY_NORMAL_CACHE,
                JunkDetailEntity.TYPE_NORMAL,
                "内存缓存",
                info.packageName,
                1
            )
        )
        return RamCacheEntity(appName, "内存缓存", info.packageName, icon, detailList, 1)
    }

}